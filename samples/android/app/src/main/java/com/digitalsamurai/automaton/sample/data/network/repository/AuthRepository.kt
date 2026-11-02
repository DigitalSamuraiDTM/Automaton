package com.digitalsamurai.automaton.sample.data.network.repository

import android.content.Context
import com.digitalsamurai.opentelemetry.example.core.network.models.Jwt
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ТАК ДЕЛАТЬ НИКОГДА НЕ НАДО!!! НЕ СТОИТ ХРАНИТЬ АВТОРИЗАЦИОННЫЕ ТОКЕНЫ В НЕЗАШИФРОВАННОМ ХРАНИЛИЩЕ
 */
class AuthRepository @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
) {

    private val preferences = applicationContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    @Volatile
    private var cache: Jwt? = null

    suspend fun set(jwt: Jwt) {
        preferences.edit().putString(PREFERENCE_KEY, jwt.value).also {
            cache = jwt
        }.apply()
    }

    suspend fun get(): Jwt?  {
        return cache ?: preferences.getString(PREFERENCE_KEY, null)?.let {
            cache = Jwt(it)
            cache
        }
    }

    fun isTokenExist(): Boolean {
        return (cache ?: preferences.getString(PREFERENCE_KEY, null)) != null
    }

    private companion object {
        const val PREFERENCE_NAME = "secret"
        const val PREFERENCE_KEY  = "auth_key"
    }
}