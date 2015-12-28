package org.esf.taxiapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsoftware.elasticactors.Asynchronous;
import org.elasticsoftware.elasticactors.ServiceActor;
import org.elasticsoftware.elasticactors.configuration.BackplaneConfiguration;
import org.elasticsoftware.elasticactors.configuration.ClusteringConfiguration;
import org.elasticsoftware.elasticactors.configuration.MessagingConfiguration;
import org.elasticsoftware.elasticactors.configuration.NodeConfiguration;
import org.elasticsoftware.elasticactors.spring.ActorAnnotationBeanNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.util.Log4jConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author Joost van de Wijgerd
 */
@Configuration
@EnableSpringConfigured
@EnableAsync(annotation = Asynchronous.class, mode = AdviceMode.ASPECTJ)
@EnableMBeanExport
@PropertySource(value = "file:/etc/taxiapp/system.properties")
@ComponentScan(nameGenerator = ActorAnnotationBeanNameGenerator.class,
        includeFilters = {@ComponentScan.Filter(value = {ServiceActor.class}, type = FilterType.ANNOTATION)},
        excludeFilters = {@ComponentScan.Filter(value = {Controller.class}, type = FilterType.ANNOTATION)})
@Import(value = {ClusteringConfiguration.class, NodeConfiguration.class, MessagingConfiguration.class, BackplaneConfiguration.class})
public class AppConfig {
    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() throws IOException {

        Log4jConfigurer.initLogging("/etc/taxiapp/log4j.properties", 30000);
    }

    @PreDestroy
    public void destroy() {
        Log4jConfigurer.shutdownLogging();
    }
}
