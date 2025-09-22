# API Java

La clase `Modelo.trabajo` es el punto de entrada para realizar búsquedas. Internamente utiliza `HttpURLConnection`, expresiones regulares y la clase `PersonaInfo` para encapsular resultados.

## Crear la instancia

```java
trabajo servicio = new trabajo();
```

La clase no mantiene estado entre búsquedas más allá de los últimos valores accesibles vía `getNombre()`, `getCuil()`, etc., para la antigua UI Swing.

## Búsquedas disponibles

### `PersonaInfo buscarPorDni(String dni)`

- Realiza una consulta directa a `https://www.dateas.com/es/consulta_cuit_cuil?name=&cuit=<dni>`.
- Devuelve la primera coincidencia o `null` si no se encuentra.
- Después de obtener la fila principal, solicita la ficha individual y completa `detalleNombre` y `detalleFecha` si están disponibles.

### `List<PersonaInfo> buscarPorNombre(String consulta)`

- Busca múltiples coincidencias paginadas (hasta 10 páginas por defecto).
- Utiliza `buscarPorNombre(String consulta, int paginaInicial, int maxPaginas)` internamente.

### `List<PersonaInfo> buscarPorNombre(String consulta, int pagina, int maxPaginas)`

- Permite controlar la página inicial y el número máximo de páginas a recorrer.
- Cada página se detiene si no hay resultados.

### `PersonaInfo buscarPorNombreExacto(String consulta, String nombreExacto, int maxPaginas)`

- Combina la búsqueda paginada con una comparación `equalsIgnoreCase` sobre el nombre devuelto.
- Útil cuando se busca una persona conocida dentro de un conjunto grande.

### `void bucar(String numero)`

- Método heredado para la UI Swing. Invoca `buscarPorDni` y rellena propiedades internas recuperables por `getNombre()`, `getCuil()`, `getEdad()`, `getProvincia()`, `getLocalidad()` y `getDetalleUrl()`.

## Clase `PersonaInfo`

```java
String nombre = persona.getNombre();
String cuil = persona.getCuil();
String dni = persona.getDni();
String edad = persona.getEdad();
String provincia = persona.getProvincia();
String localidad = persona.getLocalidad();
String detalleUrl = persona.getDetalleUrl();
String detalleNombre = persona.getDetalleNombre();
String detalleFecha = persona.getDetalleFecha();
```

- `getDni()` extrae los 8 dígitos centrales del CUIL.
- `getDetalleNombre()` y `getDetalleFecha()` pueden ser vacíos si Dateas no expone esa información o si la ficha de detalle requiere autenticación.

## Buenas prácticas

- **Respeto del servicio**: evitá disparar muchas consultas en paralelo. Dateas puede aplicar limitaciones.
- **Cacheo**: si consumís el servicio de forma intensiva, almacená respuestas localmente para reducir tráfico.
- **Manejo de errores**: los métodos capturan `IOException` y retornan colecciones vacías o `null`. Controlá esos casos al integrar.
- **Testing**: Dateas es un sitio externo; los tests automáticos deberían usar mocks o fixtures para evitar depender de la red en entornos de CI.
