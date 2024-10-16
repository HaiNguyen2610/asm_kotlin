package fpoly.hainxph46327.broken_rice_food.retrofit_request

import fpoly.hainxph46327.broken_rice_food.model.Food
import fpoly.hainxph46327.broken_rice_food.result.FoodResponse
import fpoly.hainxph46327.broken_rice_food.result.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitRequest {
    //Dinh nghia request food
    //dinh nghia doi tuwng de nap du lieu
    //json vao object List cua kotlin
    @GET("/foods")
    fun getFoods(): Call<List<Food>>

    @GET("deleteFood/{id}") // Đường dẫn cần bao gồm {id}
    fun deleteFood(@Path("id") id: Int): Call<Void>

    @POST("/createFood")
    suspend fun addFood(@Body foodRequest: Food): Response<Food>

    @POST("searchFood")
    fun searchFoods(@Body text: Map<String, String>): Call<List<Food>>

    @POST("updateFood/{id}") // Đảm bảo URL có chứa {id}
    fun updateFood(@Path("id") id: Int, @Body food: Food): Call<Food>
    @POST("/login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

}
