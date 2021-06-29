package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

public class URLControlador {

    private Javalin app;

    public URLControlador(Javalin app)
    {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }


    public void aplicarRutas() throws NumberFormatException {

        app.post("/:URL", ctx -> {



        });


    }

}
