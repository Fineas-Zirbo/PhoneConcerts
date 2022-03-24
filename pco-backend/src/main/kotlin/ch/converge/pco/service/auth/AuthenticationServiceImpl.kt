package ch.converge.pco.service.auth

import ch.converge.pco.controller.utils.addCookieToResponse
import ch.converge.pco.dto.TokenData
import ch.converge.pco.dto.UserLoginDto
import ch.converge.pco.util.JwtUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletResponse

@Service
class AuthenticationServiceImpl : AuthenticationService {

    val logger: Logger = LoggerFactory.getLogger(AuthenticationServiceImpl::class.java)

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    override fun authenticateUserAndSetJwtCookie(userLoginDto: UserLoginDto, httpServletResponse: HttpServletResponse): Boolean {
        var authenticated = false
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(userLoginDto.email, userLoginDto.password))
            val jwt: String = jwtUtil.generateToken(TokenData(userLoginDto.email))
            addCookieToResponse(httpServletResponse, "jwt", jwt)
            authenticated = true
            logger.info("User ${userLoginDto.email} authenticated successfully.")
        } catch (e: AuthenticationException) { // todo create and catch a more specific exception
            logger.warn("Incorrect username or password ${userLoginDto.email}")
        }
        return authenticated
    }
}