# AuthUI Module

Android authentication UI module built with Jetpack Compose and Retrofit for easy integration of login functionality into your Android applications.

## Features

- Ready-to-use Login UI component built with Jetpack Compose
- Material3 design
- Integrated Retrofit API client for authentication
- Built-in HTTP logging support
- Configurable base URL and logging
- Coroutine-based async operations
- Error handling and loading states

## Dependencies

This module includes:

- **Jetpack Compose** - Modern UI toolkit
  - Compose UI
  - Compose Material3
  - Activity Compose
- **Retrofit 3.0.0** - HTTP client
- **Gson 2.13.2** - JSON serialization
- **OkHttp Logging Interceptor 4.12.0** - Network request/response logging

## Installation

### Option 1: Maven Central (Recommended)

Add the dependency in your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.fahmihidayah:authui:0.1.0")
}
```

### Option 2: Local Module

Add the authui module to your project's `settings.gradle.kts`:

```kotlin
include(":authui")
```

Then add the dependency in your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":authui"))
}
```

## Configuration

Initialize the AuthUI module in your Application class or before using it:

```kotlin
import com.fahmi.authui.AuthUi

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AuthUi.initialize(
            baseURL = "https://api.example.com/",
            enableLog = BuildConfig.DEBUG
        )
    }
}
```

### Configuration Parameters

- `baseURL` - Your API base URL (must end with `/`)
- `enableLog` - Enable HTTP request/response logging (recommended for debug builds only)

## Usage

### LoginView Composable

The `LoginView` composable provides a complete login form with email and password fields.

```kotlin
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.fahmi.authui.ui.LoginView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    LoginView(
                        onLoginSuccess = { loginResponse ->
                            // Handle successful login
                            val token = loginResponse.token
                            val userId = loginResponse.userId

                            // Save token, navigate to home, etc.
                            Log.d("Login", "Token: $token, User ID: $userId")
                        },
                        onLoginError = { errorMessage ->
                            // Handle login error
                            Log.e("Login", "Error: $errorMessage")

                            // Show error to user
                            Toast.makeText(
                                this@MainActivity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            }
        }
    }
}
```

### LoginView Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `modifier` | Modifier | No | Compose modifier for layout customization |
| `onLoginSuccess` | (LoginResponse) -> Unit | No | Callback invoked on successful login |
| `onLoginError` | (String) -> Unit | No | Callback invoked on login failure |

### UI Features

- **Email Field**: Text input with email keyboard type
- **Password Field**: Secure text input with password masking
- **Submit Button**:
  - Automatically disabled when fields are empty
  - Shows loading spinner during API call
  - Displays "Logging in..." text when processing
- **Error Display**: Shows error messages below the password field
- **Loading State**: Disables inputs and shows progress indicator during authentication

## API Structure

### AuthAPI Interface

The module uses Retrofit to communicate with your authentication API.

```kotlin
interface AuthAPI {
    @POST("login")
    suspend fun login(@Body loginForm: LoginForm): Response<LoginResponse>
}
```

### Request Model

```kotlin
data class LoginForm(
    @field:SerializedName("email")
    val email: String?,

    @field:SerializedName("password")
    val password: String?
)
```

### Response Model

```kotlin
data class LoginResponse(
    val token: String,
    val userId: String
)
```

### Expected API Endpoint

Your API should have a `POST /login` endpoint that:

- Accepts JSON request body:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```

- Returns JSON response on success:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1...",
    "userId": "12345"
  }
  ```

## Advanced Usage

### Using AuthAPI Directly

You can use the AuthAPI client directly without the UI component:

```kotlin
import com.fahmi.authui.data.api.AuthAPIFactory
import com.fahmi.authui.data.request.LoginForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val authAPI = AuthAPIFactory.createAuthAPI()

CoroutineScope(Dispatchers.IO).launch {
    try {
        val response = authAPI.login(
            LoginForm(
                email = "user@example.com",
                password = "password123"
            )
        )

        if (response.isSuccessful) {
            val loginResponse = response.body()
            // Handle success
        } else {
            // Handle error
        }
    } catch (e: Exception) {
        // Handle exception
    }
}
```

### Custom OkHttp Configuration

To customize the OkHttp client, you can modify `AuthAPIFactory.kt`:

```kotlin
fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}
```

## Example Project Structure

```
authui/
├── src/main/java/com/fahmi/authui/
│   ├── AuthUi.kt                    # Configuration object
│   ├── ui/
│   │   └── LoginView.kt             # Composable UI component
│   └── data/
│       ├── api/
│       │   ├── AuthAPI.kt           # Retrofit interface
│       │   └── AuthAPIFactory.kt    # API client factory
│       ├── request/
│       │   └── LoginForm.kt         # Login request model
│       └── response/
│           └── LoginResponse.kt     # Login response model
└── build.gradle.kts
```

## Error Handling

The LoginView handles the following error scenarios:

1. **Network errors** - Connection failures, timeouts
2. **HTTP errors** - 4xx, 5xx response codes
3. **Empty response** - Server returns null body
4. **Validation** - Empty email or password fields

All errors are displayed in the UI and passed to the `onLoginError` callback.

## Requirements

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Kotlin**: 2.0.21
- **Java**: 11

## License

This module is part of the AuthUI project.
