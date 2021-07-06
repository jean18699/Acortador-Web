package org.pucmm.web.Controlador;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.pucmm.web.Modelo.Usuario;
import org.pucmm.web.Servicio.UsuarioServices;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

public class UsuarioControlador {

    private Javalin app;
    Map<String, Object> modelo = new HashMap<>();
    Usuario usuarioSeleccionado;
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

            if(UsuarioServices.getInstancia().getUsuario(ctx.formParam("usuario")) != null)
            {
                ctx.result("El usuario ya existe");
            }else {
                Usuario user = new Usuario(ctx.formParam("usuario"), ctx.formParam("password"), ctx.formParam("nombre"), false);

                if (UsuarioServices.getInstancia().registrarUsuario(user) != null) {
                    ctx.redirect("/dashboard");
                } else {
                    ctx.redirect("/usuario/registrarse");
                }
            }
        });

        app.post("/usuario/registrarse-dashboard",ctx ->{

            boolean isAdmin;

            if(ctx.formParam("admin").equalsIgnoreCase("on"))
            {
                isAdmin = true;
            }else
            {
                isAdmin = false;
            }

            if(UsuarioServices.getInstancia().getUsuario(ctx.formParam("usuario")) != null)
            {
                ctx.result("El usuario ya existe");
            }else
            {
                Usuario user = new Usuario(ctx.formParam("usuario"), ctx.formParam("password"), ctx.formParam("nombre"), isAdmin);

                if(UsuarioServices.getInstancia().registrarUsuario(user) != null)
                {
                    ctx.redirect("/dashboard/usuarios");
                }
                else {
                    ctx.redirect("/usuario/crear");
                }
            }
        });


        app.get("/usuario/iniciarSesion",ctx ->{

            if(ctx.cookie("usuario_recordado") != null && ctx.cookie("password_recordado") !=null)
            {
                ctx.sessionAttribute("usuario", ctx.cookie("usuario_recordado"));
                ctx.sessionAttribute("vistaUsuario", ctx.cookie("usuario_recordado"));
                ctx.redirect("/dashboard");
            }else
            {
                ctx.render("/vistas/templates/login.html");
            }

        });

        app.get("/usuario/cerrarSesion",ctx ->{

            Cookie cookie_usuario = new Cookie("usuario_recordado",ctx.formParam(""));
            Cookie cookie_password = new Cookie("password_recordado",ctx.formParam(""));
            cookie_usuario.setMaxAge(0);
            cookie_password.setMaxAge(0);
            ctx.res.addCookie(cookie_usuario);
            ctx.res.addCookie(cookie_password);

            if(ctx.req.getSession() != null)
            {
                ctx.sessionAttribute("usuario", null);
                ctx.sessionAttribute("vistaUsuario", null);
            }

            ctx.redirect("/");

        });


        app.post("/usuario/iniciarSesion",ctx ->{


            if (UsuarioServices.getInstancia().getUsuario(ctx.formParam("nombreUsuario")) != null) { //Si el usuario existe...
                if (!UsuarioServices.getInstancia().getUsuario(ctx.formParam("nombreUsuario")).getPassword().equals(ctx.formParam("password"))) { //Si sus credenciales NO son correctas...
                    ctx.redirect("/usuario/iniciarSesion");
                }
                else{ //Si las credenciales del usuario son correctas...

                    //Guardamos el usuario en una cookie
                    if(ctx.formParam("recordar") != null)
                    {
                        if(ctx.formParam("recordar").equals("on"))
                        {

                            StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
                            String encryptedPassword = passwordEncryptor.encryptPassword(ctx.formParam("password"));

                            Cookie cookie_usuario = new Cookie("usuario_recordado",ctx.formParam("nombreUsuario"));
                            Cookie cookie_password = new Cookie("password_recordado",encryptedPassword);
                            cookie_usuario.setMaxAge(604800); //una semana
                            cookie_password.setMaxAge(604800);

                            ctx.res.addCookie(cookie_usuario);
                            ctx.res.addCookie(cookie_password);
                        }
                    }

                    ctx.sessionAttribute("usuario", ctx.formParam("nombreUsuario"));
                    ctx.sessionAttribute("vistaUsuario", ctx.formParam("nombreUsuario")); //Una variable separada para ver a un usuario diferente
                    ctx.redirect("/dashboard");
                }
            }
        });

        app.get("/dashboard/usuarios", ctx -> {
            modelo.put("usuarioActual",ctx.sessionAttribute("usuario"));
            modelo.put("usuarios",UsuarioServices.getInstancia().getAllUsuarios());

            if(modelo.get("selected") == null)
            {
                modelo.put("selected", new Usuario("","","",false));
            }
            ctx.render("/vistas/templates/users.html",modelo);
        });


        app.get("/usuario/crear",ctx -> {
            ctx.render("/vistas/templates/createUser.html",modelo);
        });

        app.post("/usuario/verUsuario",ctx -> {
            Usuario user = UsuarioServices.getInstancia().getUsuario(ctx.formParam("usuarioLista"));
            modelo.put("selected",user);
            ctx.redirect("/dashboard/usuarios");
        });

        app.post("/usuario/eliminar",ctx -> {
            UsuarioServices.getInstancia().eliminarUsuario(ctx.formParam("eliminar"));
            ctx.redirect("/dashboard/usuarios");
        });

        app.post("usuario/editar",ctx -> {

           if(modelo.get("usuarioActual") == "admin")
           {
               UsuarioServices.getInstancia().editarUsuario(
                       ctx.formParam("usuario"),ctx.formParam("nombre"),ctx.formParam("password"),true);
           }else
           {
               UsuarioServices.getInstancia().editarUsuario(
                       ctx.formParam("usuario"),ctx.formParam("nombre"),ctx.formParam("password"),Boolean.parseBoolean(ctx.formParam("admin")));
           }

            ctx.redirect("/dashboard/usuarios");
            modelo.put("selected", new Usuario("","","",false));
        });

    }

}
