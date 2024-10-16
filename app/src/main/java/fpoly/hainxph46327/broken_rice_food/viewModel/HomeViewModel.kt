package fpoly.hainxph46327.broken_rice_food.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fpoly.hainxph46327.broken_rice_food.model.Food
import fpoly.hainxph46327.broken_rice_food.retrofit_request.RetrofitRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel:ViewModel() {
    private val _foods = mutableStateListOf<Food>()
    private val _allFoods = mutableListOf<Food>()
    val foods: List<Food> = _foods

    init {
        fetchFoods()
    }

    private fun fetchFoods() {
        viewModelScope.launch(Dispatchers.IO) { // Thực hiện trong background thread
            val retrofit = Retrofit.Builder()
                .baseUrl("https://comtam.phuocsangbn.workers.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val request = retrofit.create(RetrofitRequest::class.java)
            request.getFoods().enqueue(object : Callback<List<Food>> {
                override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                    response.body()?.let {
                        // Thực hiện cập nhật giao diện trên Main Thread
                        viewModelScope.launch(Dispatchers.Main) {
                            _foods.clear()
                            _allFoods.clear()
                            _foods.addAll(it)
                            _allFoods.addAll(it)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                    Log.e("HomeViewModel", "Failed to fetch foods", t)
                }
            })
        }
    }
    fun addFood(food: Food) {
        viewModelScope.launch(Dispatchers.IO) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://comtam.phuocsangbn.workers.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val request = retrofit.create(RetrofitRequest::class.java)

            try {
                val response = request.addFood(food)

                if (response.isSuccessful) {
                    response.body()?.let { foodResponse ->
                        Log.d("HomeViewModel", "Thêm món ăn thành công: ${foodResponse.name}")
                        _foods.add(foodResponse)
                        _allFoods.add(foodResponse)
                    } ?: Log.e("HomeViewModel", "Thêm món ăn thất bại: Không nhận được phản hồi từ server.")
                } else {
                    Log.e("HomeViewModel", "Thêm món ăn thất bại: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Thêm món ăn thất bại: ${e.message}", e)
            }
        }
    }






    fun updateFood(id: Int, food: Food) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://comtam.phuocsangbn.workers.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request = retrofit.create(RetrofitRequest::class.java)
        request.updateFood(id, food).enqueue(object : Callback<Food> {
            override fun onResponse(call: Call<Food>, response: Response<Food>) {
                if (response.isSuccessful) {
                    val updatedFood = response.body()
                    updatedFood?.let {
                        // Cập nhật món ăn trong danh sách
                        val index = _foods.indexOfFirst { it.id == id }
                        if (index != -1) {
                            _foods[index] = it
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Food>, t: Throwable) {
                // Xử lý lỗi
            }
        })
    }

    fun deleteFood(id: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://comtam.phuocsangbn.workers.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request = retrofit.create(RetrofitRequest::class.java)
        request.deleteFood(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _foods.removeAll { it.id == id }
                    _allFoods.removeAll { it.id == id }
                } else {
                    // Xử lý trường hợp không thành công
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Xử lý lỗi
            }
        })
    }

    fun searchFoods(query: String) {
        if (query.isBlank()) {
            _foods.clear()
            _foods.addAll(_allFoods) // Nếu không có từ khóa, hiển thị tất cả món ăn
        } else {
            // Lọc danh sách dựa trên từ khóa
            val filteredFoods = _allFoods.filter { food ->
                food.name.contains(query, ignoreCase = true) // So sánh không phân biệt chữ hoa chữ thường
            }

            // Nếu không tìm thấy món ăn nào, bạn có thể hiển thị thông báo hoặc xử lý khác
            if (filteredFoods.isEmpty()) {
                // Có thể hiển thị một thông báo hoặc một danh sách trống
                // Ví dụ: _foods.clear() hoặc để nguyên danh sách
            }

            _foods.clear()
            _foods.addAll(filteredFoods) // Cập nhật danh sách món ăn hiển thị
        }
    }
}