# Constancia CUIL Scraper

Aplicación Java 8 que consulta los listados públicos de Dateas para obtener información de personas (nombre, CUIL/CUIT, edad, provincia, localidad y URL de detalle). El módulo principal (`Modelo.trabajo`) expone métodos pensados para integrarse con otros proyectos que requieran automatizar búsqueda por DNI o por nombre.

> ¿Buscás más detalles? Consultá la [wiki del proyecto](docs/README.md) para guías paso a paso, referencia de API y notas de resolución de problemas.

## Requisitos

- JDK 8 o superior (probado con OpenJDK 17)
- Conexión a internet para acceder a https://www.dateas.com
- Maven opcional si querés empaquetar el proyecto; también podés compilar con `javac`
- Si no tenés Java instalado en macOS, podés usar `brew install openjdk@17` y añadir `/opt/homebrew/opt/openjdk@17/bin` a tu `PATH`

## Construcción rápida

Compilación sin Maven:

```bash
javac -d target/classes $(find src -name '*.java')
```

Para ejecutar un ejemplo rápido:

```bash
cat <<'JAVA' > TestRunner.java
import Modelo.PersonaInfo;
import Modelo.trabajo;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        trabajo servicio = new trabajo();
        PersonaInfo personaPorDni = servicio.buscarPorDni("27185953144");
        System.out.println(personaPorDni.getNombre());
        System.out.println(personaPorDni.getCuil());
        System.out.println(personaPorDni.getEdad());

        List<PersonaInfo> coincidencias = servicio.buscarPorNombre("daiana yataco", 0, 1);
        for (PersonaInfo persona : coincidencias) {
            System.out.println(persona.getNombre() + " -> " + persona.getDni());
        }
    }
}
JAVA

javac -cp target/classes TestRunner.java
java -cp target/classes TestRunner
```

## API expuesta

La clase `Modelo.trabajo` ahora incluye:

- `PersonaInfo buscarPorDni(String dni)`: devuelve la primera coincidencia para un DNI/CUIL específico.
- `List<PersonaInfo> buscarPorNombre(String consulta)`: recorre hasta 10 páginas (por defecto) y acumula todas las coincidencias por nombre.
- `List<PersonaInfo> buscarPorNombre(String consulta, int pagina, int maxPaginas)`: control fino sobre la paginación.
- `PersonaInfo buscarPorNombreExacto(String consulta, String nombreExacto, int maxPaginas)`: busca una coincidencia exacta dentro de los resultados paginados.
- `void bucar(String numero)`: mantiene compatibilidad con la UI Swing original; populariza los campos internos para `getNombre()`, `getCuil()`, etc.

Cada `PersonaInfo` ofrece:

- `getNombre()`, `getCuil()`, `getEdad()`, `getProvincia()`, `getLocalidad()`, `getDetalleUrl()`
- `getDetalleNombre()` y `getDetalleFecha()`: cuando la página de detalle está disponible, se devuelve el nombre normalizado y la fecha de nacimiento expuesta (por defecto sólo se completa en búsquedas por DNI).
- `getDni()`: extrae el DNI a partir del CUIL (posición 3 a 10 de los dígitos)

## CLI incluido

Desde `src/consultaCuil/CuilCli.java` podés ejecutar un CLI sencillo para consumir los resultados en formato JSON:

```bash
javac -d target/classes $(find src -name '*.java')
java -cp target/classes consultaCuil.CuilCli --dni 27185953144

# o por nombre, con paginación opcional
java -cp target/classes consultaCuil.CuilCli --name "yataco" --maxPages 3
```

El CLI emite un objeto JSON con `status` y, según el caso, `persona`, `personas` o `message` para facilitar la integración con otros servicios.

## Consideraciones

- Dateas puede cambiar el HTML en cualquier momento; si la estructura deja de coincidir, ajustá los patrones regulares en `Modelo.trabajo`.
- El scraping de Dateas está sujeto a sus términos de servicio; usalo de forma responsable.
- La información de detalle requiere una segunda solicitud HTTP; si la página está protegida o se aplica rate limiting, esos campos se devolverán en blanco.

## Integración con otros proyectos

1. Incluí `src/Modelo/PersonaInfo.java` y `src/Modelo/trabajo.java` en tu proyecto o genera un JAR.
2. Instanciá `trabajo` y llamá a los métodos de búsqueda; recibirás `PersonaInfo` listo para serializar o mapear según tus necesidades.
3. El método `getDni()` te permite trabajar directamente con DNI sin recalcularlo.

Ejemplo de integración mínima:

```java
trabajo servicio = new trabajo();
PersonaInfo resultado = servicio.buscarPorNombreExacto("YATACO", "YATACO SARAVIA DIANA CIELO", 6);
if (resultado != null) {
    System.out.println(resultado.getNombre());
    System.out.println(resultado.getDni());
    System.out.println(resultado.getEdad());
}
```

## Licencia

Este repositorio reutiliza contenidos disponibles públicamente en Dateas. Consultá al autor original para obtener información sobre la licencia del código.
