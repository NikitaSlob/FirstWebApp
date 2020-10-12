package ru.slobodchikov.xmltocsv.XmltoCsv.config;

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
import ru.slobodchikov.xmltocsv.XmltoCsv.XmlToCsv;
import ru.slobodchikov.xmltocsv.XmltoCsv.controllers.CsvFile;
import ru.slobodchikov.xmltocsv.XmltoCsv.controllers.XmlFile;


@Configuration
@ComponentScan("ru.slobodchikov.xmltocsv.XmltoCsv")
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
    public XmlToCsv xmlToCsv() {
        XmlToCsv xmlToCsv=new XmlToCsv();
        return xmlToCsv;
    }
    @Bean
    public CsvFile csvFile() {
        CsvFile csvFile=new CsvFile();
        csvFile.setXmlToCsv(xmlToCsv());
        return csvFile;
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
}
