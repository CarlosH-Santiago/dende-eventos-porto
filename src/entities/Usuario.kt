package entities

import enums.Sexo
import java.time.LocalDate

data class UsuarioComum(
    val nome: String,
    val dataNacimento: LocalDate,
    val sexo: Sexo,
    val email: String,
    val senha: String,
    val ativo: Boolean = true
)
