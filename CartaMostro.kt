class CartaMostro(val nombre: String, val nivel: Int, val atributo: String, val poder: Int) {
	companion object {
		val atributos = listOf("AGUA", "FUEGO", "VIENTO", "TIERRA", "LUZ", "OSCURIDAD", "DIVINO")
	}

	init {
		require(nombre.isNotEmpty())
		require(nivel > 0 && nivel < 13)
		require(atributo in atributos)
		require(poder >= 0)
		require(poder % 50 == 0)
	}

	override fun toString() = nombre

}
