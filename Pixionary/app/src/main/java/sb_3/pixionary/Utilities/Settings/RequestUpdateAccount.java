package sb_3.pixionary.Utilities.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spencern319 on 3/24/18.
 */

public class RequestUpdateAccount extends  StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/update_account.php";
    private Map<String, String> parameters;

    public RequestUpdateAccount(String new_username, String new_password, String old_username, String old_password, String change, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("new_username", new_username);
        parameters.put("new_password", new_password);
        parameters.put("old_username", old_username);
        parameters.put("old_password", old_password);
        parameters.put("action", change);
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
