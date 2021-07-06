package org.pucmm.web.Modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Usuario implements Serializable {

    @Id
    private String nombreUsuario;
    private String password;
    private String nombre;
    private boolean isAdmin;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<URL> urls;

    public Usuario(){}

    public Usuario(String usuario, String password, String nombre, boolean isAdmin){
        this.nombreUsuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.isAdmin = isAdmin;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<URL> getUrls() {
        return urls;
    }

    public void setUrls(Set<URL> urls) {
        this.urls = urls;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
