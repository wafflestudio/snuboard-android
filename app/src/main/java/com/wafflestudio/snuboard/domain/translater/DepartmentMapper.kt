package com.wafflestudio.snuboard.domain.translater

import com.wafflestudio.snuboard.data.retrofit.dto.DepartmentDto
import com.wafflestudio.snuboard.domain.model.Department
import com.wafflestudio.snuboard.domain.model.DepartmentColor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepartmentMapper
@Inject
constructor() {

    fun mapFromDepartmentDto(dto: List<DepartmentDto>): List<Department> {
        return dto.map {
            Department(it.id, it.name, it.tags, it.follow, DepartmentColor.SKY)
        }
    }
}