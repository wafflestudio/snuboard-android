package com.wafflestudio.snuboard.di

object SharedPreferenceConst {
    const val ACCESS_TOKEN_KEY = "access_token_key"
    const val REFRESH_TOKEN_KEY = "refresh_token_key"

    private const val DEPARTMENT_COLOR_KEY_PREFIX = "department_color_key_"
    fun getDepartmentColorKey(departmentId: Int): String =
            DEPARTMENT_COLOR_KEY_PREFIX + departmentId.toString()

    private const val DEPARTMENT_HOME_KEY_PREFIX = "department_home_key_"
    fun getDepartmentHomeKey(departmentId: Int): String =
            DEPARTMENT_HOME_KEY_PREFIX + departmentId.toString()

    private const val DEPARTMENT_NOTI_KEY_PREFIX = "department_noti_key_"
    fun getDepartmentNotiKey(departmentId: Int): String =
            DEPARTMENT_NOTI_KEY_PREFIX + departmentId.toString()

    const val IS_NOTIFICATION_ACTIVE_KEY = "is_notification_active_key"
}
