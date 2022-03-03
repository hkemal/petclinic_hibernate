package com.javaegitimleri.petclinic.tests;

import com.javaegitimleri.petclinic.config.JpaConfig;
import com.javaegitimleri.petclinic.entity.*;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.SessionStatistics;
import org.junit.Test;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

public class JpaTests {

    @Test
    public void testEntityCache() {
        EntityManager entityManager1 = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx1 = entityManager1.getTransaction();
        tx1.begin();
        Pet pet1 = entityManager1.find(Pet.class, 1L);
        tx1.commit();
        entityManager1.close();

        EntityManager entityManager2 = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx2 = entityManager2.getTransaction();
        tx2.begin();
        Pet pet2 = entityManager2.find(Pet.class, 1L);
        System.out.println(pet2.getName());
        tx2.commit();
        entityManager2.close();
    }

    @Test
    public void testBulkDeleteWithCriteriaApi() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Image> criteriaDelete = criteriaBuilder.createCriteriaDelete(Image.class);
        Root<Image> root = criteriaDelete.from(Image.class);
        criteriaDelete.from(Image.class);
        Integer deleteCount = entityManager.createQuery(criteriaDelete).executeUpdate();
        System.out.println("--- query executed, delete count is : " + deleteCount);
        tx.commit();
        entityManager.close();
    }

    @Test
    public void testBulkUpdateWithCriteriaApi() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Image> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Image.class);
        Root<Image> root = criteriaUpdate.from(Image.class);
        criteriaUpdate.set(root.get("pet"), (Pet) null);
        Integer updateCount = entityManager.createQuery(criteriaUpdate).executeUpdate();
        System.out.println("--- query executed, update count is : " + updateCount);
        tx.commit();
        entityManager.close();
    }

    @Test
    public void testCriteriaWithMetaModel() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
        Root<Pet> root = criteriaQuery.from(Pet.class);
        Predicate predicate = criteriaBuilder.like(root.get(Pet_.name), "K%");
        criteriaQuery.where(predicate);
        TypedQuery<Pet> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pet> resultList = typedQuery.getResultList();
        resultList.forEach(System.out::println);
    }

    @Test
    public void testCriteriaMultiSelectWithTuple() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> criteriaQuery = criteriaBuilder.createQuery(Pet.class);
        Root<Pet> root = criteriaQuery.from(Pet.class);
        criteriaQuery.multiselect(root.get("name"), root.get("birthDate"));
        TypedQuery<Pet> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pet> resultList = typedQuery.getResultList();
        resultList.forEach(System.out::println);
    }

    @Test
    public void testCriteriaReturnDTO() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> tupleCriteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Pet> root = tupleCriteriaQuery.from(Pet.class);
        tupleCriteriaQuery.multiselect(root.get("name").alias("petName"), root.get("birthDate").alias("birthDate"));
        TypedQuery<Tuple> tupleTypedQuery = entityManager.createQuery(tupleCriteriaQuery);
        List<Tuple> resultList = tupleTypedQuery.getResultList();
        resultList.forEach(item -> {
            System.out.println(item.get(0) + " - " + item.get("birthDate"));
        });
    }

    @Test
    public void testCriteriaWithLeftJoin() {
        EntityManager entityManager = JpaConfig.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Owner> criteriaQuery = criteriaBuilder.createQuery(Owner.class);
        Root<Owner> root = criteriaQuery.from(Owner.class);
        Join<Owner, Pet> join = root.join("pets", JoinType.LEFT);
        Predicate predicate = criteriaBuilder.like(join.get("name"), "K%");
        criteriaQuery.where(predicate);
        TypedQuery<Owner> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Owner> resultList = typedQuery.getResultList();
        resultList.forEach(System.out::println);
    }

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
