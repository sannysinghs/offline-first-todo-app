package com.tixeon.todos.android.util

import javax.inject.Inject
import javax.inject.Singleton

interface CryptoHelper {
    fun encrypt(data: String): String?
    fun decrypt(data: String): String?
}

@Singleton
class DefaultCryptoHelper @Inject constructor(
) : CryptoHelper {

    override fun encrypt(data: String): String? =
        ""

    override fun decrypt(data: String): String? =
        ""
}