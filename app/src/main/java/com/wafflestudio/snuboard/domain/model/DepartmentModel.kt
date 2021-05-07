package com.wafflestudio.snuboard.domain.model

data class Department(
        val id: Int,
        val name: String,
        val college: String,
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

data class CollegeDepartment(val id: Int, val name: String, val college: String) {

     companion object {
          const val ENGINEERING = "공과대학"
          const val HUMANITIES = "인문대학"
          const val SOCIAL = "사회과학대학"
          const val SCIENCE = "자연과학대학"
          const val NURSING = "간호대학"
          const val CBA = "경영대학"
          const val CALS = "농업생명과학대학"
          const val ART = "미술대학"
          const val EDU = "사범대학"
          const val CHE = "생활과학대학"
          const val VET = "수의과대학"
          const val PHARM = "약학대학"
          const val MUSIC = "음악대학"
          const val MEDICINE = "의과대학"
          const val CLS = "자유전공학부"
     }
}

data class TagDepartment(
    val id: Int,
    val name: String,
    val tags: List<Tag>,
    val departmentColor: DepartmentColor
)

data class TagDepartmentFull(
    val id: Int,
    val name: String,
    val tags: List<Tag>,
    val homeTags: List<Tag>,
    val departmentColor: DepartmentColor
)
