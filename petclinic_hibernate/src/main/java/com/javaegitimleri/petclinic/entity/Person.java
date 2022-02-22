package com.javaegitimleri.petclinic.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//@MappedSuperclass
@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "hsr_person")
public abstract class Person extends BaseEntity {

    @NaturalId
    @Column(name = "first_name")
    private String firstName;

    @NaturalId
    @Column(name = "last_name")
    private String lastName;

    @ElementCollection
    @CollectionTable(name = "hsr_person_emails", joinColumns = @JoinColumn(name = "person_id"))
    private Set<Email> emails = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Email> getEmails() {
        return emails;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
    }
}

