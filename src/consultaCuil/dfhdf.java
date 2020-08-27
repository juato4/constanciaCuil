package consultaCuil;




import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class dfhdf {

	public static void main(String[] args) {
		
		 Flowable.fromArray(args).subscribe(new Consumer<String>() {
		      @Override
		      public void accept(String s) {
		          System.out.println("Hello " + s + "!");
		      }
		  });
		
	}
}
