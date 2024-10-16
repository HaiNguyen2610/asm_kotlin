package fpoly.hainxph46327.broken_rice_food.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import fpoly.hainxph46327.broken_rice_food.R
import fpoly.hainxph46327.broken_rice_food.model.Food
import fpoly.hainxph46327.broken_rice_food.viewModel.HomeViewModel

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeView()
        }
    }
}

@Composable
fun HomeView() {
    val viewModel: HomeViewModel = viewModel()
    var searchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var selectedFood by remember { mutableStateOf<Food?>(null) }

    LaunchedEffect(searchText) {
        viewModel.searchFoods(searchText)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Poly Food",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { searchVisible = !searchVisible }) {
                Image(
                    painterResource(id = R.drawable.ic_search),
                    contentDescription = "button search",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        if (searchVisible) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.searchFoods(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(15.dp),
                placeholder = { Text(text = "Nhập đồ ăn muốn tìm...") }
            )
        }
        ListFood(foods = viewModel.foods) { food ->
            selectedFood = food
        }
    }

    // Hiển thị dialog khi có món ăn được chọn
    selectedFood?.let { food ->
        FoodDetailDialog(food = food) {
            selectedFood = null
        }
    }
}

@Composable
fun ListFood(foods: List<Food>, onFoodClick: (Food) -> Unit) {
    val gridState = rememberLazyStaggeredGridState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = gridState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(8.dp),
    ) {
        items(foods) { food ->
            Card(
                onClick = { onFoodClick(food) },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
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
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        BoldValueText(
                            label = "Giá tiền: ", value = food.price
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodDetailDialog(food: Food, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = food.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        text = {
            Column {
                AsyncImage(
                    model = food.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(180.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Giá: ${food.price}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Mô tả: ${food.description}", style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Đóng")
            }
        }
    )
}

@Composable
fun BoldValueText(label: String, value: Any) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = label, style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f) // Chia đều không gian cho title
        )
        Text(
            text = value.toString(), style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f) // Chia đều không gian cho value
        )
    }
}
