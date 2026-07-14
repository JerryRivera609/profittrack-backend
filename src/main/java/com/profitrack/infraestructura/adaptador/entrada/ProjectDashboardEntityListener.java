package com.profitrack.infraestructura.adaptador.entrada;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectDashboardEntityListener {

    private static LiveMetricsWebSocketHandler metricsHandler;

    @Autowired
    public void setMetricsHandler(LiveMetricsWebSocketHandler handler) {
        ProjectDashboardEntityListener.metricsHandler = handler;
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void onEntityChange(Object entity) {
        if (metricsHandler != null) {
            metricsHandler.notifyDashboardUpdate();
        }
    }
}
