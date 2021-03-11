package com.lifefighter.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module


/**
 * @author xzp
 * @created on 2021/3/9.
 */
object NetUtils {
    val netClientModule = module {
        single<OkHttpClient> {
            val logInterceptor = HttpLoggingInterceptor {
                logDebug(it)
            }
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder().addInterceptor(logInterceptor).build()
        }
    }
}
