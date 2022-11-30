package com.codinginflow.imagesearchapp.api

import com.codinginflow.imagesearchapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {
    //getting api key from gradle local properties
    companion object{
        const val BASE_URL= "https://api.unsplash.com/"
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    //this case the header is mandatory for a request since api demands it, here its and api version and acces key
    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
       @Query("query")query: String,
       @Query("page")page: Int,
       @Query("per_page")perPage: Int
    ): UnsplashResponse
    //:UnsplashResponse is what we get from this request which is one object of photo




}