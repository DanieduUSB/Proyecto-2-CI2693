import java.io.File
import java.io.BufferedReader

fun leerArchivo(archivo: File): Pair<Grafo<CartaMostro>,MutableSet<CartaMostro>> {
	val bufferedReader = archivo.bufferedReader()
	var linea = bufferedReader.readLine()
	//Verifica que la primera línea del archivo contenga el encabezado correcto
	if (linea != "Nombre,Nivel,Atributo,Poder") {
		error("Formato de archivo inválido (Encabezado)")
	}

	linea = bufferedReader.readLine()
	//Crea el grafo de salida
	val cartas = ListaAdyacenciaGrafo<CartaMostro>()
	//Variable que contiene las cartas ingresadas al grafo
	val listaCartas: MutableSet<CartaMostro> = mutableSetOf()
	var numeroLinea = 1
	while (linea != null) {
		val lineaElementos = linea.split(",")
		if (lineaElementos.size != 4) {
			error("Entrada inválida: Línea #${numeroLinea} '${linea}'")
		}
		try {
			val nombre = lineaElementos[0]

			if (listaCartas.any { it.nombre == nombre }) {
				error("Entrada inválida: Línea #${numeroLinea} '${linea}'. El nombre de la carta es repetido")
			}

			val nivel = lineaElementos[1].toInt()
			val atributo = lineaElementos[2]
			val poder = lineaElementos[3].toInt()
			val cartaMostro = CartaMostro(nombre, nivel, atributo, poder)
			cartas.agregarVertice(cartaMostro)
			val cartasCualidadUnica = listaCartas.filter {
				( (it.nivel == cartaMostro.nivel) xor (it.atributo == cartaMostro.atributo) ) xor (it.poder == cartaMostro.poder)
			}
			for (carta in cartasCualidadUnica) {
				cartas.conectar(carta,cartaMostro)
				cartas.conectar(cartaMostro,carta)
			}
			listaCartas.add(cartaMostro)
		} catch (e: IllegalArgumentException) {
			error("Entrada inválida: Línea #${numeroLinea} '${linea}'")
		} catch (e: NumberFormatException) {
			error("Entrada inválida: Línea #${numeroLinea} '${linea}'")
		}
		numeroLinea++
		linea = bufferedReader.readLine()
	}

	return Pair(cartas,listaCartas)
}

fun bfsTernas(cartas: Grafo<CartaMostro>, primeraCarta: CartaMostro): MutableSet<Array<CartaMostro>> {
	val cola = ArrayDeque<Array<CartaMostro>>()
	val primeraCadena = arrayOf(primeraCarta)
	cola.addLast(primeraCadena)
	val cadenas: MutableSet<Array<CartaMostro>> = mutableSetOf()
	while (cola.isNotEmpty()) {
		val cadena = cola.removeFirst()
		if (cadena.size == 3) {
			cadenas.add(cadena)
			continue
		}

		val carta = cadena.last()

		for (sucesor in cartas.obtenerArcosSalida(carta)) {
			val nuevaCadena = cadena + sucesor
			cola.addLast(nuevaCadena)
		}
	}
	return cadenas
}

fun main(args: Array<String>) {
	if (args.size != 1) {
		error("Debe indicar un nombre de archivo como único argumento")
	}

	val archivo = File(args[0])

	val (cartas, listaCartas) = leerArchivo(archivo)
	var cadenas: Set<Array<CartaMostro>> = mutableSetOf()

	for (carta in listaCartas) {
		val nuevasCadenas = bfsTernas(cartas,carta)
		cadenas = cadenas.union(nuevasCadenas)
	}

	for (cadena in cadenas) {
		println(cadena.joinToString(" "))
	}

}
