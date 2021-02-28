package com.wafflestudio.snuboard.domain.model

import com.wafflestudio.snuboard.R

enum class DepartmentColor(val colorId: Int) {
    BLACK(R.color.black),
    SKY(R.color.sky),
    POMEGRANATE(R.color.pomegranate),
    CITRUS(R.color.citrus),
    JADE(R.color.jade),
    KOREAN_DAISY(R.color.korean_daisy),
    PEA(R.color.pea),
    MEDITERRANEAN(R.color.mediterranean),
    AMETHYST(R.color.amethyst),
    LAVENDER(R.color.lavender),
    TAG_COLOR(R.color.gray4);

    companion object {
        fun fromColorId(colorId: Int): DepartmentColor? =
                values().find { it.colorId == colorId }
    }
}

data class Tag(val content: String, val color: DepartmentColor)