package fpoly.hainxph46327.broken_rice_food

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fpoly.hainxph46327.broken_rice_food.screen.AdminView
import fpoly.hainxph46327.broken_rice_food.screen.HomeAdminScreen
import fpoly.hainxph46327.broken_rice_food.screen.HomeView
import fpoly.hainxph46327.broken_rice_food.screen.LoginScreen
import fpoly.hainxph46327.broken_rice_food.screen.Welcome
import fpoly.hainxph46327.broken_rice_food.screen.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Home()
        }
    }
}
@Composable
fun Home() {
    // Khởi tạo NavController
    val navController = rememberNavController()
    // Thiết lập NavHost
    NavHost(
        navController = navController,
        startDestination = "welcome" // Màn hình mặc định
    ) {
        // Định nghĩa các màn hình (routes)
        composable("welcome") { WelcomeScreen(navController) }
        composable("signIn") { LoginScreen(navController) }
        composable("home") { HomeView() }
        composable("homeAdmin") { AdminView() }
    }
}
