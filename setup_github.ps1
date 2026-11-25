# setup_github.ps1
#
# Creates the blank public GitHub repository for this project and pushes it.
#
# Prerequisites:
#   1. GitHub CLI installed and signed in:
#        gh auth login --hostname github.com --git-protocol https --web
#   2. This script run from the project root.
#
# Usage:
#   powershell -ExecutionPolicy Bypass -File setup_github.ps1
#   powershell -ExecutionPolicy Bypass -File setup_github.ps1 -RepoName 20243006972-Practical5

param(
    [string]$RepoName = "20243006972-Practical5",
    [string]$Visibility = "public"
)

$ErrorActionPreference = "Stop"

# --- sanity checks -------------------------------------------------------
if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    Write-Output "ERROR: GitHub CLI (gh) not found. Install it from https://cli.github.com/"
    exit 1
}

gh auth status 2>&1 | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Output "ERROR: not signed in to GitHub. Run: gh auth login --hostname github.com --git-protocol https --web"
    exit 1
}

$remote = git remote get-url origin 2>$null
if ($remote) {
    Write-Output "origin already exists: $remote"
    Write-Output "Set a different remote name if you also want a GitHub remote."
    exit 1
}

# --- check the repo name is free ----------------------------------------
$owner = gh api user --jq .login
Write-Output "GitHub account: $owner"

$existing = gh repo view "$owner/$RepoName" --json name 2>$null
if ($existing) {
    Write-Output "ERROR: $owner/$RepoName already exists."
    exit 1
}

# --- create a BLANK public repository ----------------------------------
# No --add-readme, no .gitignore, no license: the repository must be empty
# so that the local Android Studio project can be pushed to it cleanly.
Write-Output "Creating blank $Visibility repository: $owner/$RepoName"
gh repo create "$owner/$RepoName" --public --description "Practical 5: Resources, Intents, and Dynamic UI in Android - Chinese Municipalities" 2>&1 | Out-String

if ($LASTEXITCODE -ne 0) {
    Write-Output "ERROR: repository creation failed."
    exit 1
}

git remote add origin "https://github.com/$owner/$RepoName.git"
git push -u origin main 2>&1 | Out-String

if ($LASTEXITCODE -ne 0) {
    Write-Output "ERROR: push failed. If Git asks for credentials, use a Personal Access Token as the password."
    exit 1
}

Write-Output ""
Write-Output "SUCCESS"
Write-Output "Repository: https://github.com/$owner/$RepoName"
Write-Output "Submit this URL on Canvas."
