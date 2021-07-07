package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.pucmm.web.Modelo.Cliente;
import org.pucmm.web.Modelo.URL;
import org.pucmm.web.Modelo.Usuario;
import org.pucmm.web.Servicio.URLServices;
import org.pucmm.web.Servicio.UsuarioServices;

import java.time.LocalDate;
import java.util.*;

public class DashboardControlador {

    private Javalin app;
    Map<String, Object> modelo = new HashMap<>();
    Map<String, Object> modeloVistaUsuario = new HashMap<>();
    private String dominio = "https://shorter.jgshopping.games/";
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

            if(ctx.sessionAttribute("usuario") == null || ctx.sessionAttribute("usuario")=="")
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else
            {
                Usuario user =  UsuarioServices.getInstancia().getUsuario(ctx.sessionAttribute("usuario"));

                if(user != null)
                {
                    //Colocando las URL almacenadas en la cookie dentro de la cuenta del usuario
                    for(Map.Entry<String, String> urlCliente : ctx.cookieMap().entrySet())
                    {
                        if(!urlCliente.getKey().equalsIgnoreCase("usuario_recordado") || urlCliente.getKey().equalsIgnoreCase("password_recordado"))
                        {
                            URL url = URLServices.getInstance().getURL(urlCliente.getKey());
                            if(url != null)
                            {
                                if(!UsuarioServices.getInstancia().getURLsByUsuario(user.getNombreUsuario()).contains(url))
                                {
                                    URLServices.getInstance().registrarURLUsuario(user.getNombreUsuario(),url);
                                    ctx.removeCookie(urlCliente.getKey());
                                }
                            }
                        }
                    }

                    modelo.put("clientes",null);
                    modelo.put("visitasFechas","");
                    modelo.put("visitasFechas","");
                    modelo.put("usuarioActual", UsuarioServices.getInstancia().getUsuario(user.getNombreUsuario()));
                    modelo.put("urls", UsuarioServices.getInstancia().getURLsByUsuario(user.getNombreUsuario()));
                    ctx.render("/vistas/templates/dashboard.html",modelo);
                }

            }
        });

        app.post("/dashboard/infoOtro", ctx -> {

            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else
            {
                if(UsuarioServices.getInstancia().getUsuario(ctx.formParam("verUsuario")) == null)
                {
                    ctx.result("El usuario no existe");
                }else
                {
                    modeloVistaUsuario.put("urlActual", ctx.formParam("url"));
                    modeloVistaUsuario.put("clientes",null);
                    modeloVistaUsuario.put("visitasFechas","");
                    modeloVistaUsuario.put("visitasFechas","");
                    modeloVistaUsuario.put("verUsuario", ctx.formParam("verUsuario"));
                    modeloVistaUsuario.put("usuarioActual", UsuarioServices.getInstancia().getUsuario(ctx.sessionAttribute("usuario")));
                    modeloVistaUsuario.put("urls", UsuarioServices.getInstancia().getURLsByUsuario(ctx.formParam("verUsuario")));
                    modeloVistaUsuario.put("dominio", dominio);
                    ctx.render("/vistas/templates/dashboardOtro.html",modeloVistaUsuario);
                }

            }
        });

        app.post("/dashboard/infoURLOtro",ctx -> {

            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                URL url = URLServices.getInstance().getURL(ctx.formParam("url"));
                fechas = new HashSet<>();
                visitasFechas = new ArrayList<>();

                for (Cliente cliente : url.getClientes()) {
                    LocalDate date = cliente.getFechaAcceso();
                    fechas.add(date);
                }

                for (LocalDate fecha : fechas) {
                    visitasFechas.add(URLServices.getInstance().getCantidadVisitasFecha(url.getDireccionAcortada(), fecha.toString()));
                }

                modeloVistaUsuario.put("urlActual", ctx.formParam("url"));
                modeloVistaUsuario.put("usuarioActual", UsuarioServices.getInstancia().getUsuario(ctx.sessionAttribute("usuario")));
                modeloVistaUsuario.put("fechaAcceso", "");
                modeloVistaUsuario.put("fechas", fechas);
                modeloVistaUsuario.put("visitasFechas", visitasFechas);
                modeloVistaUsuario.put("clientes", new HashSet<Cliente>());
                ctx.render("/vistas/templates/dashboardOtro.html", modeloVistaUsuario);
            }
        });


        app.get("/dashboard/infoURL", ctx -> {
            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                ctx.render("/vistas/templates/dashboard.html", modelo);
            }
        });

        app.get("/dashboard/infoURLOtro", ctx -> {
            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                ctx.render("/vistas/templates/dashboardOtro.html", modeloVistaUsuario);
            }
        });


        app.post("/dashboard/infoURL", ctx -> {
            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                URL url = URLServices.getInstance().getURL(ctx.formParam("url"));
                fechas = new HashSet<>();
                visitasFechas = new ArrayList<>();

                for (Cliente cliente : url.getClientes()) {
                    LocalDate date = cliente.getFechaAcceso();
                    fechas.add(date);
                }

                for (LocalDate fecha : fechas) {
                    visitasFechas.add(URLServices.getInstance().getCantidadVisitasFecha(url.getDireccionAcortada(), fecha.toString()));
                }

                modelo.put("urlActual", ctx.formParam("url"));
                modelo.put("fechaAcceso", "");
                modelo.put("clientes", new HashSet<Cliente>());
                modelo.put("fechas", fechas);
                modelo.put("visitasFechas", visitasFechas);
                ctx.render("/vistas/templates/dashboard.html", modelo);
            }
        });

        app.get("/dashboard/infoURL/:url/estadisticas/:fecha",ctx->{
            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                URL url = URLServices.getInstance().getURL(ctx.pathParam("url"));
                fechas = new HashSet<>();
                visitasFechas = new ArrayList<>();

                for (Cliente cliente : url.getClientes()) {
                    LocalDate date = cliente.getFechaAcceso();
                    fechas.add(date);
                }

                for (LocalDate fecha : fechas) {
                    visitasFechas.add(URLServices.getInstance().getCantidadVisitasFecha(url.getDireccionAcortada(), fecha.toString()));
                }


                modelo.put("fechaAcceso", ctx.pathParam("fecha"));
                modelo.put("fechas", fechas);
                modelo.put("visitasFechas", visitasFechas);
                modelo.put("clientes", URLServices.getInstance().getClientesURLByFecha(
                        ctx.pathParam("url"), ctx.pathParam("fecha")
                ));
                ctx.redirect("/dashboard/infoURL");
            }
        });

        app.get("/dashboard/infoURLOtro/:url/estadisticas/:fecha",ctx->{
            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                URL url = URLServices.getInstance().getURL(ctx.pathParam("url"));
                fechas = new HashSet<>();
                visitasFechas = new ArrayList<>();

                for (Cliente cliente : url.getClientes()) {
                    LocalDate date = cliente.getFechaAcceso();
                    fechas.add(date);
                }

                for (LocalDate fecha : fechas) {
                    visitasFechas.add(URLServices.getInstance().getCantidadVisitasFecha(url.getDireccionAcortada(), fecha.toString()));
                }

                modeloVistaUsuario.put("fechaAcceso", ctx.pathParam("fecha"));
                modeloVistaUsuario.put("fechas", fechas);
                modeloVistaUsuario.put("visitasFechas", visitasFechas);
                modeloVistaUsuario.put("clientes", URLServices.getInstance().getClientesURLByFecha(
                        ctx.pathParam("url"), ctx.pathParam("fecha")
                ));
                ctx.redirect("/dashboard/infoURLOtro");
            }
        });

        //Eliminacion de URLs de otros usuarios
        app.post("/url/eliminar-otro", ctx -> {
            URLServices.getInstance().eliminarURL(modeloVistaUsuario.get("verUsuario").toString(), ctx.formParam("eliminar"));
            modeloVistaUsuario.put("urls", UsuarioServices.getInstancia().getURLsByUsuario(modeloVistaUsuario.get("verUsuario").toString()));
            ctx.redirect("/dashboard/infoURLOtro");
        });

    }
}
