package ch.converge.pco.data

data class UserData(
        var email: String,
        var firstName: String,
        var lastName: String,
        var phone: String?,
        var password: String? = null,
        var newsletter: Boolean? = false
)