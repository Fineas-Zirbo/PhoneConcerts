package ch.converge.pco.controller

import ch.converge.pco.controller.utils.*
import ch.converge.pco.data.UserData
import ch.converge.pco.dto.*
import ch.converge.pco.service.auth.AuthenticationService
import ch.converge.pco.service.user.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/user")
class UserController {

    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/register")
    fun register(@RequestBody userRegisterDto: UserRegisterDto, httpServletResponse: HttpServletResponse): LoginResponseDto {
        logger.info("Trying to register user ${userRegisterDto.email}")
        var authenticated = false

        // todo validate user data (email, password etc)

        val registered: Boolean = userService.registerUser(userRegisterDto)
        if (registered) {
            authenticated =
                    authenticationService.authenticateUserAndSetJwtCookie(UserLoginDto(userRegisterDto.email, userRegisterDto.password), httpServletResponse)
        }

        return buildLoginResponseDto(authenticated, registered, UserDto(userRegisterDto.email, userRegisterDto.firstName))
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginDto: UserLoginDto, httpServletResponse: HttpServletResponse): LoginResponseDto {

        logger.info("Trying to login user ${userLoginDto.email}")

        val authenticated: Boolean =
                authenticationService.authenticateUserAndSetJwtCookie(userLoginDto, httpServletResponse)

        return buildLoginResponseDto(authenticated, UserDto(userLoginDto.email, SUCCESS_FIRST_NAME))
    }

    @GetMapping("/isAuthenticated")
    fun isAuthenticated(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse): AuthenticatedDto {
        val jwtValue = getCookieValue(httpServletRequest, "jwt")
        logger.info("isAuthenticated check for: $jwtValue")

        var isAuthenticated = false
        var userDto: UserDto? = null
        if (jwtValue == JWT_MOCK_VALUE) {
            isAuthenticated = true
            userDto = UserDto(SUCCESS_EMAIL, SUCCESS_FIRST_NAME)
        }

        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication is UsernamePasswordAuthenticationToken && authentication.isAuthenticated) {
            isAuthenticated = true
            userDto = UserDto(authentication.principal as String, SUCCESS_FIRST_NAME)
        }

        return AuthenticatedDto(isAuthenticated, userDto)
    }

    @GetMapping("/account")
    fun getUserData(@RequestParam email: String): UserData? {
        logger.info("Retrieving account info for user $email")

        return userService.findUserByEmail(email)
    }

    @PostMapping("/logout")
    fun logout(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        logger.info("Logging out...")
        deleteCookie(httpServletRequest, httpServletResponse, "jwt")
    }
}