package sb_3.pixionary.Utilities;

import com.android.volley.RequestQueue;

/**
 * Created by spencern319 on 2/18/18.
 */

public class RequestLeaderboard{
    private static final String URL = ""; //TODO add PHP to pull leaderboard
    RequestQueue requestQueue;

    /*
    public RequestLeaderboard(){
        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
        JsonObjectRequest obreq = new JsonObjectRequest(URL, Request.Method.GET, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("colorObject");


                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON object request "obreq" to the request queue
        requestQueue.add(obreq);
    } */
}
