package sb_3.pixionary.Utilities;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spencern319 on 3/24/18.
 */

public class RequestDeleteAccount extends  StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/delete_account.php";
    private Map<String, String> parameters;

    public RequestDeleteAccount(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
