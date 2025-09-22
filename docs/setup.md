# Instalación y configuración

## Requisitos

- **JDK 8 o superior** (las pruebas se realizaron con OpenJDK 17).
- Acceso a Internet para comunicarte con https://www.dateas.com.
- Git si vas a clonar el repositorio.
- (Opcional) Maven para empaquetar el proyecto o gestionar dependencias.

## Instalación de Java en macOS

```bash
brew install openjdk@17
# Añadí el binario real a tu PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

En otros sistemas podés usar [Temurin](https://adoptium.net) o la distribución de OpenJDK que prefieras. Verificá la instalación con:

```bash
javac -version
java -version
```

## Clonado y compilación

```bash
git clone https://github.com/juato4/constanciaCuil.git
cd constanciaCuil
javac -d target/classes $(find src -name '*.java')
```

Los JAR de RxJava ya no son necesarios, salvo que quieras reutilizar el método demostrativo `lasuscr()`.

## Variables y configuración

No se requiere configuración adicional. La clase `Modelo.trabajo` utiliza un `User-Agent` genérico y realiza solicitudes HTTP directas. Si necesitás pasar por un proxy, podés establecer las propiedades estándar de Java:

```bash
export JAVA_TOOL_OPTIONS="-Dhttp.proxyHost=proxy.local -Dhttp.proxyPort=8080 -Dhttps.proxyHost=proxy.local -Dhttps.proxyPort=8080"
```

## Tareas opcionales

- **Build con Maven**: el `pom.xml` es mínimo. Podés ejecutar `mvn package` una vez que tengas Maven disponible.
- **Empaquetar un JAR ejecutable**: genera un `MANIFEST.MF` apuntando a `consultaCuil.CuilCli` y usa `jar --create --file constanciacuil.jar -C target/classes .`.
