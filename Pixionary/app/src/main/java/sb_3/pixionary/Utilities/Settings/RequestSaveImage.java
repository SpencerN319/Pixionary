package sb_3.pixionary.Utilities.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestSaveImage extends StringRequest{
    private static final String LOGIN_URL = "http://proj-309-sb-3.cs.iastate.edu:80/save_image.php";
    private Map<String, String> parameters;

    public RequestSaveImage(String image, String word, String category, String username, String file_name, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("image", image);
        parameters.put("word", word);
        parameters.put("category", category);
        parameters.put("username", username);
        parameters.put("file_name", file_name);
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
