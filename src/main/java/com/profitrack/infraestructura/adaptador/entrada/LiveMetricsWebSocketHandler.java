package com.profitrack.infraestructura.adaptador.entrada;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.scheduling.annotation.Scheduled;

@Component
public class LiveMetricsWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        // Send initial metrics immediately upon connection
        sendMetricsToSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 2000)
    public void broadcastMetrics() {
        if (sessions.isEmpty()) {
            return;
        }

        String jsonPayload = generateMetricsJson();
        TextMessage message = new TextMessage(jsonPayload);

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    sessions.remove(session);
                }
            } else {
                sessions.remove(session);
            }
        }
    }

    public void notifyDashboardUpdate() {
        if (sessions.isEmpty()) {
            return;
        }
        TextMessage updateMessage = new TextMessage("project-updated");
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(updateMessage);
                } catch (IOException e) {
                    sessions.remove(session);
                }
            } else {
                sessions.remove(session);
            }
        }
    }

    private void sendMetricsToSession(WebSocketSession session) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(generateMetricsJson()));
            }
        } catch (IOException e) {
            sessions.remove(session);
        }
    }

    private String generateMetricsJson() {
        String hostname = "Nodo-Default";
        try {
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            // Ignore
        }

        double totalMemoryMb = 512.0;
        double usedMemoryMb = 180.0;
        double memoryPercentage = 35.0;
        double cpuLoad = 12.5;

        try {
            Runtime runtime = Runtime.getRuntime();
            long totalMemoryBytes = runtime.totalMemory();
            long freeMemoryBytes = runtime.freeMemory();
            long usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;

            totalMemoryMb = totalMemoryBytes / (1024.0 * 1024.0);
            usedMemoryMb = usedMemoryBytes / (1024.0 * 1024.0);
            memoryPercentage = (usedMemoryMb / totalMemoryMb) * 100;

            // CPU
            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            double systemCpuLoad = osBean.getCpuLoad() * 100;
            if (systemCpuLoad < 0) {
                systemCpuLoad = osBean.getSystemLoadAverage();
                if (systemCpuLoad < 0) {
                    systemCpuLoad = 12.5;
                }
            }
            cpuLoad = systemCpuLoad;

        } catch (Exception e) {
            // Use defaults
        }

        // Format to 2 decimal places
        double roundedCpuLoad = Math.round(cpuLoad * 100.0) / 100.0;
        double roundedMemoryUsed = Math.round(usedMemoryMb * 100.0) / 100.0;
        double roundedMemoryTotal = Math.round(totalMemoryMb * 100.0) / 100.0;
        double roundedMemoryPercentage = Math.round(memoryPercentage * 100.0) / 100.0;

        return String.format(
            "{\"cpuLoad\": %.2f, \"memoryUsed\": %.2f, \"memoryTotal\": %.2f, \"memoryPercentage\": %.2f, \"nodeIdentifier\": \"%s\"}",
            roundedCpuLoad, roundedMemoryUsed, roundedMemoryTotal, roundedMemoryPercentage, hostname
        ).replace(",", "."); // Ensure decimal points are dot-based
    }
}
