// Arquivo: Components.kt

import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Duration


/**
 * Lê valores inteiros até que o valor esteja dentro do intervalo especificado.
 */
fun readInt(message: String, errorMessage: String, range: IntRange = 0..Int.MAX_VALUE): Int {
    while (true) {
        print(message)
        val input = readlnOrNull()

        try {
            val value = input?.toInt()
            if (value != null && value in range) {
                return value
            } else {
                println("Erro: $errorMessage")
            }
        } catch (e: NumberFormatException) {
            println("Erro: $errorMessage")
        }
    }
}

/**
 * Lê valores decimais até que o valor atenda os critérios de mínimo e máximo definidos.
 */
fun readDouble(message: String, errorMessage: String, minValue: Double = 0.0, maxValue: Double = Double.MAX_VALUE): Double {
    while (true) {
        print(message)
        val input = readlnOrNull()

        try {
            val value = input?.toDouble()
            if (value != null && value >= minValue && value <= maxValue) {
                return value
            } else {
                println("Erro: $errorMessage")
            }
        } catch (e: NumberFormatException) {
            println("Erro: $errorMessage")
        }
    }
}

/**
 * Lê valores textuais até que uma string atenda o critério de tamanho mínimo definido.
 */
fun readString(message: String, errorMessage: String, minLength: Int = 0): String {
    while (true) {
        print(message)
        val input = readlnOrNull() ?: ""

        if (input.length >= minLength) {
            return input
        } else {
            println("Erro: $errorMessage")
        }
    }
}

fun lerDatasEvento(): Pair<LocalDateTime, LocalDateTime> {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    while (true) {
        try {
            print("Início (dd/MM/yyyy HH:mm): ")
            val dataIni = LocalDateTime.parse(readln(), formatter)

            print("Fim (dd/MM/yyyy HH:mm): ")
            val dataFim = LocalDateTime.parse(readln(), formatter)

            val agora = LocalDateTime.now()

            when {
                dataIni.isBefore(agora) -> {
                    println("Erro: O evento não pode começar no passado.")
                }

                dataFim.isBefore(dataIni) -> {
                    println("Erro: A data de fim não pode ser antes do início.")
                }

                Duration.between(dataIni, dataFim).toMinutes() < 30 -> {
                    println("Erro: O evento deve ter no mínimo 30 minutos.")
                }

                else -> {
                    return Pair(dataIni, dataFim)
                }
            }

        } catch (e: Exception) {
            println("Formato inválido. Use dd/MM/yyyy HH:mm")
        }
    }
}

/**
 * Imprime todos os dados de uma coleção de forma tabular.
 */
fun printTable(header: String, items: List<Any>) {
    // Cria uma linha separadora baseada no tamanho do cabeçalho
    val separator = "-".repeat(header.length + 20)

    println(separator)
    println(header)
    println(separator)

    if (items.isEmpty()) {
        println("Nenhum registro encontrado.")
    } else {
        for (item in items) {
            // A classe (Evento, Ingresso, etc) precisa ter um toString() bem formatado
            // para que a exibição fique bonita aqui.
            println(item.toString())
        }
    }

    println(separator)
}