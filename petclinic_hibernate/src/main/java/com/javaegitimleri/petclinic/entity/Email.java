package com.javaegitimleri.petclinic.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Email {
    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "person_id_1",table = "hsr_person_emails")
    private Person person;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }


}
