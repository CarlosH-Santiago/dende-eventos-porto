import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.text.format
import util.ConsoleTextColor as COR

val lineBar = "-".repeat(40)
val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")

// ---------------------------------------------------------
// MENUS DE ROTEAMENTO (Auxiliares)
// ---------------------------------------------------------

fun realizarLogin() : Any? {
    println(lineBar)
    println("\n---- LOGIN ----")
    val emailLogin = readString("Digite seu Email: ", COR.VERMELHO + "Email inválido" + COR.RESET, 5)
    val senhaLogin = readString("Digite sua Senha: ", COR.VERMELHO + "Senha inválida" + COR.RESET, 8)
    println(lineBar)

    val usuarioEncontrado = BuscarUsuario(emailLogin) ?: BuscarOrganizador(emailLogin)

    when (usuarioEncontrado) {
        is UsuarioComum -> {
            if(usuarioEncontrado.senha == senhaLogin && usuarioEncontrado.ativo == true){
                println(COR.VERDE + "Login realizado! Bem-vindo, ${usuarioEncontrado.nome}." + COR.RESET)
                return usuarioEncontrado// <-- RETORNA O USUÁRIO
            } else {
                println(COR.VERMELHO + "ERRO: Senha incorreta ou conta inativada." + COR.RESET)
                return null
            }
        }
        is Organizador -> {
            if(usuarioEncontrado.senha == senhaLogin && usuarioEncontrado.ativo == true){
                println(COR.VERDE + "Login realizado! Bem-vindo, ${usuarioEncontrado.nome}." + COR.RESET)
                return usuarioEncontrado// <-- RETORNA O USUÁRIO
            } else {
                println(COR.VERMELHO + "ERRO: Senha incorreta ou conta inativada." + COR.RESET)
                return null //
            }
        }
        else -> {
            println(COR.VERMELHO + "ERRO: Conta não encontrada." + COR.RESET)
            return null
        }
    }
}

fun iniciarSessaoAtiva(contaLogada: Any){
    var sessaoAtiva = true
    while(sessaoAtiva){
        when (contaLogada) {
            is UsuarioComum -> {
                sessaoAtiva = menuUsuarioComum(contaLogada)
            }
            is Organizador -> {
                sessaoAtiva = menuOrganizador(contaLogada)
            }
            else -> {
                println(COR.VERMELHO + "Erro crítico de sessão." + COR.RESET)
                sessaoAtiva = false
            }
        }
    }
}

fun menuUsuarioComum(contaLogada: UsuarioComum): Boolean {
    val opcoes = listOf(
        "1) Meu Perfil",
        "2) Alterar dados do Perfil",
        "3) Inativar Minha Conta",
        "4) Ver Feed de Eventos (Comprar Ingresso)",
        "0) Sair (logout)"
    )

    printTable("PAINEL DO USUÁRIO - Olá, ${contaLogada.nome}!", opcoes)
    val opcao = readInt("Opção: ", "Inválido", 0..4)

    when (opcao) {
        // Passa a contaLogada pra frente!
        1 -> visualizarPerfil(contaLogada)
        2 -> alterarPerfil(contaLogada)
        3 -> {
            val tentandoInativarConta = inativarConta(contaLogada)

            if (tentandoInativarConta) {
                return false // Realmente inativou, então encerra a sessão
            } else {
                return true  // O usuário cancelou, então mantém ele no menu!
            }
        }
        4 -> menuEventosUsuarioComum(contaLogada)
        0 -> {
            println("Saindo da conta...")
            return false // Encerra a sessão
        }
    }
    return true
}

fun menuOrganizador(contaLogada: Organizador): Boolean  {

    // 1. Cria a lista de opções
    val opcoes = listOf(
        "1) Meu Perfil",
        "2) Alterar dados do Perfil",
        "3) Inativar Minha Conta",
        "4) Gerenciar Eventos",
        "0) Sair (logout)"
    )

    // 2. Desenha o menu usando a tabela
    printTable("PAINEL DO ORGANIZADOR - Olá, ${contaLogada.nome}!", opcoes)

    val opcao = readInt("Opção: ", "Inválido", 0..4)
    when (opcao) {
        1 -> visualizarPerfil(contaLogada)
        2 -> alterarPerfil(contaLogada)
        3 -> {
            val tentandoInativarConta = inativarConta(contaLogada)

            if (tentandoInativarConta) {
                return false // Realmente inativou, então encerra a sessão
            } else {
                return true  // O usuário cancelou, então mantém ele no menu!
            }
        }
        4 -> menuGerenciamentoEventos(contaLogada)
        0 -> {
            println("Saindo da conta...")
            return false // Encerra a sessão
        }
    }
    return true
}

fun menuGerenciamentoEventos(contaLogada: Organizador) {
    var gerenciando = true

    while (gerenciando) {
        val opcoes = listOf(
            "1) Cadastrar Novo Evento",
            "2) Meus Eventos Cadastrados",
            "3) Alterar Dados de um Evento",
            "4) Ativar/Desativar Evento",
            "0) Voltar ao Painel Principal"
        )

        printTable("GERENCIAMENTO DE EVENTOS", opcoes)
        val opcao = readInt("Opção: ", COR.VERMELHO + "Opção inválida." + COR.RESET, 0..4)

        when (opcao) {
            1 -> cadastrarEvento(contaLogada)
            2 -> listagemDeEventos(contaLogada.email)
            3 -> alterarEvento(contaLogada.email)
            4 -> modificarStatusEvento(contaLogada.email)
            0 -> {
                println(COR.AMARELO + "Voltando ao Painel Principal..." + COR.RESET)
                gerenciando = false
            }
        }
    }
}

fun menuEventosUsuarioComum(contaLogada: UsuarioComum) {
    var navegando = true

    while (navegando) {
        val opcoes = listOf(
            "1) Ver Feed de Eventos Disponíveis",
            "2) Comprar Ingresso",
            "3) Minha Carteira de Ingressos",
            "0) Voltar ao Painel Principal"
        )

        printTable("ÁREA DE EVENTOS E INGRESSOS", opcoes)
        val opcao = readInt("Opção: ", COR.VERMELHO + "Opção inválida." + COR.RESET, 0..3)

        when (opcao) {
            1 -> exibirFeedEventos()
            2 -> comprarIngresso(contaLogada)
            3 -> exibirCarteiraIngressos(contaLogada)
            0 -> {
                println(COR.AMARELO + "Voltando ao Painel Principal..." + COR.RESET)
                navegando = false
            }
        }
    }
}


fun menuCadastro() {
    println("\n---- REGISTRO DE NOVO USUÁRIO ----"
            + "\nPara qual finalidade gostaria de Criar sua conta?"
            + "\n 1) Quero participar de Eventos (Usuário Comum)"
            + "\n 2) Quero Organizar Eventos (Conta de Organizador)"
            + "\n 0) Voltar")

    val opcaoRegistroConta =
        readInt("Opção: ", COR.VERMELHO + "ERRO: Opção Inválida." + COR.RESET, 0..2)

    when (opcaoRegistroConta) {
        0 -> println("Voltando..")
        1 -> cadastrarUsuarioComum()
        2 -> cadastrarOrganizador()
    }
}

// ---------------------------------------------------------
// MÓDULO DE USUÁRIOS (User Stories 01 a 06)
// ---------------------------------------------------------

fun cadastrarUsuarioComum() {
    println(COR.AMARELO + "--- CRIANDO PERFIL (USUÁRIO) ---" + COR.RESET)

    println(lineBar)
    println(COR.AMARELO + "--- CRIANDO PERFIL (USUÁRIO) ---" + COR.RESET)

    // 1. Coleta e valida todos os dados passo a passo usando suas funções
    val nome = validarNomeCadastro()
    val email = validarEmailCadastro()
    val senha = validarSenhaCadastro()
    val sexo = validarSexoCadastro()
    val dataNascimento = validarDataNascimentoCadastro(12) // Exige 12 anos

    // 2. Cria o objeto "UsuarioComum" com os dados limpos
    val novoUsuario = UsuarioComum(nome, dataNascimento, sexo, email, senha)

    // 3. Salva no Repositorio
    SalvarUsuario(novoUsuario)

    // 4. Finaliza com sucesso
    println(lineBar)
    println(COR.VERDE + "Usuário cadastrado com sucesso! Você já pode fazer o Login." + COR.RESET)
}

fun cadastrarOrganizador() {
    println(lineBar)
    println(COR.AMARELO + "--- CRIANDO PERFIL (ORGANIZADOR) ---" + COR.RESET)

    // 1. Coleta os dados básicos (reaproveitando as MESMAS funções)
    val nome = validarNomeCadastro()
    val email = validarEmailCadastro()
    val senha = validarSenhaCadastro()
    val sexo = validarSexoCadastro()
    val dataNascimento = validarDataNascimentoCadastro(18) // Exige 18 anos

    // 2. Coleta os dados da Empresa (Pode vir preenchido ou nulo)
    val dadosEmpresa = cadastroEmpresa()

    // Variáveis que vão para o banco (começam nulas, caso ele seja Pessoa Física)
    var cnpjFinal: String? = null
    var razaoFinal: String? = null
    var fantasiaFinal: String? = null

    // Se ele escolheu PJ e a função não devolveu nulo, desempacota o Triple
    if (dadosEmpresa != null) {
        cnpjFinal = dadosEmpresa.first
        razaoFinal = dadosEmpresa.second
        fantasiaFinal = dadosEmpresa.third
    }

    // 3. Cria o objeto "Organizador"
    val novoOrganizador = Organizador(
        nome, dataNascimento, sexo, email, senha,
        cnpjFinal, razaoFinal, fantasiaFinal
    )

    // 4. Salva no Repositorio
    SalvarOrganizador(novoOrganizador)

    // 5. Finaliza
    println(lineBar)
    println(COR.VERDE + "Organizador cadastrado com sucesso! Você já pode fazer o Login." + COR.RESET)
}

fun validarEmailCadastro(): String {
    while (true) {
        val inputEmail =
            readString(
                "\nDigite seu Email: ",
                COR.VERMELHO +
                        "ERRO: O e-mail precisa ter pelo menos 5 caracteres." +
                        COR.RESET,
                5
            )

        // 2. Agora valida o "@" e a duplicidade
        if (inputEmail.contains("@")) {
            val emailDuplicado = if (BuscarUsuario(inputEmail) != null || BuscarOrganizador(inputEmail) != null) {
                true
            } else {
                false
            }

            if (emailDuplicado) {
                println(
                    COR.VERMELHO +
                            "ERRO: " +
                            COR.AMARELO +
                            "E-mail já cadastrado, por favor efetue o login ou utilize um e-mail diferente." +
                            COR.RESET
                )
            } else {
                println(COR.VERDE + "E-mail válido e disponível. Prosseguindo..." + COR.RESET)
                return inputEmail
            }
        } else {
            println(
                COR.VERMELHO +
                        "ERRO: " +
                        COR.AMARELO +
                        "O e-mail precisa conter o caractere '@'." +
                        COR.RESET
            )
        }
    }
}
fun validarSenhaCadastro(): String {
    while (true) {
        println(lineBar)
        val inputSenha = readString("\nDigite sua senha: ", COR.VERMELHO +
                "ERRO: a senha precisa ter pelo menos 8 caracteres." +
                COR.RESET, 8)
        val inputSenhaConfirmacao = readString("\nDigite novamente sua senha: ", COR.VERMELHO +
                "ERRO: a senha precisa ter pelo menos 8 caracteres." +
                COR.RESET, 8)

        if (inputSenha != inputSenhaConfirmacao) {
            println(
                COR.VERMELHO +
                        "ERRO: " +
                        COR.AMARELO +
                        "As senhas não coincidem por favor digite a senha novamente" +
                        COR.RESET
            )
        } else {
            println(COR.VERDE + "Senha cadastrada com sucesso! Prosseguindo..." + COR.RESET)
            return inputSenha
        }
    }
}

fun validarNomeCadastro(): String {
    println(lineBar)
    val inputNome = readString("\nDigite seu Nome: ", COR.AMARELO +
            "\nVocê digitou um nome vazio ou muito curto, por favor digite um nome válido: " + COR.RESET, 2)
    println(COR.VERDE + "Nome cadastrado com sucesso! Prosseguindo..." + COR.RESET)
    return inputNome
}

fun validarSexoCadastro(): Sexo {
    println(lineBar)
    val inputSexoOpcao = readInt("\nQual gênero você se identifica: \n1) MASCULINO, \n2) FEMININO, \n3) OUTROS \nDigite o número da opção: ",
        COR.VERMELHO +
                "ERRO: " +
                COR.AMARELO +
                "Opção invalida. Digite Novamente" +
                COR.RESET, 1..3)

    val sexoSelecionado = when (inputSexoOpcao) {
        1 -> Sexo.MASCULINO
        2 -> Sexo.FEMININO
        else -> Sexo.OUTROS
    }
    println(COR.VERDE + "Gênero cadastrado com sucesso! Prosseguindo..." + COR.RESET)
    return sexoSelecionado
}

fun validarDataNascimentoCadastro(idadeMinima: Int): LocalDate {
    while (true) {
        println(lineBar)
        val inputDataNascimento = readString("\nQual sua data de nascimento? \nDigite nesse formato Dia/Mês/Ano, Ex.: 21/02/1992: ",
            COR.VERMELHO + "ERRO: Formato inválido! " + COR.RESET,
            10)

        // 1. Chama a "Tradutora" (Sua nova função separada)
        val dataConvertida = converterData(inputDataNascimento)

        if (dataConvertida != null) {

            val hoje = LocalDate.now()

            // 3. Aplica as regras de negócio (A Fiscalização)
            if (dataConvertida.isAfter(hoje)) {
                println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Você não pode ter nascido no futuro!" + COR.RESET)

            } else if (dataConvertida.isBefore(hoje.minusYears(120))) {
                println(COR.VERMELHO + "ERRO: Data inválida." + COR.RESET)

            } else if (dataConvertida.isAfter(hoje.minusYears(idadeMinima.toLong()))) {
                println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Você precisa ter pelo menos " + COR.NEGRITO + COR.VERDE + "$idadeMinima anos " + COR.VERMELHO + "para se cadastrar." + COR.RESET)

                // Mostra a idade calculada para o usuário
                val idadeCalculada = Period.between(dataConvertida, hoje).years
                println(COR.AMARELO + "Sua idade atual: $idadeCalculada anos." + COR.RESET)

            } else {
                // Se passou por todos os IFs de erro, a data é perfeita!
                println(COR.VERDE + "Data de nascimento válida! Idade Confirmada." + COR.RESET)
                return dataConvertida // Retorna a data e encerra o loop infinitamente
            }
        }
    }
}

fun formatarData(dataString: String): LocalDate? {
    return try{
        LocalDate.parse(dataString, formatterDate)
    } catch (e: Exception){
        null
    }
}

fun converterData(dataString: String): LocalDate? {
    val dataConvertida = formatarData(dataString)

    // 2. Se a tradutora devolveu nulo, o formato estava errado. Avisa e reinicia o loop.
    if (dataConvertida == null) {
        println(COR.VERMELHO + "ERRO: Formato inválido! " + COR.AMARELO + "Use o padrão dia/mês/ano (ex: 20/05/2000)." + COR.RESET)
        return null
    } else {
        return dataConvertida
    }
}

fun isEmpresa(): Int {
    val opcoes = listOf(
        "1) Sim (Sou Pessoa Jurídica)",
        "2) Não (Sou Pessoa Física)"
    )
    printTable("Você representa uma Empresa/Instituição?", opcoes)
    val isEmpresa = readInt("Opção: ",
        COR.VERMELHO + "ERRO: Opção inválida! Digite novamente" + COR.RESET, 1..2)
    return isEmpresa
}

// O "?", no final, indica que pode retornar os 3 dados OU nulo (se for Pessoa Física)
fun cadastroEmpresa(): Triple<String, String, String>? {
    val opcao = isEmpresa()

    if (opcao == 1) {
        // Se for PJ, chama as funções de validação
        println(lineBar)
        println(COR.AMARELO + "--- DADOS DA EMPRESA ---" + COR.RESET)

        val cnpj = validarCnpjCadastro()
        val razao = validarRazaoSocialCadastro()
        val fantasia = validarNomeFantasiaCadastro()

        println(COR.VERDE + "Dados empresariais validados com sucesso!" + COR.RESET)

        // Empacota os 3 dados e os devolve
        return Triple(cnpj, razao, fantasia)

    } else {
        // Se escolheu 2 (Pessoa Física), simplesmente devolve nulo
        return null
    }
}

fun validarCnpjCadastro(): String {
    while (true) {
        val inputCnpj = readString("\nDigite o CNPJ (14 números): ",
            COR.VERMELHO + "O CNPJ precisa ter exatamente 14 números." + COR.RESET,
            14)
        if (inputCnpj.length == 14) {
            return inputCnpj
        } else {
            println(COR.VERMELHO + "CNPJ inválido (deve conter 14 dígitos)." + COR.RESET)
        }
    }
}

fun validarRazaoSocialCadastro(): String {
    return readString("Digite a Razão Social: ",
        COR.VERMELHO + "Nome muito curto." + COR.RESET,
        2)
}

fun validarNomeFantasiaCadastro(): String {
    return readString("Digite o Nome Fantasia: ",
        COR.VERMELHO + "Nome muito curto." + COR.RESET,
        2)
}

fun visualizarPerfil(contaLogada: Any) {
    println(lineBar)

    if (contaLogada is Organizador) {
        // --- PERFIL DO ORGANIZADOR ---
        println(COR.AMARELO + "--- SEU PERFIL (ORGANIZADOR) ---" + COR.RESET + "\n")
        println("Nome: ${COR.VERDE}${contaLogada.nome}${COR.RESET}")
        println("Email: ${contaLogada.email}")
        println("Gênero: ${contaLogada.sexo}")
        println(lineBar)

        // Chama a sua função passando apenas a data!
        calcularEImprimirIdadeExata(contaLogada.dataNascimento)

        // Dados empresariais
        if (contaLogada.cnpj != null) {
            println(lineBar)
            println(COR.AMARELO + "--- DADOS DA EMPRESA ---" + COR.RESET)
            println("Razão Social: ${contaLogada.razaoSocial}")
            println("Nome Fantasia: ${contaLogada.nomeFantasia}")
            println("CNPJ: ${contaLogada.cnpj}")
        } else {
            println(lineBar)
            println("Perfil de Pessoa Física, sem dados empresariais cadastrados.")
        }

    } else if (contaLogada is UsuarioComum) {
        // --- PERFIL DO USUÁRIO COMUM ---
        println(COR.AMARELO + "--- SEU PERFIL (USUÁRIO) ---" + COR.RESET + "\n")
        println("Nome: ${COR.VERDE}${contaLogada.nome}${COR.RESET}")
        println("Email: ${contaLogada.email}")
        println("Gênero: ${contaLogada.sexo}")
        println(lineBar)

        // Chama a MESMA função passando a data!
        calcularEImprimirIdadeExata(contaLogada.dataNascimento)

    } else {
        // Segurança extra
        println(COR.VERMELHO + "Erro: Tipo de conta inválido." + COR.RESET)
        return
    }

    println(lineBar)
    println("Pressione ENTER para voltar...")
    readln()
}

fun calcularEImprimirIdadeExata(dataNascimento: LocalDate) {
    val hoje = LocalDate.now()
    val idadeExataCalculada = Period.between(dataNascimento, hoje)

    println("Data de Nascimento: ${dataNascimento.format(formatterDate)}")
    println("Idade: ${COR.VERDE}${idadeExataCalculada.years} Anos, ${idadeExataCalculada.months} Meses, ${idadeExataCalculada.days} Dias${COR.RESET}")
}

fun alterarPerfil(contaLogada: Any) {
    while (true) {
        // 1. Monta as opções dinamicamente
        val opcoesMenu = mutableListOf(
            "1) Nome",
            "2) Senha",
            "3) Sexo/Gênero"
        )

        // Só adiciona a opção 4 se for Organizador
        if (contaLogada is Organizador) {
            opcoesMenu.add("4) Dados Empresariais (Adicionar ou Editar)")
        }
        opcoesMenu.add("0) Cancelar")

        // 2. Desenha o Menu
        printTable("ALTERAR DADOS", opcoesMenu)

        // O limite da opção depende de quem está logado
        val limiteOpcao = if (contaLogada is Organizador) 4 else 3
        val opcaoAlterar = readInt("Opção: ", COR.VERMELHO + "Opção inválida." + COR.RESET, 0..limiteOpcao)

        // 3. Executa a Ação
        when (opcaoAlterar) {

            1 -> {
                val novoNome = validarNovoNome()
                if (contaLogada is Organizador) contaLogada.nome = novoNome
                else if (contaLogada is UsuarioComum) contaLogada.nome = novoNome
                println(COR.VERDE + "Nome atualizado!" + COR.RESET)
            }

            2 -> {
                // Descobre qual é a senha verdadeira atual
                val senhaReal = if (contaLogada is Organizador) contaLogada.senha else (contaLogada as UsuarioComum).senha

                if (validarSenhaAtual(senhaReal)) {
                    val novaSenha = validarNovaSenha()
                    if (contaLogada is Organizador) contaLogada.senha = novaSenha
                    else if (contaLogada is UsuarioComum) contaLogada.senha = novaSenha
                    println(COR.VERDE + "Nova senha cadastrada com sucesso!" + COR.RESET)
                } else {
                    println(COR.VERMELHO + "ERRO: Senha incorreta! Voltando para o Menu..." + COR.RESET)
                }
            }

            3 -> {
                val novoSexo = validarNovoSexo()
                if (contaLogada is Organizador) contaLogada.sexo = novoSexo
                else if (contaLogada is UsuarioComum) contaLogada.sexo = novoSexo
                println(COR.VERDE + "Gênero atualizado!" + COR.RESET)
            }

            4 -> {
                // A opção 4 só é acessível se for Organizador (já garantido pelo limiteOpcao)
                if (contaLogada is Organizador) {
                    println(lineBar)

                    if (contaLogada.cnpj == null) {
                        println(COR.AMARELO + "Atualmente você é Pessoa Física." + COR.RESET)
                        val opcaoTornarPJ = readInt("Deseja adicionar dados de Empresa?\n1) Sim \n2) Não\nOpção: ", "Inválido", 1..2)

                        if (opcaoTornarPJ == 1) {
                            contaLogada.cnpj = validarNovoCNPJ()
                            contaLogada.razaoSocial = validarNovaRazaoSocial()
                            contaLogada.nomeFantasia = validarNovoNomeFantasia()
                            println(COR.VERDE + "Sucesso! Agora você é um Organizador PJ." + COR.RESET)
                        }
                    } else {
                        // Se já tem CNPJ, permite editar
                        val opcoesEmpresa = listOf(
                            "CNPJ Atual: ${contaLogada.cnpj}",
                            "1) Editar Nome Fantasia/Razão Social",
                            "2) Corrigir CNPJ",
                            "0) Voltar"
                        )
                        printTable("EDITAR DADOS DA EMPRESA", opcoesEmpresa)

                        val opcaoEmpresa = readInt("Opção: ", "Inválido", 0..2)

                        if (opcaoEmpresa == 1) {
                            contaLogada.razaoSocial = validarNovaRazaoSocial()
                            contaLogada.nomeFantasia = validarNovoNomeFantasia()
                            println(COR.VERDE + "Dados empresariais atualizados!" + COR.RESET)
                        } else if (opcaoEmpresa == 2) {
                            contaLogada.cnpj = validarNovoCNPJ()
                            println(COR.VERDE + "CNPJ atualizado!" + COR.RESET)
                        }
                    }
                }
            }

            0 -> {
                println(COR.AMARELO + "Voltando..." + COR.RESET)
                break // Sai do loop
            }
        }
    }
}

// --- FUNÇÕES DE VALIDAÇÃO (Específicas para Alteração) ---

fun validarNovoNome(): String {
    return readString(
        "Novo Nome: ",
        COR.VERMELHO + "Nome inválido. Mínimo 2 caracteres." + COR.RESET,
        2
    )
}

fun validarSenhaAtual(senhaVerdadeira: String): Boolean {
    val senhaDigitada = readString(
        "Digite sua senha atual: ",
        COR.VERMELHO + "Senha inválida." + COR.RESET,
        1
    )
    return senhaDigitada == senhaVerdadeira
}

fun validarNovaSenha(): String {
    while (true) {
        val novaSenha = readString(
            "Nova Senha: ",
            COR.VERMELHO + "A nova senha precisa ter 8 ou mais caracteres." + COR.RESET,
            8
        )
        val confirmacao = readString(
            "Confirme a Nova Senha: ",
            COR.VERMELHO + "A nova senha precisa ter 8 ou mais caracteres." + COR.RESET,
            8
        )

        if (novaSenha == confirmacao) {
            return novaSenha
        } else {
            println(COR.VERMELHO + "ERRO: As senhas não coincidem. Digite novamente." + COR.RESET)
        }
    }
}

fun validarNovoSexo(): Sexo {
    val opcao = readInt(
        "Novo Gênero \n1) Masculino \n2) Feminino \n3) Outros \nOpção: ",
        COR.VERMELHO + "Opção inválida." + COR.RESET,
        1..3
    )
    return when (opcao) {
        1 -> Sexo.MASCULINO
        2 -> Sexo.FEMININO
        else -> Sexo.OUTROS
    }
}

fun validarNovoCNPJ(): String {
    while (true) {
        val cnpj = readString(
            "Novo CNPJ (14 números): ",
            COR.VERMELHO + "CNPJ inválido (deve conter 14 dígitos)." + COR.RESET,
            14
        )
        if (cnpj.length == 14) return cnpj
        println(COR.VERMELHO + "CNPJ inválido." + COR.RESET)
    }
}

fun validarNovaRazaoSocial(): String {
    return readString(
        "Nova Razão Social: ",
        COR.VERMELHO + "Nome inválido." + COR.RESET,
        2
    )
}

fun validarNovoNomeFantasia(): String {
    return readString(
        "Novo Nome Fantasia: ",
        COR.VERMELHO + "Nome inválido." + COR.RESET,
        2
    )
}

fun inativarConta(contaLogada: Any): Boolean {
    // 1. Pergunta se o usuário tem certeza
    val confirmou = confirmarInativacao()

    // Se ele escolheu 2 (Não), cancelamos a operação e mantemos a sessão (return false)
    if (!confirmou) {
        println(COR.VERDE + "Operação cancelada. Sua conta continua ativa! Ufa!" + COR.RESET)
        return false
    }

    // 2. Se ele confirmou (1), vamos aplicar as regras dependendo do tipo de conta
    when (contaLogada) {

        is Organizador -> {
            val possuiEventosAtivos = verificarEventosAtivosOrganizador(contaLogada.email)

            if (possuiEventosAtivos) {
                // Barra a inativação
                println(COR.VERMELHO + "ERRO: Não é possível desativar a conta." + COR.RESET)
                println("Você possui eventos ativos ou em andamento. Cancele-os primeiro.")
                return false // Retorna false para o menu não deslogar a pessoa

            } else {
                // Inativa com sucesso
                contaLogada.ativo = false
                println(COR.VERMELHO + "Conta de Organizador inativada com sucesso." + COR.RESET)
                return true // Retorna true para o Menu encerrar o loop da sessão!
            }
        }

        is UsuarioComum -> {
            // Usuário comum não tem eventos, então inativa direto
            contaLogada.ativo = false
            println(COR.VERMELHO + "Conta de Usuário inativada com sucesso." + COR.RESET)
            return true // Retorna true para o Menu encerrar a sessão!
        }

        else -> {
            println(COR.VERMELHO + "Erro crítico ao identificar a conta." + COR.RESET)
            return false
        }
    }
}

fun confirmarInativacao(): Boolean {
    println(lineBar)
    println(COR.VERMELHO + "ATENÇÃO: Você está prestes a desativar sua conta." + COR.RESET)
    println("Para entrar novamente, você precisará usar a opção 'Reativar Conta' no menu principal.")

    val confirmacao = readInt("Tem certeza? (1) SIM, (2) NÃO\nOpção: ", "Opção inválida", 1..2)

    return confirmacao == 1
}

fun verificarEventosAtivosOrganizador(emailOrganizador: String): Boolean {
    val agora = LocalDateTime.now()

    // O '.any' já devolve um Boolean (true se achar algo, false se não achar)
    return listaEventos.any { evento ->
        evento.idOrganizador == emailOrganizador &&
                evento.ativo &&
                agora.isBefore(evento.dataFim) // cobre eventos futuros e em andamento
    }
}


fun buscarContaInativa(emailBusca: String, senhaBusca: String): Any? {
    // 1. Tenta achar na lista de Usuários Comuns
    val contaEncontrada = BuscarUsuario(emailBusca) ?: BuscarOrganizador(emailBusca)
        return when (contaEncontrada) {
            is UsuarioComum -> if (contaEncontrada.senha == senhaBusca) contaEncontrada else null
            is Organizador -> if (contaEncontrada.senha == senhaBusca) contaEncontrada else null
            else -> null
        }
}


fun reativarConta() {
    println(lineBar)
    println(COR.AMARELO + "--- REATIVAR CONTA ---" + COR.RESET)
    println("Informe suas credenciais para reativar seu acesso.")

    // Usa os componentes para ler os dados sem quebrar o sistema
    val emailDigitado = readString("\nDigite seu E-mail cadastrado: ", COR.VERMELHO + "E-mail inválido." + COR.RESET, 5)
    val senhaDigitada = readString("Digite sua Senha: ", COR.VERMELHO + "Senha inválida." + COR.RESET, 1)

    // Chama o seu novo componente de busca
    val contaEncontrada = buscarContaInativa(emailDigitado, senhaDigitada)

    if (contaEncontrada != null) {
        // Usa o Smart Cast para acessar o atributo 'ativo' dependendo de quem logou
        when (contaEncontrada) {
            is UsuarioComum -> {
                if (!contaEncontrada.ativo) {
                    contaEncontrada.ativo = true
                    println(COR.VERDE + "SUCESSO: Conta de Usuário Comum reativada!" + COR.RESET)
                    println("Você já pode fazer login no menu principal.")
                } else {
                    println(COR.AMARELO + "Atenção: Sua conta já está ativa. Basta fazer login." + COR.RESET)
                }
            }

            is Organizador -> {
                if (!contaEncontrada.ativo) {
                    contaEncontrada.ativo = true
                    println(COR.VERDE + "SUCESSO: Conta de Organizador reativada!" + COR.RESET)
                    println("Você já pode fazer login no menu principal.")
                } else {
                    println(COR.AMARELO + "Atenção: Sua conta já está ativa. Basta fazer login." + COR.RESET)
                }
            }
        }
    } else {
        println(lineBar)
        println(COR.VERMELHO + "ERRO: Conta não encontrada ou credenciais inválidas." + COR.RESET)
    }

    println(lineBar)
    println("Pressione ENTER para voltar ao menu principal...")
    readln()
}
