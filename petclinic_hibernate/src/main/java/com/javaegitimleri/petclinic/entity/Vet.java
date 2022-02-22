package com.javaegitimleri.petclinic.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@DiscriminatorValue("V")
@Entity
@Table(name = "hsr_vet ")
public class Vet extends Person {

    @Column(name = "works_full_time")
    private Boolean worksFullTime;

    @ManyToMany
    @JoinTable(name = "hsr_vet_specialty", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Speciality> specialities = new HashSet<>();

    public Boolean getWorksFullTime() {
        return worksFullTime;
    }

    public void setWorksFullTime(Boolean worksFullTime) {
        this.worksFullTime = worksFullTime;
    }
}

