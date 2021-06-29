package org.pucmm.web.Servicio;

import fr.plaisance.bitly.Bit;
import fr.plaisance.bitly.Bitly;
import org.pucmm.web.Controlador.URLControlador;

import java.util.HashMap;
import java.util.Random;

public class URLServices {

    private static URLServices instancia;
    private HashMap<String, String> keyMap; // key-url map
    private HashMap<String, String> valueMap;// url-key map to quickly check

    private char caracteres[]; //Variable donde almacenaremos los 62 posibles caracteres de una URL
    private int longitud_url;
    private String dominioURL; //Aqui se especifica un dominio personalizado para la URL

    public URLServices()
    {
        keyMap = new HashMap<String, String>();
        valueMap = new HashMap<String, String>();
        longitud_url = 5;
        dominioURL = "http://localhost:7000";

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


    public String acortarURL(String url)
    {
        String urlAcortada = "";
        url = formatearURL(url);

        if (valueMap.containsKey(url)) {
            url = dominioURL + "/" + valueMap.get(url);
        } else {
            urlAcortada = dominioURL + "/" + getKey(url);
        }

		return urlAcortada;
    }

    public String expandirURL(String urlAcortada)
    {
        String url = "";
        //System.out.println(urlAcortada);
        //String key = urlAcortada.substring(dominioURL.length()+1);
        url = keyMap.get(urlAcortada);
        return url;
    }


    //Funcion para que la url escrita de varias maneras siga siendo valida
    public String formatearURL(String url)
    {
        if (url.startsWith("http://"))
            url = url.substring(7);

        if (url.startsWith("https://"))
            url = url.substring(8);

        if (url.charAt(url.length() - 1) == '/')
            url = url.substring(0, url.length() - 1);
        return url;
    }


    /*
     * Get Key method
     */
    private String getKey(String longURL) {
        String key;
        key = generateKey();
        keyMap.put(key, longURL);
        valueMap.put(longURL, key);
        return key;
    }

    // generateKey
    private String generateKey() {

        Random rand = new Random();

        String key = "";
        boolean flag = true;
        while (flag) {
            key = "";
            for (int i = 0; i <= longitud_url; i++) {
                key += caracteres[rand.nextInt(62)];
            }
            // System.out.println("Iteration: "+ counter + "Key: "+ key);
            if (!keyMap.containsKey(key)) {
                flag = false;
            }
        }
        return key;
    }







}
