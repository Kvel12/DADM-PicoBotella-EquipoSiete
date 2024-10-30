package com.example.dadm.webservice

import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class PokemonRepository {

    private val pokemonService: PokemonService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokemonService = retrofit.create(PokemonService::class.java)
    }

    fun getRandomPokemon(callback: (Pokemon?) -> Unit) {
        pokemonService.getPokemons().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { pokemonResponse ->
                        val randomIndex = Random.nextInt(pokemonResponse.pokemon.size)
                        val randomPokemon = pokemonResponse.pokemon[randomIndex]
                        callback(randomPokemon)
                    } ?: callback(null)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

}