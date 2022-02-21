package com.example.metindogun_challenge.data.api

import com.example.metindogun_challenge.data.model.LocationPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("?method=flickr.photos.search")
    suspend fun getLocationPhoto(@Query("lat") lat: String,
                                 @Query("lon") long: String,
                                 @Query("format") format: String = "json",
                                 @Query("nojsoncallback") nojsoncallback: Int = 1,
                                 @Query("per_page") perPage: Int = 1,
                                 @Query("radius") radius: Float = 0.1f
    ): Response<LocationPhoto>

}