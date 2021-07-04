package org.pucmm.web.Modelo;

import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Usuario {
    @Id
    private String user;
    private String name;
    private String password;
    private boolean admin;

    public Usuario(String user, String name, String password, boolean admin){
        this.user = user;
        this.name = name;
        this.password = password;
        this.admin = admin;
    }

    public Usuario(){

    }

    @OneToMany(fetch = FetchType.EAGER)
    private Set<URL> url;

    public Set<URL> getURL() {
        return url;
    }
    public void setURL(Set<URL> url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
