package sb_3.pixionary.Utilities.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steven Rein on 3/8/2018.
 */
public class RequestLeaderboard extends StringRequest {

    private static final String LEADERBOARD_URL = "http://proj-309-sb-3.cs.iastate.edu:80/leaderboard.php";
    private Map<String, String> parameters;

    //Actually needs to return a JSONarray just convert it to string before sending. -- Note for the server side.
    public RequestLeaderboard(String username, int pageRequested, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, LEADERBOARD_URL, listener, errorListener);
        String page = String.valueOf(pageRequested);
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("page", page);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}