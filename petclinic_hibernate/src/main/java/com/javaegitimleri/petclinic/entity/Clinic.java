package com.javaegitimleri.petclinic.entity;

import sun.misc.OSEnvironment;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hsr_clinic")
public class Clinic extends BaseEntity {

    @Column(name = "name")
    private String name;

//    @OneToMany
//    @JoinTable(name = "hsr_clinic_owner", joinColumns = @JoinColumn(name = "clinic_id"),
//            inverseJoinColumns = @JoinColumn(name = "owner_id"))
//    private Set<Owner> owners = new HashSet<>();
//
//    @OneToMany
//    @JoinTable(name = "hsr_clinic_vet", joinColumns = @JoinColumn(name = "clinic_id"),
//            inverseJoinColumns = @JoinColumn(name = "vet_id"))
//    private Set<Vet> vets = new HashSet<>();

    @OneToMany
    @JoinTable(name = "hsr_clinic_person", joinColumns = @JoinColumn(name = "clinic_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> people = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }
}
