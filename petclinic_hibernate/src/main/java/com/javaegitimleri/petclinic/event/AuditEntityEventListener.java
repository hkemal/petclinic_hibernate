package com.javaegitimleri.petclinic.event;

import com.javaegitimleri.petclinic.entity.BaseEntity;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class AuditEntityEventListener {

    @PostPersist
    public void postInsert(BaseEntity entity) {
        System.out.println(">>>Entity inserted : " + entity);
    }

    @PostUpdate
    public void postUpdate(BaseEntity entity) {
        System.out.println(">>>Entity updated : " + entity);
    }

    @PostRemove
    public void postDelete(BaseEntity entity) {
        System.out.println(">>>Entity deleted : " + entity);
    }

}
