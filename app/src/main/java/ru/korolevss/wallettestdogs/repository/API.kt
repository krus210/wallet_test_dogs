package ru.korolevss.wallettestdogs.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.korolevss.wallettestdogs.model.Breeds
import ru.korolevss.wallettestdogs.model.Images

interface API {

    @GET("api/breeds/list/all")
    suspend fun getAllBreeds(): Response<Breeds>

    @GET("api/breed/{breed}/images")
    suspend fun getImagesByBreed(@Path("breed") breedName: String): Response<Images>

    @GET("api/breed/{breed}/{subbreed}/images")
    suspend fun getImagesBySubbreed(
        @Path("breed") breedName: String,
        @Path("subbreed") subbreedName: String
    ): Response<Images>

}