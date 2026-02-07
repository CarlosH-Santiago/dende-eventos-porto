import entities.Evento

fun main() {
    val agenda = mutableListOf<Evento>()
    var continuar = true

    // 1. Criar o menu de seleção em relaçaõ ao eventos
    // Funções: Visualizar eventos, criar eventos, apagar eventos, alterar os eventos
    while(continuar){
        println("\n--- MENU DE EVENTOS ---")
        println("1 - Ver eventos disponíveis")
        println("2 - Cadastrar novo evento")
        println("3 - Deletar evento por nome")
        println("4 - Alterar um evento")
        println("5 - Alterar o status de um evento")
        println("0 - Sair")
        print("Escolha uma opção: ")

        val opcao = readln().toIntOrNull() ?: -1

        when(opcao){

            // Ver os eventos registrados
            1 -> {
                if (agenda.isEmpty()){
                    print("Não temos nenhum evento cadastrado ")
                } else {
                    agenda.forEachIndexed { index, evento ->
                        // Lógica visual para o status
                        val statusVisual = if (evento.status) "[ATIVO]" else "[CANCELADO]"

                        println("${index + 1}. $statusVisual ${evento.titulo}")
                        println("   Data: ${evento.data} | Desc: ${evento.descricao}")
                    }
                }
            }

            // Cadastrar novos eventos
            2 -> {
                println("Cadastre um novo evento: ")

                print("Digite o título do evento: ")
                var titulo = readln();

                print("Digite a data do evento: ")
                var data = readln();

                print("Digite a descrição do evento: ")
                var descricao = readln().toString();

                val novoEvento = Evento(titulo, data, descricao)

                agenda.add(novoEvento)
            }

            // Deletar um evento
            3 -> {
                println("--- DELETAR ---")
                if (agenda.isEmpty()) {
                    println("Lista vazia.")
                } else {
                    print("Digite o TÍTULO exato do evento para apagar: ")
                    val tituloDigitado = readln()

                    val index = agenda.indexOfFirst { it.titulo == tituloDigitado }

                    if (index != -1) {
                        val removido = agenda.removeAt(index)
                        println("Evento '${removido.titulo}' apagado!")
                    } else {
                        println("Evento não encontrado com esse nome.")
                    }
                }
            }

            // Modificar ou alterar um evento
            4 -> {
                println("\n--- EDITAR EVENTO ---")
                if (agenda.isEmpty()) {
                    println("Lista vazia.")
                } else {
                    print("Digite o TÍTULO do evento que deseja alterar: ")
                    val buscaTitulo = readln()
                    val index = agenda.indexOfFirst {
                        it.titulo.equals(buscaTitulo, ignoreCase = true)
                    }

                    if (index != -1) {

                        val evento = agenda[index]

                        println("--- Editando '${evento.titulo}' ---")
                        println("(Deixe em branco e dê Enter para manter o valor atual)")

                        print("Novo Título [Atual: ${evento.titulo}]: ")
                        val novoTitulo = readln()
                        if (novoTitulo.isNotBlank()) evento.titulo = novoTitulo

                        print("Nova Data [Atual: ${evento.data}]: ")
                        val novaData = readln()
                        if (novaData.isNotBlank()) evento.data = novaData

                        print("Nova Descrição [Atual: ${evento.descricao}]: ")
                        val novaDesc = readln()
                        if (novaDesc.isNotBlank()) evento.descricao = novaDesc

                        println("Evento atualizado com sucesso!")
                    } else {
                        println("Evento com título '$buscaTitulo' não foi encontrado.")
                    }
                }
            }

            // 5. Alterar o status (Busca pelo Título)
            5 -> {
                println("\n--- ALTERAR STATUS ---")
                if (agenda.isEmpty()) {
                    println("Lista vazia.")
                } else {
                    print("Digite o TÍTULO do evento para mudar o status: ")
                    val buscaTitulo = readln()

                    val index = agenda.indexOfFirst {
                        it.titulo.equals(buscaTitulo, ignoreCase = true)
                    }

                    if (index != -1) {
                        val evento = agenda[index]

                        // Inverte o status
                        evento.status = !evento.status

                        val novoEstado = if (evento.status) "ATIVADO" else "CANCELADO"
                        println("O status do evento '${evento.titulo}' foi alterado para: $novoEstado")
                    } else {
                        println("Evento com título '$buscaTitulo' não foi encontrado.")
                    }
                }
            }

            0 -> {
                println("Saindo do sistema...")
                continuar = false // Isso fará o loop parar
            }

            else -> println("Opção inválida! Tente novamente.")
        }
    }
}