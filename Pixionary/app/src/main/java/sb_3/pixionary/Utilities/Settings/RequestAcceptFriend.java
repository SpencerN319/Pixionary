package sb_3.pixionary.Utilities.Settings;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestAcceptFriend extends StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/accept_friend.php";
    private Map<String, String> parameters;

    public RequestAcceptFriend(String username, String friend,  Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("friend", friend);
    }



    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
