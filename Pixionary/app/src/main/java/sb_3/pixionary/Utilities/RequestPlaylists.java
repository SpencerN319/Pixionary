package sb_3.pixionary.Utilities;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fastn on 3/11/2018.
 */

public class RequestPlaylists extends StringRequest {
    private static final String CATEGORIES_URL = "http://proj-309-sb-3.cs.iastate.edu:80/category.php";
    private Map<String, String> parameters;

    public RequestPlaylists(String username, int pageRequested, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, CATEGORIES_URL, listener, errorListener);
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