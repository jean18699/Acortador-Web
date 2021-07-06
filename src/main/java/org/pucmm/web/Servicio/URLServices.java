package org.pucmm.web.Servicio;

import org.pucmm.web.Modelo.Cliente;
import org.pucmm.web.Modelo.URL;
import org.pucmm.web.Modelo.Usuario;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class URLServices {

    private static URLServices instancia;
    private HashMap<String, String> keyMap; // key-url map
    private HashMap<String, String> valueMap;// url-key map to quickly check

    private char caracteres[]; //Variable donde almacenaremos los 62 posibles caracteres de una URL
    private int longitud_url;
    private Set<URL> urlsCliente;

    GestionDb gestionDb = new GestionDb(URL.class);

    public URLServices()
    {
        keyMap = new HashMap<String, String>();
        valueMap = new HashMap<String, String>();
        urlsCliente = new HashSet<>();
        longitud_url = 5;

        String alfabeto = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int cantidad_letras = alfabeto.length();

        caracteres = new char[62];

        for(int i = 0 ; i < cantidad_letras; i++)
        {
            caracteres[i] = alfabeto.charAt(i);
        }

    }


    public static URLServices getInstance()
    {
        if(instancia == null)
        {
            instancia = new URLServices();
        }
        return instancia;
    }


    private String acortarURL(String url)
    {
        EntityManager em = gestionDb.getEntityManager();

        try{
            String urlAcortada = "";
            url = formatearURL(url);

            if(gestionDb.find(url) != null)
            {
                url = em.find(URL.class,url).getDireccionAcortada();
            }else
            {
                urlAcortada = getClave(url);
            }

            return urlAcortada;

        }finally {
            em.close();
        }


       /* if (valueMap.containsKey(url)) {
           //url = dominioURL + "/" + valueMap.get(url);
            url = valueMap.get(url);
        } else {
            //urlAcortada = dominioURL + "/" + getClave(url);
        }
*/

    }


    public String expandirURL(String urlAcortada)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        return url.getOrigen();
    }


    //Funcion para que la url escrita de varias maneras siga siendo valida
    private String formatearURL(String url)
    {
        if (url.startsWith("http://"))
            url = url.substring(7);

        if (url.startsWith("https://"))
            url = url.substring(8);

        if (url.charAt(url.length() - 1) == '/')
            url = url.substring(0, url.length() - 1);
        return url;
    }


    private String getClave(String longURL) {
        String clave = generarClave();
        keyMap.put(clave, longURL);
        valueMap.put(longURL, clave);
        return clave;
    }


    //Funcion para generar la clave de 5 digitos de la url
    private String generarClave() {

        Random rand = new Random();
        String clave = "";

        while (true) {
            clave = "";

            //Se va a ir creando una clave aleatorea
            for (int i = 0; i <= longitud_url; i++) {
                clave += caracteres[rand.nextInt(62)];
            }

            //Si la clave se encuentra en el mapa de claves (keymap) significa que ya existe y se termina la generacion
            if (!keyMap.containsKey(clave)) {
                break;
            }
        }
        return clave;



    }

    public URL nuevaUrlAcortada(String url)
    {
        URL nuevaURL = new URL();
        nuevaURL.setOrigen(url);
        nuevaURL.setDireccionAcortada(acortarURL(url));
        urlsCliente.add(nuevaURL);
        gestionDb.crear(nuevaURL);

        return nuevaURL;
    }

    public void eliminarURL(String userId, String urlAcortada)
    {
        //URL url = (URL) gestionDb.find(urlAcortada);
        GestionDb gestionUser = new GestionDb(Usuario.class);
        Usuario user = (Usuario) gestionUser.find(userId);
        for(URL url : user.getUrls())
        {
            if(url.getDireccionAcortada().equalsIgnoreCase(urlAcortada))
            {
                user.getUrls().remove(url);
            }
        }
        gestionUser.editar(user);
        gestionDb.eliminar(urlAcortada);
    }

    public void visitar(String urlAcortada, String navegador, String direccionIP, LocalDate fechaAcceso, LocalTime horaAcceso, String sistemaOperativo)
    {

        URL url = (URL) gestionDb.find(urlAcortada);
        Cliente cliente = new Cliente(navegador,direccionIP,fechaAcceso, horaAcceso, sistemaOperativo);

        GestionDb gestionDbCliente = new GestionDb(Cliente.class);
        gestionDbCliente.crear(cliente);

        url.getClientes().add(cliente);

        gestionDb.editar(url);

    }

    public List<URL> getURLs()
    {
        return gestionDb.findAll();
    }


    public Set<URL> getUrlsCliente() {
        return urlsCliente;
    }

    public void setUrlsCliente(Set<URL> urlsCliente) {
        this.urlsCliente = urlsCliente;
    }

    public long getCantidadVisitas(String urlAcortada)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        if(url != null)
        {
            return url.getClientes().size();
        }
        else
        {
            return 0;
        }
    }

    public long getCantidadVisitasFecha(String urlAcortada, String fecha)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        long contador = 0;
        for(Cliente cliente : url.getClientes())
        {
            if(cliente.getFechaAcceso().toString().equalsIgnoreCase(fecha))
            {
                contador++;
            }
        }
        return contador;
    }

    public ArrayList<LocalTime> getHorasFecha(String urlAcortada, String fecha)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        ArrayList<LocalTime> horas = new ArrayList<>();
        for(Cliente cliente : url.getClientes())
        {
            if(cliente.getFechaAcceso().toString().equalsIgnoreCase(fecha))
            {
                horas.add(cliente.getHoraAcceso());
            }
        }
        return horas;
    }

    public long getCantidadVisitasNavegador(String urlAcortada, String navegador)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        long contador = 0;
        for(Cliente cliente : url.getClientes())
        {
            if(cliente.getNavegador().toString().equalsIgnoreCase(navegador))
            {
                contador++;
            }
        }
        return contador;
    }

    public URL getURL(String urlAcortada)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        return url;
    }

    public Set<Cliente> getClientesURLByFecha(String urlAcortada, String fecha)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        Set<Cliente> clientes = new HashSet<>();

        for(Cliente cliente : url.getClientes())
        {
            if(cliente.getFechaAcceso().toString().equals(fecha))
            {
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    public void registrarURLUsuario(String idUser, URL url)
    {
        GestionDb gestionUsuario = new GestionDb(Usuario.class);
        Usuario user = (Usuario) gestionUsuario.find(idUser);

        if(gestionDb.find(url.getDireccionAcortada()) == null)
        {
            gestionDb.crear(url);
        }

        user.getUrls().add(url);
        gestionDb.editar(user);
    }

}
