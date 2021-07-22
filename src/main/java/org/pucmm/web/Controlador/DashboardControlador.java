package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.pucmm.web.Modelo.Cliente;
import org.pucmm.web.Modelo.URL;
import org.pucmm.web.Servicio.URLServices;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class DashboardControlador {

    private Javalin app;
    Map<String, Object> modelo = new HashMap<>();
    private String dominio = "http://localhost:7000/";
    Set<LocalDate> fechas;
    List<Long> visitasFechas;

    public DashboardControlador(Javalin app)
    {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        modelo.put("dominio", dominio);


    }


    public void aplicarRutas() throws NumberFormatException {

        app.get("/dashboard", ctx -> {
                modelo.put("clientes",null);
                modelo.put("visitasFechas","");
                modelo.put("visitasFechas","");
                modelo.put("urls", URLServices.getInstance().getURLs());
            ctx.render("/vistas/templates/dashboard.html",modelo);
        });

        app.get("/dashboard/infoURL", ctx -> {

            ctx.render("/vistas/templates/dashboard.html",modelo);
        });

        app.post("/dashboard/infoURL", ctx -> {

            System.out.println(ctx.formParam("url"));
            URL url = URLServices.getInstance().getURL(ctx.formParam("url"));
            fechas = new HashSet<>();
            visitasFechas = new ArrayList<>();

            for(Cliente cliente : url.getClientes())
            {
               LocalDate date = cliente.getFechaAcceso();
               fechas.add(date);
            }

            for(LocalDate fecha : fechas)
            {
                visitasFechas.add(URLServices.getInstance().getCantidadVisitasFecha(url.getDireccionAcortada(), fecha.toString()));
            }

            modelo.put("urlActual", ctx.formParam("url"));
           // modelo.put("dominio", dominio);
            modelo.put("fechaAcceso", "");
            modelo.put("fechas",fechas);
            modelo.put("visitasFechas",visitasFechas);
          //  modelo.put("urls", URLServices.getInstance().getURLs());
            ctx.render("/vistas/templates/dashboard.html",modelo);
        });

        app.get("/dashboard/infoURL/:url/estadisticas/:fecha",ctx->{

            URL url = URLServices.getInstance().getURL(ctx.pathParam("url"));
            fechas = new HashSet<>();
            visitasFechas = new ArrayList<>();

            for(Cliente cliente : url.getClientes())
            {
                LocalDate date = cliente.getFechaAcceso();//Date.from(cliente.getFechaAcceso().atStartOfDay(ZoneId.systemDefault()).toInstant());
                fechas.add(date);
            }

            for(LocalDate fecha : fechas)
            {
                visitasFechas.add(URLServices.getInstance().getCantidadVisitasFecha(url.getDireccionAcortada(), fecha.toString()));
            }


            modelo.put("fechaAcceso", ctx.pathParam("fecha"));
            modelo.put("fechas",fechas);
            modelo.put("visitasFechas",visitasFechas);
            modelo.put("clientes",URLServices.getInstance().getClientesURLByFecha(
                    ctx.pathParam("url"), ctx.pathParam("fecha")
            ));
            ctx.redirect("/dashboard/infoURL");
        });

        app.get("/dashboard/usuarios", ctx -> {
            ctx.result("vista usuarios");
        });

    }
}
