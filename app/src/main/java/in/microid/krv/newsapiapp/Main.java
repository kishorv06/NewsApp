package in.microid.krv.newsapiapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends AppCompatActivity {
    private API api;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        rv = findViewById(R.id.feeds_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        api = new API(this);
        if (sp.getString("news", null) == null) {
            refreshNews();
        } else {
            try {
                JSONArray news = new JSONArray(sp.getString("news", "{}"));
                loadNEWS(news);
            } catch (JSONException e) {
                onError(e);
            }
        }
        SwipeRefreshLayout swp = findViewById(R.id.feeds_swipe);
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
        ((EditText) findViewById(R.id.feed_search)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    api.searchNews(v.getText().toString(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("status").equals("ok")) {
                                    loadNEWS(response.getJSONArray("articles"));
                                } else {
                                    Toast.makeText(Main.this, "No results", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                onError(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    private void refreshNews() {
        final SwipeRefreshLayout swp = findViewById(R.id.feeds_swipe);
        swp.setRefreshing(true);
        api.getNEWS(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("ok")) {
                        loadNEWS(response.getJSONArray("articles"));
                        PreferenceManager.getDefaultSharedPreferences(Main.this)
                                .edit()
                                .putString("news", response.getJSONArray("articles").toString())
                                .apply();
                        swp.setRefreshing(false);
                    }
                } catch (Exception e) {
                    onError(e);
                    swp.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
                swp.setRefreshing(false);
            }
        });
    }

    private void loadNEWS(JSONArray news) {
        NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter(news);
        rv.setAdapter(adapter);
    }

    private void onError(Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "Failed to load news feeds.", Toast.LENGTH_SHORT).show();
    }
}
