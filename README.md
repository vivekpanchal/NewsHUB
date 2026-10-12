# NewsHUB

An Android news reader built with Kotlin, Jetpack Compose and an MVI architecture. Headlines come from [NewsAPI.org](https://newsapi.org).

## Features

- Top headlines feed and a personalized "Your Feed" from onboarding topic choices
- Search
- Article detail view
- Favorites, stored offline in Room
- Glance home-screen widget, refreshed by WorkManager
- No-connection banner when the device goes offline

## Tech stack

| Area | Library |
|------|---------|
| Language | Kotlin 1.9, Java 17 target |
| UI | Jetpack Compose (Material 3), Navigation Compose |
| Architecture | MVI (`util/mvi/MviViewModel`), Hilt DI |
| Async | Coroutines, Flow, kotlinx-collections-immutable |
| Network | Retrofit, OkHttp, Moshi (codegen) |
| Storage | Room, DataStore Preferences |
| Images | Coil |
| Widget | Glance, WorkManager |
| Ads | Google Mobile Ads |

Versions live in the Gradle version catalog: `gradle/libs.versions.toml`.

## Architecture

```
com.vivekpanchal.newshub
├── data/        connectivity, local (Room DAO, DataStore), remote (Retrofit API, DTOs), repositories
├── di/          Hilt modules (network, database, repositories)
├── domain/      domain models
├── ui/          one package per screen + common, navigation, theme
├── util/mvi/    MVI base: UiState, UiIntent, UiEffect, MviViewModel
└── widget/      Glance widget, receiver, refresh worker
```

Each screen package follows the same shape:

- `XContract.kt`: the screen's `State`, `Intent` and `Effect` types
- `XViewModel.kt`: extends `MviViewModel`, handles intents, updates state with `setState { }`, sends one-shot events with `setEffect { }`
- `XScreen.kt`: a stateful composable that collects state and effects, plus a stateless `Content` composable with `@Preview`s

Data flows one way: UI sends an intent, the ViewModel reduces it into a new state, Compose renders the state.

## Getting started

Requirements: Android Studio (Koala or newer), JDK 17, Android SDK 34. Minimum device API: 24.

1. Clone the repository.
2. Get a free API key from [newsapi.org](https://newsapi.org/register).
3. Add it to `local.properties` in the project root (this file is gitignored):

   ```properties
   NEWS_API_KEY=yourNewsApiOrgKeyHere
   ```

4. Build and run:

   ```bash
   ./gradlew assembleDebug
   ```

### Optional `local.properties` keys

| Key | Purpose | Default |
|-----|---------|---------|
| `ADMOB_APP_ID` | AdMob app ID | Google test ID |
| `ADMOB_BANNER_AD_UNIT_ID` | Banner ad unit | Google test ID |
| `RELEASE_KEY_ALIAS` | Release signing alias | `key` |
| `RELEASE_KEY_PASSWORD` | Release key password | empty |
| `RELEASE_STORE_PASSWORD` | Keystore password | empty |

Release builds expect a keystore at `app/keystore.jks`.

## CI

GitHub Actions (`.github/workflows/android-build.yml`) runs `./gradlew assembleDebug` on every push to `master` and every pull request, and uploads the debug APK as an artifact.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).
