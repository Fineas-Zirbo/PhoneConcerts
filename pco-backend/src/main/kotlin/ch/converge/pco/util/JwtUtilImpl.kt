package ch.converge.pco.util

import ch.converge.pco.dto.TokenData
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import kotlin.reflect.KFunction1

const val SECRET_KEY_STRING = "temp_secret_key_temp_secret_key_temp_secret_key_temp_secret_key"
val SECRET_KEY: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.toByteArray())

@Component
class JwtUtilImpl : JwtUtil {

    val logger = LoggerFactory.getLogger(JwtUtil::class.java)

    override fun extractEmail(token: String): String? {
        return getClaimForTokenSafe(token, Claims::getSubject)
    }

    private fun <T> getClaimForTokenSafe(token: String, claimsResolver: KFunction1<Claims, T>): T? {
        return try {
            getClaimForToken(token, claimsResolver)
        } catch (e: Exception) {
            logger.error(e.message, e)
            null
        }
    }

    override fun extractExpiration(token: String): Date? {
        return getClaimForTokenSafe(token, Claims::getExpiration)
    }

    private fun <T> getClaimForToken(token: String, claimsResolver: KFunction1<Claims, T>): T {
        val claims: Claims = getAllClaimsForToken(token);
        return claimsResolver.call(claims)
    }

    private fun getAllClaimsForToken(token: String): Claims {
        val parseClaimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token)
        return parseClaimsJws.body
    }

    override fun isTokenExpired(token: String): Boolean {
        val extractExpiration: Date? = extractExpiration(token)
        return extractExpiration?.before(Date()) ?: false
    }

    override fun generateToken(tokenData: TokenData): String {
        return Jwts.builder()
                .setSubject(tokenData.email)
                .signWith(SECRET_KEY)
                .compact();
    }

    override fun validateToken(token: String, tokenData: TokenData): Boolean {
        return try {
            val email: String? = extractEmail(token)
            email == tokenData.email && !isTokenExpired(token)
        } catch (e: JwtException) {
            logger.error(e.message, e)
            false
        }
    }
}