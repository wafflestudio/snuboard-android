package com.wafflestudio.snuboard.di

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.BuildConfig
import com.wafflestudio.snuboard.data.retrofit.dto.UserTokenDto
import com.wafflestudio.snuboard.data.retrofit.service.DepartmentService
import com.wafflestudio.snuboard.data.retrofit.service.NoticeService
import com.wafflestudio.snuboard.data.retrofit.service.UserService
import com.wafflestudio.snuboard.di.AuthInterceptor.Companion.AUTHORIZATION
import com.wafflestudio.snuboard.di.SharedPreferenceConst.ACCESS_TOKEN_KEY
import com.wafflestudio.snuboard.di.SharedPreferenceConst.REFRESH_TOKEN_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Response as Okhttp3Response

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authenticator: TokenAuthenticator
    ) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .authenticator(authenticator)
                .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .addInterceptor(authInterceptor)
        .authenticator(authenticator)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.TEST_SERVER_BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideDepartmentService(
        retrofit: Retrofit
    ): DepartmentService =
        retrofit.create(DepartmentService::class.java)

    @Provides
    @Singleton
    fun provideNoticeService(
        retrofit: Retrofit
    ): NoticeService =
        retrofit.create(NoticeService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
        retrofit: Retrofit
    ): UserService =
        retrofit.create(UserService::class.java)

}

@Singleton
class AuthInterceptor
@Inject
constructor(@ApplicationContext appContext: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Okhttp3Response {
        val accessToken = pref.getString(ACCESS_TOKEN_KEY, null)
        return if (accessToken != null) {
            val auth = "Bearer $accessToken"
            val request = chain.request()
            val builder = request.newBuilder().header(AUTHORIZATION, auth)
            chain.proceed(builder.build())
        } else {
            chain.proceed(chain.request())
        }

    }

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}

@Singleton
class TokenAuthenticator
@Inject
constructor(@ApplicationContext appContext: Context) : Authenticator {

    override fun authenticate(route: Route?, response: Okhttp3Response): Request? {
        val refreshToken = pref.getString(REFRESH_TOKEN_KEY, null)
        if (response.code == 401 && refreshToken != null) {
            val auth = "Bearer $refreshToken"
            val call: Call<UserTokenDto> =
                    userService.authWithToken("refresh_token", auth)
            val tokenResponse: Response<UserTokenDto> = call.execute()
            if (tokenResponse.isSuccessful) {
                var newRefreshToken: String? = null
                var newAccessToken: String? = null
                tokenResponse.body()?.let {
                    newRefreshToken = it.refreshToken
                    newAccessToken = it.accessToken
                }

                pref.edit {
                    putString(REFRESH_TOKEN_KEY, newRefreshToken)
                    putString(ACCESS_TOKEN_KEY, newAccessToken)
                }

                return response.request.newBuilder()
                        .header(AUTHORIZATION, "Bearer $newAccessToken")
                        .build()
            }
        }
        return null
    }

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    private val client: OkHttpClient = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    } else OkHttpClient
            .Builder()
        .build()

    private val userService: UserService = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.TEST_SERVER_BASE_URL)
        .client(client)
        .build()
        .create(UserService::class.java)
}