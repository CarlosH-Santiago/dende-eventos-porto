package entities

import enums.StatusIngresso

data class Ingresso(
    // [Critério 7 - val/var em Data Class] 'id' é 'val', correto — o identificador do ingresso não muda.
    val id: Int,
    // [Critério 18 - Relacionamento OO] 'idUsuario' armazena o ÍNDICE do usuário na lista, funcionando como "chave estrangeira" frágil.
    // Se a lista for reordenada ou um usuário removido, o índice apontará para o usuário errado.
    // Em orientação a objetos, o relacionamento é feito por referência ao objeto, não por índice primitivo.
    // Talvez, se você substituísse por 'val emailUsuario: String' ou diretamente 'val usuario: UsuarioComum',
    // o relacionamento ficaria mais seguro e dispensaria o uso de 'listaUsuarios.indexOf(usuarioLogado)'.
    val idUsuario: Int,
    // [Critério 18 - Relacionamento OO] 'idEvento' armazena apenas o ID do evento como Int, funcionando como "chave estrangeira".
    // Talvez, se você substituísse por 'val evento: Evento', o código ficaria mais OO e você acessaria os dados do evento diretamente.
    val idEvento: Int,
    // [Critério 8 - Valores Financeiros] 'precoPago' é 'Double', o que pode causar imprecisão em cálculos de estorno.
    // Talvez, se você adotasse 'BigDecimal', as operações financeiras ficariam mais precisas e confiáveis.
    // A variável seria declarada da seguinte forma: val precoPago: BigDecimal
    val precoPago: Double,
    // [Critério 7 - val/var em Data Class] 'status' é 'var', correto — o status muda de ATIVO para CANCELADO.
    // [Critério 17 - Valor Inicial em Data Class] 'StatusIngresso.ATIVO' como padrão faz sentido — ingresso nasce ativo ao ser comprado.
    var status: StatusIngresso = StatusIngresso.ATIVO
)
