package com.nikitahotel.nikitahotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@SpringBootApplication
public class NikitahotelApplication {
	private static final Logger log = LoggerFactory.getLogger(NikitahotelApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NikitahotelApplication.class, args);
	}

	/*
	 * @Bean public ClassLoaderTemplateResolver thymeleafTemplateResolver() {
	 * ClassLoaderTemplateResolver templateResolver = new
	 * ClassLoaderTemplateResolver();
	 * templateResolver.setPrefix("/email-templates/"); //templateResolver.set
	 * templateResolver.setSuffix(".html");
	 * templateResolver.setTemplateMode("HTML");
	 * templateResolver.setCharacterEncoding("UTF-8"); return templateResolver; }
	 * 
	 * @Bean public SpringTemplateEngine templateEngine() { SpringTemplateEngine
	 * templateEngine = new SpringTemplateEngine();
	 * templateEngine.setTemplateResolver(thymeleafTemplateResolver()); return
	 * templateEngine; }
	 */

	/*
	 * @Bean public ThymeleafViewResolver thymeleafViewResolver() {
	 * ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
	 * thymeleafViewResolver.setTemplateEngine(templateEngine()); return
	 * thymeleafViewResolver; }
	 */
	

}
