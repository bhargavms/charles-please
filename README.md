# Charles Please Gradle Plugin

A Gradle plugin for automatically configuring Android device proxy settings via ADB. Supports Charles proxy auto-discovery and manual proxy configuration.

## Features

- **Auto-discovery**: Automatically detect Charles proxy host and port
- **Manual configuration**: Set custom proxy settings
- **PAC support**: Configure Proxy Auto-Configuration URLs
- **Device selection**: Target specific Android devices by serial
- **Bypass lists**: Configure proxy exclusions

## Usage

### Apply Plugin

```kotlin
plugins {
    id("dev.mogra.charlesplease")
}
```

### Configuration

```kotlin
charlesPlease {
    // Manual proxy configuration
    host.set("10.0.0.5")
    port.set(8888)
    bypass.set("localhost,127.0.0.1,*.corp")
    serial.set("") // Optional: specific device serial

    // Or use PAC URL
    // pacUrl.set("http://proxy.example/pacfile.pac")

    // Or enable auto-discovery
    // autoDiscover.set(true)
}
```

### Available Tasks

- `charlesPleaseApply` - Apply Charles proxy settings (auto-discovery or manual)
- `charlesPleaseClear` - Clear Charles proxy settings

### Examples

```bash
# Apply Charles proxy settings (uses charlesPlease config)
./gradlew charlesPleaseApply

# Clear Charles proxy settings
./gradlew charlesPleaseClear
```

## Requirements

- Android device connected via ADB
- Charles proxy running (for auto-discovery)
- Java 21+ (managed via JVM toolchain)
- Gradle 8.4+

## Development Setup

### Pre-commit Hooks

This project uses pre-commit hooks with ktlint for code quality and formatting.

#### Setup

```bash
# Install pre-commit (one-time)
pip install pre-commit

# Install hooks (one-time per repo)
pre-commit install
```

#### Usage

```bash
# Run on all files
pre-commit run --all-files

# Run on staged files only
pre-commit run

# Format code with ktlint
./gradlew ktlintFormat

# Check code style
./gradlew ktlintCheck
```

#### Available Hooks

- **ktlint**: Kotlin code formatting and linting
- **trailing-whitespace**: Remove trailing whitespace
- **end-of-file-fixer**: Ensure files end with newline
- **check-yaml**: Validate YAML syntax
- **check-json**: Validate JSON syntax
- **check-merge-conflict**: Detect merge conflicts
- **check-added-large-files**: Prevent large file commits
- **gradle-wrapper**: Validate Gradle wrapper

#### GitHub Actions

This project uses GitHub Actions for continuous integration:

- **Pre-commit**: Runs pre-commit hooks on all pull requests
- **CI**: Runs tests, ktlint checks, and builds the project
- **Conventional Commits**: Validates PR titles and commit messages follow Conventional Commits
- **Publish**: Automatically publishes to Gradle Plugin Portal on tag releases

All workflows run on Ubuntu with Java 21 and include caching for faster builds.

### Commit Message Guidelines

This project follows [Conventional Commits](https://www.conventionalcommits.org/). All commit messages and PR titles must follow this format:

```
<type>(<scope>): <subject>
```

Example: `feat(proxy): add auto-discovery for Charles proxy`

See [CONTRIBUTING.md](CONTRIBUTING.md#commit-messages) for detailed guidelines.

## Installation

### Local Development

```bash
./gradlew publishToMavenLocal
```

Then in your project:

```kotlin
plugins {
    id("dev.mogra.charlesplease") version "1.0.0"
}
```

### Publishing

```bash
./gradlew publishPlugins
```

## License

MIT License
