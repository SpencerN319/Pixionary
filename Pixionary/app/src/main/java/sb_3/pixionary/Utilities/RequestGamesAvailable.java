package sb_3.pixionary.Utilities;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Steven Rein on 3/8/2018.
 *
 *
 * This will return 10 games that match to the current "page."
 */

public class RequestGamesAvailable extends StringRequest {

    private static final String GAMES_URL = "http://proj-309-sb-3.cs.iastate.edu:80/active.php";
    private Map<String, String> parameters;

    public RequestGamesAvailable(String username, int pageRequested, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, GAMES_URL, listener, errorListener);
        String page = String.valueOf(pageRequested);
        parameters = new HashMap<>();
        parameters.put("usernane", username);
        parameters.put("page", page);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
