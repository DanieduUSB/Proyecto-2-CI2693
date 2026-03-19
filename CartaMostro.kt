// Clase que representa una carta de monstruo en el juego de cartas coleccionables.
// Al crear una nueva instancia de la clase, se validan que las características de la carta sean válidas, y las guarda como valores.
// El array de atributos es una lista que usa la clase para verificar que el atributo de cada carta sea correcto.
class CartaMostro(private val nombre: String, private val nivel: Int, private val atributo: String, private val poder: Int) {
	companion object {
		private val atributos = listOf("AGUA", "FUEGO", "VIENTO", "TIERRA", "LUZ", "OSCURIDAD", "DIVINO")
	}

	init {
		require(nombre.isNotEmpty())
		require(nivel > 0 && nivel < 13)
		require(atributo in atributos)
		require(poder >= 0)
		require(poder % 50 == 0)
	}

	// Funciones para obtener las características de la carta.
	fun verNivel() = nivel
	fun verAtributo() = atributo
	fun verPoder() = poder

	override fun toString() = nombre

}
