package com.reclamos.dao;

import java.util.List;

import com.reclamos.entities.Atencion;
import com.reclamos.utils.EMFUtil;

import jakarta.persistence.EntityManager;

public class AtencionDAO {

  public Atencion save(Atencion atencion) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      em.getTransaction().begin();
      if (atencion.getId() == null) {
        em.persist(atencion);
      } else {
        atencion = em.merge(atencion);
      }
      em.getTransaction().commit();
      return atencion;
    } finally {
      em.close();
    }
  }

  public List<Atencion> findAll(Integer reclamoId) {
    EntityManager em = EMFUtil.getEntityManager();
    try {
      String query = """
        SELECT a FROM Atencion a WHERE a.reclamo.id = :reclamoId
      """;

      return em.createQuery(query, Atencion.class)
        .setParameter("reclamoId", reclamoId)
        .getResultList();
    } finally {
      em.close();
    }
  }

}
