package com.example.falconfituser.authentication

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val authenticationService: AuthenticationService
):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        if(chain.request().method == "POST" && (chain.request().url.encodedPath == "api/auth/local" ||
                                                chain.request().url.encodedPath == "api/auth/local/register")
        ){
            return chain.proceed(chain.request())
        }

        val token: String? =
            runBlocking {
                authenticationService.getJwtToken()?.let {
                    return@runBlocking it
                }
                    return@runBlocking null
            }

        token?.let {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $it")
                .build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(chain.request())
    }

}