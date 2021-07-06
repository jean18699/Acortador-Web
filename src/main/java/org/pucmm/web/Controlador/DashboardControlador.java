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

    public DashboardControlador(Javalin app)
    {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }


    public void aplicarRutas() throws NumberFormatException {

        app.get("/dashboard/example", ctx -> {

            ctx.render("/ejemplos/now-ui/examples/dashboard.html",modelo);
        });

        app.get("/dashboard", ctx -> {

            modelo.put("visitasFechas","");
            modelo.put("fechas","");
            modelo.put("urls", URLServices.getInstance().getURLs());
            ctx.render("/vistas/templates/dashboard.html",modelo);
        });

        app.get("/dashboard/:url", ctx -> {

            URL url = URLServices.getInstance().getURL(ctx.pathParam("url"));

            Set<LocalDate> fechas = new HashSet<>();
            List<Long> visitasFechas = new ArrayList<>();

            for(Cliente cliente : url.getClientes())
            {
               LocalDate date = cliente.getFechaAcceso();//Date.from(cliente.getFechaAcceso().atStartOfDay(ZoneId.systemDefault()).toInstant());
               fechas.add(date);
            }

            for(LocalDate fecha : fechas)
            {
                visitasFechas.add(URLServices.getInstance().getCantidadVisitasFecha(url.getDireccionAcortada(), fecha.toString()));
            }


            modelo.put("fechaAcceso", "");
            modelo.put("fechas",fechas);
            modelo.put("visitasFechas",visitasFechas);
            modelo.put("urls", URLServices.getInstance().getURLs());
            ctx.render("/vistas/templates/dashboard.html",modelo);
        });

        app.get("/dashboard/:url/:fecha",ctx->{
            modelo.put("fechaAcceso", ctx.pathParam("fecha"));
            modelo.put("clientes",URLServices.getInstance().getClientesURLByFecha(
                    ctx.pathParam("url"), ctx.pathParam("fecha")
            ));
            ctx.redirect("/dashboard/"+ctx.pathParam("url"));
        });

        

        app.get("/dashboard/usuarios", ctx -> {
            ctx.result("vista usuarios");
        });

    }
}
