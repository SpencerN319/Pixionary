package sb_3.pixionary.Utilities.AdminSettings;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spencern319 on 3/25/18.
 */

public class RequestViewUsers extends StringRequest{
    private static final String CATEGORIES_URL = "http://proj-309-sb-3.cs.iastate.edu:80/admin_ViewUsers.php";
    private Map<String, String> parameters;

    public RequestViewUsers(int pageRequested, Response.Listener<String> listener) {
        super(Request.Method.POST, CATEGORIES_URL, listener, null);
        String page = String.valueOf(pageRequested);
        parameters = new HashMap<>();
        parameters.put("page", page);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
