package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.pucmm.web.Modelo.Usuario;
import org.pucmm.web.Servicio.UsuarioServices;

import java.util.HashMap;
import java.util.Map;

public class UsuarioControlador {

    private Javalin app;
    Map<String, Object> modelo = new HashMap<>();

    public UsuarioControlador(Javalin app)
    {
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    public void aplicarRutas() throws NumberFormatException {

        app.get("/usuario/registrarse",ctx ->{

            ctx.render("/vistas/templates/registro.html");

        });

        app.post("/usuario/registrarse",ctx ->{

                Usuario user = new Usuario(
                        ctx.formParam("usuario"),
                        ctx.formParam("password"),
                        ctx.formParam("nombre"));

                if(UsuarioServices.getInstancia().registrarUsuario(user) != null)
                {
                    ctx.redirect("/dashboard");
                }
                else {
                    ctx.redirect("/usuario/registrarse");
                }
        });

        app.get("/usuario/iniciarSesion",ctx ->{
            ctx.render("/vistas/templates/login.html");
        });

        app.post("/usuario/iniciarSesion",ctx ->{

            if (UsuarioServices.getInstancia().getUsuario(ctx.formParam("nombreUsuario")) != null) { //Si el usuario existe...
                if (!UsuarioServices.getInstancia().getUsuario(ctx.formParam("nombreUsuario")).getPassword().equals(ctx.formParam("password"))) { //Si sus credenciales NO son correctas...
                    ctx.redirect("/usuario/iniciarSesion");
                }
                else{ //Si las credenciales del usuario son correctas...
                    ctx.sessionAttribute("usuario", ctx.formParam("nombreUsuario"));
                    ctx.redirect("/dashboard");
                }
            }


        });


    }

}
