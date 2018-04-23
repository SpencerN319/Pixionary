package sb_3.pixionary.Utilities.AdminSettings;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestHosts extends StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/get_host_requests.php";
    private Map<String, String> parameters;

    public RequestHosts(Response.Listener<String> listener) {
        super(Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
