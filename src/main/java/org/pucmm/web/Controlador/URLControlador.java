package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.pucmm.web.Servicio.URLServices;

public class URLControlador {

    private Javalin app;

    public URLControlador(Javalin app)
    {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    public void aplicarRutas() throws NumberFormatException {

        app.get("/acortar/*", ctx -> {
            String url = ctx.req.getPathInfo().substring(9);
            String urlAcortada = URLServices.getInstance().acortarURL(url);
            System.out.println(urlAcortada);
        });

        app.get("/:url", ctx -> {
           ctx.redirect("http://"+URLServices.getInstance().expandirURL(ctx.pathParam("url")));
        });


    }

}
