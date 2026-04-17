package com.movie.reservation.movie_service.config;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class HateoasLinkBuilder<T> {
    
    public EntityModel<T> buildLinks(T entity, String basePath, Long id, boolean isAdmin) {
        // Creamos el modelo. Importante: EntityModel.of() es estático.
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        EntityModel<T> model = EntityModel.of(entity);
        
        // USAR .add() en lugar de .link()
        model.add(Link.of(basePath + "/" + id).withSelfRel());
        
        // Generar dinámicamente el nombre de la relación para la lista
        String resourceName = basePath.substring(basePath.lastIndexOf("/") + 1);
        model.add(Link.of(basePath).withRel("all-" + resourceName));
        
        // Links de escritura solo para ADMIN
        if (isAdmin) {
            model.add(Link.of(basePath + "/" + id)
                .withRel("update")
                .withType("PUT"));
                
            model.add(Link.of(basePath + "/" + id)
                .withRel("delete")
                .withType("DELETE"));
        }
        
        return model;
    }
}

