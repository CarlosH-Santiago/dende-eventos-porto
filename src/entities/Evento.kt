package entities

import enums.CategoriaEvento
import enums.Modalidade
import java.time.LocalDateTime

data class Evento(
    val id: Int,
    val idOrganizador: Int,
    var paginaEvento: String,
    var nome: String,
    var descricao: String,
    var dataInicio: LocalDateTime,
    var dataFim: LocalDateTime,
    var tipo: CategoriaEvento,
    var idEventoVinculado: Int? = null,
    var modalidade: Modalidade,
    var capacidadeTotal: Int,
    var local: String,
    var ativo: Boolean = false,
    var preco: Double,
    var estornaValor: Boolean,
    var taxaEstorno: Double,
    var ingressosVendidos: Int = 0
)
