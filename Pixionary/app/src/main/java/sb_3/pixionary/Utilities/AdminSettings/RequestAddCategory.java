package sb_3.pixionary.Utilities.AdminSettings;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestAddCategory extends StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/add_category.php";
    private Map<String, String> parameters;

    public RequestAddCategory(String username, String password, String category_name, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("name", category_name);
    }



    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}
