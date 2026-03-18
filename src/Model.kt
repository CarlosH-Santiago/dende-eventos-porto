import java.time.LocalDate
import java.time.LocalDateTime

// ENUMS
enum class Sexo {
    MASCULINO,
    FEMININO,
    OUTROS
}

enum class Modalidade {
    PRESENCIAL,
    REMOTO,
    HIBRIDO
}

enum class CategoriaEvento(val descricao: String) {
    SOCIAL("Social"),
    CORPORATIVO("Corporativo"),
    ACADEMICO("Acadêmico"),
    CULTURAL("Cultural/Entretenimento"),
    RELIGIOSO("Religioso"),
    ESPORTIVO("Esportivo"),
    FEIRA("Feira"),
    CONGRESSO("Congresso"),
    OFICINA("Oficina"),
    CURSO("Curso"),
    TREINAMENTO("Treinamento"),
    AULA("Aula"),
    SEMINARIO("Seminário"),
    PALESTRA("Palestra"),
    SHOW("Show"),
    FESTIVAL("Festival"),
    EXPOSICAO("Exposição"),
    RETIRO("Retiro"),
    CULTO("Culto"),
    CELEBRACAO("Celebração"),
    CAMPEONATO("Campeonato"),
    OUTRO("Outro"),
    CORRIDA("Corrida");
}

enum class StatusIngresso {
    ATIVO,
    CANCELADO
}


//CLASSES
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

data class Ingresso(
    val id: Int,
    val idUsuario: Int,
    val idEvento: Int,
    val precoPago: Double,
    var status: StatusIngresso = StatusIngresso.ATIVO
)

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

data class UsuarioComum(
    var nome: String,
    var dataNascimento: LocalDate,
    var sexo: Sexo,
    val email: String,
    var senha: String,
    var ativo: Boolean = true
)