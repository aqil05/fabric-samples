package application.java.models;

public class Basil {

    private String qr;

    private String extraInfo;

    private Owner owner;

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Basil() {
    }

    public Basil(String qr, String extraInfo, Owner owner) {
        this.qr = qr;
        this.extraInfo = extraInfo;
        this.owner = owner;
    }
}
