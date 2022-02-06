package com.example.jenkins.repository

import com.example.jenkins.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {

}