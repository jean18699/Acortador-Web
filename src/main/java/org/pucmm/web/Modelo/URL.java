package org.pucmm.web.Modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
public class URL implements Serializable {

    @Id
    @Column(name="id")
    private String direccionAcortada; //direccion acortada
    private String origen; //Origen que se va a acortar

  /*  @Column(name = "cantidad_visitas")
    private long visitas = 0L;

    //Variables para indicar la cantidad de veces que ha sido accedido dependiendo del navegador
    @Column(name = "Chrome")
    private Long chrome = 0L;

    @Column(name = "Firefox")
    private Long firefox = 0L;

    @Column(name = "Safari")
    private Long safari = 0L;

    @Column(name = "Opera")
    private Long opera = 0L;

    @Column(name = "Edge")
    private Long edge = 0L;

    @Column(name = "Internet_Explorer")
    private Long internetExplorer = 0L;

    //Para pruebas, postman
    @Column(name = "Postman")
    private Long postman = 0L;

    @Column(name = "Navegador_desconocido")
    private Long unknownBrowser = 0L;
*/
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Cliente> clientes;


    public URL() {}

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDireccionAcortada() {
        return direccionAcortada;
    }

    public void setDireccionAcortada(String direccion) {
        this.direccionAcortada = direccion;
    }
/*
    public long getVisitas() {
        return visitas;
    }

    public void setVisitas(long visitas) {
        this.visitas = visitas;
    }

    public Long getChrome() {
        return chrome;
    }

    public void setChrome(Long chrome) {
        this.chrome = chrome;
    }

    public Long getFirefox() {
        return firefox;
    }

    public void setFirefox(Long firefox) {
        this.firefox = firefox;
    }

    public Long getSafari() {
        return safari;
    }

    public void setSafari(Long safari) {
        this.safari = safari;
    }

    public Long getOpera() {
        return opera;
    }

    public void setOpera(Long opera) {
        this.opera = opera;
    }

    public Long getEdge() {
        return edge;
    }

    public void setEdge(Long edge) {
        this.edge = edge;
    }

    public Long getInternetExplorer() {
        return internetExplorer;
    }

    public void setInternetExplorer(Long internetExplorer) {
        this.internetExplorer = internetExplorer;
    }

    public Long getPostman() {
        return postman;
    }

    public void setPostman(Long postman) {
        this.postman = postman;
    }

    public Long getUnknownBrowser() {
        return unknownBrowser;
    }

    public void setUnknownBrowser(Long unknown) {
        this.unknownBrowser = unknown;
    }
*/
    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }
}
