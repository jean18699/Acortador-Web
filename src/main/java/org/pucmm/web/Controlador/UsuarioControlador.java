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

public class UsuarioControlador {

    private Javalin app;
    private String dominio = "http://localhost:7000/";
    Map<String, Object> modelo = new HashMap<>();

    public UsuarioControlador(Javalin app){
        this.app = app;
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        modelo.put("dominio", dominio);
    }

    public void aplicarRutas() throws NumberFormatException {

    }
}
