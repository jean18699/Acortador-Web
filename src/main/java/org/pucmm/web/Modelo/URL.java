package org.pucmm.web.Modelo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class URL {

    @Id
    private long id;
    private String origen; //Origen que se va a acortar
    private String direccion; //direccion acortada

    public URL() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
