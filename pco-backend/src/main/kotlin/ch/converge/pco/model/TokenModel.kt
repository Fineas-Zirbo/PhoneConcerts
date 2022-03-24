package ch.converge.pco.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "token")
@Table(name = "token")
class TokenModel(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(nullable = false)
        var value: String,

        var expiry: LocalDateTime?,

        @OneToOne
        @MapsId
        @JoinColumn(name = "login_id")
        var login: LoginModel,
)