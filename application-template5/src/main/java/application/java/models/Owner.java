package application.java.models;

import java.util.Objects;

public class Owner {

    private String org;
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

    public Owner(String org, String user) {
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
