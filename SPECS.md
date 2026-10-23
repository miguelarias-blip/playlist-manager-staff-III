# Especificación Técnica — Gestor de Listas de Reproducción

## 1. Backend: API REST (`/lists`)

Implementar utilizando **Maven**, **JPA**, y las bibliotecas Java de elección un conjunto de APIs cuyo contrato se detalla a continuación:

### Contrato de Endpoints HTTP

| Método HTTP | Plantilla URI | Descripción y Reglas de Respuesta |
| :--- | :--- | :--- |
| **POST** | `/lists` | Añadir una nueva lista de reproducción.<br>- Si se añade satisfactoriamente, devuelve **`201 Created`** con la referencia a la URI (`Location` header) y el contenido de la lista creada.<br>- Si el nombre de la lista no es válido (ej: `null` o vacío) debe devolver un error **`400 Bad Request`**. |
| **GET** | `/lists` | Ver todas las listas de reproducción existentes (`200 OK`). |
| **GET** | `/lists/{listName}` | Ver descripción / detalle de una lista de reproducción seleccionada (`200 OK`).<br>- Si la lista no existe, debe devolver un **`404 Not Found`**. |
| **DELETE** | `/lists/{listName}` | Borrar una lista de reproducción.<br>- Si se realiza correctamente, debe devolver un **`204 No Content`**.<br>- Si la lista no existe, debe devolver un **`404 Not Found`**. |

---

### Representación JSON de los Recursos

#### Recurso: Lista de Reproducción
Una lista de reproducción tiene `nombre`, `descripcion` y un conjunto de `canciones`:

```json
{
  "nombre": "Lista 1",
  "descripcion": "Lista de canciones de Spotify",
  "canciones": [
    {
      "titulo": "",
      "artista": "",
      "album": "",
      "anno": "",
      "genero": ""
    },
    {
      "titulo": "",
      "artista": "",
      "album": "",
      "anno": "",
      "genero": ""
    }
  ]
}
```

#### Recurso: Canción
Cada canción tiene un `titulo`, nombre del `artista`, `album`, `anno` (año) y `genero`:

```json
{
  "titulo": "",
  "artista": "",
  "album": "",
  "anno": "",
  "genero": ""
}
```

---

### Requerimientos No Funcionales y Técnicos (Backend)
1. **Seguridad:** Implementar autenticación y autorización.
2. **Persistencia:** Implementar una capa de persistencia en una base de datos *runtime* en memoria (**H2**).
3. **Cliente REST:** Probar la aplicación usando un cliente REST (ej. Postman) y entregar el archivo de colección/cliente configurado en el repositorio.
4. **Testing:** Implementar pruebas unitarias.

---

## 2. Frontend: Aplicación Web

Implementar un proyecto Web (**Angular**) en el cual se visualicen formularios y vistas que permitan interactuar con todas las operaciones de las APIs creadas en el punto anterior (`POST /lists`, `GET /lists`, `GET /lists/{listName}`, `DELETE /lists/{listName}`), incluyendo la integración con el esquema de autenticación configurado.