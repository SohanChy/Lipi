package view.hugo.pane;

import javafx.application.HostServices;

/*
From Stackoverflow, utility class
http://stackoverflow.com/questions/33094981/javafx-8-open-a-link-in-a-browser-without-reference-to-application
*/

public enum HostServicesProviderUtil {

    INSTANCE;

    private HostServices hostServices;

    public void init(HostServices hostServices) {
        if (this.hostServices != null) {
            throw new IllegalStateException("Host services already initialized");
        }
        this.hostServices = hostServices;
    }

    public HostServices getHostServices() {
        if (hostServices == null) {
            throw new IllegalStateException("Host services not initialized");
        }
        return hostServices;
    }
}