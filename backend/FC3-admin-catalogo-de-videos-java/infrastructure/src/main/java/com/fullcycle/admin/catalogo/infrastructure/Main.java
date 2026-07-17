package com.fullcycle.admin.catalogo.infrastructure;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication // Anotacao para subir a aplicacao
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("[step:to-be-init] [id:1] Inicializando o Spring");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
        LOG.info("[step:inittialized] [id:2] Spring inicializado..");
    }

//    @Bean
//    public ApplicationRunner runner(CategoryRepository repository) {
//        return args -> {
//            List<CategoryJpaEntity> all = repository.findAll();
//
//            Category filmes = Category.newCategory("Filmes", null, true);
//
//            repository.saveAndFlush(CategoryJpaEntity.from(filmes));
//
//            repository.deleteAll();
//        };
//    }

}