package com.javaegitimleri.petclinic.repository;

import com.javaegitimleri.petclinic.entity.Owner;
import org.hibernate.SessionFactory;

public class OwnerRepository {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Owner owner) {
        sessionFactory.getCurrentSession().persist(owner);
    }

}
