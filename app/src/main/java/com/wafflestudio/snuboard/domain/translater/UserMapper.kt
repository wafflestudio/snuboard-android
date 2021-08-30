package com.wafflestudio.snuboard.domain.translater

import com.wafflestudio.snuboard.data.retrofit.dto.UserTokenDto
import com.wafflestudio.snuboard.domain.model.User
import javax.inject.Inject

class UserMapper
@Inject
constructor() {

    fun mapFromUserTokenDto(dto: UserTokenDto): User {
        return User(dto.id)
    }
}
