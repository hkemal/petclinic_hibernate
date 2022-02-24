package com.javaegitimleri.petclinic.repository;

import com.javaegitimleri.petclinic.entity.Clinic;
import org.hibernate.SessionFactory;
import org.osgi.service.component.annotations.Component;

public class ClinicRepository {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Clinic clinic) {
        sessionFactory.getCurrentSession().persist(clinic);
    }

    public Clinic findById(Long id) {
        return sessionFactory.getCurrentSession().get(Clinic.class, id);
    }

}
