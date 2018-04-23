package sb_3.pixionary.Utilities.Settings;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestViewFriends extends StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/friends_list.php";
    private Map<String, String> parameters;

    public RequestViewFriends(String username, int page,  Response.Listener<String> listener) {
        super(Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        String val = String.valueOf(page);
        parameters.put("username", username);
        parameters.put("page", val);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
