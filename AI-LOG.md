# AI-LOG — Bitácora de uso de Inteligencia Artificial

> **Instrucciones:** Completa esta bitácora mientras resuelves el Módulo de IA. Es un entregable obligatorio. Sé honesto y concreto. Copia los prompts tal cual los usaste. No hay respuestas "incorrectas" por usar IA — lo que evaluamos es tu criterio.

**Candidato:** Miguel Ángel Arias Castaño
**Nivel:** ☐ Staff I/II  ☑ Staff III  ☐ Senior
**Fecha:** 23/10/2026
**Asistente(s) de IA usado(s):** Innti (Kilo Code en VS Code)

---

## Ejercicio 1

**Prompt(s) usado(s):**
```
Excelente trabajo con las Fases 8 y 9. Antes de redactar el README.md final (Fase 10), vamos a resolver un ejercicio de refactorización orientado a objetos en una carpeta dedicada /ejercicio-1-refactor en la raíz del repositorio.

Tenemos esta clase Java legado con mal diseño (todo en main, uso de primitivos/strings concatenados con guiones sin encapsulamiento y mezcla de responsabilidades):

[...código original de Equipo.java...]

Por favor, propón e implementa la refactorización de este código en /ejercicio-1-refactor aplicando Principios SOLID, POO (clases Equipo, Jugador, separación de responsabilidades e inmutabilidad) y garantizando exactamente la misma salida por consola.
```

Segundo prompt para corregir lo que hizo:
```
He revisado tu propuesta para /ejercicio-1-refactor. Acepto la separación de responsabilidades con EquipoReporte, el enum Posicion para eliminar el split("-") sobre strings mágicos y el uso de copias defensivas inmutables (List.copyOf).

Sin embargo, rechazo dos decisiones de tu implementación que debemos corregir:

1. Falta de invariantes de dominio y uso de record sin validación para Equipo y Jugador:
   - Tu implementación actual permite instanciar un Equipo con titulos < 0, nombre nulo/vacío, o un Jugador con nombre en blanco o posicion == null (lo que causaría un NullPointerException al invocar jugador.posicion().getEtiqueta() en EquipoReporte).
   - Convierte Equipo en una clase de dominio POO encapsulada (final class Equipo con atributos private final, getters y métodos de dominio) que valide en su constructor: nombre y estadio no nulos ni en blanco, titulos >= 0, y listas titulares/suplentes no nulas (aplicando List.copyOf).
   - En Jugador, mantén el record pero agrega un constructor compacto que valide que nombre no sea nulo/blanco y posicion no sea null (Objects.requireNonNull).
2. Retrocompatibilidad del punto de entrada (main):
   - El código original se ejecutaba desde Equipo.main(String[] args). Para garantizar el mismo comportamiento observable tanto si el evaluador ejecuta java Equipo como java Main, conserva Main.java pero incluye también el método public static void main(String[] args) en Equipo delegando a la misma ejecución.

Aplica estas correcciones en /ejercicio-1-refactor.
```

**Respuesta / código relevante de la IA:**
```java
// Primera versión que me dio la IA:
public record Jugador(String nombre, Posicion posicion) {}

public record Equipo(
        String nombre,
        String estadio,
        int titulos,
        List<Jugador> titulares,
        List<Jugador> suplentes
) {
    public Equipo {
        titulares = List.copyOf(titulares);
        suplentes = List.copyOf(suplentes);
    }

    public int totalJugadores() {
        return titulares.size() + suplentes.size();
    }
}

// Versión final después de pedirle las correcciones:
public final class Equipo {
    private final String nombre;
    private final String estadio;
    private final int titulos;
    private final List<Jugador> titulares;
    private final List<Jugador> suplentes;

    public Equipo(String nombre, String estadio, int titulos, List<Jugador> titulares, List<Jugador> suplentes) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacío");
        }
        if (estadio == null || estadio.isBlank()) {
            throw new IllegalArgumentException("El estadio no puede estar vacío");
        }
        if (titulos < 0) {
            throw new IllegalArgumentException("Los títulos no pueden ser negativos");
        }
        this.nombre = nombre;
        this.estadio = estadio;
        this.titulos = titulos;
        this.titulares = List.copyOf(Objects.requireNonNull(titulares));
        this.suplentes = List.copyOf(Objects.requireNonNull(suplentes));
    }
    // getters, totalJugadores() y main()
}
```

**¿Qué acepté y por qué?**
- Acepté que creara el enum Posicion porque en el código original tenían los datos quemados en un string separado por guion como Juan-Delantero y tocaba hacer split cada vez que se recorría el arreglo. Con el enum ya queda tipado y no se presta para errores de escritura.
- También acepté sacar la lógica que arma el texto del reporte a una clase aparte llamada EquipoReporte, así la clase Equipo solo maneja sus datos y no tiene System.out.println mezclados adentro.
- Me pareció bien que usara List.copyOf para guardar las listas de titulares y suplentes porque así nadie puede modificar la lista desde afuera después de crear el equipo.

**¿Qué rechacé o corregí y por qué?**
- Rechacé que dejara Equipo y Jugador como records simples sin ninguna validación. Como los dejó al principio uno podía mandar títulos negativos, nombres vacíos o una posición nula en Jugador y eso hacía que el programa explotara con un NullPointerException más adelante al armar el reporte. Le pedí pasar Equipo a una clase normal bien encapsulada con validaciones en el constructor y validar también los datos en Jugador.
- También corregí que había quitado el método main de la clase Equipo para pasarlo a una clase Main nueva. Si alguien prueba el ejercicio corriendo directamente la clase Equipo como venía originalmente no le iba a funcionar, entonces hice que dejara el main también en Equipo para no romper la forma en que se ejecutaba antes.

**Explicación con mis palabras (¿entiendo lo que entrego?):**
- El código inicial tenía todo metido en el main y manejaba a los jugadores como texto plano separado por guiones, lo cual es difícil de mantener si luego piden agregar más datos al jugador. Lo que se hizo fue separar cada cosa en su clase: Jugador con su nombre y su Posicion, Equipo con sus listas protegidas y sus validaciones para que no entren datos malos, y EquipoReporte que es el único que se encarga de armar el texto que sale en consola manteniendo exactamente el mismo resultado.

---

## Ejercicio 2

**Prompt(s) usado(s):**
```
Innti, pasamos a la estrategia de testing (Fase 7 / Ejercicio 2 del Módulo de IA):

1. Genera una clase de pruebas unitarias para ListServiceImpl usando JUnit 5 y Mockito, cubriendo la creación exitosa de una lista (POST) y la obtención por nombre (GET /lists/{listName}).
2. Genera una clase de pruebas para ListController utilizando @WebMvcTest, validando el endpoint POST /lists (incluyendo la correcta verificación del header Location y códigos 201 Created / 400 Bad Request) y el endpoint GET /lists/{listName} (códigos 200 OK y 404 Not Found).

Por favor, implementa estas clases bajo la carpeta src/test/java/com/playlist/manager/...
```

Segundo prompt para completar los casos que faltaban:
```
He auditado las pruebas generadas en ListServiceImplTest y ListControllerTest. Para alcanzar el estándar de cobertura requerido, identifiqué los siguientes escenarios críticos y casos borde que faltan por cubrir:

1. En ListServiceImplTest:
   - create_shouldThrowInvalidListNameWhenBlankOrNull: Validar la defensa de dominio cuando el nombre es nulo o solo espacios.
   - create_shouldHandleNullSongsListGracefully: Enviar canciones = null y verificar que se persiste sin NullPointerException devolviendo lista vacía.
   - getAll_shouldReturnAllLists: Cobertura de GET /lists.
   - delete_shouldRemoveExistingList y delete_shouldThrowNotFoundWhenMissing: Cobertura de DELETE /lists/{listName} (éxito y ListNotFoundException).

2. En ListControllerTest:
   - create_shouldReturn400WhenDuplicateName: Verificar que cuando ListService.create lanza DuplicateListException, el GlobalExceptionHandler responde 400 Bad Request con el JSON de ErrorResponse.
   - getAll_shouldReturn200WithLists: Cobertura del endpoint GET /lists.
   - delete_shouldReturn204WhenExists y delete_shouldReturn404WhenMissing: Cobertura del endpoint DELETE /lists/{listName} (204 No Content y 404 Not Found).
   - getByName_shouldDecodeSpecialCharactersInPath: Verificar GET /lists/Lista%20Con%20Espacios invocando al servicio con "Lista Con Espacios".

3. Nueva clase SecurityIntegrationTest (o test de seguridad con filtros activos):
   - Dado que en ListControllerTest se usó addFilters = false, crea una prueba específica con los filtros de Spring Security activos (importando SecurityConfig y RestAuthenticationEntryPoint) que verifique:
     a) Una petición sin cabecera Authorization a GET /lists o POST /lists devuelve 401 Unauthorized con cuerpo JSON (ErrorResponse).
     b) Una petición con @WithMockUser (o cabecera Basic Auth válida) permite el acceso (200 OK).

Por favor, implementa estos tests adicionales sin eliminar los existentes.
```

**Análisis / casos faltantes / hallazgos:**
- Revisando los primeros tests que armó la IA vi que para no complicarse con Spring Security en el controlador le puso addFilters = false al MockMvc. Eso deja pasando los tests del controlador pero deja toda la parte de autenticación sin probar, o sea que no se estaba validando qué pasa cuando alguien llama la API sin credenciales.
-En el controlador faltaba probar qué respondía la API cuando se intenta crear una lista con un nombre repetido (que debe devolver 400 cuando salta DuplicateListException).
- Tampoco había pruebas para cuando mandan la lista de canciones en null en el request, ni cuando se consulta por GET un nombre de lista que trae espacios codificados en la URL como %20.
- Además faltaban las pruebas de los otros endpoints del servicio y controlador como listar todas las listas y el DELETE (tanto cuando borra bien con 204 como cuando no existe y da 404).

**Lo que implementé o corregí yo mismo:**
- Agregué los casos borde en ListServiceImplTest para validar cuando el nombre viene nulo o lleno de espacios, cuando la lista de canciones llega nula para asegurar que no reviente por NullPointerException, y los tests de listar todo y eliminar.
- En ListControllerTest agregué el test de nombre duplicado validando el JSON de error 400, la prueba de espacios en el path variable del GET y las pruebas de DELETE para 204 y 404.
- Creé aparte la clase SecurityIntegrationTest con los filtros de seguridad prendidos para probar de verdad el 401 Unauthorized cuando no se manda usuario y el 200 OK cuando sí va autenticado.

---

## Ejercicio 3

**Prompt(s) usado(s):**
```
¿Cómo conecto mi API de Spring Boot a la base de datos H2 y cómo configuro las credenciales?
```

**Hallazgos / revisión crítica / riesgos detectados:**
- La IA me dio la configuración típica de application.properties y aunque usó variables de entorno con valores por defecto como ${DB_USER:sa} y ${DB_PASSWORD:}, dejó activada la consola web de H2 directamente con spring.h2.console.enabled=true. Dejar esa consola prendida en la configuración general es peligroso porque si eso se sube a otro ambiente cualquiera podría entrar a la ruta /h2-console.
- Otro problema es usar el usuario sa sin contraseña como valor por defecto. Para correr el proyecto local en la prueba funciona fácil, pero en un entorno real si se olvida configurar la variable de entorno la base de datos queda abierta con el usuario administrador por defecto sin clave.
- También propuso dejar las credenciales de autenticación básica con valores por defecto en el mismo archivo (${BASIC_PASSWORD:playlist}) y usar ddl-auto=update, que en producción no se debería usar porque modifica las tablas automáticamente sin control.

**Decisiones tomadas:**
- En el application.properties final del proyecto quité la habilitación de la consola H2 para que no quede expuesta.
- Mantuve el uso de variables de entorno para la base de datos y para el usuario de la API dejando los valores por defecto solo para que cuando revisen la prueba técnica el proyecto levante de una vez sin que tengan que configurar variables en la máquina, pero en SecurityConfig aseguré que la contraseña pase por BCryptPasswordEncoder en vez de dejarla en texto plano.
- Dejé spring.jpa.open-in-view=false como buena práctica para no dejar sesiones de base de datos abiertas en la capa web y manejar la carga de canciones desde el repositorio con EntityGraph.

---

## Reflexión final (obligatoria)

**¿Dónde falló, alucinó o se equivocó la IA durante esta prueba?**
- En el ejercicio 1 se fue por lo más fácil haciendo records sin validar nada, dejando que se pudieran crear equipos con títulos negativos o datos nulos que luego rompían el reporte, y además me cambió el método main a otra clase sin tener en cuenta cómo se ejecutaba el archivo original.
- En los tests del ejercicio 2 apagó los filtros de seguridad con addFilters = false para que las pruebas del controlador pasaran rápido, pero con eso dejó sin probar toda la seguridad de la API.
- En el ejercicio 3 sugirió prender la consola de H2 directamente en el archivo de propiedades principal.

**¿Qué parte del trabajo NO delegarías a la IA y por qué?**
- No le delegaría la revisión de casos borde ni la parte de seguridad o reglas del negocio. La IA ayuda mucho a escribir rápido la estructura de carpetas, los DTOs, el código repetitivo de Angular o los primeros tests del camino feliz, pero tiende a omitir validaciones importantes o a desactivar cosas como los filtros de seguridad para que todo funcione a la primera. Definir cómo debe comportarse la API cuando entran datos raros y revisar que el código sea seguro es algo que toca verificar uno mismo.

**Autoría:** indica de forma general qué porcentaje/partes fueron asistidas por IA vs. escritas a mano.
- La estructura base del backend y frontend, los mapeos, la colección de Postman y la primera versión de los tests y del refactor fueron generados apoyándome en Innti (alrededor de un 75-80% del código base).
- El planteamiento de cómo organizar el proyecto, las correcciones de validaciones, los casos de prueba que faltaban (seguridad, nulos, duplicados, borrado), la prueba teórica y el análisis de esta bitácora los hice y revisé por mi cuenta (20-25%).