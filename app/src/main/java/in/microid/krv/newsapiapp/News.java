package in.microid.krv.newsapiapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class News extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (!getIntent().hasExtra("data"))
            finish();

        try {
            JSONObject data = new JSONObject(getIntent().getStringExtra("data"));
            ((TextView) findViewById(R.id.news_heading)).setText(data.getString("title"));
            ((TextView) findViewById(R.id.news_source)).setText(data.getJSONObject("source").getString("name"));
            if (!data.getString("content").equals("null"))
                ((TextView) findViewById(R.id.news_content)).setText(data.getString("content"));
            if (!data.getString("urlToImage").equals("null"))
                Picasso.get()
                        .load(data.getString("urlToImage"))
                        .into((ImageView) findViewById(R.id.news_image));
            ((TextView) findViewById(R.id.news_time)).setText(getTimeAgo(data.getString("publishedAt")));
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private String getTimeAgo(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return DateUtils.getRelativeTimeSpanString(sdf.parse(time).getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        } catch (Exception e) {
            return "";
        }
    }
}
