package com.wafflestudio.snuboard.domain.translater

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.wafflestudio.snuboard.data.retrofit.dto.DepartmentDto
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.model.Department
import com.wafflestudio.snuboard.domain.model.DepartmentColor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class DepartmentMapper
@Inject
constructor(@ApplicationContext appContext: Context) {

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    fun mapFromListDepartmentDto(dto: List<DepartmentDto>): List<Department> {
        return dto.map {
            mapFromDepartmentDto(it)
        }
    }

    fun mapFromDepartmentDto(dto: DepartmentDto): Department {
        val preferenceKey = SharedPreferenceConst.getDepartmentColorKey(dto.id)
        var colorId = pref.getInt(preferenceKey, -1)
        if (colorId == -1) {
            val r = Random.nextInt(0, 9)
            pref.edit {
                when (r) {
                    0 -> putInt(preferenceKey, DepartmentColor.SKY.colorId)
                    1 -> putInt(preferenceKey, DepartmentColor.POMEGRANATE.colorId)
                    2 -> putInt(preferenceKey, DepartmentColor.CITRUS.colorId)
                    3 -> putInt(preferenceKey, DepartmentColor.JADE.colorId)
                    4 -> putInt(preferenceKey, DepartmentColor.KOREAN_DAISY.colorId)
                    5 -> putInt(preferenceKey, DepartmentColor.PEA.colorId)
                    6 -> putInt(preferenceKey, DepartmentColor.MEDITERRANEAN.colorId)
                    7 -> putInt(preferenceKey, DepartmentColor.AMETHYST.colorId)
                    8 -> putInt(preferenceKey, DepartmentColor.LAVENDER.colorId)
                }
            }
            colorId = when (r) {
                0 -> DepartmentColor.SKY.colorId
                1 -> DepartmentColor.POMEGRANATE.colorId
                2 -> DepartmentColor.CITRUS.colorId
                3 -> DepartmentColor.JADE.colorId
                4 -> DepartmentColor.KOREAN_DAISY.colorId
                5 -> DepartmentColor.PEA.colorId
                6 -> DepartmentColor.MEDITERRANEAN.colorId
                7 -> DepartmentColor.AMETHYST.colorId
                8 -> DepartmentColor.LAVENDER.colorId
                else -> throw error("Not right random number.")
            }
        }
        return Department(dto.id, dto.name, dto.college, dto.link, dto.tags, dto.follow, DepartmentColor.fromColorId(colorId)!!)
    }
}
