package com.example.dadm.webservice


import retrofit2.Call
import retrofit2.http.GET

interface PokemonService {
    @GET("pokedex.json")
    fun getPokemons(): Call<PokemonResponse>
}