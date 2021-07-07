package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.eclipse.jetty.server.session.Session;
import org.pucmm.web.Modelo.URL;
import org.pucmm.web.Servicio.URLServices;
import org.pucmm.web.Servicio.UsuarioServices;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class URLControlador {

    private Javalin app;
    private String dominio = "https://shorter.jgshopping.games/";

    Map<String, Object> modelo = new HashMap<>();

    public URLControlador(Javalin app)
    {
        this.app = app;
        modelo.put("dominio",dominio);
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    public void aplicarRutas() throws NumberFormatException {

        app.get("/",ctx -> {
            modelo.put("urls", ctx.cookieMap().entrySet());
            ctx.render("vistas/templates/index.html",modelo);
        });


        app.post("/acortar", ctx -> {
            Cookie cookie_url = new Cookie(URLServices.getInstance().crearRetornarUrlAcortada(ctx.formParam("url")),ctx.formParam("url"));
            cookie_url.setMaxAge(3600*24*30*6); //6 meses
            ctx.res.addCookie(cookie_url);
            ctx.redirect("/");
        });

        app.post("/acortar-registrar", ctx -> {
            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                URLServices.getInstance().registrarURLUsuario(ctx.sessionAttribute("usuario"), URLServices.getInstance().nuevaUrlAcortada(ctx.formParam("url")));
                URLServices.getInstance().getUrlsCliente().clear();
                ctx.redirect("/dashboard");
            }
        });

        app.post("/url/eliminar", ctx -> {
            if(ctx.sessionAttribute("usuario") == null)
            {
                ctx.redirect("/usuario/iniciarSesion");
            }else {
                URLServices.getInstance().eliminarURL(ctx.sessionAttribute("vistaUsuario"), ctx.formParam("eliminar"));
                ctx.redirect("/dashboard");
            }
        });


        app.get("/:url",ctx -> {

            if(!ctx.pathParam("url").equalsIgnoreCase("favicon.ico"))
            {
                ctx.redirect("redireccionar/"+ctx.pathParam("url"));
            }
        });

        app.get("/redireccionar/:url", ctx -> {

            //Obteniendo el cliente (navegador) desde donde se accede
            String[] InfoNavegador = ctx.req.getHeader("sec-ch-ua").split(",");
            String[] DatosCliente = InfoNavegador[1].split(";");
            String nombreCliente = DatosCliente[0];
            LocalDate fechaAcceso = LocalDate.now();
            LocalTime horaAcceso = LocalTime.now();

            //Sistema operativo
            String os = System.getProperty("os.name");

            URLServices.getInstance().visitar(ctx.pathParam("url"),nombreCliente,ctx.host(),fechaAcceso, horaAcceso, os);
            String direccion = URLServices.getInstance().expandirURL(ctx.pathParam("url"));
            if(direccion.contains("https:"))
            {
                ctx.redirect(URLServices.getInstance().expandirURL(ctx.pathParam("url")));
            }else if(!direccion.contains("http:"))
            {
                ctx.redirect("http://"+ URLServices.getInstance().expandirURL(ctx.pathParam("url")));
            }

        });

    }

}
