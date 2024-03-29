package com.javaegitimleri.petclinic.entity;

import com.javaegitimleri.petclinic.event.AuditEntityEventListener;

import javax.persistence.*;

@EntityListeners(AuditEntityEventListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "bigint default 0")
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

