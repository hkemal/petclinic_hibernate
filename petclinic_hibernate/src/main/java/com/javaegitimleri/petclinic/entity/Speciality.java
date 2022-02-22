package com.javaegitimleri.petclinic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hsr_speciality")
public class Speciality extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "specialities")
    private Set<Vet> vets = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
