# Chinese Municipalities — Practical 5

An Android application that lets the user look up one of China's four
direct-administered municipalities — Beijing, Shanghai, Tianjin and Chongqing.

**Student:** Guo Yuqi
**ID:** 20243006972

## What the app does

1. The **main screen** shows a title, an illustration of China's municipalities, and a
   single-line text field labelled *I want to know about:*.
2. The user types a municipality name and presses **Go** on the keyboard. The input is
   validated by a plain Java class, `TextProcessor`, which accepts the name in any
   capitalisation (`beijing`, `BEIJING`, `Beijing`) and rejects empty, whitespace-only
   or unsupported names such as *Guangzhou*.
3. On a valid input the normalised name is passed to the **second screen**,
   `Municipality`, with an **explicit Intent**. That screen shows the municipality's
   name, photograph and a factual summary, all styled in the municipality's own colour.
4. The **Visit site** button opens the municipality's official government website using
   an **implicit Intent** (`Intent.ACTION_VIEW`), so Android hands the URL to a browser.

## Colour scheme

| Municipality | Name bar / button | Information text | Image        |
|--------------|-------------------|------------------|--------------|
| Beijing      | Red               | Red              | beijing.jpg  |
| Shanghai     | Magenta           | Magenta          | shanghai.jpg |
| Tianjin      | Charcoal          | Charcoal         | tianjin.jpg  |
| Chongqing    | Bright orange     | Bright orange    | chongqing.jpg |

Every colour is a **named resource** in `res/values/colors.xml`; no hexadecimal colour
value is repeated in the Java code. All four colours meet the WCAG AA contrast ratio of
4.5:1 against both the white text on the name bar and the light app background.

## Official websites

| Municipality | URL                              |
|--------------|----------------------------------|
| Beijing      | https://english.beijing.gov.cn/  |
| Shanghai     | https://english.shanghai.gov.cn/ |
| Tianjin      | https://en.tj.gov.cn/            |
| Chongqing    | https://english.cq.gov.cn/       |

## Project structure

```
app/src/main/
├── java/com/example/practicalfive/
│   ├── MainActivity.java      Main screen, validation, explicit Intent
│   ├── Municipality.java      Second screen, styling, implicit Intent
│   └── TextProcessor.java     Plain Java validation and normalisation class
└── res/
    ├── drawable/              Municipality photos + launcher foreground
    ├── layout/
    │   ├── activity_main.xml
    │   └── activity_municipality.xml
    ├── mipmap-*/              Adaptive and legacy launcher icons
    └── values/
        ├── arrays.xml         municipalities_array (Beijing, Shanghai, Chongqing, Tianjin)
        ├── colors.xml         Municipality and supporting colour resources
        ├── strings.xml
        └── themes.xml
```

### A note on the array order

`municipalities_array` stores the four summaries in the order **Beijing, Shanghai,
Chongqing, Tianjin**. `TextProcessor.MUNICIPALITIES` uses the same order, so a
municipality's index in one is its index in the other, and the correct summary is
always retrieved.

## Building and running

Open the project in Android Studio and press **Run**, or build from the command line:

```bash
./gradlew assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

- Minimum SDK: 24 (Android 7.0)
- Target / compile SDK: 34 (Android 14)
- Language: Java, with XML layouts

## Tests

`tools/` contains two small verification scripts used during development:

```bash
# Case-insensitive validation, index mapping and summary word counts
javac -d out app/src/main/java/com/example/practicalfive/TextProcessor.java tools/TestTextProcessor.java
java -cp out TestTextProcessor

# WCAG AA contrast ratios of the four municipality colours
javac -d out tools/TestContrast.java
java -cp out TestContrast
```
