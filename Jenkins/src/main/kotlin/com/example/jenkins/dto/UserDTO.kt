package com.example.jenkins.dto

import com.example.jenkins.entity.User
import java.time.OffsetDateTime

data class CreateUserDTO (
    val name: String,
    val email: String,
    val password: String,
) {
    fun toEntity() : User {
        return User(
            name = name,
            email = email,
            password = password,
            createdDate = OffsetDateTime.now(),
        )
    }
}

data class ReadUserDTO(
    val id: Long,
    val name: String,
    val email: String,
    val createdDate: OffsetDateTime,
    val updatedDate: OffsetDateTime?,
)

