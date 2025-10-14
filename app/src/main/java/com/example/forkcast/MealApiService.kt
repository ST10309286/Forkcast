package com.example.forkcast.network

import com.example.forkcast.MealsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    suspend fun getRandomMeal(): MealsResponse?

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") id: String): MealsResponse?

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealsResponse


}
