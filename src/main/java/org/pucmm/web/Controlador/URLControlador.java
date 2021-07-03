package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.pucmm.web.Servicio.URLServices;

import javax.servlet.http.Cookie;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class URLControlador {

    private Javalin app;
    private String dominio = "http://localhost:7000/";

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
            URLServices.getInstance().nuevaUrlAcortada(ctx.formParam("url"));
            for(int i = 0; i < URLServices.getInstance().getUrlsCliente().size(); i++)
            {
                Cookie cookie_url = new Cookie(URLServices.getInstance().getUrlsCliente().get(i).getDireccionAcortada(),URLServices.getInstance().getUrlsCliente().get(i).getOrigen());
                cookie_url.setMaxAge(157680);
                ctx.res.addCookie(cookie_url);
            }
            ctx.redirect("/");
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

            //Para obtener la direccion IP
            InetAddress ip;
            try
            {
                ip = InetAddress.getLocalHost();
                URLServices.getInstance().visitar(ctx.pathParam("url"),nombreCliente,ip.getHostAddress(),fechaAcceso, horaAcceso, os);
            }catch (UnknownHostException e) {
                e.printStackTrace();
            }

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
