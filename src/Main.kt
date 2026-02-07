//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import entities.Organizador
import entities.UsuarioComum
import enums.Sexo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Period
import util.ConsoleTextColor as COR

fun main() {
    // Banco de dados na memoria para os usuarios e organizadores
    val listaUsuarios = mutableListOf<UsuarioComum>()
    val listaOrganizadores = mutableListOf<Organizador>()
    val lineBar = "-".repeat(40)

    // Formatador para ler datas no padrão dd/MM/yyyy
    val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // loop para o funcionamento do programa
    var sistemaRodando = true
    while (sistemaRodando) {
        println(lineBar)
        println("Bem vindo ao Dedê Eventos")
        println(lineBar)

        // Menu de login/reegistro de usuario
        println("Você já é um usuario cadastrado?\n" +
                "Escolha uma opção digitando os números\n" +
                "1) Sim - Fazer login\n" +
                "2) Não - Registrar-se (Novo Usuário)\n" +
                "3) Reativar Conta\n" +
                "0) Sair do Programa")
                println(lineBar)

        print(COR.AMARELO + "Opção: ")
        val inputAutenticacao = readln().toInt()
        when(inputAutenticacao) {
            0 -> {
                println(COR.AMARELO + "Saindo do Dendê Eventos." + COR.AZUL + " Até logo!" + COR.RESET)
                sistemaRodando = false
            }
            1 -> {
                println("\n---- LOGIN ----")
                println("Digite seu Email: ")
                val emailLogin = readln().trim()
                println("Digite sua Senha: ")
                val senhaLogin = readln().trim()

                // varre a lista para encontrar o usario
                val usuarioLogado = listaUsuarios.find { it.email == emailLogin && it.senha == senhaLogin}
                val organizadorLogado = listaOrganizadores.find { it.email == emailLogin && it.senha == senhaLogin}

                if (usuarioLogado != null || organizadorLogado != null) {
                    println(lineBar)
                    println(COR.VERDE + "Login realizado com sucesso!" + COR.RESET)
                    println(lineBar)


                    var sessaoAtiva = true
                    while (sessaoAtiva) {
                        println("\n---- MENU LOGADO ----")
                        println(lineBar)

                        // Logica para menu especifico para cada tipo de usuario
                        if (organizadorLogado != null) {
                            println("Olá Organizador ${organizadorLogado.nome}!")
                            println("1) Meu Perfil")
                            println("2) Alterar dados do Perfil")
                            println("3) Inativar Minha Conta")
                            println("4) Gerenciar Eventos")
                            println(lineBar)
                        } else if (usuarioLogado != null){
                            println("Olá Usuario ${usuarioLogado.nome}!")
                            println("1) Meu Perfil")
                            println("2) Alterar dados do Perfil")
                            println("3) Inativar Minha Conta")
                            println("4) Ver feed de Eventos")
                            println(lineBar)
                        } else {
                            println(COR.VERMELHO + "Erro no Sistema por favor reinicie o programa" + COR.RESET)
                            sessaoAtiva = false
                        }
                        println("0) Sair (logout)")
                        println(lineBar)

                        println("Escolha: ")
                        val opcaoMenuLogado = readln().toInt()
                        println(lineBar)

                        when(opcaoMenuLogado) {
                            0 -> {
                                println(COR.AMARELO + "Realizando logout..." + COR.RESET)
                                sessaoAtiva = false
                            }
                            1 -> {}
                            2 -> {}
                            3 -> {}
                            4 -> {}
                        }
                    }
                } else {
            println("ERRO: Usuário ou senha inválidos (ou conta inativa).")
        }
            }

            2 -> {
                println("\n---- REGISTRO DE NOVO USUÁRIO ----")
                println("Para qual finalidade gostaria de Criar sua conta?")
                println("1) Quero participar de Eventos (Usuario Comum)")
                println("2) Quero Organizar Eventos (Conta de Organizador")
                println("0) Voltar")
                println(lineBar)

                val opcaoResgistroConta = readln().toInt()
                println(lineBar)
                when(opcaoResgistroConta) {
                    0 -> {
                        sistemaRodando = true
                    }
                    1 -> {
                        var criandoUsuarioComum = true
                        while (criandoUsuarioComum) {
                            print("\nDigite seu email: ")
                            val inputEmail = readln().trim()
                            // Verificação de formato correto do email, criterio: Conter o @ e 5 ou mais caracteres
                            if (inputEmail.contains("@") && inputEmail.length >= 5){
                                // Variaveis para verificação de duplicidade de email nos usuarios comuns e organizadores
                                val verificarDuplicidadeEmailUsuarioComum = listaUsuarios.any { it.email == inputEmail}
                                val verificarDuplicidadeEmailOrganizador = listaOrganizadores.any { it.email == inputEmail}
                                // Condicional Verifdicando emails duplicados
                                if (verificarDuplicidadeEmailUsuarioComum || verificarDuplicidadeEmailOrganizador) {
                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO +  " Email informado ja cadastrado, por favor efetui o login ou utilize um email diferente" + COR.RESET)
                                }else {
                                    println(COR.VERDE + "E-mail válido e disponível. Prosseguindo..." + COR.RESET)
                                }
                            } else {
                                println( COR.VERMELHO+ "ERRO: " + COR.AMARELO + "Email no formato incorreto. O e-mail precisa ter '@' e possuir mais de 4 caracteres\n" +
                                        " Por favor digite novamente" + COR.RESET)
                            }

                            print("\nDigite sua senha: ")
                            val inputSenha = readln().trim()
                            print("\nDigite novamente sua senha: ")
                            val inputSenhaConfirmacao = readln().trim()

                        }
                    }
                    2 -> {

                    }
                    else -> println(COR.VERMELHO + "ERRO: Opção Inválida." + COR.RESET)
                }
            }
            3 -> {

            }

        }
    }
}
