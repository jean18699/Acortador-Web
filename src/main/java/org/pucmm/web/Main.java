package org.pucmm.web;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import org.pucmm.web.Controlador.DashboardControlador;
import org.pucmm.web.Controlador.URLControlador;
import org.pucmm.web.Controlador.UsuarioControlador;
import org.pucmm.web.Servicio.BootStrapServices;
import org.pucmm.web.Servicio.DataBaseServices;
import org.pucmm.web.Servicio.URLServices;

import java.sql.SQLException;

public class Main {

    private static String modoConexion = "";

    public static void main(String[] args) throws SQLException {

        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }

        //Iniciando el servicio
        BootStrapServices.startDb();

        //Prueba de Conexión.
        DataBaseServices.getInstancia().getConexion();


        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/vistas");
            config.registerPlugin(new RouteOverviewPlugin("/rutas"));
            config.enableCorsForAllOrigins();
        }).start(getHerokuAssignedPort());

        //Clases gestoras de rutas
        new DashboardControlador(app).aplicarRutas();
        new URLControlador(app).aplicarRutas();
        new UsuarioControlador(app).aplicarRutas();


    }

    private static int getHerokuAssignedPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        return 7000;
    }

    public static String getModoConexion(){
        return modoConexion;
    }

}
