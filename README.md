# AuthUI

An Android authentication UI library built with Jetpack Compose and Retrofit. AuthUI provides ready-to-use authentication components with built-in API integration.

## Features

- Pre-built login UI with Material 3 design
- Built with Jetpack Compose
- Integrated Retrofit for API calls
- Loading states and error handling
- Customizable callbacks for success and error scenarios
- Configurable base URL and logging

## Installation

Add the dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.fahmihidayah:authui:0.1.0")
}
```

## Usage

### 1. Initialize the Library

Initialize AuthUI in your Application class or before using any components:

```kotlin
import com.fahmi.authui.AuthUi

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AuthUi.initialize(
            baseURL = "https://api.example.com/",
            enableLog = true
        )
    }
}
```

### 2. Use the LoginView Composable

Add the `LoginView` composable to your screen:

```kotlin
import com.fahmi.authui.ui.LoginView

@Composable
fun LoginScreen() {
    LoginView(
        modifier = Modifier.fillMaxSize(),
        onLoginSuccess = { response ->
            // Handle successful login
            println("Login successful: ${response.token}")
        },
        onLoginError = { error ->
            // Handle login error
            println("Login failed: $error")
        }
    )
}
```

### API Requirements

The library expects your API to have a POST endpoint at `/login` that:
- Accepts a JSON body with `email` and `password` fields
- Returns a response matching the `LoginResponse` data class structure

Example API request:
```json
POST /login
{
  "email": "user@example.com",
  "password": "password123"
}
```

## Requirements

- Android SDK 24+
- Kotlin
- Jetpack Compose

## License

```
Copyright 2025 Fahmi Hidayah

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Author

Fahmi Hidayah - [m.fahmi.hidayah@gmail.com](mailto:m.fahmi.hidayah@gmail.com)
