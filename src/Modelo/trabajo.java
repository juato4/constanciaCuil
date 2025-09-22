package Modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class trabajo {

	private static final String BASE_HOST = "https://www.dateas.com";
	private static final String BUSQUEDA_POR_CUIL = BASE_HOST + "/es/consulta_cuit_cuil?name=&cuit=";
	private static final String BUSQUEDA_BASE = BASE_HOST + "/es/consulta_cuit_cuil";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
	private static final int DEFAULT_MAX_PAGES = 10;
	private static final Pattern NOMBRE_PATTERN = Pattern.compile("data-label=\\\"Nombre/Raz[^>]*\\\"><a href=\\\"([^\\\"]+)\\\">([^<]+)</a>");
	private static final Pattern CUIL_PATTERN = Pattern.compile("data-label=\\\"CUIT / CUIL / CDI\\\"><a href=\\\"([^\\\"]+)\\\">([^<]+)</a>");
	private static final Pattern EDAD_PATTERN = Pattern.compile("data-label=\\\"Edad\\\">([^<]*)</td>");
	private static final Pattern PROVINCIA_PATTERN = Pattern.compile("data-label=\\\"Provincia\\\">([^<]*)</td>");
	private static final Pattern LOCALIDAD_PATTERN = Pattern.compile("data-label=\\\"Localidad\\\">([^<]*)</td>");

	private String cuil = "";
	private String nombre = "";
	private String edad = "";
	private String provincia = "";
	private String localidad = "";
	private String detalleUrl = "";

	public trabajo() {}

	public void bucar(String numero) {
		PersonaInfo info = buscarPorDni(numero);
		if (info != null) {
			this.nombre = info.getNombre();
			this.cuil = info.getCuil();
			this.edad = info.getEdad();
			this.provincia = info.getProvincia();
			this.localidad = info.getLocalidad();
			this.detalleUrl = info.getDetalleUrl();
		} else {
			this.nombre = "";
			this.cuil = "";
			this.edad = "";
			this.provincia = "";
			this.localidad = "";
			this.detalleUrl = "";
		}
	}

	public PersonaInfo buscarPorDni(String dni) {
		String targetUrl = BUSQUEDA_POR_CUIL + dni;
		try {
			List<PersonaInfo> resultados = leerPersonas(targetUrl);
			return resultados.isEmpty() ? null : resultados.get(0);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<PersonaInfo> buscarPorNombre(String consulta) {
		return buscarPorNombre(consulta, 0, DEFAULT_MAX_PAGES);
	}

	public List<PersonaInfo> buscarPorNombre(String consulta, int pagina) {
		String encoded = encode(consulta);
		String targetUrl = BUSQUEDA_BASE + "?page=" + pagina + "&name=" + encoded;
		try {
			return leerPersonas(targetUrl);
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<PersonaInfo> buscarPorNombre(String consulta, int paginaInicial, int maxPaginas) {
		List<PersonaInfo> acumulado = new ArrayList<>();
		if (maxPaginas < 1) {
			return acumulado;
		}
		int pagina = paginaInicial;
		int restantes = maxPaginas;
		while (restantes > 0) {
			List<PersonaInfo> paginaActual = buscarPorNombre(consulta, pagina);
			if (paginaActual.isEmpty()) {
				break;
			}
			acumulado.addAll(paginaActual);
			pagina++;
			restantes--;
		}
		return acumulado;
	}

	public PersonaInfo buscarPorNombreExacto(String consulta, String nombreExacto, int maxPaginas) {
		List<PersonaInfo> personas = buscarPorNombre(consulta, 0, maxPaginas);
		for (PersonaInfo persona : personas) {
			if (persona.getNombre().equalsIgnoreCase(nombreExacto)) {
				return persona;
			}
		}
		return null;
	}

	private List<PersonaInfo> leerPersonas(String targetUrl) throws IOException {
		List<PersonaInfo> personas = new ArrayList<>();
		try (BufferedReader in = abrirStream(targetUrl)) {
			String line;
			while ((line = in.readLine()) != null) {
				PersonaInfo persona = parsearPersona(line);
				if (persona != null) {
					personas.add(persona);
				}
			}
		}
		return personas;
	}

	private BufferedReader abrirStream(String targetUrl) throws IOException {
		URL url = new URL(targetUrl);
		URLConnection connection = url.openConnection();
		connection.addRequestProperty("User-Agent", USER_AGENT);
		return new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}

	private PersonaInfo parsearPersona(String linea) {
		if (linea == null || !linea.contains("data-label=\"Nombre/Razón Social\"")) {
			return null;
		}
		Matcher nombreMatcher = NOMBRE_PATTERN.matcher(linea);
		Matcher cuilMatcher = CUIL_PATTERN.matcher(linea);
		if (!nombreMatcher.find() || !cuilMatcher.find()) {
			return null;
		}
		Matcher edadMatcher = EDAD_PATTERN.matcher(linea);
		Matcher provinciaMatcher = PROVINCIA_PATTERN.matcher(linea);
		Matcher localidadMatcher = LOCALIDAD_PATTERN.matcher(linea);

		String detallePath = nombreMatcher.group(1).trim();
		String nombreEncontrado = nombreMatcher.group(2).trim();
		String cuilEncontrado = cuilMatcher.group(2).trim();
		String edadEncontrada = edadMatcher.find() ? edadMatcher.group(1).trim() : "";
		String provinciaEncontrada = provinciaMatcher.find() ? provinciaMatcher.group(1).trim() : "";
		String localidadEncontrada = localidadMatcher.find() ? localidadMatcher.group(1).trim() : "";
		String detalleCompleto = detallePath.startsWith("http") ? detallePath : BASE_HOST + detallePath;

		return new PersonaInfo(nombreEncontrado, cuilEncontrado, edadEncontrada, provinciaEncontrada, localidadEncontrada, detalleCompleto);
	}

	private String encode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			return value;
		}
	}

	public String getNombre() {
		return nombre;
	}

	public String getCuil() {
		return cuil;
	}

	public String getEdad() {
		return edad;
	}

	public String getProvincia() {
		return provincia;
	}

	public String getLocalidad() {
		return localidad;
	}

	public String getDetalleUrl() {
		return detalleUrl;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public void setDetalleUrl(String detalleUrl) {
		this.detalleUrl = detalleUrl;
	}
}
