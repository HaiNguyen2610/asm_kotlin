package fpoly.hainxph46327.broken_rice_food.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import fpoly.hainxph46327.broken_rice_food.model.Food
import fpoly.hainxph46327.broken_rice_food.viewModel.HomeViewModel

class HomeAdminScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}
// AdminView Composable
@Composable
fun AdminView() {
    val viewModel: HomeViewModel = viewModel()
    var selectedFood by remember { mutableStateOf<Food?>(null) }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Quản lý món ăn", style = MaterialTheme.typography.headlineMedium)

            ListFood(
                foods = viewModel.foods,
                onEdit = { food -> selectedFood = food }, // Chọn món ăn để sửa
                onDelete = { food -> viewModel.deleteFood(food.id) } // Xóa món ăn
            )
        }

        // Floating Action Button để thêm món ăn mới
        FloatingActionButton(
            onClick = {
                selectedFood = Food(id = 0, name = "", price = 0.0, thumbnail = "", description = "")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Đặt nút ở dưới cùng bên phải
                .padding(16.dp) // Padding cho nút
        ) {
            Text("+")
        }
    }

    // Hiển thị thông tin chi tiết món ăn
    if (selectedFood != null) {
        FoodDetailDialog(food = selectedFood!!, onDismiss = { selectedFood = null }, onSave = { food ->
            if (food.id == 0) {
                viewModel.addFood(food) // Thêm món ăn mới
            } else {
                viewModel.updateFood(food.id, food) // Cập nhật món ăn
            }
            selectedFood = null // Đóng dialog
        })
    }
}


@Composable
fun ListFood(foods: List<Food>, onEdit: (Food) -> Unit, onDelete: (Food) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight() // Đảm bảo danh sách chiếm hết chiều cao và có thể cuộn
            .padding(16.dp) // Thêm padding nếu cần
    ) {
        items(foods) { food ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = { onEdit(food) }
            ) {
                Column {
                    AsyncImage(
                        model = food.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .wrapContentHeight()
                            .size(width = 180.dp, height = 180.dp)
                    )
                    Text(text = food.name)
                    Button(onClick = { onDelete(food) }) {
                        Text("Xóa")
                    }
                }
            }
        }
    }
}


// FoodDetailDialog Composable
@Composable
fun FoodDetailDialog(food: Food, onDismiss: () -> Unit, onSave: (Food) -> Unit) {
    var name by remember { mutableStateOf(food.name) }
    var price by remember { mutableStateOf(food.price.toString()) }
    var thumbnail by remember { mutableStateOf(food.thumbnail) }
    var description by remember { mutableStateOf(food.description) } // Thêm description

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Thông tin món ăn") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên món ăn") }
                )
                TextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Giá tiền") }
                )
                TextField(
                    value = thumbnail,
                    onValueChange = { thumbnail = it },
                    label = { Text("URL hình ảnh") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it }, // Thêm trường mô tả
                    label = { Text("Mô tả") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val newFood = food.copy(name = name, price = price.toDoubleOrNull() ?: 0.0, thumbnail = thumbnail, description = description)
                onSave(newFood) // Gọi onSave với newFood
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Hủy")
            }
        }
    )
}
