import java.time.LocalDateTime

// Simulação de repositórios globais (conforme seu código original)
val listaEventos = mutableListOf<entities.Evento>()
val listaIngressos = mutableListOf<entities.Ingresso>()

// === US 6: CADASTRAR EVENTO ===
fun cadastrarEvento(organizadorLogado: entities.UsuarioComum) {
    println("\n--- NOVO EVENTO ---")

    val nomeEv = readString("Nome do Evento: ", "Nome inválido!", minLength = 3)
    val descEv = readString("Descrição: ", "Descrição muito curta!", minLength = 5)

    // Usando a função do seu amigo para tratar as datas e validações de 30min
    val (dataIniEv, dataFimEv) = lerDatasEvento()

    // Seleção de Tipo (Refatorado para ser mais limpo)
    val tipoEv = selecionarCategoria()

    // Seleção de Modalidade
    val modEv = selecionarModalidade()

    val localEv = readString("Local (Endereço ou Link): ", "Local não pode ser vazio!")
    val capEv = readInt("Capacidade Máxima: ", "Digite um número válido!", 1..1000000)
    val precoEv = readDouble("Preço do Ingresso (0 para gratuito): ", "Preço inválido!")

    // Política de Estorno
    val permiteEstorno = readInt("Permite estorno? (1-Sim, 2-Não): ", "Opção inválida!", 1..2) == 1
    val taxaEstorno = if (permiteEstorno) {
        readDouble("Qual a taxa de estorno (ex: 10.0 para 10%): ", "Valor inválido!") / 100.0
    } else 0.0

    // Evento Vinculado
    val idVinc = readInt("ID do evento principal (0 para nenhum): ", "ID inválido!")
    val idVinculadoFinal = if (idVinc > 0 && listaEventos.any { it.id == idVinc }) idVinc else null

    // Status Inicial
    val statusEv = readInt("Status (1-Ativo, 2-Inativo): ", "Opção inválida!", 1..2) == 1

    // Criação do Objeto
    val novoId = (listaEventos.maxOfOrNull { it.id } ?: 0) + 1
    val novoEvento = entities.Evento(
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

// === US 10: LISTAGEM DE EVENTOS ===
fun listagemDeEventos(emailOrganizador: String) {
    val meusEventos = listaEventos.filter { it.idOrganizador == emailOrganizador }

    if (meusEventos.isEmpty()) {
        println("Você não possui eventos cadastrados.")
        return
    }

    val header = String.format("%-5s %-20s %-15s %-10s %-10s", "ID", "NOME", "DATA", "PREÇO", "STATUS")
    printTable(header, meusEventos)
}

// === US 7: ALTERAR EVENTO ===
fun alterarEvento(emailOrganizador: String) {
    val meusEventosAtivos = listaEventos.filter { it.idOrganizador == emailOrganizador && it.ativo }

    if (meusEventosAtivos.isEmpty()) {
        println("Não há eventos ativos para alteração.")
        return
    }

    val idBusca = readInt("Digite o ID do evento para alterar (0 para cancelar): ", "ID inválido")
    val evento = meusEventosAtivos.find { it.id == idBusca }

    if (evento == null) {
        println("Evento não encontrado ou indisponível.")
    } else if (evento.ingressosVendidos > 0) {
        println("Erro: Evento com ingressos vendidos não pode ser alterado.")
    } else {
        println("Deixe em branco para manter o atual.")
        val novoNome = readln().trim()
        if (novoNome.isNotBlank()) evento.nome = novoNome
        // Repetir lógica para outros campos...
        println("Atualizado com sucesso!")
    }
}

// === US 8 e 9: MODIFICAR STATUS (Ativar/Desativar) ===
fun modificarStatusEvento(emailOrganizador: String) {
    val idStatus = readInt("Digite o ID do evento: ", "ID inválido")
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

// Funções auxiliares

private fun selecionarCategoria(): enums.CategoriaEvento {
    println("Categorias:")
    enums.CategoriaEvento.values().forEachIndexed { i, t -> println("$i - $t") }
    val index = readInt("Escolha o número: ", "Opção inválida", 0..enums.CategoriaEvento.values().size - 1)
    return enums.CategoriaEvento.values()[index]
}

private fun selecionarModalidade(): enums.Modalidade {
    println("Modalidade: 1-Presencial, 2-Remoto, 3-Híbrido")
    return when (readInt("Escolha: ", "Inválido", 1..3)) {
        2 -> enums.Modalidade.REMOTO
        3 -> enums.Modalidade.HIBRIDO
        else -> enums.Modalidade.PRESENCIAL
    }
}

private fun ativarEvento(evento: entities.Evento) {
    if (evento.dataInicio.isBefore(LocalDateTime.now())) {
        println("Erro: Não é possível ativar evento no passado.")
    } else {
        evento.ativo = true
        println("Evento Ativado!")
    }
}

private fun desativarEvento(evento: entities.Evento) {
    if (evento.ingressosVendidos > 0) {
        println("Existem ingressos vendidos! Digite 'CONFIRMAR' para cancelar tudo:")
        if (readln() == "CONFIRMAR") {
            evento.ativo = false
            listaIngressos.filter { it.idEvento == evento.id }.forEach { it.status = enums.StatusIngresso.CANCELADO }
            println("Evento e ingressos cancelados.")
        }
    } else {
        evento.ativo = false
        println("Evento Desativado.")
    }
}