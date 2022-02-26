package com.javaegitimleri.petclinic.event;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;

public class AuditEntityDeleteListener implements PostDeleteEventListener {

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostDelete(PostDeleteEvent postDeleteEvent) {
        System.out.println(">>>Entity deleted : " + postDeleteEvent.getEntity());
    }
}
