# Renovate Bot Auto-Merge Setup

This repository is configured with Renovate bot for automatic dependency updates with CI integration and auto-merge capabilities.

## Configuration Files

### `.github/workflows/build.yml`
- Main CI workflow that runs tests and builds on all PRs and pushes to master
- Enhanced with test result publishing and failure notifications
- Runs on all PRs including those created by Renovate

### `.github/workflows/auto-merge.yml`
- Dedicated workflow for auto-merging Renovate PRs
- Only triggers on PRs created by `renovate[bot]`
- Waits for all required status checks to pass before merging

### `renovate.json`
- Renovate configuration with auto-merge enabled
- Requires "build" status check to pass before auto-merge
- Limits PR creation to prevent spam (5 per hour, 10 concurrent)
- Creates dependency dashboard for monitoring

### `.github/CODEOWNERS`
- Ensures proper review assignment for dependency updates
- Assigns repository owner to review critical files

## How It Works

1. **Renovate Detection**: Renovate bot scans for dependency updates daily
2. **PR Creation**: Creates PRs for available updates with proper labels
3. **CI Execution**: GitHub Actions runs the build workflow on all PRs
4. **Auto-Merge**: If CI passes, the auto-merge workflow automatically merges Renovate PRs
5. **Failure Alerting**: If CI fails, comments are added to PRs with failure notifications

## Benefits

- ✅ Automatic dependency updates
- ✅ CI validation before merge
- ✅ Auto-merge when tests pass
- ✅ Failure notifications when CI breaks
- ✅ Dependency dashboard for monitoring
- ✅ Rate limiting to prevent spam