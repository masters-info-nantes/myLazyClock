import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.myLazyClock.travelApi.TravelDuration;
import org.myLazyClock.travelApi.TravelStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * Created by david on 03/12/14.
 */

public class travelGmapStrategy implements TravelStrategy {

    public static final int ID = 1;

    @Override
    public Integer getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "traveling with Gmap";
    }

    @Override
    public TravelDuration getDuration(String from, String to, Date dateArrival, Map<String, String> param) {

        String requestURI=this.constructGoogleRequestURI(from,to,param);
        int travelTime=0;

        try {
            URL url = new URL(requestURI);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // création d'un objet Json associer au retour de l'url
            JsonParser jp = new JsonParser();
            //on ce place a la racine
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject();

            // les méthodes get sur un JsonObjet renvoie un JsonArray dans le cas ou sa n'est pas une "feuille"
            JsonArray routesArray = (JsonArray) jp.parse(rootobj.get("routes").toString());
            JsonElement routes = jp.parse(routesArray.get(0).toString());
            JsonObject routesObj = routes.getAsJsonObject();

            JsonArray legsArray = (JsonArray) jp.parse(routesObj.get("legs").toString());
            JsonElement legs = jp.parse(legsArray.get(0).toString());
            JsonObject legsObj = legs.getAsJsonObject();

            // on est sur une "feuille" l'objet est directement interprété par le parseur.
            JsonObject durationObj = (JsonObject) jp.parse(legsObj.get("duration").toString());
            travelTime= durationObj.get("value").getAsInt();


        }catch (IOException e) {
            e.getMessage();
        }

        return new TravelDuration(travelTime);
    }


    /** Méthode de mise en place de l'url d'appel pour le calcul d'itinéraire
     * @return void;
     */

    private String constructGoogleRequestURI(String from,String to,Map<String,String> param){
        from=from.replaceAll("\\s","");
        to=to.replaceAll("\\s","");

        String googleUri = "https://maps.googleapis.com/maps/api/directions/json?"
                                    + "origin=" + from
                                    + "&destination="+ to;

        for(String key:param.keySet()){
            googleUri+="&"+key+"="+param.get(key);
        }

        return googleUri;
    }


}
