package ch.converge.pco.configuration.security

val PATHS_WITH_NO_JWT_AUTHENTICATION = arrayOf("/api/user/login",
                                               "/api/user/register",
                                               "/api/user/logout")

val PATHS_WITH_JWT_AUTHENTICATION = arrayOf("/api/user/isAuthenticated",
                                            "/api/user/account",
                                            "/api/concerts",
                                            "/api/concert")