package com.study.demo.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

	@Override
	public void customize(Connector connector) {
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		protocol.setAcceptCount(2000);
		protocol.setAcceptorThreadCount(2);

		protocol.setMaxThreads(100);
		protocol.setMinSpareThreads(100);
		protocol.setMaxConnections(10000);


		protocol.setConnectionTimeout(20000);

	}


	@Bean
	public ConfigurableServletWebServerFactory  createEmbeddedServletContainerFactory(){
		TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory ();
		tomcatFactory.setPort(8090);
		tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
		return tomcatFactory;
	}
}