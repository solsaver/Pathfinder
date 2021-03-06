package edu.calvin.cs262.prototype.client;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import edu.calvin.cs262.prototype.models.Building;
import edu.calvin.cs262.prototype.models.Floor;
import edu.calvin.cs262.prototype.models.Room;

/**
 * PathfinderClient is a singleton class which handles communication with the server.
 * It provides methods to get building and room model objects.
 * To instantiate PathfinderClient, call the static method PathfinderClient.getInstance()
 * Created by Derek Dik (drd26) on 11/23/2015.
 */
public class PathfinderClient {
    private static PathfinderClient instance;
    private Map<String, Building> demoBuildings;
    private Map<String, Floor[]> demoFloors;
    private static String BUILDING_URI = "http://153.106.75.120:9998/pathfinder/building";
    private static String buildingResults = "";


    private PathfinderClient(){
        demoBuildings = new HashMap<String, Building>();
        demoFloors = new HashMap<String, Floor[]>();
        demoBuildings.put("SB", new Building(0, "SB", 42.931003, -85.588937, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-1.gif"));
        demoFloors.put("SB", new Floor[]{new Floor(0, 0, 0, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-0.gif"),
                new Floor(0, 0, 1, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-1.gif"),
                new Floor(0, 0, 2, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-2.gif"),
                new Floor(0, 0, 3, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-3.gif")
        });
        demoBuildings.put("NH", new Building(1, "NH", 42.931739, -85.588872, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-1.gif"));
        demoBuildings.put("DH", new Building(2, "DH", 42.931010, -85.588801, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-1.gif"));
        demoBuildings.put("SC", new Building(2, "SC", 42.930323, -85.589324, "https://raw.githubusercontent.com/CS262B/Pathfinder/master/materials/Floor%20Plans/SB-1.gif"));
    }

    public static PathfinderClient getInstance(){
        if(instance == null){
            instance = new PathfinderClient();
        }
        return instance;
    }

    /**
     * getBuilding() method retrieves a given building from the server.
     * @param name
     * @throws NullPointerException Throws Null Pointer exception if no object is found
     * @return Building model object
     */
    public Building getBuilding(String name) throws NullPointerException {
        BUILDING_URI += "?name=" + name;
        new LongRunningGetIO().execute();
        // Placeholder method looks up the building in a dictionary
        Building building;
        try {
            building = new Building(buildingResults);
        } catch (Exception e) {
            building = demoBuildings.get(name);
            if (building == null) {
                throw new NullPointerException("No buidling with given name");
            }

    }
        return building;
    }

    /**
     * getFloor() method retrieves a given building floor from the server.
     * @param buildingName
     * @param floorNum
     * @return Floor model object
     */
    public Floor getFloor(String buildingName, int floorNum) throws NullPointerException{
        // TODO: Replace return statement of getFloorByBuilding with actual get method from server
        return demoFloors.get(buildingName)[floorNum];
    }

    /**
     * getRoom() method retrieves a given room from the server.
     * @param buildingName Building name or code
     * @param roomNum Specific room number
     * @return Room model object
     */
    public Room getRoom(String buildingName, int roomNum){
        // TODO: Replace with queries to find correct room
        return new Room(0, 1, 100, 100, roomNum);
    }

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {

        /**
         * This method extracts text from the HTTP response entity.
         *
         * @param entity
         * @return
         * @throws IllegalStateException
         * @throws IOException
         */
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        /**
         * This method issues the HTTP GET request.
         *
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(BUILDING_URI);
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }

        /**
         * The method takes the results of the request, when they arrive, and updates the interface.
         *
         * @param results
         */
        protected void onPostExecute(String results) {
            if (results != null) {
                buildingResults = results;
            }
        }
    }
}
