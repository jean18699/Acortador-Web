package org.pucmm.web.Modelo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    private String nombreUsuario;
    private String password;
    private String nombre;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<URL> urls;

    public Usuario(){}

    public Usuario(String usuario, String password, String nombre){
        this.nombreUsuario = usuario;
        this.password = password;
        this.nombre = nombre;
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
}
