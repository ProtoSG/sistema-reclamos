package com.reclamos.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EMFUtil {

  private static final EntityManagerFactory emf;

  static {
    try {
      emf = Persistence.createEntityManagerFactory("reclamosPU");
    } catch (Throwable ex) {
      System.err.println("Error al crear EntityManagerFactory: " + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public static void close() {
    if (emf != null && emf.isOpen()) {
      emf.close();
    }
  }
}
