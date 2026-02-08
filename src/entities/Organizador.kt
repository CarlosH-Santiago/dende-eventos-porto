package entities

import enums.Sexo
import java.time.LocalDate

data class Organizador(
    val nome: String,
    val dataNacimento: LocalDate,
    val sexo: Sexo,
    val email: String,
    val senha: String,
    val cnpj: String? = null,
    val razaoSocial: String? = null,
    val nomeFantasia: String? = null,
    val ativo: Boolean = true
)
