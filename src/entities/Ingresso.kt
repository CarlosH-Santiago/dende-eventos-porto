package entities

import enums.StatusIngresso

data class Ingresso(
    val id: Int,
    val idUsuario: Int,
    val idEvento: Int,
    val precoPago: Double,
    var status: StatusIngresso = StatusIngresso.ATIVO
)
