package org.pucmm.web.Servicio;

import org.pucmm.web.Modelo.URL;
import org.pucmm.web.Modelo.Usuario;

import java.util.List;
import java.util.Set;

public class UsuarioServices {

    private static UsuarioServices instancia;
    private GestionDb gestionDb;

    public UsuarioServices(){
        gestionDb = new GestionDb(Usuario.class);

        //Creando el admin
        if(gestionDb.find("admin")== null)
        {
            Usuario admin = new Usuario("admin","admin","Administrador");
            gestionDb.crear(admin);
        }

    }

    public static UsuarioServices getInstancia()
    {
        if(instancia == null)
        {
            instancia = new UsuarioServices();
        }
        return instancia;
    }

    public Usuario registrarUsuario(Usuario user)
    {
        gestionDb.crear(user);

        return user;
    }
    public void eliminarUsuario(Usuario user)
    {
        gestionDb.eliminar(user);
    }


    public Usuario getUsuario(String idUser)
    {
        return (Usuario) gestionDb.find(idUser);
    }

    public List<Usuario> getAllUsuarios(Usuario user)
    {
        return gestionDb.findAll();
    }

    public Set<URL> getURLsByUsuario(String idUser)
    {
        return ((Usuario) gestionDb.find(idUser)).getUrls();
    }

}
