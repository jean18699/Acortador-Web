package org.pucmm.web.Servicio;

import org.pucmm.web.Modelo.URL;
import org.pucmm.web.Modelo.Usuario;

import javax.persistence.EntityManager;
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
            Usuario admin = new Usuario("admin","admin","Administrador",true);
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

    public List<Usuario> getAllUsuarios()
    {
        return gestionDb.findAll();
    }

    public Set<URL> getURLsByUsuario(String idUser)
    {
        return ((Usuario) gestionDb.find(idUser)).getUrls();
    }

    public void asignarAdmin(String nombreUsuario)
    {
        Usuario user = (Usuario) gestionDb.find(nombreUsuario);
        if(user.isAdmin())
        {
            user.setAdmin(false);
        }else
        {
            user.setAdmin(true);
        }

        gestionDb.editar(user);
    }

    public Usuario editarUsuario(String idUser, String nombre, String password, boolean admin)
    {
        Usuario user = (Usuario) gestionDb.find(idUser);
        EntityManager em = gestionDb.getEntityManager();
        try{
            em.getTransaction().begin();
            user.setAdmin(admin);
            user.setNombre(nombre);
            user.setPassword(password);
            em.merge(user);
            em.getTransaction().commit();
        }finally
        {
            em.close();
        }

        return user;
    }

    public void eliminarUsuario(String idUser)
    {
        gestionDb.eliminar(idUser);
    }

}
