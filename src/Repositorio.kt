// --- TABELAS EM MEMÓRIA ---
private val usuarios = mutableListOf<UsuarioComum>()
private val organizadores = mutableListOf<Organizador>()
private val eventos = mutableListOf<Evento>()
private val ingressos = mutableListOf<Ingresso>()

// USUÁRIO COMUM
fun SalvarUsuario(usuario: UsuarioComum) = usuarios.add(usuario)

fun BuscarUsuario(email: String) = usuarios.find { it.email == email }


// ORGANIZADOR
fun SalvarOrganizador(organizador: Organizador) = organizadores.add(organizador)

fun BuscarOrganizador(email: String) = organizadores.find { it.email == email }


// EVENTO
fun SalvarEvento(evento: Evento) = eventos.add(evento)

fun BuscarEventoPorId(id: Int) = eventos.find { it.id == id }

fun FiltrarEventosPorCategoria(categoria: CategoriaEvento) =
    eventos.filter { it.tipo == categoria }

fun FiltrarEventosPorOrganizador(emailOrg: String) =
    eventos.filter { it.idOrganizador == emailOrg }

fun ListarTodosEventos() = eventos.toList()

fun ExcluirEvento(id: Int) = eventos.removeIf { it.id == id }


// INGRESSO
fun SalvarIngresso(ingresso: Ingresso) = ingressos.add(ingresso)

fun BuscarIngressoPorId(id: Int) = ingressos.find { it.id == id }

fun BuscarIngressosPorUsuario(emailUsuario: String): List<Ingresso> {
    return ingressos.filter { it.idUsuario.toString() == emailUsuario }
}

fun ListarTodosIngressos() = ingressos.toList()

fun ExcluirIngresso(id: Int) = ingressos.removeIf { it.id == id }