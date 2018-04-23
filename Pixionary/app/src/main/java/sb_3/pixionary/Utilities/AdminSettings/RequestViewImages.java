package sb_3.pixionary.Utilities.AdminSettings;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestViewImages extends StringRequest{
    private static final String CATEGORIES_URL = "http://proj-309-sb-3.cs.iastate.edu:80/view_image.php"; //TODO add url
    private Map<String, String> parameters;

    public RequestViewImages(int pageRequested, String category_name, Response.Listener<String> listener) {
        super(Request.Method.POST, CATEGORIES_URL, listener, null);
        String page = String.valueOf(pageRequested);
        parameters = new HashMap<>();
        parameters.put("page", page);
        parameters.put("category", category_name);
    }


    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
