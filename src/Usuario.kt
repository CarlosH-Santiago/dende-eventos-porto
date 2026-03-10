import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Period
import util.ConsoleTextColor as COR

val lineBar = "-".repeat(40)
val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")

// ---------------------------------------------------------
// MENUS DE ROTEAMENTO (Auxiliares)
// ---------------------------------------------------------

fun realizarLogin() {
    println("\n---- LOGIN ----")
    println("Digite seu Email: ")
    val emailLogin = readln().trim()
    println("Digite sua Senha: ")
    val senhaLogin = readln().trim()
    
    // NOTA: Estas variáveis darão erro até o Repositorio.kt estar pronto.
    val usuario = listaUsuarios.find { it.email == emailLogin && it.senha == senhaLogin} ?: 
    listaOrganizadores.find { it.email == emailLogin && it.senha == senhaLogin}
}

fun menuCadastro() {
    println("\n---- REGISTRO DE NOVO USUÁRIO ----")
    println("Para qual finalidade gostaria de Criar sua conta?")
    println("1) Quero participar de Eventos (Usuário Comum)")
    println("2) Quero Organizar Eventos (Conta de Organizador)")
    println("0) Voltar")
    
    // Substituir por readInt futuramente
    print("Opção: ")
    val opcaoRegistroConta = readln().toIntOrNull() ?: 0
    
    when(opcaoRegistroConta) {
        0 -> println("Voltando..")
        1 -> cadastrarUsuarioComum()
        2 -> cadastrarOrganizador()
        else -> println(COR.VERMELHO + "ERRO: Opção Inválida." + COR.RESET)
    }
}

// ---------------------------------------------------------
// MÓDULO DE USUÁRIOS (User Stories 01 a 06)
// ---------------------------------------------------------

fun cadastrarUsuarioComum() {
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
    val ativo: Boolean = true
    // Para a verificação de idade na data de nascimento
    val idadeMinima = 12

    while (cicloCriarUsuarioComum) {
        while (cicloEmail) {
            println("Vamos criar um usuário comum então")
            println(lineBar)
            print("\nDigite seu email: ")
            val inputEmail = readln().trim()
            // Verificação de formato correto do email, critério: Conter o @ e 5 ou mais caracteres
            if (inputEmail.contains("@") && inputEmail.length >= 5) {
                // Variáveis para verificação de duplicidade de email nos usuários comuns e organizadores
                val verificarDuplicidadeEmailUsuarioComum = listaUsuarios.any { it.email == inputEmail }
                val verificarDuplicidadeEmailOrganizador = listaOrganizadores.any { it.email == inputEmail }
                // Condicional Verificando emails duplicados
                if (verificarDuplicidadeEmailUsuarioComum || verificarDuplicidadeEmailOrganizador) {
                    println(COR.VERMELHO + "ERRO: " + COR.AMARELO + " Email informado ja cadastrado, por favor efetue o login ou utilize um email diferente" + COR.RESET)
                } else {
                    println(COR.VERDE + "E-mail válido e disponível. Prosseguindo..." + COR.RESET)
                    email = inputEmail
                    cicloEmail = false
                }
            } else {
                println(COR.VERMELHO + "ERRO: " + COR.AMARELO + "Email no formato incorreto. O e-mail precisa ter '@' e possuir mais de 4 caracteres\n" +
                    " Por favor digite novamente" + COR.RESET)
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

fun cadastrarOrganizador() {
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
    val ativo: Boolean = true

    // Dados opcionais da empresa
    var cnpj: String? = null
    var razaoSocial: String? = null
    var nomeFantasia: String? = null

    // Para a verificação de idade na data de nascimento
    val idadeMinima = 18

    while (cicloCriarOrganizador) {
        while (cicloEmail) {
            println("Vamos criar um usuário organizador então")
            println(lineBar)
            print("\nDigite seu email: ")
            val inputEmail = readln().trim()
            // Verificação de formato correto do email, critério: Conter o @ e 5 ou mais caracteres
            if (inputEmail.contains("@") && inputEmail.length >= 5) {
                // Variáveis para verificação de duplicidade de email nos usuários comuns e organizadores
                val verificarDuplicidadeEmailUsuarioComum = listaUsuarios.any { it.email == inputEmail }
                val verificarDuplicidadeEmailOrganizador = listaOrganizadores.any { it.email == inputEmail }
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

fun visualizarPerfil() {
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
        println("Pressione enter para voltar")
        readln()
    } else if (usuarioLogado != null) {
        // Perfil do usuário comum
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
                                    
fun alterarPerfil() {
    var alterandoPerfil = true
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
                    } else println(COR.VERMELHO + "Nome inválido." + COR.RESET)
                }

                2 -> {
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
                            print("Digite o CNPJ (14 números): ")
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
}

fun inativarConta() {
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
                // Nota: sessaoAtiva = false deverá ser ajustado no novo menu
            }

        } else if (usuarioLogado != null) {
            usuarioLogado.ativo = false
            println(COR.VERMELHO + "Conta de Usuário inativada." + COR.RESET)
            // Nota: sessaoAtiva = false deverá ser ajustado no novo menu
        }

    } else {
        println(COR.VERDE + "Operação cancelada." + COR.RESET)
        println("Pressione ENTER para voltar...")
        readln()
    }
}

fun reativarConta() {
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
