# Contributing to NewsHUB

Thanks for helping out. This guide covers setup, code conventions and the pull request flow.

## Setup

Follow [Getting started](README.md#getting-started) in the README. You need a `NEWS_API_KEY` in `local.properties`. Never commit `local.properties`, API keys or keystores.

## Workflow

1. Branch from `master`.
2. Keep each pull request focused on one change.
3. Confirm the debug build passes locally before you push:

   ```bash
   ./gradlew assembleDebug
   ```

4. Open a pull request against `master`. CI must pass before merge.

## Code conventions

### Language and UI

- Kotlin only. No new Java files.
- Jetpack Compose only. No XML layouts or Fragments.
- Use the theme tokens in `ui/theme` (`Color`, `Type`, `Spacing`, `Shape`, `Motion`) instead of hard-coded values.
- Put user-facing text in `res/values/strings.xml`.

### MVI

Every screen extends `MviViewModel<State, Intent, Effect>` from `util/mvi`.

- Define `State`, `Intent` and `Effect` in a `XContract.kt` file in the screen's package.
- `State` must be immutable and Compose-stable. Use `ImmutableList` / `PersistentList` from kotlinx-collections-immutable, not `List`.
- Change state only through `setState { copy(...) }`.
- Send navigation, toasts and snackbars through `setEffect { }`, not through state.
- The UI talks to the ViewModel only through `setIntent(...)`.

### Composables

- Split each screen into a stateful composable (gets the ViewModel, collects state and effects) and a stateless `Content` composable (takes state and an `onIntent` lambda).
- Give every stateless composable an `@Preview`. Wrap previews in `NewsHubPreviewSurface` from `ui/theme/PreviewTheme.kt`.
- Use stable keys in `LazyColumn` / `LazyRow` items.

### Data and DI

- Repositories expose an interface (`XRepository`) with an `XRepositoryImpl`, bound in `di/RepositoryModule.kt`.
- JSON uses Moshi codegen: annotate DTOs with `@JsonClass(generateAdapter = true)`. Do not add Gson.
- Map DTOs to `domain/model` types before they reach the UI.

### Dependencies

- Add new libraries to `gradle/libs.versions.toml` and reference them as `libs.*`. No hard-coded versions in `build.gradle`.
- Prefer a library the project already uses before adding a new one.

## Commits

- Use a short imperative subject line, for example `Add search history` or `Fix favorites crash on rotate`.
- Explain the why in the body when it is not obvious.

## Reporting bugs

Open a GitHub issue with steps to reproduce, the expected and actual behavior, and your device and Android version.
