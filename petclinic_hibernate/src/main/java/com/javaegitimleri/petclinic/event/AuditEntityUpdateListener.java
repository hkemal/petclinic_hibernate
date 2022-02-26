package com.javaegitimleri.petclinic.event;

import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

public class AuditEntityUpdateListener implements PostUpdateEventListener {

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        System.out.println(">>>Entity updated : " + postUpdateEvent.getEntity() + " with id : " + postUpdateEvent.getId());
    }
}
