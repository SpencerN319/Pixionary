package sb_3.pixionary.Utilities;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

/**
 * Created by fastn on 3/8/2018.
 */

public class RequestLeaderboard extends JsonArrayRequest {

    private static final String GAMES_URL = "http://proj-309-sb-3.cs.iastate.edu:80/somethingToDOWithLeaderboards";

    //Receiving an array of possibilities.
    public RequestLeaderboard(JSONArray jsonArray, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(Method.GET, GAMES_URL, jsonArray, listener, errorListener);

    }
}
