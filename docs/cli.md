# Uso del CLI

El proyecto incluye `consultaCuil.CuilCli`, una aplicación de línea de comandos que consulta Dateas y devuelve los resultados en JSON.

## Compilación

```bash
javac -d target/classes $(find src -name '*.java')
```

## Consultar por DNI/CUIL

```bash
java -cp target/classes consultaCuil.CuilCli --dni 27185953144
```

Respuesta típica:

```json
{"status":"ok","persona":{"nombre":"SABBAG LAURA GRISELDA","cuil":"27-18595314-4","dni":"18595314","edad":"57 años","provincia":"Ciudad Autónoma de Buenos Aires","localidad":"","detalleUrl":"https://www.dateas.com/es/persona/laura-griselda-sabbag-27185953144","detalleNombre":"SABBAG LAURA GRISELDA","detalleFecha":""}}
```

Los campos `detalleNombre` y `detalleFecha` sólo se completan si Dateas expone esa información en la ficha individual.

## Consultar por nombre

```bash
java -cp target/classes consultaCuil.CuilCli --name "yataco" --maxPages 3
```

- `--name` define la cadena a buscar.
- `--maxPages` es opcional; por defecto se revisan 5 páginas.

La salida contiene un array `personas` con los mismos campos disponibles para el caso puntual.

## Códigos de salida

- `0`: ejecución exitosa.
- `1`: parámetros inválidos (se imprime `Uso: ...`).
- `2`: no se encontraron resultados para el DNI.
- `3`: error inesperado (por ejemplo, fallo de red).

## Integración

El JSON se emite en `stdout`. Podés consumirlo desde scripts bash, Node.js, Python, etc. Ejemplo con `jq`:

```bash
java -cp target/classes consultaCuil.CuilCli --dni 27185953144 | jq '.persona.nombre'
```
