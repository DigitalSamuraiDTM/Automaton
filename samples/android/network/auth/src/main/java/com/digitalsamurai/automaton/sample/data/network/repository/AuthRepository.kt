package com.digitalsamurai.automaton.sample.data.network.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private var cache: String? = null

    /**
     * Сохраняет авторизационный токен.
     *
     * @param jwt значение токена.
     */
    suspend fun set(jwt: String) {
        preferences.edit().putString(PREFERENCE_KEY, jwt).also {
            cache = jwt
        }.apply()
    }

    /**
     * Возвращает сохранённый авторизационный токен.
     *
     * @return токен или `null`, если авторизация ещё не выполнялась.
     */
    suspend fun get(): String? {
        return cache ?: preferences.getString(PREFERENCE_KEY, null)?.let {
            cache = it
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