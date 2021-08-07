package com.wafflestudio.snuboard.domain.model

import android.content.Context
import com.wafflestudio.snuboard.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class User(
        val id: Int,
        val username: String,
        val email: String,
        val keywords: List<String>
)

data class Member(
        val name: String,
        val tags: List<Tag>,
        val github: String?,
        val email: String,
        val web: String?
)

@Singleton
class Team
@Inject
constructor(@ApplicationContext appContext: Context) {

        init {
                val res = appContext.resources
                JINUK = Member(
                        res.getString(R.string.JINUK_NAME),
                        listOf(
                                Tag("Android", DepartmentColor.JADE),
                                Tag("PO", DepartmentColor.TAG_COLOR),
                                Tag("21 S/S", DepartmentColor.TAG_COLOR)
                        ),
                        res.getString(R.string.JINUK_GITHUB),
                        res.getString(R.string.JINUK_EMAIL),
                        null
                )
                MYUNGHOON = Member(
                        res.getString(R.string.MYUNGHOON_NAME),
                        listOf(
                                Tag("Server", DepartmentColor.POMEGRANATE),
                                Tag("21 S/S", DepartmentColor.TAG_COLOR)
                        ),
                        res.getString(R.string.MYUNGHOON_GITHUB),
                        res.getString(R.string.MYUNGHOON_EMAIL),
                        null
                )
                SANGGYU = Member(
                        res.getString(R.string.SANGGYU_NAME),
                        listOf(
                                Tag("Server", DepartmentColor.POMEGRANATE),
                                Tag("21 S/S", DepartmentColor.TAG_COLOR)
                        ),
                        res.getString(R.string.SANGGYU_GITHUB),
                        res.getString(R.string.SANGGYU_EMAIL),
                        null
                )
                SANGMIN = Member(
                        res.getString(R.string.SANGMIN_NAME),
                        listOf(
                                Tag("Server", DepartmentColor.POMEGRANATE),
                                Tag("21 S/S", DepartmentColor.TAG_COLOR)
                        ),
                        res.getString(R.string.SANGMIN_GITHUB),
                        res.getString(R.string.SANGMIN_EMAIL),
                        null
                )
                SUBEEN = Member(
                        res.getString(R.string.SUBEEN_NAME),
                        listOf(
                                Tag("iOS", DepartmentColor.KOREAN_DAISY),
                                Tag("21 S/S", DepartmentColor.TAG_COLOR)
                        ),
                        res.getString(R.string.SUBEEN_GITHUB),
                        res.getString(R.string.SUBEEN_EMAIL),
                        res.getString(R.string.SUBEEN_WEB)
                )
                NAKYUNG = Member(
                        res.getString(R.string.NAKYUNG_NAME),
                        listOf(
                                Tag("Design", DepartmentColor.LAVENDER),
                                Tag("21 S/S", DepartmentColor.TAG_COLOR)
                        ),
                        null,
                        res.getString(R.string.NAKYUNG_EMAIL),
                        null
                )
        }

        companion object {
                lateinit var JINUK: Member
                lateinit var MYUNGHOON: Member
                lateinit var SANGGYU: Member
                lateinit var SANGMIN: Member
                lateinit var SUBEEN: Member
                lateinit var NAKYUNG: Member
        }
}
