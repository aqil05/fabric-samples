package org.hyperledger.fabric.samples.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class Basil {

    @Property()
    private String qr;

    @Property()
    private String extraInfo;

    @Property()
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

    public Basil(@JsonProperty("qr") String qr,
                 @JsonProperty("extraInfo") String extraInfo,
                 @JsonProperty("owner") Owner owner) {
        this.qr = qr;
        this.extraInfo = extraInfo;
        this.owner = owner;
    }
}
