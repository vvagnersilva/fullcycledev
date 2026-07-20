package com.example.grpcmessagepipeline.infrastructure.persistence;

import jakarta.annotation.PreDestroy;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Sem spring-boot-starter-web nao ha servlet container para o console H2
 * auto-configurado do Spring Boot. O H2 traz seu proprio mini servidor web
 * (independente de Tomcat/Spring MVC), usado aqui para expor o console.
 */
@Configuration
public class H2ConsoleConfig {

    private Server webServer;

    @Bean
    public Server h2WebServer(@Value("${app.h2-console.port:8082}") String port) throws SQLException {
        webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", port).start();
        return webServer;
    }

    @PreDestroy
    public void stop() {
        if (webServer != null) {
            webServer.stop();
        }
    }
}
