package org.pucmm.web.Modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String navegador;
    private String ip;
    private LocalDate fechaAcceso;
    private LocalTime horaAcceso;
    private String sistemaOperativo;


    public Cliente(String navegador, String ip, LocalDate fechaAcceso, LocalTime horaAcceso, String sistemaOperativo)
    {
        this.ip = ip;
        this.navegador = navegador;
        this.fechaAcceso = fechaAcceso;
        this.horaAcceso = horaAcceso;
        this.sistemaOperativo = sistemaOperativo;
    }

    public Cliente(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNavegador() {
        return navegador;
    }

    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDate getFechaAcceso() {

        return fechaAcceso;
    }

    public void setFechaAcceso(LocalDate fecha) {
        this.fechaAcceso = fecha;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public LocalTime getHoraAcceso() {
        return horaAcceso;
    }

    public void setHoraAcceso(LocalTime horaAcceso) {
        this.horaAcceso = horaAcceso;
    }
}
