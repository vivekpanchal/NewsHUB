package com.vivekpanchal.newshub.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.vivekpanchal.newshub.BuildConfig
import com.vivekpanchal.newshub.data.remote.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cache(Cache(File(context.cacheDir, "http"), 10L * 1024 * 1024))
            // Header (not query param) keeps the key out of URLs, logs and cache keys.
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().header("X-Api-Key", BuildConfig.NEWS_API_KEY).build())
            }
            // Offline: serve the last cached response, however stale, instead of failing.
            .addInterceptor { chain ->
                try {
                    chain.proceed(chain.request())
                } catch (e: IOException) {
                    chain.proceed(chain.request().newBuilder().cacheControl(CacheControl.FORCE_CACHE).build())
                }
            }
            // NewsAPI sends no-cache headers; store responses anyway and treat them fresh for 5 min.
            .addNetworkInterceptor { chain ->
                chain.proceed(chain.request()).newBuilder()
                    .header("Cache-Control", "public, max-age=300")
                    .removeHeader("Pragma")
                    .build()
            }
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
}
