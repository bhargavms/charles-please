#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<EOF
Usage:
  $0 --host HOST --port PORT [--bypass "comma,separated,exclusions"] [--pac URL] [--serial SERIAL]
  $0 --auto [--bypass "comma,separated,exclusions"] [--serial SERIAL]
  $0 --clear [--serial SERIAL]

Examples:
  $0 --host 10.0.0.5 --port 8888 --bypass "localhost,127.0.0.1,*.corp"
  $0 --auto --bypass "localhost,127.0.0.1,*.corp"
  $0 --pac http://proxy.example/pacfile.pac
  $0 --clear
EOF
  exit 1
}

HOST="" PORT="" BYPASS="" PAC="" SERIAL="" CLEAR="" AUTO=""

# Function to discover Charles proxy automatically
discover_charles_proxy() {
  local charles_port=8888
  local discovered_host=""
  local discovered_port=""

  echo "Discovering Charles proxy..."

  # Try to find the local machine's IP address that's accessible from Android devices
  # This works for most common network setups
  local local_ip=""

  # Try different methods to get the local IP that Android emulators can reach
  if command -v ip >/dev/null 2>&1; then
    # Linux/Android - get the IP of the interface used for default route
    local_ip=$(ip route get 8.8.8.8 2>/dev/null | grep -oP 'src \K\S+' | head -1)
  elif command -v ifconfig >/dev/null 2>&1; then
    # macOS/Linux - find the first non-loopback, non-virtual interface
    # Look for common network interfaces that emulators can reach
    for interface in en0 en1 wlan0 eth0; do
      if ifconfig "$interface" >/dev/null 2>&1; then
        local_ip=$(ifconfig "$interface" 2>/dev/null | grep -E "inet [0-9]" | grep -v "127.0.0.1" | head -1 | awk '{print $2}')
        if [[ -n "$local_ip" && "$local_ip" != "127.0.0.1" ]]; then
          break
        fi
      fi
    done

    # If no specific interface found, try all interfaces
    if [[ -z "$local_ip" ]]; then
      local_ip=$(ifconfig | grep -E "inet [0-9]" | grep -v "127.0.0.1" | grep -v "169.254" | head -1 | awk '{print $2}')
    fi
  fi

  # Fallback: try to get IP from the default route interface
  if [[ -z "$local_ip" ]]; then
    if command -v route >/dev/null 2>&1; then
      local interface=$(route -n get default 2>/dev/null | grep interface | awk '{print $2}' | head -1)
      if [[ -n "$interface" ]]; then
        local_ip=$(ifconfig "$interface" 2>/dev/null | grep -E "inet [0-9]" | grep -v "127.0.0.1" | head -1 | awk '{print $2}')
      fi
    fi
  fi

  if [[ -z "$local_ip" ]]; then
    echo "Error: Could not determine local IP address"
    echo "Please specify --host and --port manually"
    exit 1
  fi

  echo "Found local IP: $local_ip"

  # Test if Charles is running on the default port
  if command -v nc >/dev/null 2>&1; then
    if nc -z "$local_ip" "$charles_port" 2>/dev/null; then
      echo "Charles proxy detected at $local_ip:$charles_port"
      discovered_host="$local_ip"
      discovered_port="$charles_port"
    else
      echo "Charles proxy not detected at $local_ip:$charles_port"
      echo "Make sure Charles is running and accessible"
      exit 1
    fi
  elif command -v telnet >/dev/null 2>&1; then
    if timeout 2 telnet "$local_ip" "$charles_port" </dev/null 2>/dev/null; then
      echo "Charles proxy detected at $local_ip:$charles_port"
      discovered_host="$local_ip"
      discovered_port="$charles_port"
    else
      echo "Charles proxy not detected at $local_ip:$charles_port"
      echo "Make sure Charles is running and accessible"
      exit 1
    fi
  else
    echo "Warning: Cannot test proxy connectivity (nc/telnet not available)"
    echo "Assuming Charles is running at $local_ip:$charles_port"
    discovered_host="$local_ip"
    discovered_port="$charles_port"
  fi

  # Set the discovered values
  HOST="$discovered_host"
  PORT="$discovered_port"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --host)   HOST="$2"; shift 2 ;;
    --port)   PORT="$2"; shift 2 ;;
    --bypass) BYPASS="$2"; shift 2 ;;
    --pac)    PAC="$2"; shift 2 ;;
    --serial) SERIAL="$2"; shift 2 ;;
    --clear)  CLEAR="1"; shift ;;
    --auto)   AUTO="1"; shift ;;
    -h|--help) usage ;;
    *) echo "Unknown arg: $1"; usage ;;
  esac
done

ADB=(adb)
[[ -n "$SERIAL" ]] && ADB+=( -s "$SERIAL" )

# Ensure a device is connected
"${ADB[@]}" get-state >/dev/null

# Auto-discover Charles proxy if requested
if [[ -n "$AUTO" ]]; then
  discover_charles_proxy
fi

if [[ -n "$CLEAR" ]]; then
  echo "Clearing proxy settings…"
  "${ADB[@]}" shell settings delete global http_proxy || true
  "${ADB[@]}" shell settings delete global global_http_proxy_host || true
  "${ADB[@]}" shell settings delete global global_http_proxy_port || true
  "${ADB[@]}" shell settings delete global global_http_proxy_exclusion_list || true
  "${ADB[@]}" shell settings delete global proxy_pac_url || true
  echo "Done."
  exit 0
fi

if [[ -n "$PAC" ]]; then
  echo "Setting PAC URL to: $PAC"
  "${ADB[@]}" shell settings put global proxy_pac_url "$PAC"
  # Clear static proxy keys to avoid conflicts
  "${ADB[@]}" shell settings delete global http_proxy || true
  "${ADB[@]}" shell settings delete global global_http_proxy_host || true
  "${ADB[@]}" shell settings delete global global_http_proxy_port || true
  "${ADB[@]}" shell settings delete global global_http_proxy_exclusion_list || true
  echo "Done."
  exit 0
fi

# Validate that we have the required parameters
if [[ -z "$AUTO" && -z "$PAC" && -z "$CLEAR" ]]; then
  [[ -n "$HOST" && -n "$PORT" ]] || usage
fi

echo "Setting global proxy to: $HOST:$PORT"
# Legacy key used by many Android versions:
"${ADB[@]}" shell settings put global http_proxy "${HOST}:${PORT}"
# “Device-owner” style keys (widely honored as well):
"${ADB[@]}" shell settings put global global_http_proxy_host "$HOST"
"${ADB[@]}" shell settings put global global_http_proxy_port "$PORT"

if [[ -n "$BYPASS" ]]; then
  echo "Exclusion list: $BYPASS"
  "${ADB[@]}" shell settings put global global_http_proxy_exclusion_list "$BYPASS"
fi

# Ensure PAC is cleared when using static proxy
"${ADB[@]}" shell settings delete global proxy_pac_url || true

echo "Proxy applied."
echo "Verify:"
"${ADB[@]}" shell settings get global http_proxy
"${ADB[@]}" shell settings get global global_http_proxy_host
"${ADB[@]}" shell settings get global global_http_proxy_port
"${ADB[@]}" shell settings get global global_http_proxy_exclusion_list
