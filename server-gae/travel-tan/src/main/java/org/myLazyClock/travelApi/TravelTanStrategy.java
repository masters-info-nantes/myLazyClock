package org.myLazyClock.travelApi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.myLazyClock.travelApi.exception.TravelNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created on 03/12/14.
 *
 * @author david, dralagen
 */
public class TravelTanStrategy implements TravelStrategy {

    public static final TravelId ID = TravelId.TAN;

    @Override
    public TravelId getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "traveling with tan";
    }

    /**
     * Calculating the travel duration with the Tan API
     *
     * @param from  Address of depart
     * @param to    Address of arrival
     * @param dateArrival  Address date when you want arrive
     * @param params Not used for this module
     * @return the travel duration calculation by the Tan API
     * @throws TravelNotFoundException
     */
    @Override
    public TravelDuration getDuration(String from, String to, Date dateArrival, Map<String, String> params) throws TravelNotFoundException {

        long travelTime=0;
        String idFrom;
        String idTo;
        try {
            idFrom = setFromId(from);
            idTo = setToId(to);
        } catch (IOException e) {
            throw new TravelNotFoundException(e);
        }

        String urlItineraire="https://www.tan.fr/ewp/mhv.php/itineraire/resultat.json?";

        // Use Date format with yyyy-mm-ddhh:mm for compatibility with tan format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");

        //utilisation d'un encodage pour les symbole | présent dans l'url / voir pour la date
        urlItineraire += "depart="+ idFrom
                + "&arrive="+ idTo
                + "&type=1&accessible=0"
                + "&temps="+dateFormat.format(dateArrival)
                + "&retour=0";

        try {

            URL url = new URL(urlItineraire);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent()))
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject();


            String strTime= root.get("duree").getAsString();

            if(strTime.contains("mn")){
                strTime=strTime.replace(" mn","");
                travelTime=(Integer.parseInt(strTime)*60);

            }else if (strTime.contains(":")){ // au dela d'une heure format hh:mm

                String[] TimeParts=strTime.split(":");
                travelTime=Long.parseLong(TimeParts[0])*3600+Long.parseLong(TimeParts[1])*60;

            }
        } catch (IOException e) {
            throw new TravelNotFoundException(e);
        } catch (IndexOutOfBoundsException e) {
            throw new TravelNotFoundException();
        }

        return new TravelDuration(travelTime);
    }


    /**
     * Méthode de récupération de l'id fromId sur l'api tan
     *
     */
    private String setFromId(String from) throws IOException, TravelNotFoundException {
        return getId(from, "depart");
    }

    /**
     * Méthode de récupération de l'id toId sur l'api tan
     *
     */
    private String setToId(String to) throws IOException, TravelNotFoundException {

        return getId(to, "arrivee");

    }

    private String getId(String nom, String prefix) throws IOException, TravelNotFoundException {
        String urlAddress="https://www.tan.fr/ewp/mhv.php/itineraire/address.json?nom="+URLEncoder.encode(nom, "UTF-8")+"&prefix="+prefix;

        URL url = new URL(urlAddress);

        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        try {
            JsonParser jp = new JsonParser();
            JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray().get(0).getAsJsonObject();
            JsonObject address = root.get("lieux").getAsJsonArray().get(0).getAsJsonObject();
            try {
                return URLEncoder.encode(address.get("id").getAsString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return address.get("id").getAsString().replace("|", "%7C");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new TravelNotFoundException(prefix + " not found");
        }
    }
}
