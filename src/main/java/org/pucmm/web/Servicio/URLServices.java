package org.pucmm.web.Servicio;

import org.pucmm.web.Modelo.Cliente;
import org.pucmm.web.Modelo.URL;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static io.javalin.Javalin.log;

public class URLServices {

    private static URLServices instancia;
    private HashMap<String, String> keyMap; // key-url map
    private HashMap<String, String> valueMap;// url-key map to quickly check

    private char caracteres[]; //Variable donde almacenaremos los 62 posibles caracteres de una URL
    private int longitud_url;
    public List<URL> urlsCliente;

    GestionDb gestionDb = new GestionDb(URL.class);

    public URLServices()
    {
        keyMap = new HashMap<String, String>();
        valueMap = new HashMap<String, String>();
        urlsCliente = new ArrayList<>();
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

    public void nuevaUrlAcortada(String url)
    {
        URL nuevaURL = new URL();
        nuevaURL.setOrigen(url);
        nuevaURL.setDireccionAcortada(acortarURL(url));
        urlsCliente.add(nuevaURL);
        gestionDb.crear(nuevaURL);
    }

    public void eliminarURL(String urlAcortada)
    {
        URL url = (URL) gestionDb.find(urlAcortada);
        gestionDb.eliminar(url);
    }

    public void visitar(String urlAcortada, String navegador, String direccionIP, LocalDate fechaAcceso, String sistemaOperativo)
    {

        URL url = (URL) gestionDb.find(urlAcortada);
        Cliente cliente = new Cliente(navegador,direccionIP,fechaAcceso,sistemaOperativo);

        GestionDb gestionDbCliente = new GestionDb(Cliente.class);
        gestionDbCliente.crear(cliente);

        url.getClientes().add(cliente);

      /*  url.setVisitas(url.getVisitas()+1);

        if(navegador.contains("Google Chrome"))
        {
            url.setChrome(url.getChrome() + 1);
        }
        else if(navegador.contains("Mozilla Firefox"))
        {
            url.setFirefox(url.getFirefox() + 1);
        }
        else if(navegador.contains("Safari"))
        {
            url.setSafari(url.getSafari() + 1);
        }
        else if(navegador.contains("Opera"))
        {
            url.setOpera(url.getOpera() + 1);
        }
        else if(navegador.contains("Microsoft Edge"))
        {
            url.setEdge(url.getEdge() + 1);
        }
        else if(navegador.contains("Internet Explorer"))
        {
            url.setInternetExplorer(url.getInternetExplorer() + 1);
        }
        else if(navegador.contains("Postman"))
        {
            url.setPostman(url.getPostman() + 1);
        }
        else{
            url.setUnknownBrowser(url.getUnknownBrowser()+1);
        }
        */

        gestionDb.editar(url);

    }

    public List<URL> getURLs()
    {
        return gestionDb.findAll();
    }


    public List<URL> getUrlsCliente() {
        return urlsCliente;
    }

    public void setUrlsCliente(List<URL> urlsCliente) {
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

}
