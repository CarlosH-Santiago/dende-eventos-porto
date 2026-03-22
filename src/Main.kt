import util.ConsoleTextColor as COR

fun main() {
    var sistemaRodando = true
    do {
        val lineBar = "-".repeat(40)
        println(lineBar)
        println("Bem vindo ao Dendê Eventos")
        println(lineBar)

        // Menu principal
        println(
            "Você já é um usuário cadastrado?\n" +
                    "Escolha uma opção digitando os números\n" +
                    "1) Sim - Fazer login\n" +
                    "2) Não - Registrar-se (Novo Usuário)\n" +
                    "3) Reativar Conta\n" +
                    "0) Sair do Programa"
        )

        val opcao = readInt(COR.AMARELO + "Opção: " + COR.RESET, COR.VERMELHO + "Erro: por favor digite uma opção válida" + COR.RESET, 0..3)
        when (opcao) {
            1 -> {
                // CAPTURAMOS QUEM LOGOU
                val contaLogada = realizarLogin()

                // SE O LOGIN DEU CERTO, ENTRAMOS NA SESSÃO DELE!
                if (contaLogada != null) {
                    iniciarSessaoAtiva(contaLogada)
                }
            }
            2 -> menuCadastro()
            3 -> reativarConta()
            0 -> {
                sistemaRodando = false
                // ADICIONADO O PRINTLN AQUI:
                println(COR.AMARELO + "Saindo do Dendê Eventos." + COR.AZUL + " Até logo!" + COR.RESET)
            }
            else -> {
                println(COR.VERMELHO + "Opção inválida! Tente novamente." + COR.RESET)
            }
        }
    } while (sistemaRodando)
}
