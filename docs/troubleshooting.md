# Solución de problemas

## `javac: Unable to locate a Java Runtime`

Instalá una JDK real. En macOS lo más simple es `brew install openjdk@17` y agregar `/opt/homebrew/opt/openjdk@17/bin` a tu `PATH`. Confirmá con `javac -version`.

## Resultado vacío al buscar por nombre

- Confirmá manualmente la URL: `https://www.dateas.com/es/consulta_cuit_cuil?name=<consulta>`.
- Puede que Dateas no tenga coincidencias o que se requiera tilde/variante distinta.
- Aumentá `maxPaginas` si el resultado aparece lejos en el listado.

## `detalleNombre` / `detalleFecha` vacíos

- Dateas no siempre muestra esa información.
- Algunos perfiles requieren sesión o pago para ver datos completos.
- El método `buscarPorDni` hace una segunda solicitud; si se bloquea por rate limiting, los campos quedan vacíos.

## Bloqueo por demasiadas solicitudes

- Implementá retardo entre consultas (por ejemplo, `Thread.sleep(1000)` después de cada llamada).
- Usá cache local o persistent storage.
- Considerá un proxy si tu IP es rate-limitada, respetando los términos del servicio.

## Incompatibilidades con la UI Swing

- `bucar(String numero)` mantiene la API original, pero ahora llena campos adicionales. Si tu UI esperaba valores en blanco, revisá el binding.
- La UI no es asíncrona; si la consulta tarda, la ventana se congelará. Considerá ejecutar el trabajo en un hilo separado.

## Ajustes de patrones

Si Dateas cambia el HTML:

1. Ejecutá `curl` sobre la página y guardá el HTML.
2. Actualizá las constantes `NOMBRE_PATTERN`, `CUIL_PATTERN`, etc., en `Modelo.trabajo`.
3. Añadí tests locales que verifiquen el parseo con el nuevo HTML estático.
