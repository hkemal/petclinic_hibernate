package com.javaegitimleri.petclinic.tests;

import com.javaegitimleri.petclinic.config.HibernateConfig;
import com.javaegitimleri.petclinic.config.JpaConfig;
import com.javaegitimleri.petclinic.entity.*;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.SessionStatistics;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;

public class JpaTests {

    @Test
    public void testLifeCycleCallback() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Pet pet = new Pet();
        pet.setName("Tikky 23");
        pet.setBirthDate(new Date());
        entityManager.persist(pet);

        Pet pet1 = entityManager.find(Pet.class, 1L);
        pet1.setBirthDate(new Date());

        Pet pet2 = entityManager.getReference(Pet.class, 100L);
        entityManager.remove(pet2);
        tx.commit();
        entityManager.close();
    }

    @Test
    public void testDelete() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Visit visit = entityManager.find(Visit.class, 3L);
        //entityManager.clear();
        entityManager.remove(visit);
        tx.commit();

    }

    @Test
    public void testHibernateApiAccess2() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Session session1 = (Session) entityManager.getDelegate();
        Session session2 = entityManager.unwrap(Session.class);

//        SessionFactory sessionFactory = (SessionFactory) JpaConfig.getEntityManagerFactory();
//        Session session = (Session) entityManager;
//        Transaction transaction = (Transaction) tx;

        SessionStatistics statistics = session1.getStatistics();
        session2.setHibernateFlushMode(FlushMode.MANUAL);

    }

    @Test
    public void testHibernateApiAccess1() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        SessionFactory sessionFactory = (SessionFactory) JpaConfig.getEntityManagerFactory();
        Session session = (Session) entityManager;
        Transaction transaction = (Transaction) tx;

        SessionStatistics statistics = session.getStatistics();
        session.setHibernateFlushMode(FlushMode.MANUAL);

    }

    @Test
    public void testFindAndGetReference() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Pet pet = entityManager.find(Pet.class, 1L);

        System.out.println(pet.getName());
        System.out.println(pet.getClass());
        System.out.println("************");
        Pet pet2 = entityManager.getReference(Pet.class, 2L);
        System.out.println(pet2.getName());
        System.out.println(pet2.getClass());

    }

    @Test
    public void testJpaSetup() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        tx.commit();
        entityManager.close();
        JpaConfig.getEntityManagerFactory().close();
    }

    @Test
    public void testWithoutTX() {
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("John");
        em.persist(pet);
        //em.flush();

        tx.commit();
        em.close();
    }

    @Test
    public void testRating() {
        EntityManager em = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Owner owner = new Owner();
        owner.setFirstName("Kemal");
        owner.setLastName("Erkaraca");
        owner.setRating(Rating.PREMIUM);

        Address address = new Address();
        address.setPhone("543");
        address.setStreet("Kisikli");
        owner.setAddress(address);

        em.persist(owner);
        tx.commit();
        em.close();
    }
}
