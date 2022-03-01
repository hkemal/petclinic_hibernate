package com.javaegitimleri.petclinic.tests;

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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

public class JpaTests {

    @Test
    public void testCriteriaWithInnerJoin() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
        Root<Pet> root = criteriaQuery.from(Pet.class);
        Join<Pet, PetType> join = root.join("type");
        Predicate predicate = criteriaBuilder.equal(join.get("id"), 4L);
        criteriaQuery.where(predicate);
        TypedQuery<Pet> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pet> resultList = typedQuery.getResultList();
        resultList.forEach(System.out::println);
    }

    @Test
    public void testCriteriaApi() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
        Root<Pet> root = criteriaQuery.from(Pet.class);
        Predicate likeNamePredicate = criteriaBuilder.like(root.get("name"), criteriaBuilder.parameter(String.class, "petName"));
        Predicate eqTypePredicate = criteriaBuilder.equal(root.get("type"), 4L);
        Predicate orPredicate = criteriaBuilder.or(likeNamePredicate, eqTypePredicate);
        criteriaQuery.where(orPredicate);
        TypedQuery<Pet> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setParameter("petName", "K%");
        List<Pet> resultList = typedQuery.getResultList();
        resultList.forEach(System.out::println);
    }

    @Test
    public void testNativeSQLDTOClass() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(" select * from hsr_pet as P where P.pet_name like ?1 ", "petWithNameAndBirthDate");
        nativeQuery.setParameter(1, "K%");
        List<Pet> resultList = nativeQuery.getResultList();
        resultList.stream().forEach(System.out::println);
    }

    @Test
    public void testNativeSQLJPA() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        List resultList = entityManager.createNativeQuery(" select * from hsr_pet as P where P.pet_name like ?1 ", Pet.class).setParameter(1, "K%").getResultList();
        resultList.stream().forEach(System.out::println);
    }

    @Test
    public void testJPQLPositionalParameters() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();

        String queryString = " " +
                " select P " +
                " from Pet as P " +
                " where P.name like ?1 " +
                " or P.type.id = ?2 ";
        TypedQuery typedQuery = entityManager.createQuery(queryString, Pet.class);
        typedQuery.setParameter(1, "%K%");
        typedQuery.setParameter(2, 1L);

        List<Pet> resultList = typedQuery.getResultList();
        System.out.println("--- query executed ---");
        resultList.forEach(System.out::println);

        entityManager.close();
    }

    @Test
    public void testJPQL() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();

        String queryString = " " +
                " select P " +
                " from Pet as P " +
                " where P.name like :petName " +
                " or P.type.id = :typeId ";
        TypedQuery typedQuery = entityManager.createQuery(queryString, Pet.class);
        typedQuery.setParameter("petName", "%K%");
        typedQuery.setParameter("typeId", 1L);

        List<Pet> resultList = typedQuery.getResultList();
        System.out.println("--- query executed ---");
        resultList.forEach(System.out::println);

        entityManager.close();
    }

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
