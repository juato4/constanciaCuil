package consultaCuil;

import java.util.ArrayList;
import java.util.List;

import Modelo.trabajo;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

public class asffas {

	public static void main(String[] args) {
		Flowable.just("Hello world").subscribe(System.out::println);
		
		trabajo t = new trabajo();
		
		t.lasuscr().subscribe(System.out::println);
		
		
	}

	
}
