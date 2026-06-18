import entities.Evento
import entities.Ingresso
import entities.Organizador
import entities.UsuarioComum
import enums.Sexo
import enums.StatusIngresso
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Period
import util.ConsoleTextColor as COR

fun main() {

    // Banco de dados na memória para os usuários e organizadores
    val listaUsuarios = mutableListOf<UsuarioComum>()
    val listaOrganizadores = mutableListOf<Organizador>()
    val listaEventos = mutableListOf<Evento>()
    val listaIngressos = mutableListOf<Ingresso>()
    var proximoIdIngresso = 1
    val lineBar = "-".repeat(40)

    // Formatador para ler datas no padrão dd/MM/yyyy
    val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // loop para o funcionamento do programa
    // [Critério 2 - Loop Adequado] 'while(sistemaRodando)' usa uma variável booleana como flag de controle, o que é equivalente a um while(true).
    // loops while(true) não são uma boa prática de programação, pois dificultam a compreensão da condição de parada.
    // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
    //   var opcao: Int
    //   do {
    //       // exibe menu principal
    //       opcao = readln().toIntOrNull() ?: 0
    //       when(opcao) { 1 -> {} ... }
    //   } while(opcao != 0)
    //   println("Saindo do Dendê Eventos...")
    var sistemaRodando = true
    while (sistemaRodando) {

        /**
         * O ideal aqui era essa parte ficar num loop de do..while.
         * No início, você teria o when
         * depois a impressão do menu
         * e por fim, vocês teriam a validação do na condição do while.
         *
         * ex:
         *
         * var inputAutenticadao: Int = 0
         *
         * do {
         *      when(inputAutenticadao) {
         *          0 -> {}
         *          1 -> { codigo}
         *          2 -> {}
         *          3 -> {}
         *          else -> println("Opção inválida")
         *      }
         *      prints do menu
         * } while(inputAutenticacao < 0 || inputAutenticacao > 3)
         * println(COR.AMARELO + "Saindo do Dendê Eventos." + COR.AZUL + " Até logo!" + COR.RESET)
         */
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
        println(lineBar)

        print(COR.AMARELO + "Opção: " + COR.RESET)
        val inputAutenticacao = readln().toIntOrNull() ?: 0
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

                // [Critério 9 - Busca Unificada] Vocês procuram em duas listas separadas e depois gerenciam dois objetos distintos ('usuarioLogado' e 'organizadorLogado') por todo o código.
                // Talvez, se você unificasse a busca usando o operador Elvis '?:', obteria um único objeto e usaria 'when' para tratar cada tipo.
                // A busca seria feita da seguinte forma:
                //   val usuarioLogado = listaUsuarios.find { it.email == emailLogin && it.senha == senhaLogin }
                //       ?: listaOrganizadores.find { it.email == emailLogin && it.senha == senhaLogin }
                // E o menu separado da seguinte forma:
                //   when(usuarioLogado) {
                //       is UsuarioComum -> { /* menu do usuário comum */ }
                //       is Organizador  -> { /* menu do organizador */ }
                //   }
                // [Critério 6 - Separação de Fluxo] Isso também permitiria separar completamente o menu do organizador do menu do usuário comum,
                // eliminando os 'if(organizadorLogado != null)' / 'else if(usuarioLogado != null)' repetitivos dentro de cada opção.
                val usuarioLogado = listaUsuarios.find { it.email == emailLogin && it.senha == senhaLogin}
                val organizadorLogado = listaOrganizadores.find { it.email == emailLogin && it.senha == senhaLogin}


                // [Critério 13 - Operador ?.] Com a busca unificada sugerida acima, você poderia simplificar toda essa verificação para:
                //   if (usuarioLogado?.ativo == true) { ... }
                // O operador '?.' já lida com o null de forma segura, eliminando o 'if != null' externo.
                //vocês poderiam deixar apenas esse if, pois o ?. faz a checagem do null
                // if(usuario?.ativo == true)
                if (usuarioLogado != null || organizadorLogado != null) {
                    if (usuarioLogado?.ativo == true || organizadorLogado?.ativo == true) {
                        println(lineBar)
                        println(COR.VERDE + "Login realizado com sucesso!" + COR.RESET)
                        println(lineBar)
                        // [Critério 2 - Loop Adequado] 'while(sessaoAtiva)' usa uma flag booleana como condição de parada, o que é equivalente a um while(true).
                        // loops while(true) não são uma boa prática de programação, pois dificultam a compreensão da condição de parada.
                        // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                        //   var opcaoMenuLogado: Int
                        //   do {
                        //       when(opcaoMenuLogado) { ... }
                        //       // exibe menu
                        //       opcaoMenuLogado = readln().toIntOrNull() ?: 0
                        //   } while(opcaoMenuLogado != 0)
                        //   println("Realizando logout...")
                        // [Critério 6 - Separação de Fluxo] Além disso, separar o fluxo do organizador e do usuário comum tornaria o código mais legível:
                        //   if (usuarioLogado is Organizador) {
                        //       do { /* menu organizador */ } while(opcao != 0)
                        //   } else {
                        //       do { /* menu usuário comum */ } while(opcao != 0)
                        //   }
                        // Assim, cada bloco teria apenas as funcionalidades do seu tipo, sem 'if(organizadorLogado != null)' repetitivos.
                        var sessaoAtiva = true

                        /* qual a diferença desse cara para um while true?
                          por que agora você tem uma variável de controle?
                          no final, você fica com duas variáveis de controle a sessaoAtiva e a opcaoMenuLogado
                          não é uma boa abordagem. Você deveria ter apenas a opcaoMenuLogado.

                          mas como fazer isso?

                          1. cria a variável opcaoMenuLogado já com a opção de saída por padrão
                          2. A condição de parada deve ser enquanto o valor digitado for igual ao valor de saída
                          3. use o do..while
                          4. coloque o when logo no início
                          5. coloque a leitura no final, coladinho com o while
                          6. faça a condição do while(opcaoMenuLogado != 0)
                          println(COR.AMARELO + "Realizando logout..." + COR.RESET)

                          eu tenho uma outra sugestão aqui:
                          eu separaria todo o fluxo de organizador e do usuário comum.
                          faria um while para cada com as coisas de cada um baseado nos seus tipos.
                          ficaria mais fácil de compreender e separaria as responsabilidades

                          tipo:

                          if( usuario is UsuarioComum) {

                            do {


                            } while(opcao != 0)

                          }  else {

                            do {


                            while(opcao != 0)

                          }

                          por quê? porque tudo que fosse do usuário comum ficaria em cima
                          e tudo que fosse do organizador em baixo, fica mais fácil de ler o código assim
                          cada um com sua responsabilidade. Até a manutenção seria melhor.


                         */
                        while (sessaoAtiva) {
                            println("\n---- MENU LOGADO ----")
                            println(lineBar)
                            // Logica para menu especifico para cada tipo de usuário
                            //if(usuario is Organizador)
                            if (organizadorLogado != null) {
                                println("Olá Organizador ${organizadorLogado.nome}!")
                                println("1) Meu Perfil")
                                println("2) Alterar dados do Perfil")
                                println("3) Inativar Minha Conta")
                                println("4) Gerenciar Eventos")
                                println(lineBar)
                            }
                            // [Critério 15 - when no lugar de if..else if] Como só existem dois tipos de usuário,
                            // o 'else if' aqui pode ser simplificado para um simples 'else', pois se não é organizador, só pode ser usuário comum.
                            //nesse caso só o else basta
                            else if (usuarioLogado != null) {
                                println("Olá Usuário ${usuarioLogado.nome}!")
                                println("1) Meu Perfil")
                                println("2) Alterar dados do Perfil")
                                println("3) Inativar Minha Conta")
                                println("4) Ver feed de Eventos")
                                println(lineBar)
                            }
                            println("0) Sair (logout)")
                            println(lineBar)

                            println("Escolha: ")
                            // [Critério 3a - val/var] 'opcaoMenuLogado' é declarada como 'val' dentro do loop, mas deveria ser 'var' fora do laço.
                            // Talvez, se você a declarasse como 'var opcaoMenuLogado = -1' antes do loop, ela poderia servir como condição de parada do do..while.
                            // A variável seria declarada da seguinte forma: var opcaoMenuLogado = -1  // declarada fora do while
                            //deveria ser var e ficar fora do laço
                            val opcaoMenuLogado = readln().toIntOrNull() ?: 0
                            println(lineBar)

                            when (opcaoMenuLogado) {
                                0 -> {
                                    println(COR.AMARELO + "Realizando logout..." + COR.RESET)
                                    sessaoAtiva = false
                                }
                                // --- OPÇÃO 1: MEU PERFIL ---
                                1 -> {
                                    //if repetitivo
                                    if (organizadorLogado != null) {
                                        // dados do usuario organizador
                                        println(lineBar)
                                        println(COR.AMARELO + "--- SEU PERFIL (ORGANIZADOR) ---" + COR.RESET + "\n")
                                        println("Nome: ${COR.VERDE}${organizadorLogado.nome}${COR.RESET}")
                                        println("Email: ${organizadorLogado.email}")
                                        println("Gênero: ${organizadorLogado.sexo}")
                                        println(lineBar)
                                        // dados relacionados a idade e nascimento
                                        val hoje = LocalDate.now()
                                        val idadeExataCalculada = Period.between(organizadorLogado.dataNascimento, hoje)
                                        println(
                                            "Data de Nascimento: ${organizadorLogado.dataNascimento.format(formatterDate)}"
                                        )
                                        println("Idade: ${COR.VERDE}${idadeExataCalculada.years} Anos, ${idadeExataCalculada.months} Meses, ${idadeExataCalculada.days} Dias")

                                        // dados empresariais
                                        if (organizadorLogado.cnpj != null) {
                                            println(lineBar)
                                            println(COR.AMARELO + "--- DADOS DA EMPRESA ---" + COR.RESET)
                                            println("Razão Social: ${organizadorLogado.razaoSocial}")
                                            println("Nome Fantasia ${organizadorLogado.nomeFantasia}")
                                            println("CNPJ ${organizadorLogado.cnpj}")
                                        } else {
                                            println(lineBar)
                                            println("Perfil de pessoa Física, Sem dados Empresariais cadastrados")
                                        }
                                        println(lineBar)
                                        println("Pressione enter para voltar") // se a pessoa digitar qualquer coisa
                                        //ele volta
                                        readln()
                                    }
                                    // Perfil do usuário comum
                                    else if (usuarioLogado != null) {
                                        println(lineBar)
                                        println(COR.AMARELO + "--- SEU PERFIL (USUÁRIO) ---" + COR.RESET)

                                        // dados do usuário comum
                                        println("Nome: ${COR.VERDE}${usuarioLogado.nome}${COR.RESET}")
                                        println("Email: ${usuarioLogado.email}")
                                        println("Gênero: ${usuarioLogado.sexo}")
                                        println(lineBar)

                                        // dados relacionados a idade e nascimento
                                        val hoje = LocalDate.now()
                                        val idadeExataCalculada = Period.between(usuarioLogado.dataNascimento, hoje)
                                        println("Data de Nascimento: ${usuarioLogado.dataNascimento.format(formatterDate)}")
                                        println("Idade: ${COR.VERDE}${idadeExataCalculada.years} Anos, ${idadeExataCalculada.months} Meses, ${idadeExataCalculada.days} Dias")

                                        println(lineBar)
                                        println("Pressione enter para voltar")
                                        readln()
                                    }
                                }
                                // --- OPÇÃO 2: ALTERAR DADOS ---
                                2 -> {
                                    var alterandoPerfil = true
                                    // [Critério 2 - Loop Adequado] 'while(alterandoPerfil)' é uma flag booleana equivalente a while(true). Não é uma boa prática de programação.
                                    // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                    //   var opcaoAlterar: Int
                                    //   do {
                                    //       println("1) Nome  2) Senha  0) Cancelar")
                                    //       opcaoAlterar = readln().toIntOrNull() ?: 0
                                    //       when(opcaoAlterar) { 1 -> {} 2 -> {} }
                                    //   } while(opcaoAlterar != 0)
                                    //outro while true, isso aqui deveria ser um do..while com when no inicio e os prints
                                    // no final. Condição de parada mal feita.
                                    while (alterandoPerfil) {
                                        if (organizadorLogado != null) {
                                            // Alterar usuário organizador

                                            println(lineBar)
                                            println(COR.AMARELO + "--- ALTERAR DADOS ---" + COR.RESET)
                                            println("O que você deseja alterar?")
                                            println("1) Nome")
                                            println("2) Senha")
                                            println("3) Sexo/Gênero")
                                            println("4) Dados Empresariais (Adicionar ou Editar)")
                                            println("0) Cancelar")
                                            print("Opção: ")
                                            val opcaoAlterar = readln().toIntOrNull() ?: 0

                                            when (opcaoAlterar) {
                                                1 -> {
                                                    print("Novo Nome: ")
                                                    val novoNome = readln().trim()
                                                    if (novoNome.length >= 2) {
                                                        organizadorLogado.nome = novoNome
                                                        println(COR.VERDE + "Nome atualizado!" + COR.RESET)
                                                    } else
                                                        println(COR.VERMELHO + "Nome inválido." + COR.RESET) // melhora a legibilidade
                                                }

                                                2 -> {

                                                    /*aqui vocês tinham que ter estruturado o código para primeiro lerem a senha atual
                                                    depois vocês fariam o ciclo da nova senha

                                                    do {
                                                        // erro
                                                        // leitura
                                                    } while(senhaAtual != organizadorLogado.senha)

                                                    do {
                                                        // erro
                                                        //leitura
                                                        //validações prévias
                                                    } while(novaSenha != novaSenhaConfirmacao)

                                                    */
                                                    print("Digite sua senha atual: ")
                                                    val senhaAtual = readln().trim()

                                                    var cicloNovaSenha = true

                                                    var novaSenha = ""
                                                    var novaSenhaConfirmacao = ""

                                                    if (senhaAtual == organizadorLogado.senha){
                                                        print("Nova Senha: ")
                                                        novaSenha = readln().trim()
                                                        print("Confirme a Nova Senha: ")
                                                        novaSenhaConfirmacao = readln().trim()

                                                    } else {
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Senha incorreta! Voltando para o Menu..." + COR.RESET)
                                                        cicloNovaSenha = false
                                                    }

                                                    //outro while true
                                                    // [Critério 2 - Loop Adequado] 'while(cicloNovaSenha)' é uma flag booleana equivalente a while(true). Loops while(true) não são uma boa prática de programação, pois dificultam a compreensão da condição de parada.
                                                    // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                                    //   do {
                                                    //       print("Nova Senha: "); novaSenha = readln().trim()
                                                    //       print("Confirme: ");   novaSenhaConfirmacao = readln().trim()
                                                    //       if (novaSenha.length < 8) println("Senha muito curta")
                                                    //   } while(novaSenha.isEmpty() || novaSenha.length < 8 || novaSenha != novaSenhaConfirmacao)
                                                    while (cicloNovaSenha) {
                                                        if (novaSenha.isEmpty()){
                                                            println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A nova senha precisa ser preenchida. Por favor digite uma senha" + COR.RESET)
                                                        }
                                                        else if (novaSenha.length < 8) {
                                                            println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A nova senha precisa possuir 8 ou mais caracteres. Por favor digite uma nova senha" + COR.RESET)
                                                        }
                                                        else if (novaSenha != novaSenhaConfirmacao) {
                                                            println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "As senhas não coincidem por favor digite a senha novamente" + COR.RESET)
                                                        } else {
                                                            println(COR.VERDE + "Nova senha cadastrada com sucesso! Prosseguindo..." + COR.RESET)
                                                            organizadorLogado.senha = novaSenha
                                                            cicloNovaSenha = false
                                                        }
                                                    }
                                                }

                                                3 -> {

                                                    println("Novo Gênero (1-Masculino, 2-Feminino, 3-Outros): ")
                                                    val opcaoSexo = readln().toIntOrNull() ?: 3
                                                    //aaah, boa, você força o gênero ser outros se colocar qualquer outra coisa que não seja 1 e 2
                                                    organizadorLogado.sexo = when (opcaoSexo) {
                                                        1 -> Sexo.MASCULINO; 2 -> Sexo.FEMININO; else -> Sexo.OUTROS
                                                    }
                                                    println(COR.VERDE + "Gênero atualizado!" + COR.RESET)
                                                }

                                                4 -> {
                                                    println(lineBar)
                                                    if (organizadorLogado.cnpj == null) {
                                                        println(COR.AMARELO + "Atualmente você é Pessoa Física." + COR.RESET)
                                                        println("Deseja adicionar dados de Empresa (Tornar-se PJ)?")
                                                        println("1) Sim \n2) Não")
                                                        val opcaoTornarPJ = readln().toIntOrNull() ?: 2

                                                        if (opcaoTornarPJ == 1) {
                                                            // vocês não pendem de novo? tipo, a pessoa só volta para o menu superior?
                                                            print("Digite o CNPJ (14 números): ") // o cnpj vai ser alfanumerico
                                                            val novoCnpj = readln().trim()
                                                            if (novoCnpj.length == 14) {
                                                                print("Razão Social: ")
                                                                val novaRazaoSocial = readln().trim()
                                                                print("Nome Fantasia: ")
                                                                val novoNomeFantasia = readln().trim()

                                                                // AQUI ACONTECE O UPGRADE
                                                                organizadorLogado.cnpj = novoCnpj
                                                                organizadorLogado.razaoSocial = novaRazaoSocial
                                                                organizadorLogado.nomeFantasia = novoNomeFantasia

                                                                println(COR.VERDE + "Sucesso! Agora você é um Organizador PJ." + COR.RESET)
                                                            } else {
                                                                println(COR.VERMELHO + "CNPJ inválido." + COR.RESET)
                                                            }

                                                        }
                                                    } else {
                                                        println(COR.AMARELO + "--- EDITAR DADOS DA EMPRESA ---" + COR.RESET)
                                                        println("CNPJ Atual: ${organizadorLogado.cnpj}")
                                                        println("1) Editar Nome Fantasia/Razão Social")
                                                        println("2) Corrigir CNPJ")
                                                        println("0) Voltar")
                                                        val opcaoEmpresa = readln().toIntOrNull() ?: 0

                                                        // [Critério 15 - when no lugar de if..else if] O bloco 'if(opcaoEmpresa == 1) ... else if(opcaoEmpresa == 2)' poderia ser substituído por 'when'.
                                                        // Talvez, se você adotasse 'when(opcaoEmpresa)', o código ficaria mais legível e idiomático em Kotlin:
                                                        // when(opcaoEmpresa) { 1 -> { /* editar */ } 2 -> { /* corrigir CNPJ */ } }
                                                        if (opcaoEmpresa == 1) {
                                                            print("Nova Razão Social: ")
                                                            organizadorLogado.razaoSocial = readln().trim()
                                                            print("Novo Nome Fantasia: ")
                                                            organizadorLogado.nomeFantasia = readln().trim()
                                                            println(COR.VERDE + "Dados empresariais atualizados!" + COR.RESET)
                                                        } else if (opcaoEmpresa == 2) {
                                                            print("Novo CNPJ: ")
                                                            val novoCnpj = readln().trim()
                                                            if (novoCnpj.length == 14) {
                                                                organizadorLogado.cnpj = novoCnpj
                                                                println(COR.VERDE + "CNPJ atualizado!" + COR.RESET)
                                                            } else {
                                                                println(COR.VERMELHO + "CNPJ inválido." + COR.RESET)
                                                            }
                                                        }
                                                    }
                                                }

                                                0 -> {
                                                    println(COR.AMARELO + "Operação cancelada." + COR.RESET)
                                                    alterandoPerfil = false
                                                }

                                                else -> println(COR.VERMELHO + "Opção inválida." + COR.RESET)
                                            }

                                        } else if (usuarioLogado != null) {
                                            // Alterar usuário comum
                                            println(lineBar)
                                            println(COR.AMARELO + "--- ALTERAR DADOS ---" + COR.RESET)
                                            println("O que você deseja alterar?")
                                            println("1) Nome")
                                            println("2) Senha")
                                            println("3) Sexo/Gênero")
                                            println("0) Cancelar")
                                            print("Opção: ")
                                            val opcaoAlterar = readln().toIntOrNull() ?: 0

                                            when (opcaoAlterar) {
                                                1 -> {
                                                    print("Novo Nome: ")
                                                    val novoNome = readln().trim()
                                                    if (novoNome.length >= 2) {
                                                        usuarioLogado.nome = novoNome
                                                        println(COR.VERDE + "Nome atualizado!" + COR.RESET)
                                                    } else println(COR.VERMELHO + "Nome inválido." + COR.RESET)
                                                }

                                                2 -> {
                                                    print("Digite sua senha atual: ")
                                                    val senhaAtual = readln().trim()

                                                    var cicloNovaSenha = true

                                                    var novaSenha = ""
                                                    var novaSenhaConfirmacao = ""

                                                    if (senhaAtual == usuarioLogado.senha){
                                                        print("Nova Senha: ")
                                                        novaSenha = readln().trim()
                                                        print("Confirme a Nova Senha: ")
                                                        novaSenhaConfirmacao = readln().trim()

                                                    } else {
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Senha incorreta! Voltando para o Menu..." + COR.RESET)
                                                        cicloNovaSenha = false
                                                    }

                                                    // [Critério 2 - Loop Adequado] 'while(cicloNovaSenha)' (usuário comum) — mesma situação do organizador acima. Não é uma boa prática.
                                                    // [Critério 14 - do..while] Prefira do..while com a condição de parada explícita.
                                                    while (cicloNovaSenha) {
                                                        if (novaSenha.isEmpty()){
                                                            println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A nova senha precisa ser preenchida. Por favor digite uma senha" + COR.RESET)
                                                        }
                                                        else if (novaSenha.length < 8) {
                                                            println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A nova senha precisa possuir 8 ou mais caracteres. Por favor digite uma nova senha" + COR.RESET)
                                                        }
                                                        else if (novaSenha != novaSenhaConfirmacao) {
                                                            println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "As senhas não coincidem por favor digite a senha novamente" + COR.RESET)
                                                        } else {
                                                            println(COR.VERDE + "Nova senha cadastrada com sucesso! Prosseguindo..." + COR.RESET)
                                                            usuarioLogado.senha = novaSenha
                                                            cicloNovaSenha = false
                                                        }
                                                    }
                                                }

                                                3 -> {
                                                    println("Novo Gênero (1-Masculino, 2-Feminino, 3-Outros): ")
                                                    val opcaoSexo = readln().toIntOrNull() ?: 3
                                                    usuarioLogado.sexo = when (opcaoSexo) {
                                                        1 -> Sexo.MASCULINO; 2 -> Sexo.FEMININO; else -> Sexo.OUTROS
                                                    }
                                                    println(COR.VERDE + "Gênero atualizado!" + COR.RESET)
                                                }

                                                0 -> {
                                                    println("Operação cancelada.")
                                                    alterandoPerfil = false
                                                }
                                                else -> println("Opção inválida.")
                                            }
                                        }
                                    }
                                    println("Pressione ENTER para voltar...")
                                    readln()
                                }

                                // --- OPÇÃO 3: INATIVAR CONTA ---
                                3 -> {
                                    println(lineBar)
                                    println(COR.VERMELHO + "ATENÇÃO: Você está prestes a desativar sua conta." + COR.RESET)
                                    println("Para entrar novamente, você precisará usar a opção 'Reativar Conta' no menu principal.")
                                    println("Tem certeza? (1) SIM, (2) NÃO)")
                                    val confirmacao = readln().toIntOrNull() ?: 2

                                    if (confirmacao == 1) {
                                        if (organizadorLogado != null) {
                                            val agora = LocalDateTime.now()

                                            // Verifica se existem eventos ativos ou em andamento
                                            val possuiEventosAtivos = listaEventos.any { evento ->
                                                //essa verificação não deveria ser baseada em chave, mas em objetos
                                                evento.idOrganizador == organizadorLogado.email &&
                                                        evento.ativo &&
                                                        agora.isBefore(evento.dataFim) // cobre eventos futuros e em andamento
                                            }

                                            if (possuiEventosAtivos) {
                                                println(COR.VERMELHO + "Não é possível desativar a conta: você possui eventos ativos ou em andamento." + COR.RESET)
                                                println("Pressione ENTER para voltar ao menu...")
                                                readln()
                                            } else {
                                                organizadorLogado.ativo = false
                                                println(COR.VERMELHO + "Conta de Organizador inativada." + COR.RESET)
                                                sessaoAtiva = false // Desloga automaticamente
                                            }

                                        } else if (usuarioLogado != null) {
                                            usuarioLogado.ativo = false
                                            println(COR.VERMELHO + "Conta de Usuário inativada." + COR.RESET)
                                            sessaoAtiva = false // Desloga automaticamente
                                        }

                                    } else {
                                        println(COR.VERDE + "Operação cancelada." + COR.RESET)
                                        println("Pressione ENTER para voltar...")
                                        readln()
                                    }
                                }

                                // --- OPÇÃO 4: FUNCIONALIDADES DAS TAREFAS 2 E 3 ---
                                // --- OPÇÃO 4: FUNCIONALIDADES DAS TAREFAS 2 E 3 ---
                                4 -> {
                                    if (organizadorLogado != null) {
                                        //while true não é uma boa prática deveria ser um do..while com a leitura da
                                        // opção no final e a condição de parada baseada na opção de saída
                                        var menuEventos = true
                                        // [Critério 2 - Loop Adequado] 'while(menuEventos)' é uma flag booleana equivalente a while(true). Loops while(true) não são uma boa prática de programação.
                                        // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                        //   var opEventos: Int
                                        //   do {
                                        //       println("1) Cadastrar  2) Listar  3) Alterar  4) Ativar/Desativar  0) Voltar")
                                        //       opEventos = readln().toIntOrNull() ?: 0
                                        //       when(opEventos) { 1 -> {} 2 -> {} 3 -> {} 4 -> {} }
                                        //   } while(opEventos != 0)
                                        while (menuEventos) {
                                            println("\n" + lineBar)
                                            println(COR.AZUL + "--- GERENCIAMENTO DE EVENTOS ---" + COR.RESET)
                                            println("1) Cadastrar Novo Evento")
                                            println("2) Listar Meus Eventos")
                                            println("3) Alterar Evento")
                                            println("4) Ativar/Desativar Evento")
                                            println("0) Voltar ao Menu Principal")
                                            println(lineBar)
                                            print("Escolha: ")
                                            val opEventos = readln().toIntOrNull() ?: 0

                                            when (opEventos) {
                                                0 -> menuEventos = false // Sai do loop

                                                // === US 6: CADASTRAR EVENTO ===
                                                1 -> {
                                                    println(COR.AMARELO + "\n--- NOVO EVENTO ---" + COR.RESET)
                                                    print("Nome do Evento: ")
                                                    val nomeEv = readln().trim()
                                                    print("Descrição: ")
                                                    val descEv = readln().trim()

                                                    // Tratamento de Datas
                                                    val formatterHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                                                    var dataIniEv: LocalDateTime? = null
                                                    var dataFimEv: LocalDateTime? = null
                                                    var datasValidas = false
                                                    // [Critério 2 - Loop Adequado] 'while(!datasValidas)' é uma flag booleana equivalente a while(true). Não é uma boa prática de programação.
                                                    // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while, pois a leitura ocorre ao menos uma vez:
                                                    //   do {
                                                    //       try {
                                                    //           print("Início: "); dataIniEv = LocalDateTime.parse(readln(), formatterHora)
                                                    //           print("Fim: ");    dataFimEv = LocalDateTime.parse(readln(), formatterHora)
                                                    //           datasValidas = dataIniEv.isAfter(agora) && dataFimEv.isAfter(dataIniEv)
                                                    //               && Duration.between(dataIniEv, dataFimEv).toMinutes() >= 30
                                                    //       } catch (e: Exception) { println("Formato inválido") }
                                                    //   } while(!datasValidas)
                                                    while (!datasValidas) {
                                                        try {
                                                            print("Início (dd/MM/yyyy HH:mm): ")
                                                            val iniStr = readln()
                                                            dataIniEv = LocalDateTime.parse(iniStr, formatterHora)

                                                            print("Fim (dd/MM/yyyy HH:mm): ")
                                                            val fimStr = readln()
                                                            dataFimEv = LocalDateTime.parse(fimStr, formatterHora)

                                                            val agora = LocalDateTime.now()
                                                            // Validações da US 6
                                                            if (dataIniEv.isBefore(agora)) {
                                                                println(COR.VERMELHO + "Erro: O evento não pode começar no passado." + COR.RESET)
                                                            } else if (dataFimEv.isBefore(dataIniEv)) {
                                                                println(COR.VERMELHO + "Erro: A data de fim não pode ser antes do início." + COR.RESET)
                                                            } else if (java.time.Duration.between(dataIniEv, dataFimEv)
                                                                    .toMinutes() < 30
                                                            ) {
                                                                println(COR.VERMELHO + "Erro: O evento deve ter no mínimo 30 minutos." + COR.RESET)
                                                            } else {
                                                                datasValidas = true
                                                            }
                                                        } catch (e: Exception) {
                                                            println(COR.VERMELHO + "Formato de data inválido. Use dia/mês/ano hora:minuto" + COR.RESET)
                                                        }
                                                    }

                                                    // Seleção de Tipo
                                                    println("\nTipos disponíveis:")
                                                    enums.CategoriaEvento.values()
                                                        .forEachIndexed { i, t -> print("$i-$t | ") }
                                                    println("\nDigite o número do tipo:")
                                                    val tipoIndex = readln().toIntOrNull() ?: 0
                                                    val tipoEv = enums.CategoriaEvento.values()
                                                        .getOrElse(tipoIndex) { enums.CategoriaEvento.OUTRO }

                                                    // Seleção de Modalidade
                                                    println("Modalidade (1-Presencial, 2-Remoto, 3-Híbrido): ")
                                                    val modInput = readln().toIntOrNull() ?: 1
                                                    val modEv = when (modInput) {
                                                        2 -> enums.Modalidade.REMOTO
                                                        3 -> enums.Modalidade.HIBRIDO
                                                        else -> enums.Modalidade.PRESENCIAL
                                                    }

                                                    print("Local (Endereço ou Link): ")
                                                    val localEv = readln().trim()
                                                    print("Capacidade Máxima de Pessoas: ")
                                                    val capEv = readln().toIntOrNull() ?: 10
                                                    print("Preço do Ingresso (0 para gratuito): ")
                                                    // [Critério 8 - Valores Financeiros] 'precoEv' é declarado como 'Double'. Valores financeiros podem ter imprecisão com Double.
                                                    // Talvez, se você usasse 'BigDecimal', as operações de soma (evento principal + secundário) ficariam mais precisas.
                                                    // A variável seria declarada da seguinte forma: val precoEv = readln().toBigDecimalOrNull() ?: BigDecimal.ZERO
                                                    val precoEv = readln().toDoubleOrNull() ?: 0.0


                                                    // Politica de cancelamento
                                                    print("Permite estorno em caso de cancelamento? (1-Sim, 2-Não): ")
                                                    val estornoOp = readln().toIntOrNull() ?: 2
                                                    val permiteEstorno = (estornoOp == 1)
                                                    // [Critério 8 - Valores Financeiros] 'taxaEstorno' é 'Double'. Talvez, se você usasse 'BigDecimal', o cálculo do valor estornado ficaria mais preciso.
                                                    // A variável seria declarada da seguinte forma: var taxaEstorno: BigDecimal = BigDecimal.ZERO
                                                    var taxaEstorno = 0.0
                                                    if (permiteEstorno) {
                                                        print("Qual a taxa de estorno (ex: 10.0 para 10%): ")
                                                        val inputTaxa = readln().toDoubleOrNull() ?: 0.0
                                                        taxaEstorno = inputTaxa / 100.0 // Transformando em porcentagem
                                                    }

                                                    // Evento Vinculado
                                                    print("Este evento é vinculado a outro principal? (ID do evento ou 0 para não): ")
                                                    val idVinc = readln().toIntOrNull() ?: 0
                                                    val idVinculadoFinal =
                                                        if (idVinc > 0 && listaEventos.any { it.id == idVinc }) idVinc else null

                                                    print("Deseja marcar esse evento como Ativo ou Inativo?" +
                                                            "\n1) Ativo" +
                                                            "\n2) Inativo\n\n")
                                                    val inputStatusEv = readln().toIntOrNull() ?: 2
                                                    // [Critério 3a - val/var] 'statusEv' é 'var', mas poderia ser 'val' pois seu valor é definido uma única vez.
                                                    // [Critério 4 - when como expressão] O bloco if/else if abaixo poderia ser substituído por 'when' como expressão, que é mais idiomático em Kotlin.
                                                    // [Critério 15 - when no lugar de if..else if] Talvez, se você usasse 'when' como expressão, ficaria mais claro e conciso:
                                                    //   val statusEv: Boolean = when (inputStatusEv) {
                                                    //       1 -> { println("Este evento foi definido como Ativo."); true }
                                                    //       else -> { println("Este evento foi definido como Inativo."); false }
                                                    //   }
                                                    var statusEv = false
                                                    if (inputStatusEv == 1){
                                                        println("Este evento foi definido como Ativo.")
                                                        statusEv = true
                                                    } else if (inputStatusEv == 2){
                                                        println("Este evento foi definido como Inativo.")
                                                        statusEv = false
                                                    } else {
                                                        println("Entrada inválida! Evento definido como Inativo.")
                                                    }

                                                    // Criação do Objeto
                                                    val novoId = (listaEventos.maxOfOrNull { it.id } ?: 0) + 1
                                                    val novoEvento = entities.Evento(
                                                        id = novoId,
                                                        nome = nomeEv,
                                                        descricao = descEv,
                                                        dataInicio = dataIniEv!!,
                                                        dataFim = dataFimEv!!,
                                                        tipo = tipoEv,
                                                        modalidade = modEv,
                                                        capacidadeTotal = capEv,
                                                        local = localEv,
                                                        preco = precoEv,
                                                        ativo = statusEv,
                                                        idOrganizador = organizadorLogado.email, // Vincula usando o email
                                                        estornaDinheiro = permiteEstorno,
                                                        taxaEstorno = taxaEstorno,
                                                        idEventoVinculado = idVinculadoFinal
                                                    )

                                                    listaEventos.add(novoEvento)
                                                    println(COR.VERDE + "Evento '${novoEvento.nome}' cadastrado com sucesso! (ID: $novoId)" + COR.RESET)
                                                    println(COR.AMARELO + "Lembre-se de ATIVAR o evento para ele aparecer no feed." + COR.RESET)
                                                }

                                                // === US 10: LISTAR MEUS EVENTOS ===
                                                2 -> {
                                                    println(COR.AMARELO + "\n--- MEUS EVENTOS ---" + COR.RESET)
                                                    val meusEventos =
                                                        listaEventos.filter { it.idOrganizador == organizadorLogado.email }

                                                    if (meusEventos.isEmpty()) {
                                                        println("Você não possui eventos cadastrados.")
                                                    } else {
                                                        val formatterDisplay =
                                                            DateTimeFormatter.ofPattern("dd/MM HH:mm")
                                                        println(
                                                            String.format(
                                                                "%-5s %-20s %-15s %-10s %-10s %-10s",
                                                                "ID",
                                                                "NOME",
                                                                "DATA",
                                                                "PREÇO",
                                                                "CAPAC.",
                                                                "STATUS"
                                                            )
                                                        )
                                                        println("-".repeat(75))
                                                        for (ev in meusEventos) {
                                                            val status =
                                                                if (ev.ativo) "${COR.VERDE}ATIVO${COR.RESET}" else "${COR.VERMELHO}INATIVO${COR.RESET}"
                                                            val dataStr = ev.dataInicio.format(formatterDisplay)
                                                            println(
                                                                String.format(
                                                                    "%-5d %-20s %-15s R$%-8.2f %-10d %s",
                                                                    ev.id,
                                                                    if (ev.nome.length > 18) ev.nome.take(17) + "." else ev.nome,
                                                                    dataStr,
                                                                    ev.preco,
                                                                    ev.capacidadeTotal,
                                                                    status
                                                                )
                                                            )
                                                        }
                                                    }
                                                    println("\nPressione Enter para continuar...")
                                                    readln()
                                                }

                                                // === US 7: ALTERAR EVENTO ===
                                                3 -> {
                                                    println("\n" + lineBar)
                                                    println(COR.AMARELO + "--- ALTERAR EVENTO ---" + COR.RESET)
                                                    val meusEventosAtivos =
                                                        listaEventos.filter { it.idOrganizador == organizadorLogado.email && it.ativo }

                                                    if (meusEventosAtivos.isEmpty()) {
                                                        println("Você não possui eventos ativos para alterar.")
                                                    } else {
                                                        println("Seus eventos disponíveis para alteração:")
                                                        meusEventosAtivos.forEach { println("ID: [${it.id}] - ${it.nome}") }
                                                        print("\nDigite o ID do evento que deseja alterar (ou 0 para cancelar): ")
                                                        val idBusca = readln().toIntOrNull() ?: 0

                                                        if (idBusca != 0) {
                                                            val eventoParaAlterar =
                                                                meusEventosAtivos.find { it.id == idBusca }
                                                            if (eventoParaAlterar == null) {
                                                                println(COR.VERMELHO + "Erro: Evento não encontrado ou não pertence a você." + COR.RESET)
                                                            } else if (eventoParaAlterar.ingressosVendidos > 0) {
                                                                println(COR.VERMELHO + "Erro: Este evento já possui ingressos vendidos. Não é possível alterá-lo." + COR.RESET)
                                                            } else {
                                                                println("Deixe em branco para manter o valor atual.")
                                                                print("Novo Nome (${eventoParaAlterar.nome}): ")
                                                                val novoNome = readln().trim()
                                                                if (novoNome.isNotBlank()) eventoParaAlterar.nome =
                                                                    novoNome

                                                                print("Novo Local (${eventoParaAlterar.local}): ")
                                                                val novoLocal = readln().trim()
                                                                if (novoLocal.isNotBlank()) eventoParaAlterar.local =
                                                                    novoLocal

                                                                print("Novo Preço (${eventoParaAlterar.preco}): ")
                                                                val novoPrecoStr = readln().trim()
                                                                if (novoPrecoStr.isNotBlank()) {
                                                                    val precoParse = novoPrecoStr.toDoubleOrNull()
                                                                    if (precoParse != null && precoParse >= 0) eventoParaAlterar.preco =
                                                                        precoParse
                                                                }
                                                                println(COR.VERDE + "Evento atualizado com sucesso!" + COR.RESET)
                                                            }
                                                        }
                                                    }
                                                    println("Pressione ENTER para voltar...")
                                                    readln()
                                                }

                                                // === US 8 e 9: ATIVAR / DESATIVAR EVENTO ===
                                                4 -> {
                                                    println(COR.AMARELO + "\n--- STATUS DO EVENTO ---" + COR.RESET)
                                                    print("Digite o ID do evento: ")
                                                    val idStatus = readln().toIntOrNull() ?: 0
                                                    val eventoStatus =
                                                        listaEventos.find { it.id == idStatus && it.idOrganizador == organizadorLogado.email }

                                                    if (eventoStatus != null) {
                                                        println("Evento: ${eventoStatus.nome}")
                                                        println("Status Atual: " + if (eventoStatus.ativo) "ATIVO" else "INATIVO")
                                                        println("Deseja alterar o status? (1-Sim, 2-Não)")

                                                        if (readln() == "1") {
                                                            if (!eventoStatus.ativo) {
                                                                val agora = LocalDateTime.now()
                                                                if (eventoStatus.dataInicio.isBefore(agora)) {
                                                                    println(COR.VERMELHO + "Não é possível ativar um evento que já passou ou começou." + COR.RESET)
                                                                } else {
                                                                    eventoStatus.ativo = true
                                                                    println(COR.VERDE + "Evento ATIVADO com sucesso! Agora está visível para compras." + COR.RESET)
                                                                }
                                                            } else {
                                                                println(COR.VERMELHO + "ATENÇÃO: Desativar o evento suspende vendas." + COR.RESET)
                                                                if (eventoStatus.ingressosVendidos > 0) {
                                                                    println("Existem ${eventoStatus.ingressosVendidos} ingressos vendidos.")
                                                                    println("Ao desativar, todos serão CANCELADOS e REEMBOLSADOS.")
                                                                    print("Confirmar desativação catastrófica? (DIGITE 'CONFIRMAR'): ")

                                                                    if (readln() == "CONFIRMAR") {
                                                                        eventoStatus.ativo = false
                                                                        var reembolsados = 0
                                                                        listaIngressos.forEach { ing ->
                                                                            if (ing.idEvento == eventoStatus.id && ing.status == enums.StatusIngresso.ATIVO) {
                                                                                ing.status =
                                                                                    enums.StatusIngresso.CANCELADO
                                                                                reembolsados++
                                                                            }
                                                                        }
                                                                        println(COR.VERDE + "Evento DESATIVADO. $reembolsados ingressos cancelados e reembolsados." + COR.RESET)
                                                                        eventoStatus.ingressosVendidos = 0
                                                                    } else {
                                                                        println("Operação cancelada.")
                                                                    }
                                                                } else {
                                                                    eventoStatus.ativo = false
                                                                    println(COR.VERDE + "Evento DESATIVADO. Não havia ingressos vendidos." + COR.RESET)
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        println(COR.VERMELHO + "Evento não encontrado." + COR.RESET)
                                                    }
                                                }

                                                else -> println("Opção inválida.")
                                            }
                                        }
                                    } else if (usuarioLogado != null) {
                                        // === MENU DO FEED DE EVENTOS (US 11-14) ===
                                        var menuFeedAtivo = true
                                        // [Critério 2 - Loop Adequado] 'while(menuFeedAtivo)' é uma flag booleana equivalente a while(true). Não é uma boa prática de programação.
                                        // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                        //   var opcaoFeed: Int
                                        //   do {
                                        //       println("1) Feed  2) Comprar  3) Cancelar  4) Meus Ingressos  0) Voltar")
                                        //       opcaoFeed = readln().toIntOrNull() ?: 0
                                        //       when(opcaoFeed) { 1 -> {} 2 -> {} ... }
                                        //   } while(opcaoFeed != 0)
                                        val formatterDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

                                        while (menuFeedAtivo) {
                                            println("\n$lineBar")
                                            println(COR.AZUL + "===== FEED DE EVENTOS =====" + COR.RESET)
                                            println(lineBar)
                                            println("1) Feed de Eventos (Ver Todos)")
                                            println("2) Comprar Ingresso")
                                            println("3) Cancelar Ingresso")
                                            println("4) Meus Ingressos")
                                            println("0) Voltar")
                                            println(lineBar)
                                            print(COR.AMARELO + "Escolha uma opção: " + COR.RESET)

                                            val opcaoFeed = readln().toIntOrNull() ?: 0

                                            when (opcaoFeed) {
                                                0 -> {
                                                    println(COR.AMARELO + "Voltando ao menu principal..." + COR.RESET)
                                                    menuFeedAtivo = false
                                                }

                                                // ===== US 11: FEED DE EVENTOS =====
                                                1 -> {
                                                    println("\n$lineBar")
                                                    println(COR.AZUL + "FEED DE EVENTOS" + COR.RESET)
                                                    println(lineBar)

                                                    val eventosDisponiveis = mutableListOf<Evento>()
                                                    val agora = LocalDateTime.now()

                                                    // [Critério 10 - filter/find] O laço 'for' abaixo com 'continue' e 'add' manual pode ser substituído por 'filter', que é mais conciso e idiomático.
                                                    // [Critério 12 - forEach] Além disso, a variável 'eventosDisponiveis' poderia ser declarada diretamente com o resultado do 'filter', eliminando o 'mutableListOf' desnecessário:
                                                    // Talvez, se você usasse:
                                                    //   val eventosDisponiveis = listaEventos.filter { it.ativo && it.dataFim.isAfter(agora) && it.ingressosVendidos < it.capacidadeTotal }
                                                    // o código ficaria mais legível e declarativo.
                                                    for (evento in listaEventos) {
                                                        if (!evento.ativo) continue
                                                        if (evento.dataFim.isBefore(agora)) continue
                                                        if (evento.ingressosVendidos >= evento.capacidadeTotal) continue
                                                        eventosDisponiveis.add(evento)
                                                    }

                                                    if (eventosDisponiveis.isEmpty()) {
                                                        println(COR.AMARELO + "Não há eventos disponíveis no momento." + COR.RESET)
                                                    } else {
                                                        // [Critério 10 - filter/find] O Bubble Sort implementado manualmente pode ser substituído pelo método 'sortedWith' do Kotlin, que é mais eficiente e legível.
                                                        // Talvez, se você usasse:
                                                        //   val eventosOrdenados = eventosDisponiveis.sortedWith(compareBy({ it.dataInicio }, { it.nome }))
                                                        // o código ficaria mais conciso e expressivo.
                                                        // Bubble sort por data
                                                        for (i in 0 until eventosDisponiveis.size - 1) {
                                                            for (j in 0 until eventosDisponiveis.size - i - 1) {
                                                                val evento1 = eventosDisponiveis[j]
                                                                val evento2 = eventosDisponiveis[j + 1]

                                                                val comparacaoData =
                                                                    evento1.dataInicio.compareTo(evento2.dataInicio)

                                                                if (comparacaoData > 0) {
                                                                    eventosDisponiveis[j] = evento2
                                                                    eventosDisponiveis[j + 1] = evento1
                                                                } else if (comparacaoData == 0) {
                                                                    if (evento1.nome > evento2.nome) {
                                                                        eventosDisponiveis[j] = evento2
                                                                        eventosDisponiveis[j + 1] = evento1
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        println(COR.VERDE + "Eventos disponíveis: ${eventosDisponiveis.size}" + COR.RESET)
                                                        println()

                                                        // [Critério 16 - forEachIndexed] A variável 'contador' manual pode ser eliminada usando 'forEachIndexed'.
                                                        // [Critério 12 - forEach] Além disso, o 'for' poderia ser substituído por 'forEachIndexed' para ser mais idiomático em Kotlin:
                                                        // Talvez, se você usasse:
                                                        //   eventosDisponiveis.forEachIndexed { index, evento -> println("[${index + 1}] ${evento.nome}") }
                                                        // o código ficaria mais limpo, sem necessidade de gerenciar a variável 'contador' manualmente.
                                                        var contador = 1
                                                        for (evento in eventosDisponiveis) {
                                                            println("${COR.AZUL}[$contador]${COR.RESET} ${COR.NEGRITO}${evento.nome}${COR.RESET}")
                                                            println(
                                                                "Início: ${
                                                                    evento.dataInicio.format(
                                                                        formatterDataHora
                                                                    )
                                                                }"
                                                            )
                                                            println("Fim: ${evento.dataFim.format(formatterDataHora)}")
                                                            println("Local: ${evento.local}")
                                                            println("Tipo: ${evento.tipo}")
                                                            println("Modalidade: ${evento.modalidade}")

                                                            if (evento.preco == 0.0) {
                                                                println("${COR.VERDE}GRATUITO${COR.RESET}")
                                                            } else {
                                                                println("R$ %.2f".format(evento.preco))
                                                            }

                                                            val vagasDisponiveis =
                                                                evento.capacidadeTotal - evento.ingressosVendidos
                                                            println("Vagas: $vagasDisponiveis/${evento.capacidadeTotal}")

                                                            // CORREÇÃO 1: Buscar organizador pelo e-mail
                                                            // [Critério 10 - filter/find] O laço 'for' com 'break' para buscar o nome do organizador pode ser substituído por 'find'.
                                                            // [Critério 13 - Operador ?.] Com 'find' e '?.', o código fica mais seguro e conciso:
                                                            // Talvez, se você usasse:
                                                            //   val nomeOrganizador = listaOrganizadores.find { it.email == evento.idOrganizador }?.nome ?: "Desconhecido"
                                                            // você eliminaria o 'for', o 'break' e a variável 'var nomeOrganizador' mutável.
                                                            var nomeOrganizador = "Desconhecido"
                                                            for (org in listaOrganizadores) {
                                                                if (org.email == evento.idOrganizador.toString()) {
                                                                    nomeOrganizador = org.nome
                                                                    break
                                                                }
                                                            }
                                                            println("Organizador: $nomeOrganizador")

                                                            // [Critério 11 - any] A variável 'jaTemIngresso' com o laço 'for' e 'break' pode ser substituída pela função 'any', que é mais idiomática e concisa.
                                                            // [Critério 13 - Operador ?.] Talvez, se você usasse:
                                                            //   val jaTemIngresso = listaIngressos.any { it.idUsuario == listaUsuarios.indexOf(usuarioLogado) && it.idEvento == evento.id && it.status == StatusIngresso.ATIVO }
                                                            // o código ficaria mais legível e sem a necessidade do 'break'.
                                                            var jaTemIngresso = false
                                                            for (ingresso in listaIngressos) {
                                                                if (ingresso.idUsuario == listaUsuarios.indexOf(
                                                                        usuarioLogado
                                                                    ) &&
                                                                    ingresso.idEvento == evento.id &&
                                                                    ingresso.status == StatusIngresso.ATIVO
                                                                ) {
                                                                    jaTemIngresso = true
                                                                    break
                                                                }
                                                            }
                                                            if (jaTemIngresso) {
                                                                println(COR.VERDE + "Você já tem ingresso para este evento" + COR.RESET)
                                                            }
                                                            println(lineBar)
                                                            contador++
                                                        }
                                                    }
                                                    println("\nPressione ENTER para voltar...")
                                                    readln()
                                                }

                                                // ===== US 12: COMPRAR INGRESSO =====
                                                2 -> {
                                                    println("\n$lineBar")
                                                    println(COR.AZUL + "COMPRAR INGRESSO" + COR.RESET)
                                                    println(lineBar)

                                                    val eventosDisponiveis = mutableListOf<Evento>()
                                                    val agora = LocalDateTime.now()

                                                    // [Critério 10 - filter/find] O laço 'for' abaixo pode ser substituído por 'filter', que é mais idiomático e conciso.
                                                    // [Critério 5 - Tipo de Coleção] Com 'filter', a variável 'eventosDisponiveis' poderia ser um 'val' imutável (listOf):
                                                    // Talvez, se você usasse:
                                                    //   val eventosDisponiveis = listaEventos.filter { it.ativo && it.dataFim.isAfter(agora) && it.ingressosVendidos < it.capacidadeTotal }
                                                    // o código ficaria mais limpo.
                                                    for (evento in listaEventos) {
                                                        if (evento.ativo && evento.dataFim.isAfter(agora) && evento.ingressosVendidos < evento.capacidadeTotal) {
                                                            eventosDisponiveis.add(evento)
                                                        }
                                                    }

                                                    if (eventosDisponiveis.isEmpty()) {
                                                        println(COR.AMARELO + "Não há eventos disponíveis para compra." + COR.RESET)
                                                    } else {
                                                        // [Critério 10 - filter/find] O Bubble Sort manual aqui (mesmo padrão do feed) pode ser substituído por 'sortedWith':
                                                        // Talvez, se você usasse:
                                                        //   val eventosOrdenados = eventosDisponiveis.sortedWith(compareBy({ it.dataInicio }, { it.nome }))
                                                        // o código ficaria mais legível e sem duplicação de lógica de ordenação.
                                                        // Ordenação
                                                        for (i in 0 until eventosDisponiveis.size - 1) {
                                                            for (j in 0 until eventosDisponiveis.size - i - 1) {
                                                                val e1 = eventosDisponiveis[j]
                                                                val e2 = eventosDisponiveis[j + 1]
                                                                val comp = e1.dataInicio.compareTo(e2.dataInicio)
                                                                if (comp > 0 || (comp == 0 && e1.nome > e2.nome)) {
                                                                    eventosDisponiveis[j] = e2
                                                                    eventosDisponiveis[j + 1] = e1
                                                                }
                                                            }
                                                        }

                                                        // [Critério 16 - forEachIndexed] A variável 'contador' manual aqui pode ser eliminada com 'forEachIndexed', assim como no feed de eventos.
                                                        // [Critério 12 - forEach] Talvez, se você usasse:
                                                        //   eventosDisponiveis.forEachIndexed { index, evento -> println("[${index + 1}] ${evento.nome}  R$ %.2f".format(evento.preco)) }
                                                        var contador = 1
                                                        for (evento in eventosDisponiveis) {
                                                            val vagas =
                                                                evento.capacidadeTotal - evento.ingressosVendidos
                                                            println("${COR.AZUL}[$contador]${COR.RESET} ${evento.nome}")
                                                            println(" ${evento.dataInicio.format(formatterDataHora)}")
                                                            println(" R$ %.2f | Vagas: $vagas".format(evento.preco))
                                                            println(lineBar)
                                                            contador++
                                                        }

                                                        print("\nDigite o número do evento (0 para cancelar): ")
                                                        val escolhaEvento = readln().toIntOrNull() ?: 0

                                                        if (escolhaEvento in 1..eventosDisponiveis.size) {
                                                            val eventoEscolhido = eventosDisponiveis[escolhaEvento - 1]

                                                            println("\n$lineBar")
                                                            println("Evento selecionado: ${COR.NEGRITO}${eventoEscolhido.nome}${COR.RESET}")
                                                            println(lineBar)

                                                            if (eventoEscolhido.ingressosVendidos >= eventoEscolhido.capacidadeTotal) {
                                                                println(COR.VERMELHO + "ERRO: Evento lotado!" + COR.RESET)
                                                            } else {
                                                                var jaTemIngresso = false
                                                                // [Critério 11 - any] Mesmo padrão de 'jaTemIngresso' visto no feed. Substitua o laço 'for' + 'break' pela função 'any'.
                                                                // Talvez, se você usasse:
                                                                //   val jaTemIngresso = listaIngressos.any { it.idUsuario == listaUsuarios.indexOf(usuarioLogado) && it.idEvento == eventoEscolhido.id && it.status == StatusIngresso.ATIVO }
                                                                for (ing in listaIngressos) {
                                                                    if (ing.idUsuario == listaUsuarios.indexOf(
                                                                            usuarioLogado
                                                                        ) &&
                                                                        ing.idEvento == eventoEscolhido.id &&
                                                                        ing.status == StatusIngresso.ATIVO
                                                                    ) {
                                                                        jaTemIngresso = true
                                                                        break
                                                                    }
                                                                }

                                                                if (jaTemIngresso) {
                                                                    println(COR.AMARELO + "Você já possui ingresso para este evento!" + COR.RESET)
                                                                } else {
                                                                    var eventoVinculado: Evento? = null
                                                                    // [Critério 10 - filter/find] O laço 'for' com 'break' para buscar o evento vinculado pode ser substituído por 'find'.
                                                                    // [Critério 13 - Operador ?.] Talvez, se você usasse:
                                                                    //   val eventoVinculado = eventoEscolhido.idEventoVinculado?.let { id -> listaEventos.find { it.id == id } }
                                                                    // o código ficaria mais conciso e sem a necessidade do 'if != null' e do laço manual.
                                                                    if (eventoEscolhido.idEventoVinculado != null) {
                                                                        for (ev in listaEventos) {
                                                                            if (ev.id == eventoEscolhido.idEventoVinculado) {
                                                                                eventoVinculado = ev
                                                                                break
                                                                            }
                                                                        }
                                                                    }

                                                                    var valorTotal = eventoEscolhido.preco
                                                                    val ingressosAComprar =
                                                                        mutableListOf<Pair<Evento, Double>>()
                                                                    ingressosAComprar.add(
                                                                        Pair(
                                                                            eventoEscolhido,
                                                                            eventoEscolhido.preco
                                                                        )
                                                                    )

                                                                    if (eventoVinculado != null) {
                                                                        println(COR.AMARELO + "Este evento está vinculado ao evento: ${eventoVinculado.nome}" + COR.RESET)
                                                                        println("Você receberá ingressos para AMBOS os eventos.")
                                                                        valorTotal += eventoVinculado.preco
                                                                        ingressosAComprar.add(
                                                                            Pair(
                                                                                eventoVinculado,
                                                                                eventoVinculado.preco
                                                                            )
                                                                        )
                                                                    }

                                                                    println("\nValor total: R$ %.2f".format(valorTotal))
                                                                    println("\nConfirmar compra? (1-Sim, 2-Não)")

                                                                    val confirmacao = readln().toIntOrNull() ?: 2
                                                                    if (confirmacao == 1) {
                                                                        var compraRealizada = true
                                                                        val ingressosCriados = mutableListOf<Ingresso>()

                                                                        for (par in ingressosAComprar) {
                                                                            val evt = par.first
                                                                            val vlr = par.second

                                                                            if (evt.ingressosVendidos < evt.capacidadeTotal) {
                                                                                val novoIngresso = Ingresso(
                                                                                    id = proximoIdIngresso,
                                                                                    idUsuario = listaUsuarios.indexOf(
                                                                                        usuarioLogado
                                                                                    ),
                                                                                    idEvento = evt.id,
                                                                                    precoPago = vlr,
                                                                                    status = StatusIngresso.ATIVO
                                                                                )
                                                                                listaIngressos.add(novoIngresso)
                                                                                ingressosCriados.add(novoIngresso)
                                                                                proximoIdIngresso++
                                                                                evt.ingressosVendidos++
                                                                            } else {
                                                                                compraRealizada = false
                                                                                println(COR.VERMELHO + "ERRO: Sem vagas para ${evt.nome}" + COR.RESET)
                                                                                for (ingCriado in ingressosCriados) {
                                                                                    listaIngressos.remove(ingCriado)
                                                                                    for (e in listaEventos) {
                                                                                        if (e.id == ingCriado.idEvento) {
                                                                                            e.ingressosVendidos--
                                                                                            break
                                                                                        }
                                                                                    }
                                                                                }
                                                                                break
                                                                            }
                                                                        }

                                                                        if (compraRealizada) {
                                                                            println("\n$lineBar")
                                                                            println(COR.VERDE + "COMPRA REALIZADA COM SUCESSO!" + COR.RESET)
                                                                            println(lineBar)
                                                                            println("Ingressos adquiridos:")
                                                                            for (ing in ingressosCriados) {
                                                                                var nomeEvt = ""
                                                                                for (e in listaEventos) {
                                                                                    if (e.id == ing.idEvento) {
                                                                                        nomeEvt = e.nome
                                                                                        break
                                                                                    }
                                                                                }
                                                                                println(
                                                                                    " • $nomeEvt - R$ %.2f".format(
                                                                                        ing.precoPago
                                                                                    )
                                                                                )
                                                                            }
                                                                            println(
                                                                                "Total pago: R$ %.2f".format(
                                                                                    valorTotal
                                                                                )
                                                                            )
                                                                            println(lineBar)
                                                                        }
                                                                    } else {
                                                                        println(COR.AMARELO + "Compra cancelada." + COR.RESET)
                                                                    }
                                                                }
                                                            }
                                                        } else if (escolhaEvento != 0) {
                                                            println(COR.VERMELHO + "Opção inválida!" + COR.RESET)
                                                        }
                                                    }
                                                    println("\nPressione ENTER para continuar...")
                                                    readln()
                                                }

                                                // ===== US 13: CANCELAR INGRESSO =====
                                                3 -> {
                                                    println("\n$lineBar")
                                                    println(COR.AZUL + "CANCELAR INGRESSO" + COR.RESET)
                                                    println(lineBar)

                                                    val ingressosAtivos = mutableListOf<Ingresso>()
                                                    // [Critério 10 - filter/find] O laço 'for' abaixo pode ser substituído por 'filter', que é mais idiomático:
                                                    // Talvez, se você usasse:
                                                    //   val ingressosAtivos = listaIngressos.filter { it.idUsuario == listaUsuarios.indexOf(usuarioLogado) && it.status == StatusIngresso.ATIVO }
                                                    for (ingresso in listaIngressos) {
                                                        if (ingresso.idUsuario == listaUsuarios.indexOf(usuarioLogado) && ingresso.status == StatusIngresso.ATIVO) {
                                                            ingressosAtivos.add(ingresso)
                                                        }
                                                    }

                                                    if (ingressosAtivos.isEmpty()) {
                                                        println(COR.AMARELO + "Você não possui ingressos ativos para cancelar." + COR.RESET)
                                                    } else {
                                                        // [Critério 16 - forEachIndexed] A variável 'contador' manual pode ser substituída por 'forEachIndexed'.
                                                        // [Critério 12 - forEach] Talvez, se você usasse:
                                                        //   ingressosAtivos.forEachIndexed { index, ingresso -> println("[${index + 1}] ...") }
                                                        var contador = 1
                                                        for (ingresso in ingressosAtivos) {
                                                            var eventoNome = "Desconhecido"
                                                            var eventoData = ""
                                                            // [Critério 10 - filter/find] O laço 'for' com 'break' para buscar o evento pode ser substituído por 'find':
                                                            // Talvez, se você usasse:
                                                            //   val eventoDoIngresso = listaEventos.find { it.id == ingresso.idEvento }
                                                            //   val eventoNome = eventoDoIngresso?.nome ?: "Desconhecido"
                                                            for (evento in listaEventos) {
                                                                if (evento.id == ingresso.idEvento) {
                                                                    eventoNome = evento.nome
                                                                    eventoData =
                                                                        evento.dataInicio.format(formatterDataHora)
                                                                    break
                                                                }
                                                            }
                                                            println("${COR.AZUL}[$contador]${COR.RESET} $eventoNome")
                                                            println("$eventoData")
                                                            println("Pago: R$ %.2f".format(ingresso.precoPago))
                                                            println(lineBar)
                                                            contador++
                                                        }

                                                        print("\nDigite o número do ingresso para cancelar (0 para voltar): ")
                                                        val escolha = readln().toIntOrNull() ?: 0

                                                        if (escolha in 1..ingressosAtivos.size) {
                                                            val ingressoEscolhido = ingressosAtivos[escolha - 1]
                                                            var eventoIngresso: Evento? = null
                                                            // [Critério 10 - filter/find] O laço 'for' com 'break' para buscar o evento do ingresso pode ser substituído por 'find':
                                                            // [Critério 13 - Operador ?.] Talvez, se você usasse:
                                                            //   val eventoIngresso = listaEventos.find { it.id == ingressoEscolhido.idEvento }
                                                            // o código ficaria mais conciso e sem a necessidade do laço manual.
                                                            for (evento in listaEventos) {
                                                                if (evento.id == ingressoEscolhido.idEvento) {
                                                                    eventoIngresso = evento
                                                                    break
                                                                }
                                                            }

                                                            if (eventoIngresso != null) {
                                                                println("\n$lineBar")
                                                                println("Cancelar ingresso de: ${COR.NEGRITO}${eventoIngresso.nome}${COR.RESET}")

                                                                // [Critério 8 - Valores Financeiros] 'valorEstorno' é 'Double'. Talvez, se você usasse 'BigDecimal', o cálculo do estorno ficaria mais preciso.
                                                                // A variável seria declarada da seguinte forma: var valorEstorno: BigDecimal = BigDecimal.ZERO
                                                                var valorEstorno = 0.0
                                                                // CORREÇÃO 2: Acessando a propriedade estornaDinheiro corretamente
                                                                if (eventoIngresso.estornaDinheiro) {
                                                                    valorEstorno =
                                                                        ingressoEscolhido.precoPago * (1 - eventoIngresso.taxaEstorno)
                                                                    println(
                                                                        "Valor pago: R$ %.2f".format(
                                                                            ingressoEscolhido.precoPago
                                                                        )
                                                                    )
                                                                    println(
                                                                        "Taxa de estorno: %.0f%%".format(
                                                                            eventoIngresso.taxaEstorno * 100
                                                                        )
                                                                    )
                                                                    println(
                                                                        "Valor a receber: R$ %.2f".format(
                                                                            valorEstorno
                                                                        )
                                                                    )
                                                                } else {
                                                                    println(COR.AMARELO + "Este evento não faz estorno de valores." + COR.RESET)
                                                                }

                                                                println("\nConfirmar cancelamento? (1-Sim, 2-Não)")
                                                                val confirmacao = readln().toIntOrNull() ?: 2
                                                                if (confirmacao == 1) {
                                                                    ingressoEscolhido.status = StatusIngresso.CANCELADO
                                                                    eventoIngresso.ingressosVendidos--
                                                                    println("\n$lineBar")
                                                                    println(COR.VERDE + "INGRESSO CANCELADO COM SUCESSO!" + COR.RESET)
                                                                    if (valorEstorno > 0) {
                                                                        println(
                                                                            "Valor estornado: R$ %.2f".format(
                                                                                valorEstorno
                                                                            )
                                                                        )
                                                                    }
                                                                    println("Vaga liberada no evento.")
                                                                    println(lineBar)
                                                                } else {
                                                                    println(COR.AMARELO + "Cancelamento não realizado." + COR.RESET)
                                                                }
                                                            }
                                                        } else if (escolha != 0) {
                                                            println(COR.VERMELHO + "Opção inválida!" + COR.RESET)
                                                        }
                                                    }
                                                    println("\nPressione ENTER para continuar...")
                                                    readln()
                                                }

                                                // ===== US 14: LISTAR INGRESSOS =====
                                                4 -> {
                                                    println("\n$lineBar")
                                                    println(COR.AZUL + "MEUS INGRESSOS" + COR.RESET)
                                                    println(lineBar)

                                                    val meusIngressos = mutableListOf<Ingresso>()
                                                    // [Critério 10 - filter/find] O laço 'for' abaixo pode ser substituído por 'filter'.
                                                    // [Critério 5 - Tipo de Coleção] Com 'filter', 'meusIngressos' poderia ser 'val' (imutável):
                                                    // Talvez, se você usasse:
                                                    //   val meusIngressos = listaIngressos.filter { it.idUsuario == listaUsuarios.indexOf(usuarioLogado) }
                                                    for (ingresso in listaIngressos) {
                                                        if (ingresso.idUsuario == listaUsuarios.indexOf(usuarioLogado)) {
                                                            meusIngressos.add(ingresso)
                                                        }
                                                    }

                                                    if (meusIngressos.isEmpty()) {
                                                        println(COR.AMARELO + "Você ainda não possui ingressos." + COR.RESET)
                                                    } else {
                                                        val ingressosAtivos = mutableListOf<Pair<Ingresso, Evento>>()
                                                        val ingressosInativos = mutableListOf<Pair<Ingresso, Evento>>()
                                                        val agora = LocalDateTime.now()

                                                        for (ingresso in meusIngressos) {
                                                            var eventoEncontrado: Evento? = null
                                                            // [Critério 10 - filter/find] O laço 'for' com 'break' pode ser substituído por 'find'.
                                                            // [Critério 13 - Operador ?.] Talvez, se você usasse:
                                                            //   val eventoEncontrado = listaEventos.find { it.id == ingresso.idEvento }
                                                            // o código ficaria mais conciso, sem laço manual e sem necessidade do 'break'.
                                                            for (evento in listaEventos) {
                                                                if (evento.id == ingresso.idEvento) {
                                                                    eventoEncontrado = evento
                                                                    break
                                                                }
                                                            }

                                                            if (eventoEncontrado != null) {
                                                                val par = Pair(ingresso, eventoEncontrado)
                                                                if (ingresso.status == StatusIngresso.ATIVO && eventoEncontrado.dataFim.isAfter(
                                                                        agora
                                                                    )
                                                                ) {
                                                                    ingressosAtivos.add(par)
                                                                } else {
                                                                    ingressosInativos.add(par)
                                                                }
                                                            }
                                                        }

                                                        // [Critério 10 - filter/find] Os dois Bubble Sorts abaixo (ativos e inativos) repetem a mesma lógica de ordenação vista em outras partes do código.
                                                        // Talvez, se você usasse 'sortedWith' diretamente após o 'filter', evitaria a duplicação e o código ficaria mais legível:
                                                        //   val ingressosAtivos = meusIngressos.filter { ... }.sortedWith(compareBy({ listaEventos.find { e -> e.id == it.idEvento }?.dataInicio }, { ... }))
                                                        // Ordenação
                                                        for (i in 0 until ingressosAtivos.size - 1) {
                                                            for (j in 0 until ingressosAtivos.size - i - 1) {
                                                                val par1 = ingressosAtivos[j]
                                                                val par2 = ingressosAtivos[j + 1]
                                                                val comp =
                                                                    par1.second.dataInicio.compareTo(par2.second.dataInicio)
                                                                if (comp > 0 || (comp == 0 && par1.second.nome > par2.second.nome)) {
                                                                    ingressosAtivos[j] = par2
                                                                    ingressosAtivos[j + 1] = par1
                                                                }
                                                            }
                                                        }
                                                        for (i in 0 until ingressosInativos.size - 1) {
                                                            for (j in 0 until ingressosInativos.size - i - 1) {
                                                                val par1 = ingressosInativos[j]
                                                                val par2 = ingressosInativos[j + 1]
                                                                val comp =
                                                                    par1.second.dataInicio.compareTo(par2.second.dataInicio)
                                                                if (comp > 0 || (comp == 0 && par1.second.nome > par2.second.nome)) {
                                                                    ingressosInativos[j] = par2
                                                                    ingressosInativos[j + 1] = par1
                                                                }
                                                            }
                                                        }

                                                        // Exibição
                                                        if (ingressosAtivos.isNotEmpty()) {
                                                            println(COR.VERDE + "EVENTOS ATIVOS (${ingressosAtivos.size})" + COR.RESET)
                                                            println(lineBar)
                                                            // [Critério 12 - forEach] O 'for' abaixo poderia ser substituído por 'forEach', que é mais idiomático em Kotlin:
                                                            // Talvez, se você usasse: ingressosAtivos.forEach { (ing, evt) -> println(evt.nome) }
                                                            for (par in ingressosAtivos) {
                                                                val ing = par.first
                                                                val evt = par.second
                                                                println("️${COR.NEGRITO}${evt.nome}${COR.RESET}")
                                                                println("ID Ingresso: #${ing.id}")
                                                                println("${evt.dataInicio.format(formatterDataHora)}")
                                                                println("${evt.local}")
                                                                println("R$ %.2f".format(ing.precoPago))
                                                                println("Status: ${COR.VERDE}ATIVO${COR.RESET}")
                                                                println(lineBar)
                                                            }
                                                        }

                                                        if (ingressosInativos.isNotEmpty()) {
                                                            println(COR.AMARELO + "EVENTOS FINALIZADOS/CANCELADOS (${ingressosInativos.size})" + COR.RESET)
                                                            println(lineBar)
                                                            // [Critério 12 - forEach] O 'for' abaixo poderia ser substituído por 'forEach'.
                                                            for (par in ingressosInativos) {
                                                                val ing = par.first
                                                                val evt = par.second
                                                                println("${evt.nome}")
                                                                println("ID Ingresso: #${ing.id}")
                                                                println("${evt.dataInicio.format(formatterDataHora)}")
                                                                println("R$ %.2f".format(ing.precoPago))
                                                                // [Critério 15 - when no lugar de if..else if] O bloco 'if/else' abaixo poderia ser substituído por 'when' como expressão.
                                                                // Talvez, se você usasse: println("Status: ${when(ing.status) { StatusIngresso.CANCELADO -> ... else -> ... }}")
                                                                if (ing.status == StatusIngresso.CANCELADO) {
                                                                    println("Status: ${COR.VERMELHO}CANCELADO${COR.RESET}")
                                                                } else {
                                                                    println("Status: ${COR.AMARELO}FINALIZADO${COR.RESET}")
                                                                }
                                                                println(lineBar)
                                                            }
                                                        }
                                                        println(COR.AZUL + "Total de ingressos: ${meusIngressos.size}" + COR.RESET)
                                                    }
                                                    println("\nPressione ENTER para voltar...")
                                                    readln()
                                                }

                                                else -> println("Opção inválida.")
                                            }
                                        }
                                }

                            } // Fecha o case 4 -> (Gerenciar/Feed)
                        } // Fecha o when (opcaoMenuLogado)
                    } // Fecha o while (sessaoAtiva)
                } else {
                    // Trata o usuário que está com a conta inativada
                    println(COR.VERMELHO + "ACESSO NEGADO: Sua conta está inativa." + COR.RESET)
                    println("Utilize a opção 3 no menu principal para reativar.")
                }
            } else {
            // Trata o erro de digitação de email/senha
            println("ERRO: Usuário ou senha inválidos (ou conta inativa).")
        }
        } // Fecha o case 1 -> (Fazer Login)

        // --- INÍCIO DA OPÇÃO 2 (REGISTRO) ---
        2 -> {
            println("\n---- REGISTRO DE NOVO USUÁRIO ----")
                                println("Para qual finalidade gostaria de Criar sua conta?")
                                println("1) Quero participar de Eventos (Usuário Comum)")
                                println("2) Quero Organizar Eventos (Conta de Organizador")
                                println("0) Voltar")
                                println(lineBar)

                                val opcaoRegistroConta = readln().toIntOrNull() ?: 0
                                println(lineBar)
                                when(opcaoRegistroConta) {
                                    0 -> {
                                        println("Voltando..")
                                    }
                                    1 -> {
                                        println(COR.AMARELO + "--- CRIANDO PERFIL (USUÁRIO) ---" + COR.RESET)
                                        // Variáveis para o ciclo de vida da criação do usuário e contramedidas contra erros do usuário possibilitando repetição
                                        var cicloCriarUsuarioComum = true
                                        var cicloEmail = true
                                        var cicloSenha = true
                                        var cicloNome = true
                                        var cicloDataNascimento = true

                                        // variáveis para armazenar os dados do usuário de forma segura
                                        var nome = ""
                                        var email = ""
                                        var senha = ""
                                        var dataNascimento : LocalDate = LocalDate.now()
                                        var sexo : Sexo = Sexo.OUTROS
                                        // [Critério 3a - val/var] 'ativo' é declarado como 'val' mas nunca é utilizado no código — o valor padrão 'true' da data class já cuida disso.
                                        // Talvez, se você removesse essa variável, o código ficaria mais limpo.
                                        val ativo: Boolean = true

                                        // Para a verificação de idade na data de nascimento
                                        val idadeMinima = 12

                                        // [Critério 2 - Loop Adequado] 'while(cicloCriarUsuarioComum)' é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                        // [Critério 14 - do..while] Todo o fluxo de cadastro é executado ao menos uma vez, o que torna o 'do..while' mais adequado.
                                        while (cicloCriarUsuarioComum) {
                                            // [Critério 2 - Loop Adequado] 'while(cicloEmail)' é também uma flag booleana. O 'do..while' seria mais adequado:
                                            // do { lê o email; valida } while(email inválido ou duplicado)
                                            while (cicloEmail) {
                                                println("Vamos criar um usuário comum então")
                                                println(lineBar)
                                                print("\nDigite seu email: ")
                                                val inputEmail = readln().trim()
                                                // Verificação de formato correto do email, critério: Conter o @ e 5 ou mais caracteres
                                                if (inputEmail.contains("@") && inputEmail.length >= 5) {
                                                    // Variáveis para verificação de duplicidade de email nos usuários comuns e organizadores
                                                    val verificarDuplicidadeEmailUsuarioComum =
                                                        listaUsuarios.any { it.email == inputEmail }
                                                    val verificarDuplicidadeEmailOrganizador =
                                                        listaOrganizadores.any { it.email == inputEmail }
                                                    // Condicional Verificando emails duplicados
                                                    if (verificarDuplicidadeEmailUsuarioComum || verificarDuplicidadeEmailOrganizador) {
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + " Email informado ja cadastrado, por favor efetue o login ou utilize um email diferente" + COR.RESET)
                                                    } else {
                                                        println(COR.VERDE + "E-mail válido e disponível. Prosseguindo..." + COR.RESET)
                                                        email = inputEmail
                                                        cicloEmail = false
                                                    }
                                                } else {
                                                    println(
                                                        COR.VERMELHO + "ERRO: " + COR.AMARELO + "Email no formato incorreto. O e-mail precisa ter '@' e possuir mais de 4 caracteres\n" +
                                                                " Por favor digite novamente" + COR.RESET
                                                    )
                                                }
                                            }

                                            // [Critério 2 - Loop Adequado] 'while(cicloSenha)' (usuário comum) é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                            // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                            //   do {
                                            //       print("Senha: "); inputSenha = readln().trim()
                                            //       print("Confirme: "); inputSenhaConfirmacao = readln().trim()
                                            //   } while(inputSenha.isEmpty() || inputSenha.length < 8 || inputSenha != inputSenhaConfirmacao)
                                            // [Critério 15 - when no lugar de if..else if] A cadeia de if/else if para validar a senha poderia ser substituída por 'when'.
                                            while (cicloSenha){
                                                println("Vamos criar um usuário comum então")
                                                println(lineBar)
                                                print("\nDigite seu email: ")
                                                val inputEmail = readln().trim()
                                                // Verificação de formato correto do email, critério: Conter o @ e 5 ou mais caracteres
                                                if (inputEmail.contains("@") && inputEmail.length >= 5) {
                                                    // Variáveis para verificação de duplicidade de email nos usuários comuns e organizadores
                                                    val verificarDuplicidadeEmailUsuarioComum =
                                                        listaUsuarios.any { it.email == inputEmail }
                                                    val verificarDuplicidadeEmailOrganizador =
                                                        listaOrganizadores.any { it.email == inputEmail }
                                                    // Condicional Verificando emails duplicados
                                                    if (verificarDuplicidadeEmailUsuarioComum || verificarDuplicidadeEmailOrganizador) {
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + " Email informado ja cadastrado, por favor efetue o login ou utilize um email diferente" + COR.RESET)
                                                    } else {
                                                        println(COR.VERDE + "E-mail válido e disponível. Prosseguindo..." + COR.RESET)
                                                        email = inputEmail
                                                        cicloEmail = false
                                                    }
                                                } else {
                                                    println(
                                                        COR.VERMELHO + "ERRO: " + COR.AMARELO + "Email no formato incorreto. O e-mail precisa ter '@' e possuir mais de 4 caracteres\n" +
                                                                " Por favor digite novamente" + COR.RESET
                                                    )
                                                }
                                            }

                                            while (cicloSenha){
                                                println(lineBar)
                                                print("\nDigite sua senha: ")
                                                val inputSenha = readln().trim()
                                                print("\nDigite novamente sua senha: ")
                                                val inputSenhaConfirmacao = readln().trim()

                                                if (inputSenha.isEmpty()){
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A senha precisa ser preenchida. Por favor digite uma senha" + COR.RESET)
                                                }
                                                else if (inputSenha.length < 8) {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A senha precisa possuir 8 ou mais caracteres. Por favor digite uma nova senha" + COR.RESET)
                                                }
                                                else if (inputSenha != inputSenhaConfirmacao) {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "As senhas não coincidem por favor digite a senha novamente" + COR.RESET)
                                                } else {
                                                    println(COR.VERDE + "Senha cadastrada com sucesso! Prosseguindo..." + COR.RESET)
                                                    senha = inputSenha
                                                    cicloSenha = false
                                                }
                                            }

                                            // [Critério 2 - Loop Adequado] 'while(cicloNome)' (usuário comum) é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                            // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                            //   do { print("Nome: "); inputNome = readln() } while(inputNome.trim().length < 2)
                                            while (cicloNome) {
                                                println(lineBar)
                                                print("\nDigite seu Nome: ")
                                                val inputNome = readln()
                                                if (inputNome.trim() != "" && inputNome.length >= 2) {
                                                    nome = inputNome
                                                    println(COR.VERDE + "Nome cadastrado com sucesso! Prosseguindo..." + COR.RESET)
                                                    cicloNome = false
                                                } else {
                                                    print(COR.AMARELO + "\nVocê digitou um nome vazio ou muito curto, por favor digite um nome válido: ")
                                                }
                                            }
                                            println(lineBar)
                                            print("\nQual gênero você se identifica: \n1) MASCULINO, \n2) FEMININO, \n3) OUTROS \nDigite o número da opção: ")
                                            val inputSexoOpcao = readln().toIntOrNull() ?: 3
                                            when(inputSexoOpcao) {
                                                1 -> sexo = Sexo.MASCULINO
                                                2 -> sexo = Sexo.FEMININO
                                                3 -> sexo = Sexo.OUTROS
                                                else -> {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Opção invalida. Assumindo opção 'OUTROS',\n" + COR.VERDE + " você pode alterar isso em outro momento ok? Vamos prosseguir" + COR.RESET)
                                                    sexo = Sexo.OUTROS
                                                }
                                            }
                                            println(COR.VERDE + "Gênero cadastrado com sucesso! Prosseguindo..." + COR.RESET)
                                            // [Critério 2 - Loop Adequado] 'while(cicloDataNascimento)' (usuário comum) é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                            // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                            //   do { lê a data; valida formato e idade } while(data inválida)
                                            // [Critério 15 - when no lugar de if..else if] A cadeia de if/else if para validar a data poderia ser substituída por 'when'.
                                            while (cicloDataNascimento) {
                                                println(lineBar)
                                                val hoje = LocalDate.now()

                                                print("\nQual sua data de nascimento? \n" +
                                                        "Digite nesse formato Dia/Mês/Ano, Ex.:21/02/1992:  ")
                                                val inputDataNascimento = readln().trim()

                                                try {
                                                    // 1. Tenta converter a String para LocalDate
                                                    val dataConvertida = LocalDate.parse(inputDataNascimento, formatterDate)

                                                    // 2. Garantindo que não seja uma data do futuro ou sem coerência
                                                    if(dataConvertida.isAfter(LocalDate.now())){
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Você não pode ter nascido no futuro!" + COR.RESET)
                                                    }
                                                    // 2. Verifica se é muito velho (Opcional - ex: 120 anos)
                                                    else if (dataConvertida.isBefore(hoje.minusYears(120))) {
                                                        println(COR.VERMELHO + "ERRO: Data inválida." + COR.RESET)
                                                    }
                                                    // Se a data de nascimento for DEPOIS de (Hoje - 12 anos), a pessoa ainda não fez 12.
                                                    else if (dataConvertida.isAfter(LocalDate.now().minusYears(idadeMinima.toLong()))) {
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Você precisa ter pelo menos" + COR.NEGRITO +  COR.VERDE + " $idadeMinima anos " + COR.VERMELHO +"para se cadastrar." + COR.RESET)

                                                        // Mostra a idade calculada para o usuário
                                                        val idadeCalculada = Period.between(dataConvertida, hoje).years
                                                        println(COR.AMARELO + "Sua idade atual: $idadeCalculada anos." + COR.RESET)
                                                    }else {
                                                        // Se for bem-sucedido
                                                        dataNascimento = dataConvertida
                                                        println(COR.VERDE + "Data de nascimento valida! Idade Confirmada" + COR.RESET)
                                                        cicloDataNascimento = false
                                                    }

                                                }catch (e: Exception) {
                                                    // O usuário digitou formato errado ou dia inexistente
                                                    println(COR.VERMELHO + "ERRO: Formato inválido!" + COR.AMARELO + " Use o padrão dia/mês/ano (ex: 20/05/2000)." + COR.RESET)
                                                }
                                            }
                                            println(lineBar)
                                            println("Deseja refazer todo o cadastro? \n1) Sim \n2) Não")
                                            val inputRepetirCadastro = readln().toIntOrNull() ?: 2
                                            if (inputRepetirCadastro == 1) {
                                                println("Reiniciando cadastro...")
                                                cicloNome = true
                                                cicloEmail = true
                                                cicloSenha = true
                                                cicloDataNascimento = true
                                            } else {
                                                try {
                                                    listaUsuarios.add(UsuarioComum(nome, dataNascimento, sexo, email, senha))
                                                } catch (e: Exception) {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + e.message + COR.RESET)
                                                }
                                                println(lineBar)
                                                cicloCriarUsuarioComum = false
                                            }
                                        }
                                    }
                                    2 -> {
                                        // Ciclo de criação de usuário organizador
                                        println(lineBar)
                                        println(COR.AMARELO + "--- CRIANDO PERFIL (ORGANIZADOR) ---" + COR.RESET)

                                        var cicloCriarOrganizador = true
                                        var cicloEmail = true
                                        var cicloSenha = true
                                        var cicloNome = true
                                        var cicloDataNascimento = true

                                        // variáveis para armazenar os dados do organizador de forma segura
                                        var nome = ""
                                        var email = ""
                                        var senha = ""
                                        var dataNascimento : LocalDate = LocalDate.now()
                                        var sexo : Sexo = Sexo.OUTROS
                                        // [Critério 3a - val/var] 'ativo' é declarado como 'val' mas nunca é utilizado — o valor padrão 'true' da data class já cuida disso.
                                        // Talvez, se você removesse essa variável, o código ficaria mais limpo.
                                        val ativo: Boolean = true

                                        // Dados opcionais da empresa
                                        var cnpj: String? = null
                                        var razaoSocial: String? = null
                                        var nomeFantasia: String? = null

                                        // Para a verificação de idade na data de nascimento
                                        val idadeMinima = 18

                                        // [Critério 2 - Loop Adequado] 'while(cicloCriarOrganizador)' é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                        // [Critério 14 - do..while] Todo o fluxo de cadastro é executado ao menos uma vez, o que torna o 'do..while' mais adequado.
                                        while (cicloCriarOrganizador) {
                                            // [Critério 2 - Loop Adequado] 'while(cicloEmail)' é também uma flag booleana. O 'do..while' seria mais adequado:
                                            // do { lê o email; valida } while(email inválido ou duplicado)
                                            while (cicloEmail) {
                                                println("Vamos criar um usuário organizador então")
                                                println(lineBar)
                                                print("\nDigite seu email: ")
                                                val inputEmail = readln().trim()
                                                // Verificação de formato correto do email, critério: Conter o @ e 5 ou mais caracteres
                                                if (inputEmail.contains("@") && inputEmail.length >= 5) {
                                                    // Variáveis para verificação de duplicidade de email nos usuários comuns e organizadores
                                                    val verificarDuplicidadeEmailUsuarioComum =
                                                        listaUsuarios.any { it.email == inputEmail }
                                                    val verificarDuplicidadeEmailOrganizador =
                                                        listaOrganizadores.any { it.email == inputEmail }
                                                    // Condicional Verificando emails duplicados
                                                    if (verificarDuplicidadeEmailUsuarioComum || verificarDuplicidadeEmailOrganizador) {
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Email informado ja cadastrado, por favor efetue o login ou utilize um email diferente" + COR.RESET)
                                                    } else {
                                                        println(COR.VERDE + "E-mail válido e disponível. Prosseguindo..." + COR.RESET)
                                                        email = inputEmail
                                                        cicloEmail = false
                                                    }
                                                } else {
                                                    println(
                                                        COR.VERMELHO + "ERRO: " + COR.AMARELO + "Email no formato incorreto. O e-mail precisa ter '@' e possuir mais de 4 caracteres\n" +
                                                                " Por favor digite novamente" + COR.RESET
                                                    )
                                                }
                                            }

                                            while (cicloSenha){
                                                println(lineBar)
                                                print("\nDigite sua senha: ")
                                                val inputSenha = readln().trim()
                                                print("\nDigite novamente sua senha: ")
                                                val inputSenhaConfirmacao = readln().trim()

                                                if (inputSenha.isEmpty()){
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A senha precisa ser preenchida. Por favor digite uma senha" + COR.RESET)
                                                }
                                                else if (inputSenha.length < 8) {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "A senha precisa possuir 8 ou mais caracteres. Por favor digite uma nova senha" + COR.RESET)
                                                }
                                                else if (inputSenha != inputSenhaConfirmacao) {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "As senhas não coincidem por favor digite a senha novamente" + COR.RESET)
                                                } else {
                                                    println(COR.VERDE + "Senha cadastrada com sucesso! Prosseguindo..." + COR.RESET)
                                                    senha = inputSenha
                                                    cicloSenha = false
                                                }
                                            }

                                            // [Critério 2 - Loop Adequado] 'while(cicloNome)' (organizador) é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                            // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                            //   do { print("Nome: "); inputNome = readln() } while(inputNome.trim().length < 2)
                                            while (cicloNome) {
                                                println(lineBar)
                                                print("\nDigite seu Nome: ")
                                                val inputNome = readln()
                                                if (inputNome.trim() != "" && inputNome.length >= 2) {
                                                    nome = inputNome
                                                    println(COR.VERDE + "Nome cadastrado com sucesso! Prosseguindo..." + COR.RESET)
                                                    cicloNome = false
                                                } else {
                                                    print(COR.AMARELO + "\nVocê digitou um nome vazio ou muito curto, por favor digite um nome válido: ")
                                                }
                                            }
                                            println(lineBar)
                                            print("\nQual gênero você se identifica: \n1) MASCULINO, \n2) FEMININO, \n3) OUTROS \nDigite o número da opção: ")
                                            val inputSexoOpcao = readln().toIntOrNull() ?: 3
                                            when(inputSexoOpcao) {
                                                1 -> sexo = Sexo.MASCULINO
                                                2 -> sexo = Sexo.FEMININO
                                                3 -> sexo = Sexo.OUTROS
                                                else -> {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Opção invalida. Assumindo opção 'OUTROS',\n" + COR.VERDE + " você pode alterar isso em outro momento ok? Vamos prosseguir" + COR.RESET)
                                                    sexo = Sexo.OUTROS
                                                }
                                            }
                                            println(COR.VERDE + "Gênero cadastrado com sucesso! Prosseguindo..." + COR.RESET)
                                            // [Critério 2 - Loop Adequado] 'while(cicloDataNascimento)' (organizador) é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                            // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                            //   do { lê a data; valida formato e idade } while(data inválida)
                                            // [Critério 15 - when no lugar de if..else if] A cadeia de if/else if para validar a data poderia ser substituída por 'when'.
                                            while (cicloDataNascimento) {
                                                println(lineBar)
                                                val hoje = LocalDate.now()

                                                print("\nQual sua data de nascimento? \n" +
                                                        "Digite nesse formato Dia/Mês/Ano, Ex.:21/02/1992:  ")
                                                val inputDataNascimento = readln().trim()

                                                try {
                                                    // 1. Tenta converter a String para LocalDate
                                                    val dataConvertida = LocalDate.parse(inputDataNascimento, formatterDate)

                                                    // 2. Garantindo que não seja uma data do futuro ou sem coerência
                                                    if(dataConvertida.isAfter(LocalDate.now())){
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Você não pode ter nascido no futuro!" + COR.RESET)
                                                    }
                                                    // 2. Verifica se é muito velho (Opcional - ex: 120 anos)
                                                    else if (dataConvertida.isBefore(hoje.minusYears(120))) {
                                                        println(COR.VERMELHO + "ERRO: Data inválida." + COR.RESET)
                                                    }
                                                    // Se a data de nascimento for DEPOIS de (Hoje - 18 anos), a pessoa ainda não fez 18.
                                                    else if (dataConvertida.isAfter(LocalDate.now().minusYears(idadeMinima.toLong()))) {
                                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Você precisa ter pelo menos" + COR.NEGRITO +  COR.VERDE + " $idadeMinima anos " + COR.VERMELHO +"para se cadastrar." + COR.RESET)

                                                        // Mostra a idade calculada para o usuário
                                                        val idadeCalculada = Period.between(dataConvertida, hoje).years
                                                        println(COR.AMARELO + "Sua idade atual: $idadeCalculada anos." + COR.RESET)
                                                    }else {
                                                        // Se for bem-sucedido
                                                        dataNascimento = dataConvertida
                                                        println(COR.VERDE + "Data de nascimento valida! Idade Confirmada" + COR.RESET)
                                                        cicloDataNascimento = false
                                                    }

                                                }catch (e: Exception) {
                                                    // O usuário digitou formato errado ou dia inexistente
                                                    println(COR.VERMELHO + "ERRO: Formato inválido!" + COR.AMARELO + " Use o padrão dia/mês/ano (ex: 20/05/2000)." + COR.RESET)
                                                }
                                            }

                                            // cadastro de dados da empresa
                                            var cadastrarEmpresa = true
                                            // [Critério 2 - Loop Adequado] 'while(cadastrarEmpresa)' é uma flag booleana equivalente a while(true). Não é uma boa prática.
                                            // [Critério 14 - do..while] Uma sugestão para esse cenário, talvez, fosse você escrever esse laço como do..while:
                                            //   do {
                                            //       println("1) Sou PJ  2) Sou PF")
                                            //       val isEmpresa = readln().toIntOrNull() ?: 2
                                            //       if(isEmpresa == 1) { lê CNPJ, razão social, nome fantasia }
                                            //   } while(isEmpresa == 1 && cnpj?.length != 14)
                                            while (cadastrarEmpresa) {
                                                println(lineBar)
                                                println("Você representa uma Empresa/Instituição?")
                                                println("1) Sim (Sou Pessoa Jurídica)")
                                                println("2) Não (Sou Pessoa Física")
                                                print("Opção: ")
                                                val isEmpresa = readln().toIntOrNull() ?: 2

                                                if (isEmpresa == 1) {
                                                    println(lineBar)
                                                    print("Digite o CNPJ (somente os número): ")
                                                    val inputCnpj = readln().trim()

                                                    if (inputCnpj.length == 14) {
                                                        cnpj = inputCnpj
                                                        print("Digite a Razão Social: ")
                                                        razaoSocial = readln().trim()
                                                        print("Digite o Nome Fantasia: ")
                                                        nomeFantasia = readln().trim()
                                                        cadastrarEmpresa = false
                                                    } else {
                                                        println(COR.VERMELHO + "CNPJ inválido (deve conter 14 dígitos)." + COR.AMARELO + " Voltando...")
                                                        println(lineBar)
                                                    }
                                                } else {
                                                    cadastrarEmpresa = false
                                                }
                                            }

                                            println(lineBar)
                                            println("Deseja refazer todo o cadastro? \n1) Sim \n2) Não")
                                            val inputRepetirCadastro = readln().toIntOrNull() ?: 2
                                            if (inputRepetirCadastro == 1) {
                                                println("Reiniciando cadastro...")
                                                cicloNome = true
                                                cicloEmail = true
                                                cicloSenha = true
                                                cicloDataNascimento = true

                                                cnpj = null
                                                razaoSocial = null
                                                nomeFantasia = null

                                            } else {
                                                try {
                                                    listaOrganizadores.add(Organizador(nome, dataNascimento, sexo, email, senha, cnpj, razaoSocial, nomeFantasia))
                                                } catch (e: Exception) {
                                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + e.message + COR.RESET)
                                                }
                                                println(lineBar)
                                                cicloCriarOrganizador = false
                                            }
                                        }

                                    }
                                    else -> println(COR.VERMELHO + "ERRO: Opção Inválida." + COR.RESET)
                                }
                            }
                            3 -> {
                                println(lineBar)
                                println(COR.AMARELO + "--- REATIVAR CONTA ---" + COR.RESET)
                                println("Informe suas credenciais para reativar seu acesso.")

                                print("\nDigite seu E-mail cadastrado: ")
                                val emailBusca = readln().trim()

                                print("Digite sua Senha: ")
                                val senhaBusca = readln().trim()

                                // 1. Procura na lista de Usuários Comuns
                                val usuarioEncontrado = listaUsuarios.find { it.email == emailBusca && it.senha == senhaBusca }

                                // 2. Procura na lista de Organizadores
                                val organizadorEncontrado = listaOrganizadores.find { it.email == emailBusca && it.senha == senhaBusca }

                                // Lógica de Reativação
                                // [Critério 9 - Busca Unificada] A lógica de busca aqui também poderia ser unificada usando o operador Elvis '?:':
                                //   val contaEncontrada = listaUsuarios.find { it.email == emailBusca && it.senha == senhaBusca }
                                //       ?: listaOrganizadores.find { it.email == emailBusca && it.senha == senhaBusca }
                                // [Critério 15 - when no lugar de if..else if] E o bloco if/else if poderia ser substituído por 'when':
                                //   when(contaEncontrada) {
                                //       is UsuarioComum -> { if(!it.ativo) { it.ativo = true; println("Conta reativada!") } else println("Já está ativa.") }
                                //       is Organizador  -> { if(!it.ativo) { it.ativo = true; println("Conta reativada!") } else println("Já está ativa.") }
                                //       null -> println("Conta não encontrada.")
                                //   }
                                if (usuarioEncontrado != null) {
                                    if (!usuarioEncontrado.ativo) {
                                        usuarioEncontrado.ativo = true
                                        println(lineBar)
                                        println(COR.VERDE + "SUCESSO: Conta de Usuário Comum reativada!" + COR.RESET)
                                        println("Você já pode fazer login na Opção 1.")
                                    } else {
                                        println(COR.AMARELO + "Atenção: Sua conta já está ativa. Basta fazer login." + COR.RESET)
                                    }
                                }
                                // [Critério 15 - when no lugar de if..else if] O 'else if' aqui poderia ser substituído por 'when', conforme sugestão acima.
                                else if (organizadorEncontrado != null) {
                                    if (!organizadorEncontrado.ativo) {
                                        organizadorEncontrado.ativo = true
                                        println(lineBar)
                                        println(COR.VERDE + "SUCESSO: Conta de Organizador reativada!" + COR.RESET)
                                        println("Você já pode fazer login na Opção 1.")
                                    } else {
                                        println(COR.AMARELO + "Atenção: Sua conta já está ativa. Basta fazer login." + COR.RESET)
                                    }
                                }
                                else {
                                    // Se não achou em nenhuma lista ou a senha está errada
                                    println(lineBar)
                                    println(COR.VERMELHO + "ERRO: Conta não encontrada ou credenciais inválidas." + COR.RESET)
                                }

                                println(lineBar)
                                println("Pressione ENTER para voltar ao menu principal...")
                                readln()
                            }
                        }
                    }
                }