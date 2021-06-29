package org.pucmm.web.Servicio;

public class URLServices {

    String

    public URLServices()
    {}

    public String getURLAcortada(String url)
    {
        var url = UrlShortener.Url.get(shortUrl, {
                projection: 'ANALYTICS_CLICKS'
  });
        Logger.log('The URL received %s clicks this week.', url.analytics.week.shortUrlClicks);

        return null;
    }

}
