package nl.jpoint.hadoop.sandbox;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldBean {

	@Value("${name}")
	private String name;

	public String getName() {
		return name;
	}
}
