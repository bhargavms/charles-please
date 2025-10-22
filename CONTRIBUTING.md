# Contributing to Charles Please

Thank you for your interest in contributing to Charles Please! This document provides guidelines for contributing to the project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Contributing Process](#contributing-process)
- [Code Style Guidelines](#code-style-guidelines)
- [Testing](#testing)
- [Pull Request Guidelines](#pull-request-guidelines)
- [Issue Reporting](#issue-reporting)

## Code of Conduct

This project follows the [Contributor Covenant Code of Conduct](https://www.contributor-covenant.org/). By participating, you agree to uphold this code.

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/yourusername/charlesplease.git`
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Set up the development environment (see below)

## Development Setup

### Prerequisites

- Java 21+
- Gradle 8.4+
- Python 3.7+ (for pre-commit hooks)
- ADB (Android Debug Bridge)
- Android device or emulator

### Setup Steps

1. **Install dependencies:**
   ```bash
   # Install pre-commit hooks
   pip install pre-commit
   pre-commit install
   ```

2. **Build the project:**
   ```bash
   ./gradlew build
   ```

3. **Run tests:**
   ```bash
   ./gradlew test
   ```

4. **Format code:**
   ```bash
   ./gradlew ktlintFormat
   ```

## Contributing Process

### 1. Choose an Issue

- Look for issues labeled `good first issue` for beginners
- Check for issues labeled `help wanted` for more complex tasks
- Comment on the issue to indicate you're working on it

### 2. Make Changes

- Follow the code style guidelines
- Write tests for new functionality
- Update documentation as needed
- Ensure all tests pass

### 3. Test Your Changes

```bash
# Run all tests
./gradlew test

# Run pre-commit hooks
pre-commit run --all-files

# Check code style
./gradlew ktlintCheck
```

### 4. Submit a Pull Request

- Create a clear, descriptive title
- Provide a detailed description of changes
- Reference any related issues
- Ensure CI passes

## Code Style Guidelines

### Kotlin Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use ktlint for automatic formatting
- Maximum line length: 120 characters
- Use meaningful variable and function names
- Add KDoc comments for public APIs

### Command Pattern

- Each command should have a single responsibility
- Commands should be immutable where possible
- Use the `Command` interface for all operations
- Chain commands using `CommandExecutor`

### Example Command Structure

```kotlin
class MyCommand(
    private val logger: Logger,
    private val param: String
) : Command {

    override fun execute() {
        logger.info("Executing MyCommand with param: $param")
        // Implementation
    }
}
```

### Naming Conventions

- **Classes**: PascalCase (e.g., `ApplyProxyCommand`)
- **Functions**: camelCase (e.g., `executeCommand`)
- **Variables**: camelCase (e.g., `commandExecutor`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `DEFAULT_PORT`)

## Testing

### Writing Tests

- Write unit tests for all new commands
- Test both success and failure scenarios
- Use descriptive test names
- Mock external dependencies

### Test Structure

```kotlin
class MyCommandTest {

    @Test
    fun `should execute successfully when valid input provided`() {
        // Given
        val command = MyCommand(logger, "valid-input")

        // When
        command.execute()

        // Then
        // Assert expected behavior
    }

    @Test
    fun `should throw exception when invalid input provided`() {
        // Given
        val command = MyCommand(logger, "invalid-input")

        // When & Then
        assertThrows<RuntimeException> {
            command.execute()
        }
    }
}
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "MyCommandTest"

# Run with coverage
./gradlew test jacocoTestReport
```

## Pull Request Guidelines

### Before Submitting

- [ ] Code follows style guidelines
- [ ] All tests pass
- [ ] Pre-commit hooks pass
- [ ] Documentation updated
- [ ] No merge conflicts

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Manual testing performed
- [ ] All tests pass

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No breaking changes (or clearly documented)
```

### Review Process

1. **Automated Checks**: CI must pass
2. **Code Review**: At least one maintainer approval
3. **Testing**: Manual testing may be required
4. **Documentation**: Ensure docs are updated

## Issue Reporting

### Bug Reports

When reporting bugs, please include:

- **Description**: Clear, concise description
- **Steps to Reproduce**: Detailed steps
- **Expected Behavior**: What should happen
- **Actual Behavior**: What actually happens
- **Environment**: OS, Java version, Gradle version
- **Logs**: Relevant error messages or logs

### Feature Requests

For feature requests, please include:

- **Use Case**: Why is this feature needed?
- **Proposed Solution**: How should it work?
- **Alternatives**: Other solutions considered
- **Additional Context**: Any other relevant information

## Development Workflow

### Branch Naming

- `feature/description` - New features
- `bugfix/description` - Bug fixes
- `docs/description` - Documentation updates
- `refactor/description` - Code refactoring

### Commit Messages

We follow the [Conventional Commits](https://www.conventionalcommits.org/) specification for all commit messages. This helps maintain a clean, readable git history and enables automated changelog generation.

#### Format

```
<type>(<scope>): <subject>

[optional body]

[optional footer(s)]
```

#### Types

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that don't affect code meaning (formatting, whitespace, etc.)
- **refactor**: Code change that neither fixes a bug nor adds a feature
- **perf**: Performance improvement
- **test**: Adding or updating tests
- **chore**: Changes to build process, dependencies, or auxiliary tools
- **ci**: Changes to CI configuration files and scripts
- **build**: Changes that affect the build system or external dependencies

#### Scopes

Common scopes in this project:
- **proxy**: Proxy configuration and application
- **adb**: ADB command execution
- **network**: Network discovery and connectivity
- **task**: Gradle task implementations
- **plugin**: Plugin configuration and setup
- **docs**: Documentation
- **test**: Test infrastructure

#### Subject

- Use imperative, present tense: "add" not "added" or "adds"
- Don't capitalize the first letter
- No period (.) at the end
- Keep it under 50 characters

#### Body (Optional)

- Explain **what** and **why**, not **how**
- Wrap at 72 characters
- Separate from subject with a blank line

#### Footer (Optional)

- Reference issues: `Fixes #123` or `Closes #456`
- Note breaking changes: `BREAKING CHANGE: description`

#### Examples

**Simple feature:**
```
feat(proxy): add auto-discovery for Charles proxy
```

**Bug fix with details:**
```
fix(adb): handle device connection errors gracefully

Previously, the plugin would crash if no device was connected.
Now it provides a clear error message and exits cleanly.

Fixes #42
```

**Breaking change:**
```
feat(plugin)!: rename charlesProxy to charlesPlease

BREAKING CHANGE: The configuration block has been renamed from
`charlesProxy` to `charlesPlease`. Update your build scripts:

Before: charlesProxy { ... }
After: charlesPlease { ... }
```

**Documentation update:**
```
docs(readme): add troubleshooting section for common ADB issues
```

**Refactoring:**
```
refactor(command): extract ADB operations into command pattern

This improves testability and separation of concerns by moving
all ADB operations into dedicated command classes.
```

#### Tips

- Make atomic commits (one logical change per commit)
- Commit often, but keep commits meaningful
- Write commit messages as if explaining to a colleague
- Use `git commit --amend` to fix the last commit message if needed

#### Automated Checking

Commit messages are automatically validated:

- **Locally**: Pre-commit hooks will check your commit messages when you commit
- **In PRs**: GitHub Actions will validate both PR titles and all commit messages

To install the commit message hook:
```bash
# Install commitizen (Python tool for conventional commits)
pip install commitizen

# Install pre-commit hooks including commit-msg hook
pre-commit install --hook-type commit-msg
```

To manually check a commit message:
```bash
# Check if a message follows conventional commits
echo "feat(proxy): add new feature" | cz check --commit-msg-file -

# Or use the interactive commit helper
cz commit
```

The configuration is in `.cz.toml` which defines:
- Allowed commit types and scopes
- Commit message format and validation rules
- Interactive prompts for guided commit creation

## Getting Help

- **Discussions**: Use GitHub Discussions for questions
- **Issues**: Create an issue for bugs or feature requests
- **Code Review**: Ask questions in PR comments

## Release Process

1. Update version in `build.gradle.kts`
2. Update `CHANGELOG.md`
3. Create release PR
4. Tag release after merge
5. Publish to Gradle Plugin Portal

## Thank You

Thank you for contributing to Charles Please! Your contributions help make Android development easier for everyone.
