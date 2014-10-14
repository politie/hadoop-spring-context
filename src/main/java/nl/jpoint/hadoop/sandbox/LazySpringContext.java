package nl.jpoint.hadoop.sandbox;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class LazySpringContext {

	private static ClassPathXmlApplicationContext springContext;

	private static final Logger log = LoggerFactory.getLogger(LazySpringContext.class);

	private static void initContext(String propertiesString) {
		springContext = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"}, false);

		Properties properties = new Properties();
		try {
			properties.load(new StringReader(propertiesString));
		} catch (IOException e) {
			e.printStackTrace();
		}

		PropertySource propertySource = new PropertiesPropertySource("configProperties", properties);
		springContext.getEnvironment().getPropertySources().addFirst(propertySource);

		springContext.refresh();

		log.info("Spring context initialized");
	}

	public static synchronized void autowireBean(Configuration conf, Object bean) {
		String propertiesString = conf.get("nl.jpoint.properties");

		if (springContext == null) {
			initContext(propertiesString);
		}

		springContext.getAutowireCapableBeanFactory().autowireBean(bean);
	}

}