class ListaAdyacenciaGrafo<T> : Grafo<T> {
    private val lista: MutableMap<T, MutableList<T>> = mutableMapOf()

    override fun agregarVertice(v: T): Boolean {
        if (contiene(v)) return false
        lista[v] = mutableListOf()
        return true
    }

    override fun eliminarVertice(v: T): Boolean {
        if (!contiene(v)) return false
        lista.remove(v)
        for ( (vertice, arcosSalida) in lista) {
            lista[vertice] = arcosSalida.filter { it != v }.toMutableList()
        }
        return true
    }

    override fun conectar(desde: T, hasta: T): Boolean {
        if (!contiene(desde) || !contiene(hasta)) return false
        if (hasta !in lista[desde]!!) {
            lista[desde]!!.add(hasta)
            return true
        }
        return false
    }

    override fun contiene(v: T): Boolean {
        return v in lista.keys
    }

    override fun obtenerArcosSalida(v: T): List<T> {
        return lista[v] ?: emptyList()
    }

    override fun obtenerArcosEntrada(v: T): List<T> {
        val arcosEntrada: MutableList<T> = mutableListOf()
        for ( (vertice,arcosSalida) in lista) {
            if (v in arcosSalida) arcosEntrada.add(vertice)
        }
        return arcosEntrada
    }

    override fun tamano() = lista.size

    override fun subgrafo(vertices: Collection<T>): Grafo<T> {
        val nuevoGrafo = ListaAdyacenciaGrafo<T>()
        for (vertice in vertices) {
            nuevoGrafo.lista[vertice] = lista[vertice]?.filter { it in vertices }?.toMutableList() ?: continue
        }
        return nuevoGrafo
    }

    override fun toString(): String {
        var extSalida = ""
        for ((vertice,arcosSalida) in lista) {
            var intSalida = vertice.toString() + "->" + arcosSalida.toString() + "\n🡓 \n"
            extSalida = extSalida + intSalida
        }
        extSalida = extSalida + "/"
        return extSalida + " \nTamaño: ${tamano()}"
    }

}
