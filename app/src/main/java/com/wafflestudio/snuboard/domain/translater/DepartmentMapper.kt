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

@Singleton
class DepartmentMapper
@Inject
constructor(@ApplicationContext appContext: Context) {

    private val pref = PreferenceManager.getDefaultSharedPreferences(
            appContext
    )

    fun mapFromListDepartmentDto(dto: List<DepartmentDto>): List<Department> {
        return dto.map {
            val preferenceKey = SharedPreferenceConst.getDepartmentKey(it.id)
            var colorId = pref.getInt(preferenceKey, -1)
            if (colorId == -1) {
                pref.edit {
                    putInt(preferenceKey, DepartmentColor.POMEGRANATE.colorId)
                }
                colorId = DepartmentColor.POMEGRANATE.colorId
            }
            Department(it.id, it.name, it.tags, it.follow, DepartmentColor.fromColorId(colorId)!!)
        }
    }

    fun mapFromDepartmentDto(dto: DepartmentDto): Department {
        val preferenceKey = SharedPreferenceConst.getDepartmentKey(dto.id)
        var colorId = pref.getInt(preferenceKey, -1)
        if (colorId == -1) {
            pref.edit {
                putInt(preferenceKey, DepartmentColor.POMEGRANATE.colorId)
            }
            colorId = DepartmentColor.POMEGRANATE.colorId
        }
        return Department(dto.id, dto.name, dto.tags, dto.follow, DepartmentColor.fromColorId(colorId)!!)
    }
}