package entities

import enums.Sexo
import java.time.LocalDate

data class UsuarioComum(
    var nome: String,
    var dataNacimento: LocalDate,
    var sexo: Sexo,
    val email: String,
    var senha: String,
    var ativo: Boolean = true
)
