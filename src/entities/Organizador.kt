package entities

import enums.Sexo
import java.time.LocalDate

data class Organizador(
    var nome: String,
    var dataNascimento: LocalDate,
    var sexo: Sexo,
    val email: String,
    var senha: String,
    var cnpj: String? = null,
    var razaoSocial: String? = null,
    var nomeFantasia: String? = null,
    var ativo: Boolean = true
)
