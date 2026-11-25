Add-Type -AssemblyName System.Drawing

$res    = "c:\Users\shang\Desktop\Practical 5 - Resources\Practical 5 - Resources"
$proj   = "c:\Users\shang\Desktop\Practical 5 - Resources\PracticalFive\app\src\main\res"
$draw   = Join-Path $proj "drawable"
$mipAny = Join-Path $proj "mipmap-anydpi-v26"

# ---------- 1. Municipality photos -> res/drawable (jpg) ----------
foreach ($n in @("beijing","shanghai","tianjin","chongqing")) {
    $src = Join-Path $res "$n.jpg"
    $dst = Join-Path $draw "$n.jpg"
    Copy-Item $src $dst -Force
    Write-Output "copied $n.jpg"
}

# ---------- 2. chinese_municipalities.png -> required .jpg (real JPEG re-encode) ----------
$src = Join-Path $res "chinese_municipalities.png"
$dst = Join-Path $draw "chinese_municipalities.jpg"
$img = [System.Drawing.Image]::FromFile($src)
$bmp = New-Object System.Drawing.Bitmap($img.Width, $img.Height)
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.Clear([System.Drawing.Color]::White)
$g.DrawImage($img, 0, 0, $img.Width, $img.Height)
$g.Dispose()
$bmp.Save($dst, [System.Drawing.Imaging.ImageFormat]::Jpeg)
$bmp.Dispose(); $img.Dispose()
Write-Output "encoded chinese_municipalities.jpg"

# ---------- 3. Adaptive icon foreground from app_logo.png ----------
$logo = [System.Drawing.Image]::FromFile((Join-Path $res "app_logo.png"))

function New-Foreground([int]$size) {
    $b = New-Object System.Drawing.Bitmap($size, $size, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $gg = [System.Drawing.Graphics]::FromImage($b)
    $gg.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $gg.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $gg.Clear([System.Drawing.Color]::Transparent)
    # logo occupies the 66% adaptive-icon safe zone, centred
    $safe = [int]($size * 0.66)
    $scale = [Math]::Min($safe / $logo.Width, $safe / $logo.Height)
    $w = [int]($logo.Width * $scale)
    $h = [int]($logo.Height * $scale)
    $x = [int](($size - $w) / 2)
    $y = [int](($size - $h) / 2)
    $gg.DrawImage($logo, $x, $y, $w, $h)
    $gg.Dispose()
    return $b
}

$fg = New-Foreground 432
$fg.Save((Join-Path $draw "ic_launcher_foreground.png"), [System.Drawing.Imaging.ImageFormat]::Png)
$fg.Dispose()
Write-Output "wrote ic_launcher_foreground.png"

# ---------- 4. Legacy launcher icons (black background + centred logo) ----------
function New-Legacy([int]$size, [string]$path) {
    $b = New-Object System.Drawing.Bitmap($size, $size)
    $gg = [System.Drawing.Graphics]::FromImage($b)
    $gg.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $gg.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $gg.Clear([System.Drawing.Color]::Black)
    $inner = [int]($size * 0.74)
    $scale = [Math]::Min($inner / $logo.Width, $inner / $logo.Height)
    $w = [int]($logo.Width * $scale)
    $h = [int]($logo.Height * $scale)
    $x = [int](($size - $w) / 2)
    $y = [int](($size - $h) / 2)
    $gg.DrawImage($logo, $x, $y, $w, $h)
    $gg.Dispose()
    $b.Save($path, [System.Drawing.Imaging.ImageFormat]::Png)
    $b.Dispose()
}

$densities = @{ "mdpi"=48; "hdpi"=72; "xhdpi"=96; "xxhdpi"=144; "xxxhdpi"=192 }
foreach ($d in $densities.Keys) {
    $dir = Join-Path $proj "mipmap-$d"
    New-Item -ItemType Directory -Force -Path $dir | Out-Null
    $px = $densities[$d]
    New-Legacy $px (Join-Path $dir "ic_launcher.png")
    New-Legacy $px (Join-Path $dir "ic_launcher_round.png")
    Write-Output "wrote mipmap-$d"
}

$logo.Dispose()
Write-Output "DONE"
