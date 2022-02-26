package com.javaegitimleri.petclinic.event;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;

public class AuditEntityInsertListener implements PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        System.out.println(">>>Entity inserted : " + postInsertEvent.getEntity() + " with id : " + postInsertEvent.getId());
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}
