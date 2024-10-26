package com.example.dadm.webservice

import com.example.dadm.model.ProductModelResponse
import com.example.dadm.utils.Constants.END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getProducts(): MutableList<ProductModelResponse>
}