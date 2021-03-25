package com.wafflestudio.snuboard.domain.translater

import com.wafflestudio.snuboard.data.retrofit.dto.UserDto
import com.wafflestudio.snuboard.data.retrofit.dto.UserTokenDto
import com.wafflestudio.snuboard.domain.model.User
import javax.inject.Inject

class UserMapper
@Inject
constructor() {
    fun mapFromUserDto(dto: UserDto): User {
        return User(dto.id, dto.username, dto.nickname, dto.keywords)
    }

    fun mapFromUserTokenDto(dto: UserTokenDto): User {
        return User(dto.id, dto.username, dto.nickname, dto.keywords)
    }
}
