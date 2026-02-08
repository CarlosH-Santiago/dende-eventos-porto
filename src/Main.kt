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

                // varre a lista para encontrar o usario
                val usuarioLogado = listaUsuarios.find { it.email == emailLogin && it.senha == senhaLogin}
                val organizadorLogado = listaOrganizadores.find { it.email == emailLogin && it.senha == senhaLogin}

                if (usuarioLogado != null || organizadorLogado != null) {
                    if (usuarioLogado?.ativo == true || organizadorLogado?.ativo == true) {
                        println(lineBar)
                        println(COR.VERDE + "Login realizado com sucesso!" + COR.RESET)
                        println(lineBar)
                    }


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
                        }
                        println("0) Sair (logout)")
                        println(lineBar)

                        println("Escolha: ")
                        val opcaoMenuLogado = readln().toIntOrNull() ?: 0
                        println(lineBar)

                        when(opcaoMenuLogado) {
                            0 -> {
                                println(COR.AMARELO + "Realizando logout..." + COR.RESET)
                                sessaoAtiva = false
                            }
                            1 -> {
                                // dados do usuario organizador
                                println(lineBar)
                                println(COR.AMARELO + "--- SEU PERFIL (ORGANIZADOR) ---" + COR.RESET + "\n")
                                println("Nome: ${COR.VERDE}${organizadorLogado?.nome}${COR.RESET}")
                                println("Email: ${organizadorLogado?.email}")
                                println("Gênero: ${organizadorLogado?.sexo}")
                                println(lineBar)
                                // dados relacionados a idade e nascimento
                                val hoje = LocalDate.now()
                                val idadeExataCalculada = Period.between(organizadorLogado?.dataNacimento, hoje)
                                println("Data de Nascimento: ${organizadorLogado?.dataNacimento?.format(formatterDate)}")
                                println("Idade: ${COR.VERDE}${idadeExataCalculada.years} Anos, ${idadeExataCalculada.months} Meses, ${idadeExataCalculada.days} Dias")

                                // dados empresariais
                                if(organizadorLogado?.cnpj != null) {
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
                                println("Pressione enter para voltar")
                                readln()
                            }
                            2 -> {
                                println(lineBar)
                                println(COR.AMARELO + "--- SEU PERFIL (USUARIO) ---" + COR.RESET)

                                // dados do usuario comum
                                println("Nome: ${COR.VERDE}${usuarioLogado?.nome}${COR.RESET}")
                                println("Email: ${usuarioLogado?.email}")
                                println("Gênero: ${usuarioLogado?.sexo}")
                                println(lineBar)

                                // dados relacionados a idade e nascimento
                                val hoje = LocalDate.now()
                                val idadeExataCalculada = Period.between(usuarioLogado?.dataNacimento, hoje)
                                println("Data de Nascimento: ${usuarioLogado?.dataNacimento?.format(formatterDate)}")
                                println("Idade: ${COR.VERDE}${idadeExataCalculada.years} Anos, ${idadeExataCalculada.months} Meses, ${idadeExataCalculada.days} Dias")

                                println(lineBar)
                                println("Pressione enter para voltar")
                                readln()
                            }
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

                val opcaoResgistroConta = readln().toIntOrNull() ?: 0
                println(lineBar)
                when(opcaoResgistroConta) {
                    0 -> {
                        println("Voltando..")
                    }
                    1 -> {
                        println(COR.AMARELO + "--- CRIANDO PERFIL (USUARIO) ---" + COR.RESET)
                        // Variaveis para o ciclo de vida da criação do usuario e contramedidas contra erros do usuario possibilitando repetição
                        var cicloCriarUsuarioComum = true
                        var cicloEmail = true
                        var cicloSenha = true
                        var cicloNome = true
                        var cicloDataNacimento = true

                        // variaveis para armazenar os dados do usuario de forma segura
                        var nome = ""
                        var email = ""
                        var senha = ""
                        var dataNascimento : LocalDate = LocalDate.now()
                        var sexo : Sexo = Sexo.OUTROS
                        val ativo: Boolean = true

                        // Para a verificação de idade na data de nascimento
                        val idadeMinima = 12



                        while (cicloCriarUsuarioComum) {
                            while (cicloEmail) {
                                println("Vamos criar um usuario comum então")
                                println(lineBar)
                                print("\nDigite seu email: ")
                                val inputEmail = readln().trim()
                                // Verificação de formato correto do email, criterio: Conter o @ e 5 ou mais caracteres
                                if (inputEmail.contains("@") && inputEmail.length >= 5) {
                                    // Variaveis para verificação de duplicidade de email nos usuarios comuns e organizadores
                                    val verificarDuplicidadeEmailUsuarioComum =
                                        listaUsuarios.any { it.email == inputEmail }
                                    val verificarDuplicidadeEmailOrganizador =
                                        listaOrganizadores.any { it.email == inputEmail }
                                    // Condicional Verifdicando emails duplicados
                                    if (verificarDuplicidadeEmailUsuarioComum || verificarDuplicidadeEmailOrganizador) {
                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + " Email informado ja cadastrado, por favor efetui o login ou utilize um email diferente" + COR.RESET)
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

                                if (inputSenha != inputSenhaConfirmacao) {
                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + " As senhas não coincidem por favor digite a senha novamente" + COR.RESET)
                                } else {
                                    println(COR.VERDE + "Senha cadastrada com sucesso! Prosseguindo..." + COR.RESET)
                                    senha = inputSenha
                                    cicloSenha = false
                                }
                            }

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
                            while (cicloDataNacimento) {
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
                                        // Se for bem sucessido
                                        dataNascimento = dataConvertida
                                        println(COR.VERDE + "Data de nascimento valida! Idade Confirmada" + COR.RESET)
                                        cicloDataNacimento = false
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
                                cicloDataNacimento = true
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
                        // Ciclo de criação de usuario organizador
                        println(lineBar)
                        println(COR.AMARELO + "--- CRIANDO PERFIL (ORGANIZADOR) ---" + COR.RESET)

                        var cicloCriarOrganizador = true
                        var cicloEmail = true
                        var cicloSenha = true
                        var cicloNome = true
                        var cicloDataNacimento = true

                        // variaveis para armazenar os dados do organizador de forma segura
                        var nome = ""
                        var email = ""
                        var senha = ""
                        var dataNascimento : LocalDate = LocalDate.now()
                        var sexo : Sexo = Sexo.OUTROS
                        val ativo: Boolean = true

                        // Dados opicionais da empresa
                        var cnpj: String? = null
                        var razaoSocial: String? = null
                        var nomeFantasia: String? = null

                        // Para a verificação de idade na data de nascimento
                        val idadeMinima = 12



                        while (cicloCriarOrganizador) {
                            while (cicloEmail) {
                                println("Vamos criar um usuario organizador então")
                                println(lineBar)
                                print("\nDigite seu email: ")
                                val inputEmail = readln().trim()
                                // Verificação de formato correto do email, criterio: Conter o @ e 5 ou mais caracteres
                                if (inputEmail.contains("@") && inputEmail.length >= 5) {
                                    // Variaveis para verificação de duplicidade de email nos usuarios comuns e organizadores
                                    val verificarDuplicidadeEmailUsuarioComum =
                                        listaUsuarios.any { it.email == inputEmail }
                                    val verificarDuplicidadeEmailOrganizador =
                                        listaOrganizadores.any { it.email == inputEmail }
                                    // Condicional Verifdicando emails duplicados
                                    if (verificarDuplicidadeEmailUsuarioComum || verificarDuplicidadeEmailOrganizador) {
                                        println(COR.VERMELHO + "ERRO: " + COR.AMARELO + " Email informado ja cadastrado, por favor efetui o login ou utilize um email diferente" + COR.RESET)
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

                                if (inputSenha != inputSenhaConfirmacao) {
                                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + " As senhas não coincidem por favor digite a senha novamente" + COR.RESET)
                                } else {
                                    println(COR.VERDE + "Senha cadastrada com sucesso! Prosseguindo..." + COR.RESET)
                                    senha = inputSenha
                                    cicloSenha = false
                                }
                            }

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
                            while (cicloDataNacimento) {
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
                                        // Se for bem sucessido
                                        dataNascimento = dataConvertida
                                        println(COR.VERDE + "Data de nascimento valida! Idade Confirmada" + COR.RESET)
                                        cicloDataNacimento = false
                                    }

                                }catch (e: Exception) {
                                    // O usuário digitou formato errado ou dia inexistente
                                    println(COR.VERMELHO + "ERRO: Formato inválido!" + COR.AMARELO + " Use o padrão dia/mês/ano (ex: 20/05/2000)." + COR.RESET)
                                }
                            }

                            // cadastro de dados da empresa
                            var cadastrarEmpresa = true
                            while (cadastrarEmpresa) {
                                println(lineBar)
                                println("Você representa uma Empresa/Intituição?")
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
                                cicloDataNacimento = true
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

            }
        }
    }
}