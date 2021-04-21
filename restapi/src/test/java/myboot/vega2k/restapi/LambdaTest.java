package myboot.vega2k.restapi;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

public class LambdaTest {

	@Test
	public void iterable() {
		List<String> myList = List.of("aa","bb","cc");
		
		//Consumer를 Annoymous Inner Class로 만들어서 accept메서드를 재정의하기
		myList.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);				
			}
		});
		
		//Consumer를 람다식으로 만들어서 재정의하기
		myList.forEach(val -> System.out.println(val + " => "));
		
		//Consumer를 Method Reference로 만들어서 재정의 하기
		myList.forEach(System.out::println);
		
	}
}
