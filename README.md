# 🌍 Country Picker KMP Library

A lightweight and customizable **Kotlin Multiplatform (KMP)** library for integrating country selection into your Android and iOS apps. Includes core country data (dial code, currency, flag) and elegant UI components like dialogs and picker fields for effortless integration.



## ✨ Features

- ✅ Predefined list of countries with:
  - Name
  - Dial code
  - Currency
  - Flag resource
- ✅ Built-in `CountryPickerDialog` and `CountryPickerField` Composables
- ✅ Real-time search support
- ✅ Multiple display options: Dial Code, Currency, Name
- ✅ Kotlin Multiplatform ready (Android, iOS, Desktop, WASM)

---


### Gradle

Make sure to include the library in your `shared` module (KMP common module):

```kotlin
dependencies {
    implementation("com.stevdza-san-countrypicker:1.0.0")
}
```

## Usage
### Show Country Picker Dialog

```kotlin
var showDialog by remember { mutableStateOf(false) }
var selectedCountry by remember { mutableStateOf(Country.Canada) }

if (showDialog) {
    CountryPickerDialog(
        selectedCountry = selectedCountry,
        onConfirmClick = {
            selectedCountry = it
            showDialog = false
        },
        onDismiss = { showDialog = false }
    )
}

CountryPickerField(
    selectedCountry = selectedCountry,
    onClick = { showDialog = true }
)
```

## Display Options

### Choose how to display the country label:

```kotlin
enum class CountryDisplayOption {
    NAME,
    CURRENCY,
    DIAL_CODE,
    DIAL_CODE_AND_NAME,
    NAME_AND_CURRENCY
}
```

### Data Model

```kotlin
enum class Country(
    val dialCode: Int,
    val currency: String,
    val flag: DrawableResource
)
```

### To get all available countries:

```kotlin
val allCountries = Country.entries
```
