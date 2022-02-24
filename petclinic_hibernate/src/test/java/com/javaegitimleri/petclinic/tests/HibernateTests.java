package com.javaegitimleri.petclinic.tests;

import com.javaegitimleri.petclinic.config.HibernateConfig;
import com.javaegitimleri.petclinic.config.JpaConfig;
import com.javaegitimleri.petclinic.entity.*;
import org.hibernate.*;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class HibernateTests {

    @Test
    public void testContextualSession() {
        Session session1 = HibernateConfig.getSessionFactory().getCurrentSession();
        Session session2 = HibernateConfig.getSessionFactory().getCurrentSession();
        System.out.println(session1 == session2);
        session1.beginTransaction().commit();
        session2 = HibernateConfig.getSessionFactory().getCurrentSession();
        System.out.println(session1 == session2);
        Session session3 = HibernateConfig.getSessionFactory().openSession();
        System.out.println(session2 == session3);
    }

    @Test
    public void testContextualSession2() {
        Session session = HibernateConfig.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        PetType petType = session.get(PetType.class, 1L);
        petType.setName("asdf");
        tx.commit();
        //session.close();
        System.out.println("--- after tx commit ---");
        System.out.println("Session open : " + session.isOpen());
    }

    @Test
    public void testCascade() throws IOException {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Owner owner = new Owner();
        owner.setFirstName("Kemal");
        owner.setLastName("Kara");

        Pet pet = new Pet();
        pet.setName("My puppy");
        owner.getPets().add(pet);
        pet.setOwner(owner);
        session.persist(owner);

        Visit visit = new Visit();
        visit.setVisitDescription("checkUp");
        visit.setVisitDate(new Date());

        tx.commit();
        session.close();
    }

    @Test
    public void testRefresh() throws IOException {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        PetType petType = session.get(PetType.class, 1L);
        System.out.println("---PetType Loaded ---");
        petType.setName("my pet type");

        System.out.println("--- waiting ---");
        System.in.read();
        session.refresh(petType);
        System.out.println("--- after refresh ---");
        System.out.println(petType.getName());
    }

    @Test
    public void testFlushManual() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.setHibernateFlushMode(FlushMode.MANUAL);
        Owner owner = session.get(Owner.class, 7L);
        owner.setRating(null);
        session.flush();
        tx.commit();
        session.close();
        System.out.println("---after session close ---");
    }

    @Test
    public void testFlushTxRelationShip() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Owner owner = session.get(Owner.class, 7L);
        owner.setRating(null);
        session.persist(new Pet("my pet", new Date()));
        session.flush();
        System.out.printf("--- After flush() ---");
        tx.rollback();
    }

    @Test
    public void testDelete() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        //Visit visit = session.load(Visit.class, 1L);
        Visit visit = session.load(Visit.class, 2L);
        session.clear();
        session.delete(visit);
        tx.commit();
        session.close();
    }

    @Test
    public void testHibernateInitialize() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet1 = session.load(Pet.class, 1L);
        System.out.println("--- before initialize ----");
//        Hibernate.initialize(pet1);
//        Hibernate.initialize(pet1.getImagesByFilePath());
        session.close();
        System.out.println("session is open : " + session.isOpen());
        System.out.println(pet1.getName());
        System.out.println(pet1.getImagesByFilePath().size());
    }

    @Test
    public void testSessionMerge() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet1 = session.get(Pet.class, 1L);
        //session.evict(pet1);
        tx.commit();
        session.close();

        session = HibernateConfig.getSessionFactory().openSession();
        tx = session.beginTransaction();
        pet1.setBirthDate(new Date());
        //session.merge(pet1);

//        System.out.println("--- after seesion evict ---");
//        System.out.println("session open" + session.isOpen());
        Pet pet2 = (Pet) session.merge(pet1);
//        Map<String, Image> imagesByFilePath = pet2.getImagesByFilePath();
//        System.out.println(imagesByFilePath.getClass());
//        System.out.println(imagesByFilePath.size());
        tx.commit();
        session.close();
    }

    @Test
    public void testDetachedEntitiesLockMethod() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet1 = session.get(Pet.class, 1L);
        City city1 = session.get(City.class, 1L);

        tx.commit();
        session.close();

        session = HibernateConfig.getSessionFactory().openSession();
        tx = session.beginTransaction();
        pet1.setBirthDate(null);

        session.lock(city1, LockMode.NONE);
        session.lock(pet1, LockMode.NONE);

        tx.commit();
        session.close();
    }

    @Test
    public void testDetachedEntitiesAndLazyUpdate() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet1 = session.get(Pet.class, 1L);
        City city1 = session.get(City.class, 1L);
//        tx.commit();
//        session.close();
//        System.out.println("After session closed");

//        session.clear();
//        System.out.println("After session clear");
//        System.out.println("session open " + session.isOpen());

        session.evict(pet1);
        session.evict(city1);
        System.out.println("After session evict");
        System.out.println("session open " + session.isOpen());
        session.update(city1);
        session.saveOrUpdate(pet1);
        System.out.println(pet1.getImagesByFilePath().getClass());
        System.out.println(city1.getName());
        tx.commit();
        session.close();
    }

    @Test
    public void testDetachedEntitiesAndLazy() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet1 = session.get(Pet.class, 1L);
        City city1 = session.get(City.class, 1L);
        tx.commit();
//        session.close();
//        System.out.println("After session closed");

//        session.clear();
//        System.out.println("After session clear");
//        System.out.println("session open " + session.isOpen());

//        session.evict(pet1);
        session.evict(city1);
        System.out.println("After session evict");
        System.out.println("session open " + session.isOpen());
        System.out.println(pet1.getImagesByFilePath().getClass());
        System.out.println(pet1.getImagesByFilePath().size());
    }

    @Test
    public void testDetachedEntities() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet1 = session.get(Pet.class, 1L);

        tx.commit();
        session.close();

        session = HibernateConfig.getSessionFactory().openSession();
        tx = session.beginTransaction();
        pet1.setBirthDate(new Date());
        tx.commit();
        session.close();
    }

    @Test
    public void testUpdate() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet1 = session.get(Pet.class, 1L);
        pet1.setBirthDate(new Date());
        pet1.setType(session.load(PetType.class, 3L));
        //session.update(pet1);
        tx.commit();
        session.close();
    }

    @Test
    public void testInsertWithMerge() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Pet pet = new Pet();
        pet.setName("Osas 3");
        Pet pet2 = (Pet) session.merge(pet);
        System.out.println("--- after merge called ---");
        pet.setBirthDate(new Date());
        System.out.println("pet id : " + pet.getId());
        System.out.println("pet2 id : " + pet2.getId());
        System.out.println("pet and pet2 reference is same : " + (pet == pet2));
        System.out.println("pet2 birthDate : " + pet2.getBirthDate());

        tx.commit();
        session.close();
    }

    @Test
    public void testSave() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Pet pet = new Pet();
        pet.setName("Osas 2");

        Serializable primayKey = session.save(pet);

        System.out.println("--- after save called ---");

        tx.commit();
        session.close();

        System.out.println(primayKey == pet.getId());
    }

    @Test
    public void testPersist() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Pet pet = new Pet();
        pet.setName("Osas");

        session.persist(pet);
        System.out.println("--- after persist called ---");
        tx.commit();
        session.close();
    }

    @Test
    public void testStatistics() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.get(Pet.class, 3L);
        session.persist(new Pet("Jane", new Date()));
        session.flush();
        session.createQuery("select p.name from Pet as p").getResultList();

        Statistics statistics = HibernateConfig.getSessionFactory().getStatistics();
        EntityStatistics entityStatistics = statistics.getEntityStatistics("com.javaegitimleri.petclinic.entity.Pet");
        QueryStatistics queryStatistics = statistics.getQueryStatistics("select p.name from Pet as p");

        System.out.println("load count : " + entityStatistics.getLoadCount());
        System.out.println("insert count : " + entityStatistics.getInsertCount());

        System.out.println("query exec count : " + queryStatistics.getExecutionCount());
        System.out.println("query avg exec time : " + queryStatistics.getExecutionAvgTime());
    }

    @Test
    public void testNaturalIdAccess() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        SimpleNaturalIdLoadAccess<Pet> simpleNaturalIdLoadAccess = session.bySimpleNaturalId(Pet.class);
        Pet pet = simpleNaturalIdLoadAccess.load("Maviş");
        System.out.println("------ pet loaded ------");
        System.out.println(pet.getId());
        System.out.println(pet.getName());

        NaturalIdLoadAccess<Person> naturalIdLoadAccess = session.byNaturalId(Person.class);
        Person person = naturalIdLoadAccess.using("firstName", "Jale").using("lastName", "Cengiz").load();
        System.out.println("------ person loaded ------");
        System.out.println(person.getId());
        System.out.println(person.getClass());
    }

    @Test
    public void testMultiIdentifierLoadAccess() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        MultiIdentifierLoadAccess<Pet> multiIdentifierLoadAccess = session.byMultipleIds(Pet.class);
        List<Pet> pets = multiIdentifierLoadAccess.multiLoad(1L, 2L, 3L, 4L, 5L);
        System.out.println("----pets loaded----");
        pets.forEach(item -> {
            System.out.println(item.getName());
        });

    }

    @Test
    public void testIdentifierLoadAccess() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        IdentifierLoadAccess<Pet> identifierLoadAccess = session.byId(Pet.class);
        Pet pet1 = identifierLoadAccess.load(1L);
        System.out.println("----pet1 loaded----");
        System.out.println(pet1.getName());
        System.out.println(pet1.getClass());

        Pet pet2 = identifierLoadAccess.getReference(2L);
        System.out.println("----pet2 loaded----");
        System.out.println(pet2.getName());
        System.out.println(pet2.getClass());
    }

    @Test
    public void testLoad() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet = session.load(Pet.class, 1L);
        System.out.println("------pet loaded--------");
        if (pet == null) {
            System.out.println("pet is null returning");
            return;
        }
        System.out.println(pet.getName());
        tx.commit();
        session.close();
        System.out.println(pet.getBirthDate());
        System.out.println("Get second time 1");
        Pet pet2 = session.get(Pet.class, 1L);
        System.out.println(pet2.getName());
        System.out.println("Get second time 2");
        System.out.println(pet.getClass());
        System.out.println(pet == pet2);
    }

    @Test
    public void testGet() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Pet pet = session.get(Pet.class, 1L);
//        tx.commit();
//        session.close();
        System.out.println("------pet loaded--------");
        if (pet == null) {
            System.out.println("pet is null returning");
            return;
        }
        System.out.println("Get second time");
        Pet pet2 = session.get(Pet.class, 1L);
        System.out.println(pet2.getName());
        System.out.println("Get second time");
        System.out.println(pet.getName());
        System.out.println(pet.getClass());
        System.out.println(pet == pet2);
    }

    @Test
    public void testHibernateSetup() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        tx.commit();
        session.close();
        HibernateConfig.getSessionFactory().close();
    }

    @Test
    public void testCreateEntity() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Tekir");

        session.persist(pet);
        tx.commit();

        session.close();
        //HibernateConfig.getSessionFactory().close();
    }


    @Test
    public void testFieldLevelAccess() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Pet pet1 = new Pet(1L, "Jane", new Date());

        session.persist(pet1);
        tx.commit();

        session.close();

        session = HibernateConfig.getSessionFactory().openSession();
        Pet pet2 = session.get(Pet.class, 1L);
        System.out.println(pet2);
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
    public void testCheckNullability() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        EntityTransaction tx = session.getTransaction();
        tx.begin();
        Pet pet = new Pet();
        pet.setId(1L);
        //pet.setName("John");
        session.persist(pet);
        //em.flush();
        tx.commit();
        session.close();
    }

    @Test
    public void testCreateEntity1() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        Pet pet = new Pet();
        //pet.setId(1L);
        pet.setName("John");
        session.persist(pet);
        tx.commit();
        session.close();
    }

    @Test
    public void testCompositePK() {
        OwnerCompositePrimaryKey owner = new OwnerCompositePrimaryKey();
        OwnerId id = new OwnerId();
        id.setFirstName("Kemal");
        id.setLastName("Erkaraca");

        owner.setId(id);
        owner.setAge(34);

        Session session = HibernateConfig.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        session.persist(owner);
        tx.commit();
        session.close();
    }

    @Test
    public void testEmbedable() {
        Owner owner = new Owner();
        owner.setFirstName("Kemal");
        owner.setLastName("Erkaraca");
        owner.setAge(25);
        owner.setRating(Rating.PREMIUM);

        Address address = new Address();
        address.setPhone("543");
        address.setStreet("Kisikli");
        owner.setAddress(address);

        Session session = HibernateConfig.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        session.persist(owner);
        tx.commit();
        session.close();
    }

    @Test
    public void testVet() {
        Vet vet = new Vet();
        vet.setFirstName("Kemal");
        vet.setLastName("Erkaraca");
        vet.setWorksFullTime(true);

        Session session = HibernateConfig.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        session.persist(vet);
        tx.commit();
        session.close();
    }

    @Test
    public void testVisit() {
        Visit visit = new Visit();
        visit.setVisitDate(new Date());

        Session session = HibernateConfig.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        session.persist(visit);
        tx.commit();
        session.close();
    }

    @Test
    public void testMappedBy() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        EntityTransaction tx = session.beginTransaction();
        Owner owner = session.get(Owner.class, 1L);
        Pet pet = session.get(Pet.class, 101L);
        owner.getPets().add(pet);
        //session.update(owner); ilişki update edilmedi
        //session.merge(owner);
        //pet.setOwner(owner); ilişki burada update edildi
        //owner.getPets().remove(owner); ilişki kaldırılmadı
        pet.setOwner(null); //Burada ilişkiyi kaldırdık
        tx.commit();
        session.close();
    }

    @Test
    public void testParentChildAssoc() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        tx.begin();

        Pet pet = session.get(Pet.class, 1L);
        Visit visit = session.get(Visit.class, 101L);
        Image image = session.get(Image.class, 1001L);

        pet.getVisits().remove(visit);
        pet.getImagesByFilePath().remove("/myimage");

        tx.commit();
        session.close();
    }

    @Test
    public void testLazyEagerAccess() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        Pet pet = session.get(Pet.class, 101L);
        System.out.println("----pet loaded----");

//        LazyInitializationError verir
//        tx.commit();
//        session.close();

        System.out.println("visit_size : " + pet.getVisits().size());
        System.out.println("--------");
        System.out.println("pet type name : " + pet.getType().getName());
        System.out.println(pet.getType().getClass());

        tx.commit();
        session.close();

    }

    @Test
    public void testOneToOneLazyProblem() {
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        Image image = session.get(Image.class, 1L);
        System.out.println("---***---ImageLoaded---***---");
        System.out.println(image.getImageContent().getContent());
        System.out.println(image.getImageContent().getClass());
        tx.commit();
        session.close();

    }

}
