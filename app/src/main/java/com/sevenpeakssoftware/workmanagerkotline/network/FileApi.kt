package com.sevenpeakssoftware.workmanagerkotline.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface FileApi {

    @GET("/sites/default/files/styles/max_1300x1300/public/image_nodes/earth-tree.jpg?itok=XjzGqPMv")
    suspend fun  downloadImageFile(): Response<ResponseBody>

    companion object{
        val instance by lazy {
            Retrofit
                .Builder()
                .baseUrl("https://www.almanac.com")
                .build().create(FileApi::class.java)
        }
    }


}