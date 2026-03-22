import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.Duration
import util.ConsoleTextColor as COR

// Repositórios de dados na memória
val listaEventos = mutableListOf<Evento>()
val listaIngressos = mutableListOf<Ingresso>()

// === US 6: CADASTRAR EVENTO ===
fun cadastrarEvento(organizadorLogado: Organizador) {
    println("\n--- NOVO EVENTO ---")

    val nomeEv = readString("Nome do Evento: ", "Nome inválido!", minLength = 3)
    val descEv = readString("Descrição: ", "Descrição muito curta!", minLength = 5)

    // Tratamento de datas e validação de duração mínima (30min)
    val (dataIniEv, dataFimEv) = lerDatasEvento()

    // Seleção de Tipo
    val tipoEv = selecionarCategoria()

    // Seleção de Modalidade
    val modEv = selecionarModalidade()

    val localEv = readString("Local (Endereço ou Link): ", "Local não pode ser vazio!")
    val capEv = readInt("Capacidade Máxima: ", "Digite um número válido!", 1..1000000)
    val precoEv = readDouble("Preço do Ingresso (0 para gratuito): ", "Preço inválido!")

    // Política de Estorno
    val permiteEstorno = readInt("Permite estorno? (1-Sim, 2-Não): ", "Opção inválida!", 1..2) == 1
    val taxaEstorno =
        if (permiteEstorno) {
            readDouble("Qual a taxa de estorno (ex: 10.0 para 10%): ", "Valor inválido!") / 100.0
        } else 0.0

    // Evento Vinculado
    val idVinc = readInt("ID do evento principal (0 para nenhum): ", "ID inválido!")
    val idVinculadoFinal = if (idVinc > 0 && listaEventos.any { it.id == idVinc }) idVinc else null

    // Status Inicial
    val statusEv = readInt("Status (1-Ativo, 2-Inativo): ", "Opção inválida!", 1..2) == 1

    // Criação do Objeto Evento
    val novoId = (listaEventos.maxOfOrNull { it.id } ?: 0) + 1
    val novoEvento =
        Evento(
            id = novoId,
            nome = nomeEv,
            descricao = descEv,
            dataInicio = dataIniEv,
            dataFim = dataFimEv,
            tipo = tipoEv,
            modalidade = modEv,
            capacidadeTotal = capEv,
            local = localEv,
            preco = precoEv,
            ativo = statusEv,
            idOrganizador = organizadorLogado.email,
            estornaDinheiro = permiteEstorno,
            taxaEstorno = taxaEstorno,
            idEventoVinculado = idVinculadoFinal
        )

    listaEventos.add(novoEvento)
    println("\nEvento '${novoEvento.nome}' cadastrado com sucesso! ID: $novoId")
}

fun lerDatasEvento(): Pair<LocalDateTime, LocalDateTime> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    var dataInicio: LocalDateTime = LocalDateTime.now()
    var dataFim: LocalDateTime = LocalDateTime.now()

    // Validação da Data de Início
    while (true) {
        print("\nData e Hora de INÍCIO (ex: 25/12/2026 14:30): ")
        val inputInicio = readln().trim()
        try {
            val dataTentativa = LocalDateTime.parse(inputInicio, formatter)
            if (dataTentativa.isBefore(LocalDateTime.now())) {
                println("ERRO: O evento não pode começar no passado.")
                continue
            }
            dataInicio = dataTentativa
            break
        } catch (e: DateTimeParseException) {
            println("ERRO: Formato inválido. Use dd/MM/yyyy HH:mm")
        }
    }

    // Validação da Data de Fim e regra de negócio de 30 minutos mínimos
    while (true) {
        print("Data e Hora de TÉRMINO (ex: 25/12/2026 18:00): ")
        val inputFim = readln().trim()
        try {
            val dataTentativa = LocalDateTime.parse(inputFim, formatter)
            val duracao = Duration.between(dataInicio, dataTentativa).toMinutes()

            if (dataTentativa.isBefore(dataInicio)) {
                println("ERRO: O término não pode ser antes do início.")
            } else if (duracao < 30) {
                println("ERRO: O evento precisa durar pelo menos 30 minutos.")
            } else {
                dataFim = dataTentativa
                break
            }
        } catch (e: DateTimeParseException) {
            println("ERRO: Formato inválido. Use dd/MM/yyyy HH:mm")
        }
    }

    return Pair(dataInicio, dataFim)
}

// === US 10: LISTAGEM DE EVENTOS ===
fun listagemDeEventos(emailOrganizador: String) {
    val meusEventos = listaEventos.filter { it.idOrganizador == emailOrganizador }

    if (meusEventos.isEmpty()) {
        println(COR.AMARELO + "Você não possui eventos cadastrados." + COR.RESET)
        return
    }

    // 1. O Cabeçalho (O molde)
    val header = String.format("%-5s %-20s %-15s %-10s %-10s", "ID", "NOME", "DATA", "PREÇO", "STATUS")

    // Formatador para a data ficar bonita (ex: 08/09/2026)
    val formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // 2. Transforma cada Evento em uma String perfeitamente alinhada com o molde
    val linhasFormatadas = meusEventos.map { evento ->

        // Se o nome for muito grande, corta e põe "..." para não quebrar a tabela
        val nomeCurto = if (evento.nome.length > 18) evento.nome.take(15) + "..." else evento.nome

        val dataStr = evento.dataInicio.format(formatadorData)
        val precoStr = "R$ %.2f".format(evento.preco)
        val statusStr = if (evento.ativo) COR.VERDE + "Ativo" + COR.RESET else COR.VERMELHO + "Inativo" + COR.RESET


        String.format("%-5d %-20s %-15s %-10s %-10s", evento.id, nomeCurto, dataStr, precoStr, statusStr)
    }

    // 3. Imprime a tabela passando a lista de Strings já formatadas!
    printTable(header, linhasFormatadas)
}

// === US 7: ALTERAR EVENTO ===
fun alterarEvento(emailOrganizador: String) {
    val meusEventosAtivos = listaEventos.filter { it.idOrganizador == emailOrganizador && it.ativo }

    if (meusEventosAtivos.isEmpty()) {
        println(COR.AMARELO + "Não há eventos ativos para alteração." + COR.RESET)
        return
    }

    val idBusca = readInt("\nDigite o ID do evento para alterar (0 para cancelar): ", COR.VERMELHO + "ID inválido." + COR.RESET, 0..99999)
    if (idBusca == 0) return

    val evento = meusEventosAtivos.find { it.id == idBusca }

    if (evento == null) {
        println(COR.VERMELHO + "Evento não encontrado ou indisponível." + COR.RESET)
        return
    }

    var alterando = true
    while (alterando) {
        println(lineBar)
        val opcoesAlteracao = listOf(
            "1) Nome (Atual: ${evento.nome})",
            "2) Descrição (Atual: ${evento.descricao})",
            "3) Local (Atual: ${evento.local})",
            "4) Capacidade Máxima (Atual: ${evento.capacidadeTotal})",
            "5) Preço do Ingresso (Atual: R$ ${evento.preco})",
            "6) Alterar Datas, Categoria e Modalidade",
            "0) Concluir e Salvar"
        )

        printTable("ALTERANDO EVENTO: ${evento.nome}", opcoesAlteracao)

        val opcaoOp = readInt("O que deseja alterar? (Opção): ", COR.VERMELHO + "Opção inválida." + COR.RESET, 0..6)

        when (opcaoOp) {
            1 -> alterarNomeEvento(evento)
            2 -> alterarDescricaoEvento(evento)
            3 -> alterarLocalEvento(evento)
            4 -> alterarCapacidadeEvento(evento)
            5 -> alterarPrecoEvento(evento)
            6 -> alterarEstruturaEvento(evento)
            0 -> {
                println(COR.VERDE + "Alterações do evento '${evento.nome}' salvas com sucesso!" + COR.RESET)
                alterando = false
            }
        }
    }
}

// === FUNÇÕES DE ALTERAÇÃO ===

private fun alterarNomeEvento(evento: Evento) {
    val novoNome = readString("Novo Nome: ", COR.VERMELHO + "Nome inválido." + COR.RESET, 3)
    evento.nome = novoNome
    println(COR.VERDE + "Nome atualizado!" + COR.RESET)
}

private fun alterarDescricaoEvento(evento: Evento) {
    val novaDescricao = readString("Nova Descrição: ", COR.VERMELHO + "Descrição muito curta." + COR.RESET, 5)
    evento.descricao = novaDescricao
    println(COR.VERDE + "Descrição atualizada!" + COR.RESET)
}

private fun alterarLocalEvento(evento: Evento) {
    val novoLocal = readString("Novo Local: ", COR.VERMELHO + "Local inválido." + COR.RESET, 2)
    evento.local = novoLocal
    println(COR.VERDE + "Local atualizado!" + COR.RESET)
}

private fun alterarCapacidadeEvento(evento: Evento) {
    val novaCap = readInt("Nova Capacidade: ", COR.VERMELHO + "Número inválido." + COR.RESET, 1..1000000)

    // Mantemos a regra de negócio intocável aqui dentro!
    if (novaCap >= evento.ingressosVendidos) {
        evento.capacidadeTotal = novaCap
        println(COR.VERDE + "Capacidade atualizada!" + COR.RESET)
    } else {
        println(COR.VERMELHO + "ERRO: A capacidade não pode ser menor que os ingressos já vendidos (${evento.ingressosVendidos})." + COR.RESET)
    }
}

private fun alterarPrecoEvento(evento: Evento) {
    val novoPreco = readDouble("Novo Preço do Ingresso: ", COR.VERMELHO + "Preço inválido." + COR.RESET)
    evento.preco = novoPreco
    println(COR.VERDE + "Preço atualizado!" + COR.RESET)
}

private fun alterarEstruturaEvento(evento: Evento) {
    println(COR.AMARELO + "--- ALTERANDO ESTRUTURA DO EVENTO ---" + COR.RESET)
    val (novaDataIni, novaDataFim) = lerDatasEvento()

    evento.dataInicio = novaDataIni
    evento.dataFim = novaDataFim
    evento.tipo = selecionarCategoria()
    evento.modalidade = selecionarModalidade()

    println(COR.VERDE + "Datas e categoria atualizadas!" + COR.RESET)
}

// === US 8 e 9: MODIFICAR STATUS (Ativar/Desativar) ===
fun modificarStatusEvento(emailOrganizador: String) {
    print("Digite o ID do evento: ")
    val idStatus = readln().toIntOrNull() ?: -1

    val evento = listaEventos.find { it.id == idStatus && it.idOrganizador == emailOrganizador }

    if (evento == null) {
        println("Evento não encontrado.")
        return
    }

    if (!evento.ativo) {
        ativarEvento(evento)
    } else {
        desativarEvento(evento)
    }
}

// === FUNÇÕES AUXILIARES ===

private fun selecionarCategoria(): CategoriaEvento {
    println("Categorias:")
    CategoriaEvento.values().forEachIndexed { i, t -> println("$i - $t") }
    val index = readInt("Escolha o número: ", "Opção inválida", 0..CategoriaEvento.values().size - 1)
    return CategoriaEvento.values()[index]
}

private fun selecionarModalidade(): Modalidade {
    println("Modalidade: 1-Presencial, 2-Remoto, 3-Híbrido")
    return when (readInt("Escolha: ", "Inválido", 1..3)) {
        2 -> Modalidade.REMOTO
        3 -> Modalidade.HIBRIDO
        else -> Modalidade.PRESENCIAL
    }
}

private fun ativarEvento(evento: Evento) {
    if (evento.dataInicio.isBefore(LocalDateTime.now())) {
        println("Erro: Não é possível ativar evento no passado.")
    } else {
        evento.ativo = true
        println("Evento Ativado!")
    }
}

private fun desativarEvento(evento: Evento) {
    if (evento.ingressosVendidos > 0) {
        println("Existem ingressos vendidos! Digite 'CONFIRMAR' para cancelar tudo:")
        if (readln() == "CONFIRMAR") {
            evento.ativo = false
            listaIngressos
                .filter { it.idEvento == evento.id }
                .forEach { it.status = StatusIngresso.CANCELADO }
            println("Evento e ingressos cancelados.")
        }
    } else {
        evento.ativo = false
        println("Evento Desativado.")
    }
}

// === INTEGRAÇÃO COM MÓDULO DE USUÁRIO ===
fun organizadorPossuiEventosAtivos(emailOrganizador: String): Boolean {
    val agora = LocalDateTime.now()
    return listaEventos.any { evento ->
        evento.idOrganizador == emailOrganizador && evento.ativo && agora.isBefore(evento.dataFim)
    }
}

// === US: FEED PÚBLICO DE EVENTOS ===
fun exibirFeedEventos() {
    val agora = LocalDateTime.now()
    // Filtra apenas eventos ativos que ainda não foram encerrados
    val eventosDisponiveis = listaEventos.filter { it.ativo && agora.isBefore(it.dataFim) }

    if (eventosDisponiveis.isEmpty()) {
        println(COR.AMARELO + "Nenhum evento disponível no momento." + COR.RESET)
        return
    }
    printTable("FEED DE EVENTOS DISPONÍVEIS", eventosDisponiveis)
}

// === US: COMPRAR INGRESSO ===
fun comprarIngresso(usuarioLogado: UsuarioComum) {
    println(lineBar)
    println(COR.AMARELO + "--- COMPRA DE INGRESSOS ---" + COR.RESET)

    // 1. Exibe o catálogo disponível
    exibirFeedEventos()

    // 2. Solicita a escolha do usuário
    val idEvento =
        readInt(
            "\nDigite o ID do evento que deseja comprar (0 para cancelar): ",
            COR.VERMELHO + "ID inválido." + COR.RESET,
            0..99999
        )

    if (idEvento == 0) {
        println("Compra cancelada.")
        return
    }

    // 3. Valida a existência do evento
    val eventoEscolhido = listaEventos.find { it.id == idEvento && it.ativo }

    if (eventoEscolhido == null) {
        println(COR.VERMELHO + "Evento não encontrado ou indisponível." + COR.RESET)
        return
    }

    // 4. Valida a capacidade máxima do evento
    if (eventoEscolhido.ingressosVendidos >= eventoEscolhido.capacidadeTotal) {
        println(COR.VERMELHO + "Que pena! Os ingressos para este evento estão esgotados." + COR.RESET)
        return
    }

    // 5. Efetiva a transação
    eventoEscolhido.ingressosVendidos++

    val novoIdIngresso = (listaIngressos.maxOfOrNull { it.id } ?: 0) + 1

    val novoIngresso =
        Ingresso(
            id = novoIdIngresso,
            idEvento = eventoEscolhido.id,
            idUsuario = usuarioLogado.email,
            precoPago = eventoEscolhido.preco,
            status = StatusIngresso.ATIVO
        )

    listaIngressos.add(novoIngresso)

    println(COR.VERDE + "Sucesso! Ingresso comprado. Verifique sua Carteira de Ingressos." + COR.RESET)
}

// === US: CARTEIRA DE INGRESSOS ===
fun exibirCarteiraIngressos(usuarioLogado: UsuarioComum) {
    // Filtra ingressos associados ao e-mail do usuário logado
    val meusIngressos = listaIngressos.filter { it.idUsuario == usuarioLogado.email }

    if (meusIngressos.isEmpty()) {
        println(COR.AMARELO + "Você ainda não possui ingressos comprados." + COR.RESET)
        return
    }

    printTable("MINHA CARTEIRA DE INGRESSOS", meusIngressos)
}
