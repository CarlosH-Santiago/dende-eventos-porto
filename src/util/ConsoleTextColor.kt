package util

object ConsoleTextColor {
    // Reset (Volta ao normal)
    const val RESET = "\u001B[0m"

    // Cores de Texto (Foreground)
    const val VERMELHO = "\u001B[31m"
    const val VERDE = "\u001B[32m"
    const val AMARELO = "\u001B[33m"
    const val AZUL = "\u001B[34m"
    const val CIANO = "\u001B[36m"
    const val BRANCO = "\u001B[37m"

    // Estilos
    const val NEGRITO = "\u001B[1m"       // Negrito
    const val SUBLINHADO = "\u001B[4m"  // Sublinhado
}