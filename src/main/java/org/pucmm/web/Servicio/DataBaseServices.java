package org.pucmm.web.Servicio;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase con patrón Singleton
 * Created by vacax on 27/05/16.
 */
public class DataBaseServices {

    private static DataBaseServices instancia;
    private String URL = "jdbc:h2:tcp://localhost/~/acortador"; //Modo Server...
    //String userHomeDir = System.getProperty("user.home");
    //File archivoBaseDeDatos = new File(userHomeDir + "/acortador.h2.db");
    /**
     *Implementando el patron Singleton
     */
    private DataBaseServices(){
        registrarDriver();
    }

    /**
     * Retornando la instancia.
     * @return
     */
    public static DataBaseServices getInstancia(){
        if(instancia==null){
            instancia = new DataBaseServices();
        }
        return instancia;
    }

    /**
     * Metodo para el registro de driver de conexión.
     */
    private void registrarDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            // Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConexion() throws SQLException {
        Connection con = null;

        con = DriverManager.getConnection(URL, "sa", "");
       /* try {
            archivoBaseDeDatos.createNewFile();
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException | IOException ex) {
           // Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return con;
    }

    public void testConexion() {
        try {
            getConexion().close();
            System.out.println("Conexión realizado con exito...");
        } catch (SQLException ex) {
            // Logger.getLogger(TiendaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
