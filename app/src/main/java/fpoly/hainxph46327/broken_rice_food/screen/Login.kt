package fpoly.hainxph46327.broken_rice_food.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import fpoly.hainxph46327.broken_rice_food.R
import fpoly.hainxph46327.broken_rice_food.viewModel.LoginViewModel
import kotlinx.coroutines.delay

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}
@Composable
fun LoginScreen(nav: NavHostController) {
   val loginViewModel: LoginViewModel = viewModel()
    val loginState = loginViewModel.loginState.value


    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painterResource(id = R.drawable.food_logo),
            contentDescription = "button search",
            modifier = Modifier
                .size(350.dp)
                .fillMaxSize()
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text(text = "Enter your Username") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            shape = RoundedCornerShape(15.dp),
            placeholder = { Text(text = "Enter your password") },
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedButton(
            onClick = { loginViewModel.login(username, password)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)

        ) {
            Row {
                Text(
                    text = "Login ",
                    color = Color.White,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            }
        }

        when (loginState) {
            is LoginViewModel.LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginViewModel.LoginState.Error -> {
                Text(text = loginState.message, color = Color.Red)
            }
            is LoginViewModel.LoginState.Success -> {
                LaunchedEffect(Unit) {
                    delay(500) // Giả lập độ trễ nếu cần thiết
                    nav.navigate("home")  // Điều hướng đến màn hình dành cho User
                }
            }
            is LoginViewModel.LoginState.AdminSuccess->{
                LaunchedEffect(Unit) {
                    delay(500)  // Giả lập độ trễ nếu cần thiết
                    nav.navigate("homeAdmin")  // Điều hướng đến màn hình dành cho Admin
                }
            }
            else -> {}
        }
    }
}
