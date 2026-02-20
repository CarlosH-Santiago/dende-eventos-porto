package entities

import enums.CategoriaEvento
import enums.Modalidade
import java.time.LocalDateTime

data class Evento(
    val id: Int,
    var nome: String,
    var descricao: String,
    var dataInicio: LocalDateTime,
    var dataFim: LocalDateTime,
    var tipo: CategoriaEvento,
    var modalidade: Modalidade,
    var capacidadeTotal: Int,
    var local: String, // Endereço ou Link
    var preco: Double,
    var ativo: Boolean = false, // Padrão inativo ao criar
    val idOrganizador: String, // Email do organizador para vincular
    var ingressosVendidos: Int = 0,
    var estornaDinheiro: Boolean = false,
    var taxaEstorno: Double = 0.0,
    var idEventoVinculado: Int? = null // Opcional (US 6)
)
