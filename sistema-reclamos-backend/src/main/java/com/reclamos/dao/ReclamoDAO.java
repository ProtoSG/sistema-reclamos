package com.reclamos.dao;

import java.util.List;
import java.util.Optional;

import com.reclamos.entities.Reclamo;
import com.reclamos.utils.EMFUtil;

import jakarta.persistence.EntityManager;

public class ReclamoDAO {

  public Reclamo save(Reclamo reclamo) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      if (reclamo.getId() == null) {
        em.persist(reclamo);
      } else {
        reclamo = em.merge(reclamo);
      }
      em.getTransaction().commit();
      return reclamo;
    } finally {
      em.close();
    }
  }

  public List<Reclamo> findAll() {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      String query = """
        SELECT r FROM Reclamo r
      """;

      return em.createQuery(query, Reclamo.class).getResultList();
  } finally {
      em.close();
    }
  }

  public Optional<Reclamo> findById(Integer id) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      Reclamo reclamo = em.find(Reclamo.class, id);
      return Optional.ofNullable(reclamo);
    } finally {
      em.close();
    }
  }

  public Reclamo update(Reclamo reclamo) {
    return save(reclamo);
  }

  public void delete(Reclamo reclamo) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
    
      if (reclamo != null) {
        em.remove(reclamo);
      }

      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
