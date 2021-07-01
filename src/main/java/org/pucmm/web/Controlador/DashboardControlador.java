package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;

public class DashboardControlador {

    private Javalin app;

    public DashboardControlador(Javalin app)
    {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }


    public void aplicarRutas() throws NumberFormatException {

        app.get("/dashboard", ctx -> {
            ctx.render("/vistas/templates/index.html");
        });


    }
}
