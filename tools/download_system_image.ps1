param(
    [string]$Url = "https://dl.google.com/android/repository/sys-img/google_apis/x86_64-34_r14.zip",
    [string]$Dest = "$env:TEMP\x86_64-34_r14.zip"
)

$ErrorActionPreference = "Stop"

Write-Output "Downloading $Url"

# Use curl.exe (present on Windows 10+) for a resumable, reliable download.
$curl = "$env:SystemRoot\System32\curl.exe"
if (Test-Path $curl) {
    & $curl -L -C - --retry 5 --retry-delay 5 --connect-timeout 30 -o $Dest $Url
} else {
    $wc = New-Object System.Net.WebClient
    $wc.DownloadFile($Url, $Dest)
}

if (-not (Test-Path $Dest)) { Write-Output "DOWNLOAD FAILED"; exit 1 }
$size = (Get-Item $Dest).Length
Write-Output "Downloaded $size bytes to $Dest"

# Extract into the SDK system-images folder.
$sdk = "$env:LOCALAPPDATA\Android\Sdk"
$target = Join-Path $sdk "system-images\android-34\google_apis\x86_64"
if (Test-Path $target) { Remove-Item -Recurse -Force $target }
New-Item -ItemType Directory -Force -Path $target | Out-Null

Write-Output "Extracting to $target"
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory($Dest, $target)
Write-Output "EXTRACT DONE"

Get-ChildItem $target | Select-Object Name, Length | Out-String
