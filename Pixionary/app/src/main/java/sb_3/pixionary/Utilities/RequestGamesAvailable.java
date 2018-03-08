package sb_3.pixionary.Utilities;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;



/**
 * Created by Steven Rein on 3/8/2018.
 *
 *
 * This will return 10 games that match to the current "page."
 */

public class RequestGamesAvailable extends JsonArrayRequest {

    private static final String GAMES_URL = "http://proj-309-sb-3.cs.iastate.edu:80/somethingToDoWithLeaderboards";

    //Receiving an
    public RequestGamesAvailable(JSONArray jsonArray, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(Method.GET, GAMES_URL, jsonArray, listener, errorListener);

    }
}
