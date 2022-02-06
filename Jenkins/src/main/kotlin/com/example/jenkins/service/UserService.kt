package com.example.jenkins.service

import com.example.jenkins.dto.CreateUserDTO
import com.example.jenkins.entity.User
import com.example.jenkins.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class UserService(
    @Autowired
    private var userRepository: UserRepository
) {


    @Transactional
    fun getUser(id: Long): User {
        return userRepository.findById(id).orElseThrow()
    }

    @Transactional
    fun createUser(createUserDTO: CreateUserDTO): User {
        return userRepository.save(createUserDTO.toEntity())
    }
}