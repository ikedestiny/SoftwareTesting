package part3;

public class Engine {
    private boolean started;
    private boolean ejected;

    public Engine() {
        started = false;
        ejected = false;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isEjected() {
        return ejected;
    }

    public void setEjected(boolean ejected) {
        this.ejected = ejected;
    }
}