package com.wafflestudio.snuboard.domain.model

data class Department(
        val id: Int,
        val name: String,
        val tags: List<String>,
        val follow: List<String>,
        val departmentColor: DepartmentColor
)

data class FollowingDepartment(
        val id: Int,
        val name: String,
        val follow: List<String>,
        val departmentColor: DepartmentColor
)

sealed class CollegeDepartment(open val id: Int, open val name: String) {
    data class EngDepartment(
            override val id: Int,
            override val name: String
    ) : CollegeDepartment(id, name)

    companion object {
        fun fromDepartmentIdName(departmentId: Int, departmentName: String): CollegeDepartment =
                when (departmentName) {
                    in engDepartmentNames -> EngDepartment(departmentId, departmentName)
                    else -> throw Error("Cannot find College of the department")
                }

        private val engDepartmentNames = listOf("기계공학부", "컴퓨터공학부", "전기정보공학부", "화학생물공학부", "에너지자원공학과")
    }
}

data class TagDepartment(
        val id: Int,
        val name: String,
        val tags: List<Tag>,
        val departmentColor: DepartmentColor
)