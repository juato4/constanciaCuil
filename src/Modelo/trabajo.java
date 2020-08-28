package Modelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class trabajo {
	
	private String cuil="",lineaQueQuiero, nombre="";
    private URL url;
    private URLConnection uc;
    private StringBuilder parsedContentFromUrl;
    private String urlString;
    private int nroLinia;
    public trabajo() {
    	parsedContentFromUrl= new StringBuilder();
    	urlString ="https://www.dateas.com/es/consulta_cuit_cuil?name=&cuit=";
    	nroLinia=0;
	}
    
    AtomicInteger counter = new AtomicInteger();
    Callable<Integer> callable = () -> counter.incrementAndGet();

    
    public Disposable lasuscr() {
    	
    	Disposable d = Observable.just("Hello world!")
    		     .delay(1, TimeUnit.SECONDS)
    		     .subscribeWith(new DisposableObserver<String>() {
    		         @Override public void onStart() {
    		             System.out.println("Start!");
    		         }
    		         @Override public void onNext(String t) {
    		             System.out.println(t);
    		         }
    		         @Override public void onError(Throwable t) {
    		             t.printStackTrace();
    		         }
    		         @Override public void onComplete() {
    		             System.out.println("Done!");
    		         }
    		     });

    		 
    	
    	//return Single.just("Hello"); 
    		 return d;
    }
    
    
    public void bucar(String numero) {
    	
    	
    	
    	try {
			url = new URL(urlString+numero);
			uc = url.openConnection();
			uc.connect();
			uc = url.openConnection();
			uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			uc.getInputStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		      
		      while ((in.readLine()) != null) {
		    	  
		    	  if(nroLinia==445) {
		    		lineaQueQuiero=in.readLine();			    		
		    	  }
		         nroLinia++;
		      }
		      
		      lineaQueQuiero = eliminarTags(lineaQueQuiero);
		      
		      recuperarNombre(lineaQueQuiero);	
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
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
