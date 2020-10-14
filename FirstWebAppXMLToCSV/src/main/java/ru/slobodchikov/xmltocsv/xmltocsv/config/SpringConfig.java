package ru.slobodchikov.xmltocsv.xmltocsv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import ru.slobodchikov.xmltocsv.xmltocsv.Metal;
import ru.slobodchikov.xmltocsv.xmltocsv.Metals;
import ru.slobodchikov.xmltocsv.xmltocsv.XmlToCsv;
import ru.slobodchikov.xmltocsv.xmltocsv.controllers.XmlFile;

import java.util.ArrayList;


@Configuration
@ComponentScan("ru.slobodchikov.xmltocsv.xmltocsv")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver=new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("WEB-INF/Views/");
        templateResolver.setSuffix(".html");
        //templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine=new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(200000);
        return multipartResolver;
    }
    @Bean
    public XmlFile xmlFile() {
        XmlFile xmlFile=new XmlFile();
        xmlFile.setXmlToCsv(new XmlToCsv());
        return xmlFile;
    }
    @Bean
    public XmlToCsv xmlToCsv() {
         XmlToCsv xmlToCsv=new XmlToCsv();
         xmlToCsv.setMetals(new Metals());
         return xmlToCsv;
    }
    @Bean
    public Metals metals() {
        Metals metals=new Metals();
        metals.setMetals(new ArrayList<Metal>());
        return metals;
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
}
