# Prueba Teórica — Desarrollador Java | STAFF III

**Nombre:** Miguel Angel Arias Castaño   **Fecha:** 23/10/2026

> Instrucciones: Responde todas las preguntas razonando tu respuesta (no se permite ejecutar el código). Este nivel exige dominio sólido de POO, colecciones, manejo de excepciones, fundamentos de Spring/JPA y SQL con joins. Duración estimada: 60–75 minutos.

---

## Parte A — Java intermedio

**1.** ¿Cuál es el resultado del siguiente código?
```java
Integer a = 127, b = 127;
Integer c = 200, d = 200;
System.out.println((a == b) + " " + (c == d));
```
- A. `true true`
- B. `true false`
- C. `false false`
- D. `false true`

Respuesta: B
Porque Java maneja un cache interno para los Integer que van desde -128 hasta 127. Como a y b valen 127 el autoboxing les asigna la misma referencia en memoria y por eso a == b da true. En cambio 200 ya se sale de ese rango entonces para c y d se crean dos objetos distintos en memoria y como el == compara referencias y no el valor como tal (para eso tocaria usar equals) c == d da false.

**2.** ¿Cuál es la salida?
```java
public class Test {
    public static void main(String[] args) {
        String s = "abc";
        s.concat("def");
        s.toUpperCase();
        System.out.println(s);
    }
}
```
- A. `abcdef`
- B. `ABC`
- C. `abc`
- D. `ABCDEF`

Respuesta: C
Los String en Java son inmutables. Cuando se llama a concat o toUpperCase esos metodos crean un nuevo String en memoria pero no modifican el original. Como en el codigo no se esta guardando el resultado de nuevo en la variable s (por ejemplo haciendo s = s.concat("def")) entonces s se queda con el valor inicial que era abc.

**3.** Sobre **clases abstractas** e **interfaces** en Java (8+), selecciona todas las afirmaciones **verdaderas**:
- A. Una clase puede implementar múltiples interfaces pero solo extender una clase.
- B. Una interfaz puede tener métodos `default` con implementación.
- C. Una clase abstracta no puede tener constructores.
- D. Una interfaz puede declarar atributos de instancia mutables.

Respuesta: A y B
En Java no existe la herencia multiple de clases asi que solo se puede hacer extends de una sola clase pero si se pueden implementar varias interfaces. Tambien desde Java 8 las interfaces dejan crear metodos default con implementacion. La C es falsa porque las clases abstractas si tienen constructores para inicializar sus atributos cuando las llaman desde las clases hijas con super y la D es falsa porque las variables en una interfaz son constantes por defecto (public static final) y no pueden cambiar de valor.

**4.** ¿Cuál es la diferencia entre una excepción **checked** y una **unchecked**?
- A. Las checked heredan de `RuntimeException`; las unchecked de `Exception`.
- B. Las checked deben declararse o capturarse en tiempo de compilación; las unchecked no.
- C. No hay diferencia, es solo una convención de nombres.
- D. Las unchecked detienen siempre la JVM.

Respuesta: B
Las checked heredan de Exception (sin contar RuntimeException) y el compilador te obliga si o si a capturarlas con un try catch o a declararlas con throws en el metodo. Las unchecked heredan de RuntimeException como NullPointerException que salen en tiempo de ejecucion normalmente por fallos de logica o validaciones y el compilador no te obliga a declararlas.

**5.** ¿Qué imprime?
```java
List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4));
nums.removeIf(n -> n % 2 == 0);
System.out.println(nums);
```
- A. `[1, 2, 3, 4]`
- B. `[2, 4]`
- C. `[1, 3]`
- D. Error de compilación

Respuesta: C
Aunque List.of devuelve una lista inmutable al meterla dentro del new ArrayList se crea una copia que si se deja modificar. Luego el removeIf evalua la condicion y saca todos los numeros pares dejando solo los impares 1 y 3.

---

## Parte B — Colecciones y rendimiento

**6.** Necesitas una estructura que mantenga **el orden de inserción** y **no permita duplicados**. ¿Cuál usas?
- A. `HashSet`
- B. `TreeSet`
- C. `LinkedHashSet`
- D. `ArrayList`

Respuesta: C
Porque al ser un Set no deja meter elementos repetidos y a diferencia del HashSet normal que no asegura ningun orden o el TreeSet que los ordena de menor a mayor el LinkedHashSet maneja una lista enlazada interna para guardar el orden exacto en que se fueron agregando.

**7.** ¿Cuál es la complejidad temporal promedio de una búsqueda por clave (`get`) en un `HashMap`?
- A. O(n)
- B. O(log n)
- C. O(1)
- D. O(n log n)

Respuesta: C
Porque con el hash de la llave calcula directo la posicion donde esta guardado el valor sin tener que recorrer toda la coleccion.

**8.** Explica brevemente la diferencia entre `ArrayList` y `LinkedList`, y un caso en el que elegirías cada una.

Respuesta:
ArrayList por debajo trabaja con un arreglo dinamico entonces buscar un elemento por su indice con get es super rapido, pero insertar o borrar cosas en la mitad de la lista es mas pesado porque tiene que mover el resto de elementos en el arreglo. LinkedList en cambio funciona con nodos doblemente enlazados entonces agregar o quitar elementos al inicio o al final es rapido pero buscar por posicion es lento porque le toca recorrer los nodos.
Casi siempre uso ArrayList cuando necesito listar datos recorrerlos o acceder por posicion que es lo mas comun en servicios REST. Usaria LinkedList si necesito armar una cola donde todo el tiempo estoy metiendo y sacando elementos al principio o al final sin buscar por indice.


---

## Parte C — Spring / REST / JPA

**9.** En una API REST, ¿qué código de estado HTTP es el más apropiado para una creación exitosa de un recurso?
- A. `200 OK`
- B. `201 Created`
- C. `204 No Content`
- D. `302 Found`

Respuesta: B
Se devuelve 201 Created porque el estandar rest indica que cuando un post crea un recurso nuevo en el servidor ese es el codigo correcto acompañado de la URL del recurso creado en el header location.

**10.** En Spring, ¿cuál es el propósito de la anotación `@Autowired`?
- A. Mapear una entidad a una tabla de base de datos.
- B. Inyectar una dependencia gestionada por el contenedor de Spring.
- C. Exponer un método como endpoint HTTP.
- D. Marcar un método como transaccional.

Respuesta: B
Sirve para hacer inyeccion de dependencias y que el contenedor de spring nos pase la instancia del bean que necesitamos sin tener que hacerle new manualmente. Aunque es mejor practica inyectar por constructor que poner el Autowired encima del atributo.

**11.** En JPA, ¿qué anotación marca una clase como entidad persistente y cuál define la clave primaria? Explica brevemente.

Respuesta:
Para marcar la clase como entidad se usa @Entity que le dice a JPA que esa clase mapea a una tabla de la base de datos. Y para la llave primaria se usa @Id encima del atributo identificador que normalmente va acompañado de @GeneratedValue si el id es autoincremental.

**12.** ¿Qué problema describe el término **"N+1 queries"** en JPA/Hibernate y cómo lo mitigarías?

Respuesta:
Eso pasa cuando tenemos una relacion entre entidades con carga perezosa lazy y hacemos una consulta para traer N registros padre (1 query) y luego al recorrer esa lista para sacar los hijos se pone a lanzar un SELECT adicional por cada padre (N queries). Al final en vez de hacer una sola consulta termina haciendo N+1 consultas a la base de datos y tumba el rendimiento.

Para solucionarlo se puede usar JOIN FETCH en la consulta JPQL o ponerle @EntityGraph al metodo en el repositorio de Spring Data para que se traiga los padres y la lista de hijos de una vez en un solo JOIN.

---

## Parte D — SQL con relaciones

Modelo de datos:
```
PERSONA(id_persona PK, nombre, email, id_ciudad FK, id_pais FK)
CIUDAD(id_ciudad PK, nombre_ciudad)
PAIS(id_pais PK, nombre_pais)
```

**13.** Escribe una consulta que devuelva el **nombre, email, ciudad y país** de todas las personas (usando los JOIN necesarios).

SELECT p.nombre, p.email, c.nombre_ciudad, pa.nombre_pais
FROM PERSONA p
INNER JOIN CIUDAD c ON p.id_ciudad = c.id_ciudad
INNER JOIN PAIS pa ON p.id_pais = pa.id_pais

**14.** Escribe una consulta que devuelva los **países que NO tienen ninguna persona asociada**.

SELECT pa.id_pais, pa.nombre_pais
FROM PAIS pa
LEFT JOIN PERSONA p ON pa.id_pais = p.id_pais
WHERE p.id_persona IS NULL

**15.** Escribe una consulta que devuelva la **cantidad de personas por país**, ordenada de mayor a menor.

SELECT pa.nombre_pais, COUNT(p.id_persona) AS cantidad_personas
FROM PAIS pa
LEFT JOIN PERSONA p ON pa.id_pais = p.id_pais
GROUP BY pa.id_pais, pa.nombre_pais
ORDER BY cantidad_personas DESC

---

## Parte E — Ejercicio aplicado (diseño de código)

> Este ejercicio mide tu criterio de diseño orientado a objetos. Escríbelo en pseudocódigo o Java.

**16.** Te piden modelar un sistema simple de **biblioteca**. Diseña las clases necesarias para representar:
- Un `Libro` (con título, autor e ISBN).
- Un `Usuario` (con nombre y una lista de libros prestados).
- Una operación para **prestar** un libro a un usuario y otra para **devolverlo**.

Define las clases, sus atributos, relaciones y las firmas de los métodos principales. Explica brevemente **dónde** pondrías la lógica de "no se puede prestar un libro que ya está prestado" y por qué.

Respuesta:

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private boolean prestado;

   public Libro(String isbn, String titulo, String autor) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.prestado = false;
    }

   public void prestar() {
        if (this.prestado) {
            throw new IllegalStateException("El libro ya se encuentra prestado");
        }
        this.prestado = true;
    }

   public void devolver() {
        if (!this.prestado) {
            throw new IllegalStateException("El libro no estaba prestado");
        }
        this.prestado = false;
    }

   public boolean isPrestado() { return prestado; }
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
}

public class Usuario {
    private String nombre;
    private List<Libro> librosPrestados;

   public Usuario(String nombre) {
        this.nombre = nombre;
        this.librosPrestados = new ArrayList<>();
    }

   public void prestarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro es obligatorio");
        }
        libro.prestar();
        this.librosPrestados.add(libro);
    }

   public void devolverLibro(Libro libro) {
        if (!this.librosPrestados.contains(libro)) {
            throw new IllegalStateException("El usuario no tiene este libro prestado");
        }
        libro.devolver();
        this.librosPrestados.remove(libro);
    }

   public String getNombre() { return nombre; }
    public List<Libro> getLibrosPrestados() { return librosPrestados; }
}

Donde pondria la logica y por que:
La validacion de que no se pueda prestar un libro que ya esta prestado la meti directamente dentro de la clase Libro en el metodo prestar() en vez de dejar un setPrestado abierto y hacer el if por fuera. Lo hago asi por encapsulamiento ya que la clase Libro es la dueña del atributo prestado y ella misma debe cuidar su estado para evitar que desde otra clase lo presten dos veces por error. Ya el metodo prestarLibro en Usuario solo llama a libro.prestar() y si todo sale bien lo agrega a la lista del usuario.