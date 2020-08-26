package Modelo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class trabajo {
	
	public String nombre="";
    public String cuil = "";
    
    public void bucar(String numero) {
    	try {
			
			URL url;
			URLConnection uc;
			StringBuilder parsedContentFromUrl = new StringBuilder();
			String urlString="https://www.dateas.com/es/consulta_cuit_cuil?name=&cuit=" + numero;
			System.out.println("Getting content for URl : " + urlString);
			url = new URL(urlString);
			uc = url.openConnection();
			uc.connect();
			uc = url.openConnection();
			uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			uc.getInputStream();
			//BufferedReader in = new BufferedReader(uc.getInputStream());
			
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			
			String linea, lineaQueQuiero = null;
		      
		      int nroLinia=0;
		      
		      while ((linea = in.readLine()) != null) {
		    	  
		    	  if(nroLinia==445) {
		    		lineaQueQuiero=in.readLine();			    		
		    	  }
		         nroLinia++;
		      }
		      
		      lineaQueQuiero = eliminarTags(lineaQueQuiero);
		      
		      recuperarNombre(lineaQueQuiero);
		      
		      System.out.println(nombre);
		      System.out.println(cuil);
		   
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		    
			/*try {
				URL url = new URL("https://www.dateas.com/es/consulta_cuit_cuil?name=&cuit=30068534");
				
				URLConnection con = new URLConnection(url) {
					
					@Override
					public void connect() throws IOException {
						
						addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
					}
				};
				//URLConnection con;
				
				con = url.openConnection();

			
			      BufferedReader in = new BufferedReader(
			         new InputStreamReader(con.getInputStream()));
			     
			 
			      String linea, lineaQueQuiero = null;
			      
			      int nroLinia=0;
			      
			      while ((linea = in.readLine()) != null) {
			    	  
			    	  if(nroLinia==445) {
			    		lineaQueQuiero=in.readLine();			    		
			    	  }
			         nroLinia++;
			      }
			      
			      lineaQueQuiero = eliminarTags(lineaQueQuiero);
			      
			      recuperarNombre(lineaQueQuiero);
			      
			      System.out.println(nombre);
			      System.out.println(cuil);
		      
			
			} catch (Exception e) {

				e.printStackTrace();
			}*/
    }
	
	public void recuperarNombre(String lineaQueQuiero) {

		int indice= 0;
				
		lineaQueQuiero = lineaQueQuiero.substring(3,lineaQueQuiero.length()-15);
		
		int cantidadCaracteres = lineaQueQuiero.length();

		
		for (int i = 0; i < cantidadCaracteres; i++) {
			
			if ((lineaQueQuiero.charAt(i) == '0') ||
					(lineaQueQuiero.charAt(i) == '1') ||
					(lineaQueQuiero.charAt(i) == '2') ||
					(lineaQueQuiero.charAt(i) == '3') ||
					(lineaQueQuiero.charAt(i) == '4') ||
					(lineaQueQuiero.charAt(i) == '5') ||
					(lineaQueQuiero.charAt(i) == '6') ||
					(lineaQueQuiero.charAt(i) == '7') ||
					(lineaQueQuiero.charAt(i) == '8') ||
					(lineaQueQuiero.charAt(i) == '9') ||
					(lineaQueQuiero.charAt(i) == '-')) {
						
						cuil = cuil + lineaQueQuiero.charAt(i);
				
					}else {
						nombre = nombre + lineaQueQuiero.charAt(i);
					}
			
			indice++;
		}
		
		nombre = nombre.substring(0,nombre.length()-5);
	
	}
		
	private  String eliminarTags(String cadena){
	    while(true){
	      int izdaTag= cadena.indexOf('<');
	      if (izdaTag < 0 ) return cadena;
	      int derTag = cadena.indexOf('>',izdaTag);
	      if (derTag < 0) return cadena;
	      cadena= cadena.substring(0,izdaTag)+" "+ cadena.substring(derTag+1);
	    }
	  }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCuil() {
		return cuil;
	}

	public void setCuil(String cuil) {
		this.cuil = cuil;
	}
	
	

}
