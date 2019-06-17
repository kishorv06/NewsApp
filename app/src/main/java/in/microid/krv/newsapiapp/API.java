package in.microid.krv.newsapiapp;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class API {
    private RequestQueue queue = null;
    private Context context = null;

    API(Context c) {
        context = c;
        queue = Volley.newRequestQueue(c);
    }

    public void getNEWS(Response.Listener<JSONObject> success, Response.ErrorListener error) {
        sendRequest("country=in", success, error);
    }

    private void sendRequest(String query, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String url = context.getResources().getString(R.string.api_url)
                + "apiKey=" + context.getResources().getString(R.string.api_token)
                + "&" + query;
        JsonRequest request = new JsonObjectRequest(url, null, success, error);
        queue.add(request);
    }
}
