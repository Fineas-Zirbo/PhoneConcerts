package ch.converge.pco.converter

import ch.converge.pco.dto.UserRegisterDto
import ch.converge.pco.model.LoginModel
import ch.converge.pco.model.UserModel
import org.springframework.security.crypto.password.PasswordEncoder

fun createUserModelWithLogin(userRegisterDto: UserRegisterDto, passwordEncoder: PasswordEncoder): UserModel {
    val userModel = UserModel(
            email = userRegisterDto.email,
            firstName = userRegisterDto.firstName,
            lastName = userRegisterDto.lastName,
            phone = userRegisterDto.phone,
            newsletter = userRegisterDto.newsletter)

    val loginModel = createLoginModel(userRegisterDto.password, userModel, passwordEncoder)
    userModel.logins = mutableListOf(loginModel)

    return userModel
}

fun createLoginModel(rawPassword: String, user: UserModel, passwordEncoder: PasswordEncoder): LoginModel {
    return LoginModel(userPassword = passwordEncoder.encode(rawPassword), user = user)
}