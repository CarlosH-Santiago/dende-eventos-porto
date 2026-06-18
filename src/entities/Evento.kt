package entities

import enums.CategoriaEvento
import enums.Modalidade
import java.math.BigDecimal
import java.time.LocalDateTime

data class Evento(
    // [Critério 7 - val/var em Data Class] 'id' é 'val', correto — o identificador único do evento não deve mudar.
    val id: Int,
    // [Critério 7 - val/var em Data Class] 'nome', 'descricao', 'dataInicio', 'dataFim', 'tipo', 'modalidade',
    // 'capacidadeTotal' e 'local' são 'var', o que está correto pois a US 8 permite alterar os dados do evento.
    var nome: String,
    var descricao: String,
    var dataInicio: LocalDateTime,
    var dataFim: LocalDateTime,
    var tipo: CategoriaEvento,
    var modalidade: Modalidade,
    var capacidadeTotal: Int,
    var local: String, // Endereço ou Link
    // [Critério 8 - Valores Financeiros] 'preco' é declarado como 'Double', o que pode causar erros de precisão em operações financeiras.
    // Talvez, se você adotasse 'BigDecimal' para representar valores monetários, as operações de soma e divisão ficariam mais precisas.
    // A variável seria declarada da seguinte forma: var preco: BigDecimal = BigDecimal.ZERO
    var preco: BigDecimal = BigDecimal.ZERO.setScale(2), // [Critério 8 - Valores Financeiros] Usando BigDecimal para maior precisão
    // [Critério 7 - val/var em Data Class] 'ativo = false' por padrão está correto — o evento precisa ser ativado explicitamente (US 9).
    // [Critério 17 - Valor Inicial em Data Class] Bom uso do valor padrão: evento nasce inativo.
    var ativo: Boolean = false, // Padrão inativo ao criar
    // [Critério 18 - Relacionamento OO] 'idOrganizador' armazena o e-mail do organizador como String, funcionando como "chave estrangeira".
    // Em um projeto orientado a objetos, o relacionamento é feito por referência ao objeto, não por um identificador primitivo.
    // Talvez, se você substituísse por uma referência ao próprio objeto, o código ficaria mais OO e dispensaria buscas manuais na lista:
    // val organizador: Organizador
    val idOrganizador: String, // [Critério 18 - Relacionamento OO] Armazena o e-mail do organizador como String
    // [Critério 7 - val/var em Data Class] 'ingressosVendidos = 0' é 'var', correto pois é incrementado/decrementado.
    // [Critério 17 - Valor Inicial em Data Class] Valor inicial 0 faz sentido — nenhum ingresso vendido ao criar.
    var ingressosVendidos: Int = 0,
    // [Critério 17 - Valor Inicial em Data Class] 'estornaDinheiro = false' por padrão faz sentido (opt-in). Correto.
    var estornaDinheiro: Boolean = false,
    // [Critério 8 - Valores Financeiros] 'taxaEstorno' é 'Double'. Assim como 'preco', deveria ser 'BigDecimal'.
    // Talvez, se você adotasse 'BigDecimal', o cálculo do valor estornado ficaria mais preciso e confiável.
    // A variável seria declarada da seguinte forma: var taxaEstorno: BigDecimal = BigDecimal.ZERO
    var taxaEstorno: BigDecimal = BigDecimal.ZERO.setScale(2), // [Critério 8 - Valores Financeiros] Usando BigDecimal para maior precisão
    // [Critério 18 - Relacionamento OO] 'idEventoVinculado' armazena o ID do evento pai como Int, funcionando como "chave estrangeira".
    // Em orientação a objetos, o relacionamento seria feito por referência direta ao objeto pai, evitando buscas manuais na lista:
    // var eventoPrincipal: Evento? = null
    var idEventoVinculado: Int? = null // Opcional (US 6)
)
