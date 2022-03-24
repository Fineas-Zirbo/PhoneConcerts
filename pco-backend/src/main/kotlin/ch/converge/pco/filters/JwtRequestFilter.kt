package ch.converge.pco.filters

import ch.converge.pco.configuration.security.PATHS_WITH_JWT_AUTHENTICATION
import ch.converge.pco.controller.utils.JWT_MOCK_VALUE
import ch.converge.pco.controller.utils.SUCCESS_EMAIL
import ch.converge.pco.controller.utils.JWT_COOKIE_NAME
import ch.converge.pco.controller.utils.addCookieToResponse
import ch.converge.pco.controller.utils.getCookie
import ch.converge.pco.dto.TokenData
import ch.converge.pco.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtUtil: JwtUtil

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (shouldBeFiltered(request)) {
            val jwtCookie: Cookie? = getCookie(request, JWT_COOKIE_NAME)
            if (jwtCookie != null && jwtCookie.maxAge != 0) {
                validateJwtAuthenticateAndSetCookie(jwtCookie, request, response)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun shouldBeFiltered(request: HttpServletRequest) =
            request.servletPath in PATHS_WITH_JWT_AUTHENTICATION

    private fun validateJwtAuthenticateAndSetCookie(cookie: Cookie, request: HttpServletRequest, response: HttpServletResponse) {
        val token = cookie.value
        val email: String? = if (JWT_MOCK_VALUE == token) SUCCESS_EMAIL else jwtUtil.extractEmail(token)

        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)
            if (JWT_MOCK_VALUE == token || jwtUtil.validateToken(token, TokenData(userDetails.username))) {
                authenticate(userDetails, request)
                addCookieToResponse(response, JWT_COOKIE_NAME, token)
            }
        }
    }

    private fun authenticate(userDetails: UserDetails, request: HttpServletRequest) {
        val usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken(userDetails.username, userDetails.password, emptyList())
        usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
    }
}