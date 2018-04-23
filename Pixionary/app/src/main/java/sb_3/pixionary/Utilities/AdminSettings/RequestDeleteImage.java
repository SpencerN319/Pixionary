package sb_3.pixionary.Utilities.AdminSettings;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestDeleteImage extends StringRequest {
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/RequestDeleteImage.php";
    private Map<String, String> parameters;

    public RequestDeleteImage(String category, String word, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("word", word);
        parameters.put("category", category);
    }



    @Override
    protected Map<String, String> getParams()  {
        return parameters;
    }
}


