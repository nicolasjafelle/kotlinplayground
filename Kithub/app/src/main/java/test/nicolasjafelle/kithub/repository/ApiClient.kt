package test.nicolasjafelle.kithub.repository


import com.jakewharton.picasso.OkHttp3Downloader
import test.nicolasjafelle.kithub.BuildConfig
import test.nicolasjafelle.kithub.Tls12SocketFactory
import test.nicolasjafelle.kithub.KithubApplication
import test.nicolasjafelle.kithub.api.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by nicolas on 11/9/17.
 */
class ApiClient private constructor() {

    companion object {

        private const val TIMEOUT_MILLIS: Long = 40000

        private val TIMEOUT_UNIT = TimeUnit.MILLISECONDS

        private const val DISK_CACHE_SIZE: Long = 10 * 1024 * 1024

        private lateinit var retrofit: Retrofit

        private lateinit var okHttpDownloader: OkHttp3Downloader

        val instance: ApiService by lazy {
            retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig::HOST.get())
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            retrofit.create(ApiService::class.java)
        }

        private fun getOkHttpClient(): OkHttpClient {

            val builder = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()

            if (BuildConfig::DEBUG.get()) {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            }

            builder.retryOnConnectionFailure(true)
            builder.connectTimeout(TIMEOUT_MILLIS, TIMEOUT_UNIT)
            builder.readTimeout(TIMEOUT_MILLIS, TIMEOUT_UNIT)
            builder.writeTimeout(TIMEOUT_MILLIS, TIMEOUT_UNIT)
            builder.addInterceptor(loggingInterceptor)
            builder.cache(getCache())

            val client = Tls12SocketFactory.enableTls12OnPreLollipop(builder).build()
            okHttpDownloader = OkHttp3Downloader(client)

            return client
        }

        private fun getCache(): Cache {
            val file = File(KithubApplication.instance.cacheDir, "cache")
            return Cache(file, DISK_CACHE_SIZE)
        }

    }

}