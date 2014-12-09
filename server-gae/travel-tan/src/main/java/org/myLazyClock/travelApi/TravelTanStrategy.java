package org.myLazyClock.travelApi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * Created by david on 03/12/14.
 */
public class TravelTanStrategy implements TravelStrategy {
    public static final int ID = 2;
    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "traveling with tan";
    }



    @Override
    public TravelDuration getDuration(String from, String to, Date dateArrival, Map<String, String> param)  {

        int travelTime=0;
        String idFrom=this.setFromId(from);
        String idTo=this.setToId(to);
        String urlItineraire="https://www.tan.fr/ewp/mhv.php/itineraire/resultat.json?";


        //utilisation d'un encodage pour les symbole | présent dans l'url / voir pour la date
        urlItineraire=urlItineraire+"depart="+ URLEncoder.encode(idFrom)+"&arrive="+URLEncoder.encode(idTo)+""+"&type=1&accessible=0&temps="+dateArrival+"&retour=0";
        urlItineraire=urlItineraire.replace("+","%20");

        URL url = null;
        HttpURLConnection request = null;
        JsonObject root=null;
        try {
            url = new URL(urlItineraire);
            request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            root = jp.parse(new InputStreamReader((InputStream) request.getContent()))
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject();


        String strTime= root.get("duree").getAsString();

        if(strTime.contains("mn")){
            strTime=strTime.replace(" mn","");
            travelTime=(Integer.parseInt(strTime)*60);

        }else if (strTime.contains(":")){ // au dela d'une heure format hh:mm

            String[] TimeParts=strTime.split(":");
            travelTime=Integer.parseInt(TimeParts[0])*3600+Integer.parseInt(TimeParts[1])*60;

        }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
         return new TravelDuration(travelTime);
}


    /**
     * méthode de récupération de l'id fromId sur l'api tan
     *
     **/

    private String setFromId(String from) {

        from=from.replace("\\s","%20");
        String urlAdressFrom="https://www.tan.fr/ewp/mhv.php/itineraire/address.json?nom="+from+"&prefix=depart";
        String fromId="";
        URL url = null;
        HttpURLConnection request = null;
        try {
            url = new URL(urlAdressFrom);
            request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray().get(0).getAsJsonObject();
            JsonObject adresseFrom = root.get("lieux").getAsJsonArray().get(0).getAsJsonObject();
            fromId+=adresseFrom.get("id").getAsString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fromId;
    }

    /**méthode de récupération de l'id toId sur l'api tan
     * */
    private String setToId(String to){

        to=to.replace("\\s","%20");
        String urlAdressTo="https://www.tan.fr/ewp/mhv.php/itineraire/address.json?nom="+to+"&prefix=arrivee";
        String toId="";
        try {
            URL url = new URL(urlAdressTo);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray().get(0).getAsJsonObject();
            JsonObject adresseFrom = root.get("lieux").getAsJsonArray().get(0).getAsJsonObject();
            toId+=adresseFrom.get("id").getAsString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return toId;
    }
}
