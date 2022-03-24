package ch.converge.pco.service.auth

import ch.converge.pco.dto.UserLoginDto
import javax.servlet.http.HttpServletResponse

interface AuthenticationService {
    fun authenticateUserAndSetJwtCookie(userLoginDto: UserLoginDto, httpServletResponse: HttpServletResponse): Boolean
}