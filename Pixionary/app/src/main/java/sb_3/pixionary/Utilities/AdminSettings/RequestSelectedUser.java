package sb_3.pixionary.Utilities.AdminSettings;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spencern319 on 3/25/18.
 */

public class RequestSelectedUser extends StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/view_selected.php";
    private Map<String, String> parameters;

    public RequestSelectedUser(String username, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username", username);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}

