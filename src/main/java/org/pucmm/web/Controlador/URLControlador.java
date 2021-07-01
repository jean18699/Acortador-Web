package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.json.simple.JSONArray;
import org.pucmm.web.Modelo.URL;
import org.pucmm.web.Servicio.URLServices;

import javax.servlet.http.Cookie;
import java.net.CookieStore;
import java.util.ArrayList;
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

          /*  for(Map.Entry<String, String> url : ctx.cookieMap().entrySet())
            {
                System.out.println(url.getKey());
                System.out.println(url.getValue());
            }*/

            modelo.put("urls", ctx.cookieMap().entrySet());
            //modelo.put("urls_acortadas", ctx.cookieMap().keySet());
            ctx.render("vistas/templates/index.html",modelo);

        });


        app.post("/acortar", ctx -> {
            URLServices.getInstance().nuevaUrlAcortada(ctx.formParam("url"));

            for(int i = 0; i < URLServices.getInstance().getUrlsCliente().size(); i++)
            {
                Cookie cookie_url = new Cookie(URLServices.getInstance().getUrlsCliente().get(i).getDireccionAcortada(),URLServices.getInstance().getUrlsCliente().get(i).getOrigen());
                cookie_url.setMaxAge((int) 15768000000L); //6 meses
                ctx.res.addCookie(cookie_url);
            }





            //modelo.put("URLs", URLServices.getInstance().getURLs());


            //Guardamos la URL en el cliente hasta que el usuario se registre
           // Cookie cookie_url = new Cookie("url",ctx.formParam("url"));
             //Cookie cookie_password = new Cookie("password_recordado",encryptedPassword);
           // cookie_usuario.setMaxAge(604800); //una semana
            //cookie_password.setMaxAge(604800);


            ctx.redirect("/");



        });


        app.get("/:url",ctx -> {
            if(!ctx.pathParam("url").equalsIgnoreCase("favicon.ico"))
            {
                ctx.redirect("redireccionar/"+ctx.pathParam("url"));
            }
        });

        app.get("/redireccionar/:url", ctx -> {

            System.out.println(ctx.pathParam("url"));

            //Obteniendo el cliente (navegador) desde donde se accede
            String[] InfoNavegador = ctx.req.getHeader("sec-ch-ua").split(",");
            String[] DatosCliente = InfoNavegador[1].split(";");
            String nombreCliente = DatosCliente[0];

            URLServices.getInstance().contarVisita(ctx.pathParam("url"),nombreCliente);

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
