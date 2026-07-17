package com.fullcycle.admin.catalogo.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Classe de configuracao para gerenciamento dos beans com o package padrao
// Os beans sao injetados por essa configuracao
@Configuration
@ComponentScan("com.fullcycle.admin.catalogo")
public class WebServerConfig {
}