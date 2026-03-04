package com.reclamos.dao;

import com.reclamos.entities.Usuario;
import com.reclamos.utils.EMFUtil;

import jakarta.persistence.EntityManager;

public class UsuarioDAO {

  public Usuario save(Usuario usuario) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      if (usuario.getId() == null) {
        em.persist(usuario);
      } else {
        usuario = em.merge(usuario);
      }
      em.getTransaction().commit();
      return usuario;
    } finally {
      em.close();
    }
  }

  public Usuario findByUsername(String username) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      String query = """
        SELECT u 
        FROM Usuario u
        WHERE u.username = :username
      """;

      return em.createQuery(query, Usuario.class)
        .setParameter("username", username)
        .getSingleResult();
    } finally {
      em.close();
    }
  }

}
