package fpoly.hainxph46327.broken_rice_food.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import fpoly.hainxph46327.broken_rice_food.model.User
import fpoly.hainxph46327.broken_rice_food.result.LoginResponse
import fpoly.hainxph46327.broken_rice_food.retrofit_request.RetrofitRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel : ViewModel() {

    var loginState = mutableStateOf<LoginState>(LoginState.Idle)
        private set
    private val adminUsername = "admin"
    private val adminPassword = "admin123"
    fun login(username: String, password: String) {
        loginState.value = LoginState.Loading
        Log.d("LoginViewModel", "Username: $username, Password: $password")

         if (username == adminUsername && password == adminPassword) {
            // Đăng nhập thành công với vai trò Admin
            loginState.value = LoginState.AdminSuccess
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://comtam.phuocsangbn.workers.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request = retrofit.create(RetrofitRequest::class.java)
        val call = request.login(username, password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    Log.d("LoginViewModel", "Response: $loginResponse")

                    if (loginResponse.errorcode == 0) {
                        loginState.value = LoginState.Success(loginResponse)
                    } else {
                        // Đăng nhập thất bại
                        loginState.value = LoginState.Error("Đăng nhập thất bại: ${loginResponse.message}")
                    }
                } else {
                    // Mã trạng thái không phải 2xx
                    Log.d("LoginViewModel", "Error body: ${response.errorBody()?.string()}")
                    loginState.value = LoginState.Error("Đăng nhập thất bại")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginState.value = LoginState.Error(t.message ?: "Đã xảy ra lỗi")
            }
        })
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
        data object AdminSuccess : LoginState()
    }
}



