# Visión general

El proyecto **Constancia CUIL Scraper** permite automatizar consultas públicas en [Dateas](https://www.dateas.com) para recuperar información de personas físicas a partir de su CUIL/CUIT o de búsquedas por nombre.

Componentes principales:

- `Modelo.trabajo`: clase de servicio que realiza las búsquedas, normaliza los datos y expone un conjunto de métodos reutilizables.
- `Modelo.PersonaInfo`: DTO inmutable que encapsula la información de cada resultado (nombre, CUIL, DNI, edad, provincia, localidad, URL de detalle y, cuando está disponible, nombre normalizado y fecha de nacimiento).
- `consultaCuil.CuilCli`: interfaz de línea de comandos que emite resultados en JSON para integraciones rápidas.
- UI Swing histórica (`consultaCuil.iniVisual`) que conserva compatibilidad con el método `bucar()` de la clase `trabajo`.

El scraping se basa en expresiones regulares sobre el HTML actual. Si Dateas cambia su estructura, los patrones definidos en `Modelo.trabajo` deberán ajustarse.
