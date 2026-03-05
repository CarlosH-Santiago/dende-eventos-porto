// Arquivo: Components.kt

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