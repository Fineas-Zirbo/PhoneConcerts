package ch.converge.pco.service.user

import ch.converge.pco.converter.createUserModelWithLogin
import ch.converge.pco.dao.UserDao
import ch.converge.pco.data.UserData
import ch.converge.pco.dto.UserRegisterDto
import ch.converge.pco.model.UserModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun registerUser(userRegisterDto: UserRegisterDto): Boolean {
        var registered = false;

        val userExists: Boolean = userDao.userExistsByEmail(userRegisterDto.email)
        if (!userExists) {
            val userModel: UserModel = createUserModelWithLogin(userRegisterDto, passwordEncoder)
            userDao.save(userModel)
            registered = true;
        } else {
            logger.info("User with email ${userRegisterDto.email} already exists.")
        }

        return registered
    }

    override fun findUserByEmail(email: String): UserData? {
        val userModel: UserModel? = userDao.findUserByEmail(email)
        return userModel?.let { convertUserModelToUserData(it) }
    }

    private fun convertUserModelToUserData(userModel: UserModel): UserData {
        return UserData(
                userModel.email,
                userModel.firstName,
                userModel.lastName,
                userModel.phone,
                null,
                userModel.newsletter
        )
    }
}