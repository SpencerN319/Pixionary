package sb_3.pixionary.Utilities;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spencern319 on 2/11/18.
 */

public class RequestProfile extends StringRequest {
    private static final String REGISTER_URL = "http://proj-309-sb-3.cs.iastate.edu:80/profile.php";
    private Map<String, String> parameters;

    public RequestProfile(String username, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username", username);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
