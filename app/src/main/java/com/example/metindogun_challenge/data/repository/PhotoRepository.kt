package com.example.metindogun_challenge.data.repository

import com.example.metindogun_challenge.data.api.ApiService
import com.example.metindogun_challenge.data.api.Result
import com.example.metindogun_challenge.data.api.apiCall
import com.example.metindogun_challenge.data.model.Photo
import java.lang.Exception
import javax.inject.Inject

interface PhotoRepository{

    suspend fun getLocationPhoto(lat: String, lon: String): Photo?

}

class PhotoRepositoryImpl @Inject constructor(private val apiService: ApiService): PhotoRepository {

    override suspend fun getLocationPhoto(lat: String, lon: String): Photo? {

            val result =  apiCall { apiService.getLocationPhoto(lat, lon) }

            when(result){
                is Result.Success -> {
                    if (result.data.photos.photo.isEmpty()){
                        return null
                    }
                    return result.data.photos.photo[0]
                }
                is Result.Error -> {
                    return null
                }
            }

    }
}