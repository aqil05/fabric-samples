package org.hyperledger.fabric.samples.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class BasilLeg {

    @Property()
    private Basil basil;

    @Property()
    private String gpsPosition;

    @Property()
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

    public BasilLeg(@JsonProperty("basil") Basil basil,
                    @JsonProperty("gpsPosition") String gpsPosition,
                    @JsonProperty("timestamp") String timestamp) {
        this.basil = basil;
        this.gpsPosition = gpsPosition;
        this.timestamp = timestamp;
    }
}
