package org.hyperledger.fabric.samples.models;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class Owner {

    @Property()
    private String org;
    @Property()
    private String user;

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Owner() {
    }

    public Owner(@JsonProperty("org") final String org,
                 @JsonProperty("user") String user) {
        this.org = org;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return org.equals(owner.org) && user.equals(owner.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(org, user);
    }
}
