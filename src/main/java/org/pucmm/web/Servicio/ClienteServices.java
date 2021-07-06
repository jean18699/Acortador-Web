package org.pucmm.web.Servicio;

import org.pucmm.web.Modelo.Cliente;

public class ClienteServices {

    private static ClienteServices instancia;
    private GestionDb gestionDb = new GestionDb(Cliente.class);

    public static ClienteServices getInstance()
    {
        if(instancia == null)
        {
            instancia = new ClienteServices();
        }
        return instancia;
    }

    public ClienteServices(){}


}
