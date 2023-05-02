package application.java.models;

public class BasilLeg {

    private Basil basil;

    private String gpsPosition;

    private String timestamp;

    public Basil getBasil() {
        return basil;
    }

    public void setBasil(Basil basil) {
        this.basil = basil;
    }

    public String getGpsPosition() {
        return gpsPosition;
    }

    public void setGpsPosition(String gpsPosition) {
        this.gpsPosition = gpsPosition;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public BasilLeg() {
    }

    public BasilLeg(Basil basil, String gpsPosition, String timestamp) {
        this.basil = basil;
        this.gpsPosition = gpsPosition;
        this.timestamp = timestamp;
    }
}
