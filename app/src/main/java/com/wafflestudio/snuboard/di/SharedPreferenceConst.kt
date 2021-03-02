package com.wafflestudio.snuboard.di

object SharedPreferenceConst {
    const val ACCESS_TOKEN_KEY = "access_token_key"
    const val REFRESH_TOKEN_KEY = "refresh_token_key"

    private const val DEPARTMENT_KEY_PREFIX = "department_key_"
    fun getDepartmentKey(departmentId: Int): String =
            DEPARTMENT_KEY_PREFIX + departmentId.toString()
}