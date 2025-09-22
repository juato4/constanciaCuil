package consultaCuil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modelo.PersonaInfo;
import Modelo.trabajo;

public class CuilCli {

    public static void main(String[] args) {
        Map<String, String> flags = parseArgs(args);
        if (flags.isEmpty() || (!flags.containsKey("dni") && !flags.containsKey("name"))) {
            printUsage();
            System.exit(1);
            return;
        }

        trabajo servicio = new trabajo();

        try {
            if (flags.containsKey("dni")) {
                String dni = flags.get("dni");
                PersonaInfo persona = servicio.buscarPorDni(dni);
                if (persona == null) {
                    emitJsonError("not_found", "No se encontró información para el DNI " + dni);
                    System.exit(2);
                } else {
                    emitJsonPersona(persona);
                }
            } else {
                String nombre = flags.get("name");
                int maxPages = flags.containsKey("maxPages") ? safeParseInt(flags.get("maxPages"), 5) : 5;
                List<PersonaInfo> personas = servicio.buscarPorNombre(nombre, 0, Math.max(1, maxPages));
                emitJsonList(personas);
            }
        } catch (Exception ex) {
            emitJsonError("exception", ex.getMessage());
            System.exit(3);
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (!arg.startsWith("--")) {
                continue;
            }
            String key = arg.substring(2);
            if ((i + 1) < args.length && !args[i + 1].startsWith("--")) {
                map.put(key, args[i + 1]);
                i++;
            } else {
                map.put(key, "true");
            }
        }
        return map;
    }

    private static void printUsage() {
        System.out.println("Uso: java consultaCuil.CuilCli --dni <numero> | --name <consulta> [--maxPages N]");
    }

    private static void emitJsonPersona(PersonaInfo persona) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"status\":\"ok\",\"persona\":{");
        appendJsonField(sb, "nombre", persona.getNombre());
        sb.append(',');
        appendJsonField(sb, "cuil", persona.getCuil());
        sb.append(',');
        appendJsonField(sb, "dni", persona.getDni());
        sb.append(',');
        appendJsonField(sb, "edad", persona.getEdad());
        sb.append(',');
        appendJsonField(sb, "provincia", persona.getProvincia());
        sb.append(',');
        appendJsonField(sb, "localidad", persona.getLocalidad());
        sb.append(',');
        appendJsonField(sb, "detalleUrl", persona.getDetalleUrl());
        sb.append(',');
        appendJsonField(sb, "detalleNombre", persona.getDetalleNombre());
        sb.append(',');
        appendJsonField(sb, "detalleFecha", persona.getDetalleFecha());
        sb.append("}}");
        sb.append('}');
        System.out.println(sb.toString());
    }

    private static void emitJsonList(List<PersonaInfo> personas) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"status\":\"ok\",\"personas\":[");
        for (int i = 0; i < personas.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append('{');
            PersonaInfo persona = personas.get(i);
            appendJsonField(sb, "nombre", persona.getNombre());
            sb.append(',');
            appendJsonField(sb, "cuil", persona.getCuil());
            sb.append(',');
            appendJsonField(sb, "dni", persona.getDni());
            sb.append(',');
            appendJsonField(sb, "edad", persona.getEdad());
            sb.append(',');
            appendJsonField(sb, "provincia", persona.getProvincia());
            sb.append(',');
            appendJsonField(sb, "localidad", persona.getLocalidad());
            sb.append(',');
            appendJsonField(sb, "detalleUrl", persona.getDetalleUrl());
            sb.append(',');
            appendJsonField(sb, "detalleNombre", persona.getDetalleNombre());
            sb.append(',');
            appendJsonField(sb, "detalleFecha", persona.getDetalleFecha());
            sb.append('}');
        }
        sb.append("]}");
        System.out.println(sb.toString());
    }

    private static void emitJsonError(String code, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        appendJsonField(sb, "status", "error");
        sb.append(',');
        appendJsonField(sb, "code", code);
        sb.append(',');
        appendJsonField(sb, "message", message);
        sb.append('}');
        System.out.println(sb.toString());
    }

    private static void appendJsonField(StringBuilder sb, String key, String value) {
        sb.append('"').append(escapeJson(key)).append('"').append(':');
        sb.append('"').append(escapeJson(value == null ? "" : value)).append('"');
    }

    private static String escapeJson(String input) {
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }

    private static int safeParseInt(String raw, int fallback) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
}
