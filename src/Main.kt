
import util.ConsoleTextColor as COR

fun main() {
     var sistemaRodando = true 
        do {
            ¹val lineBar = "-".repeat(40)
            println(lineBar)
       	 println("Bem vindo ao Dedê Eventos")
      	  println(lineBar)

        // Menu de login/registro de usuario
 	       println("Você já é um usuário cadastrado?\n" +
                "Escolha uma opção digitando os números\n" +
                "1) Sim - Fazer login\n" +
                "2) Não - Registrar-se (Novo Usuário)\n" +
                "3) Reativar Conta\n" +
                "0) Sair do Programa")
            print(COR.AMARELO + "Opção: " + COR.RESET)
       
       	 val opcao = readln().toIntOrNull() ?: -1
        
        // a linha abaixo é para usar a validação automática!
        // val opcao = readInt(COR.AMARELO + "Opção: " + COR.RESET, COR.VERMELHO + "Erro: por favor digite uma opção válida" + COR.RESET, 0..3)
        	when (opcao) {
    			1 -> realizarLogin()
    			2 -> menuCadastro()
                3 -> reativarConta()
                0 -> {
                    sistemaRodando = false
                    (COR.AMARELO + "Saindo do Dendê Eventos." + COR.AZUL + " Até logo!" + COR.RESET)
                }

   	 } while (sistemaRodando)
}