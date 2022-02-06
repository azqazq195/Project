package com.example.jenkins.service

import com.example.jenkins.dto.CreateUserDTO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserServiceTest(
    @Autowired
    private var userService: UserService
) {

    @BeforeEach
    fun setUp() {
        println("start user service test")
    }

    @AfterEach
    fun tearDown() {
        println("end user service test")
    }

    @Test
    fun getUser() {
        val user = CreateUserDTO(name = "성하", email = "azqazq195@gmail.com", password = "password")
        val storedUser = userService.createUser(user)

        assertTrue(storedUser.id!! > 0)
        assertEquals(user.name, storedUser.name)
        assertEquals(user.email, storedUser.email)
        assertEquals(user.password, storedUser.password)
    }

    @Test
    fun createUser() {
        assertTrue(false)
    }

    @Test
    fun exception() {
        throw RuntimeException("일부러 에러")
    }
}