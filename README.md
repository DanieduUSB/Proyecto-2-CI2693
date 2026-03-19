# Proyecto 2 - 2110447 - 2010518
+ Nombres: Daniel Quijada y Brian Orta
+ Carnets: 2010518, 2110447
+ Universidad Simón Bolívar

# Descripción del Proyecto: Mundo Chiquito
Para este proyecto se nos planteó la situación de un juego de cartas llamado **Duelo de cartas de mostros**, en este juego
dos jugadores se enfrentan con masos de 40-60 cartas, donde la mayoría de las cartas son mostros, estos mostros poseen
4 cualidades claves; *nombres*, *atributos*, *niveles* y *poderes*, cada uno con distintas propiedades.

En este juego existe una carta de tipo conjuro llamada *Mundo Chiquito* que tiene la siguiente descripción:
> Muéstrale a tu oponente una carta mostro de tu mano, elige un mostro de tu mazo que comparta
exactamente una característica (nivel, poder o atributo) con el mostro revelado de tu mano y
muéstraselo a tu oponente. Luego agrega desde tu mazo a tu mano un mostro que comparta
exactamente una característica (nivel, poder o atributo) con el mostro revelado de tu mazo.

Se nos pide crear una herramienta en kotlin para ayudar a los jugadores que usen esta carta a conocer las distintas combinaciones
de mostros con las que la pueden usar. Más específico, se desea que usando grafos, creemos un programa que imprima por la salida
estandar todas las ternas que se puedan crear con un maso dado.

# Instrucciones de uso
Coloque todos los archivos encontrados en este repositorio en un mismo directorio y siga los siguientes pasos (debes tener
kotlin instalado):

1. Utilize el comando `make` para compilar y preparar el pograma.
2. En el archivo .csv aportado o alguno creado por usted escriba la información correspodiente a cada carta del maso, siguiendo la organización dada por el encabezado del archivo .csv.
3. Use el siguiente comando para ejecutar el programa:
```
./mundoChiquito.sh <nombre_del_archivo_csv>
```
# Lógica del Programa
Al recibir los datos el programa los lee, verifica que todos sean válidos, y procede a crear un grafo no dirigdo basado en listas
de adyacencias definido de la siguiente manera: los vertices representan los distintos mostros del maso y dos vertices estan 
conectados si sus mostros correspondientes comparten exactamente un atributo. 

Una vez creado el grafo, programa procede a ejecutar un BFS modificado que solo busca 3 capas de profundidad sobre cada vertice,
con el proposito de encontrar todas las cadenas de longitud 3 que existen en el grafo, las busca todas porque considerando la 
definiciòn del grafo, cada camino de longitud 3 en el grafo debe ser una terna de mostros valida. Todas las cadenas encontradas se 
guardan en un conjunto solución para garantizar que no hayan cadenas repetidas.

Una vez finalizada la búsqueda el programa imprime por la salida estandar todas las ternas encontradas.

# Análisis de Complejidad
El programa usa dos funciones, la función que lee los datos y genera el grafo `leerArchivo()`, y la fución que realiza la búsqueda
`bfsTernas()`.  

La función `leerArchivo()` recibe el archivo .csv y por cada linea (carta) crea un objeto *mostro* correspondiente, lo añade al
grafo como un vértice, y lo conecta a todos los otros vértices (cartas) correspondietes. Itera sobres los vertices una vez y añade
todas los lados, por lo tanto el orden de la función es O(|V|+|E|).

Por otro lado, la función `bfsTernas()` es llamada una vez por cada vertice, y la función utiliza un algoritmo de búsqueda BFS
modificado para que tenga dos características particulares, que termine su búsqueda después de revisar la tercera capa, y que sea
capaz de revisar vértices previamente visitados otra vez. La limitación de 3 capas debería hacer que el algoritmo sea más rápido
que un BFS normal en muchos casos, pero el hecho de que puede visitar un mismo vértice varias veces puede hacerlo más lento,
se considera que estos dos cambios de cancelan y la complejidad se aproxima a la de un BFS estanar, esta siendo O(|V|+|E|),
consderando que se ejecuta una vez por vértice la complejdad total es O(|V|x(|V|+|E|)).

Así, la complejidad total del programa es O(|V|x(|V|+|E|)) + O(|V|+|E|) = O(|V|x(|V|+|E|)).

# Decisiones de Implementación
+ Considerando el requerimiento del enunciado que exigía utilizar un grafo para calcular nuestra solución, decidimos
  enfocar nuestra potencial solución en algoritmos de recorrido, tomando en cuenta que todas las respuestas tomarían la forma de
  cadenas de logitud 3, se decidió que un BFS modificado era la opción adecuada, debido a la propiedad de que realiza su recorrido
  por capas.
+ Para facilitar la búsqueda de soluciones y simplificar la estructura del grafo, se decidió hacer que la limitación de que las
  cartas solo pueden tener una característica en común sea una propiedad implícita del grafo. Al verificar esa propiedad durante
  la creación del grafo, se garantiza que toda las cadenas de longitud 3 encontradas dentro del grafo son ternas válidas.
+ Este programa hace mucho uso de la estructura de datos `set` debido a que nos ofrece una manera eficiente y conveniente de
  garantizar que no hayan soluciones repetidas.
+ Al realizar la búsqueda sobre cada vértice, garantizamos encontrar todas las permutaciones posibles de cartas validas que puedan
  existir dentro del grafo.
