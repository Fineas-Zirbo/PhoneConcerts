package ch.converge.pco.service.user

import ch.converge.pco.data.UserData
import ch.converge.pco.dto.UserRegisterDto

interface UserService {
    fun registerUser(userData: UserRegisterDto): Boolean
    fun findUserByEmail(email: String): UserData?
}