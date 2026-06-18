package entities

import enums.Sexo
import java.time.LocalDate

data class UsuarioComum(
    // [Critério 7 - val/var em Data Class] 'nome' é 'var', correto — pode ser alterado (US 3).
    var nome: String,
    // [Critério 7 - val/var em Data Class] 'dataNascimento' é 'var', mas a data de nascimento de uma pessoa não muda.
    // Talvez, se você declarasse como 'val', ficaria mais claro que esse campo não pode ser alterado após o cadastro.
    // A variável seria declarada da seguinte forma: val dataNascimento: LocalDate
    var dataNascimento: LocalDate,
    // [Critério 7 - val/var em Data Class] 'sexo' é 'var', correto — pode ser alterado (US 3).
    var sexo: Sexo,
    // [Critério 7 - val/var em Data Class] 'email' é 'val', correto — a US 3 proíbe explicitamente a alteração do e-mail.
    val email: String,
    // [Critério 7 - val/var em Data Class] 'senha' é 'var', correto — pode ser alterada (US 3).
    var senha: String,
    // [Critério 7 - val/var em Data Class] 'ativo' é 'var', correto — pode ser desativado/reativado (US 5 e 6).
    // [Critério 17 - Valor Inicial em Data Class] 'true' como padrão faz sentido — usuário nasce ativo após o cadastro.
    var ativo: Boolean = true
)
