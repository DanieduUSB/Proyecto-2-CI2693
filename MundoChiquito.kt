import java.io.File
import java.io.BufferedReader

// Funcion que le el archivo .csv, crea las cartas y construye un grafo con ellas.
// El grafo esta definido de tal manera que cada vertice es un monstruo, y dos monstruos estan conectados sii
// tienen exactamente una caracteristica en comun (nivel, atributo o poder).
// La funcion retorna el grafo con las cartas, asi como un conjunto que lista todas las cartas añadidas para conveniencia.
fun leerArchivo(archivo: File): Pair<Grafo<CartaMostro>,MutableSet<CartaMostro>> {
	val bufferedReader = archivo.bufferedReader()
	var linea = bufferedReader.readLine()
	//Verifica que la primera línea del archivo contenga el encabezado correcto
	if (linea != "Nombre,Nivel,Atributo,Poder") {
		error("Formato de archivo inválido (Encabezado)")
	}

	linea = bufferedReader.readLine()
	//Crea el grafo de mosntruos.
	val cartas = ListaAdyacenciaGrafo<CartaMostro>()
	//Variable que contiene las cartas ingresadas al grafo
	val listaCartas: MutableSet<CartaMostro> = mutableSetOf()
	var numeroLinea = 1
	// Loop que lee cada linea del archivo, crea la carta correspondiente, y la agrega al grafo.
	// Tambien verifica que el formato y propiedades de cada linea sea correcto.
	while (linea != null) {
		val lineaElementos = linea.split(",")
		if (lineaElementos.size != 4) {
			error("Entrada inválida: Línea #${numeroLinea} '${linea}'")
		}
		try {
			val nombre = lineaElementos[0]
			// Verifica que el nombre de la carta no sea repetido.
			if (listaCartas.any { it.toString() == nombre }) {
				error("Entrada inválida: Línea #${numeroLinea} '${linea}'. El nombre de la carta es repetido")
			}

			// Leemos los datos de la carta.
			val nivel = lineaElementos[1].toInt()
			val atributo = lineaElementos[2]
			val poder = lineaElementos[3].toInt()

			// Se crea la carta y se verifica que sus propiedades sean validas.
			val cartaMostro = CartaMostro(nombre, nivel, atributo, poder)

			// Se agrega la carta al grafo.
			cartas.agregarVertice(cartaMostro)

			// Se buscan las cartas que cumplan la condición de compartir exactamente una característica.
			val cartasCualidadUnica = listaCartas.filter {
				( ( (it.verNivel() == cartaMostro.verNivel()) xor (it.verAtributo() == cartaMostro.verAtributo()) )
				xor
				(it.verPoder() == cartaMostro.verPoder()) )
				&&
				!(it.verNivel() == cartaMostro.verNivel() && it.verAtributo() == cartaMostro.verAtributo()
				&&
				it.verPoder() == cartaMostro.verPoder())
			}

			// Se conectan las cartas validas.
			for (carta in cartasCualidadUnica) {
				cartas.conectar(carta,cartaMostro)
				cartas.conectar(cartaMostro,carta)
			}

			// Se añade la carta recien creada a la lista de cartas.
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

// Variación de BFS que se limita a buscar cadenas de longitud 3 y permite visitar vertices repetidos. 
// Retorna un conjunto con todas las cadenas de longitud 3 que se pueden formar a partir de la carta dada.
fun bfsTernas(cartas: Grafo<CartaMostro>, primeraCarta: CartaMostro): MutableSet<Array<CartaMostro>> {
	val cola = ArrayDeque<Array<CartaMostro>>()
	val primeraCadena = arrayOf(primeraCarta)
	cola.addLast(primeraCadena)
	//Guardará todas las cadenas de tres cartas encontradas por BFS
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

// Funcion main para llamar las funciones e imprimir el resultado.
fun main(args: Array<String>) {

	// Se verifica que la entrada sea correcta.
	if (args.size != 1) {
		error("Debe indicar un nombre de archivo como único argumento")
	}

	val archivo = File(args[0])

	// Se lee el archivo, se construye el grafo, y se obtiene la lista de cartas.
	val (cartas, listaCartas) = leerArchivo(archivo)

	// Conjunto que contendra todas las ternas que se pueden formar a partir de cada carta en el grafo.
	var cadenas: Set<Array<CartaMostro>> = mutableSetOf()

	// Ejecutamos BFS para cada carta del grafo para consegur todas las ternas validas posibles.
	for (carta in listaCartas) {
		val nuevasCadenas = bfsTernas(cartas,carta)
		cadenas = cadenas.union(nuevasCadenas)
	}

	// Imprimimos las ternas obtenidas.
	for (cadena in cadenas) {
		println(cadena.joinToString(" "))
	}

}
