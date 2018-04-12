package sb_3.pixionary.Utilities.MainMenu;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spencern319 on 2/11/18.
 */

public class RequestRegister extends StringRequest {
    private static final String REGISTER_URL = "http://proj-309-sb-3.cs.iastate.edu:80/register.php";
    private Map<String, String> parameters;

    public RequestRegister(String username, String password, String user_type, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put( "user_type", user_type);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
