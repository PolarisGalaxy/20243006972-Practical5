# Testing on a physical device

The emulator on this machine cannot use hardware virtualisation
(`vmcompute.exe` is missing, so `HypervisorPresent` is `False`), so the app was
verified on a physical Android device instead.

## 1. Install

The debug APK is at `app/build/outputs/apk/debug/app-debug.apk`.

**Option A — copy the APK to the phone and open it**

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

If you prefer not to use a cable, copy `app-debug.apk` to the phone and tap it.
Android will ask you to allow installation from your file manager the first time.

**Option B — build and install from Android Studio**

Open the project, connect the phone over USB, enable *Developer options →
USB debugging*, then press **Run**.

## 2. Enable USB debugging on the phone

1. Settings → About phone → tap **Build number** seven times.
2. Settings → Developer options → turn on **USB debugging**.
3. Connect the phone by USB and accept the *Allow USB debugging?* prompt.

Check that the device is visible:

```bash
adb devices
```

## 3. Run through the test matrix

On the main screen, type a municipality name and press **Go** on the keyboard.

| Input      | Expected result                                     |
|------------|-----------------------------------------------------|
| `Beijing`  | Beijing screen, red theme, `beijing.jpg`             |
| `beijing`  | Beijing screen (case-insensitive)                    |
| `BEIJING`  | Beijing screen (case-insensitive)                    |
| `Shanghai` | Shanghai screen, magenta theme, `shanghai.jpg`       |
| `Tianjin`  | Tianjin screen, charcoal theme, `tianjin.jpg`        |
| `Chongqing`| Chongqing screen, bright orange theme, `chongqing.jpg`|
| `Guangzhou`| Error message, stays on the main screen              |
| *(empty)*  | "Please enter a municipality name" error             |
| *(spaces)* | "Please enter a municipality name" error             |

For each of the four valid municipalities, confirm that:

- the **name** TextView shows the correctly capitalised name;
- the **image** matches the municipality;
- the **summary** matches the corresponding entry of `municipalities_array`;
- the **name bar** and **Visit site button** use the municipality's colour;
- the **summary text** uses the municipality's colour.

Then tap **Visit site** on each screen and confirm the correct website opens:

- Beijing → <https://english.beijing.gov.cn/>
- Shanghai → <https://english.shanghai.gov.cn/>
- Tianjin → <https://en.tj.gov.cn/>
- Chongqing → <https://english.cq.gov.cn/>

## 4. Useful commands

```bash
# Is the app running?
adb shell pidof com.example.practicalfive

# Watch the log for crashes while you use the app
adb logcat | findstr /i "practicalfive AndroidRuntime"

# Uninstall
adb uninstall com.example.practicalfive
```
