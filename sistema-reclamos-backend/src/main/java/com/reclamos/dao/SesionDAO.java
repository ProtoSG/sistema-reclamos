package com.reclamos.dao;

import com.reclamos.entities.Sesion;
import com.reclamos.utils.EMFUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SesionDAO {

  public Sesion save(Sesion sesion) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      if (sesion.getId() == null) {
        em.persist(sesion);
      } else {
        sesion = em.merge(sesion);
      }
      em.getTransaction().commit();
      return sesion;
    } finally {
      em.close();
    }
  }

  public Optional<Sesion> findByToken(String token) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      String query = """
        SELECT s 
        FROM Sesion s
        JOIN FETCH s.usuario
        WHERE s.token = :token
      """;

      Sesion sesion = em.createQuery(query, Sesion.class)
        .setParameter("token", token)
        .getSingleResult();
      
      return Optional.of(sesion);
    } catch (NoResultException e) {
      return Optional.empty();
    } finally {
      em.close();
    }
  }

  public List<Sesion> findActiveSesionsByUsuarioId(Integer usuarioId) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      String query = """
        SELECT s 
        FROM Sesion s
        WHERE s.usuario.id = :usuarioId
        AND s.activo = true
        AND s.fechaExpiracion > :now
      """;

      return em.createQuery(query, Sesion.class)
        .setParameter("usuarioId", usuarioId)
        .setParameter("now", LocalDateTime.now())
        .getResultList();
    } finally {
      em.close();
    }
  }

  public void invalidateByToken(String token) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      
      String query = """
        UPDATE Sesion s
        SET s.activo = false
        WHERE s.token = :token
      """;

      em.createQuery(query)
        .setParameter("token", token)
        .executeUpdate();
        
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  public void invalidateAllByUsuarioId(Integer usuarioId) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      
      String query = """
        UPDATE Sesion s
        SET s.activo = false
        WHERE s.usuario.id = :usuarioId
      """;

      em.createQuery(query)
        .setParameter("usuarioId", usuarioId)
        .executeUpdate();
        
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  public void deleteExpiredSesiones() {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      
      String query = """
        DELETE FROM Sesion s
        WHERE s.fechaExpiracion < :now
      """;

      em.createQuery(query)
        .setParameter("now", LocalDateTime.now())
        .executeUpdate();
        
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
