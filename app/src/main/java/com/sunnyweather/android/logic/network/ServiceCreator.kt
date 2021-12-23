package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 为了使定义的网络接口可以使用，还得创建一个Retrofit构建器
 * 相当于通过一个接口将对应的实现类构建出来？（代理对象）
 * 在这里加的编码器，将json数据转换为对应的类对象
 */
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}