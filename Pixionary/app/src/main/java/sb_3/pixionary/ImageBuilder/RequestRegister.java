package sb_3.pixionary.ImageBuilder;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spencern319 on 2/11/18.
 */

public class RequestRegister extends StringRequest {
    private static final String REGISTER_URL = "http://androideasily.000webhostapp.com/register.php";
    private Map<String, String> parameters;

    public RequestRegister(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
