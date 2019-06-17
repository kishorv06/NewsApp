package in.microid.krv.newsapiapp;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.CustomViewHolder> {
    private JSONArray feedItemList;

    NewsRecyclerViewAdapter(JSONArray feedItemList) {
        this.feedItemList = feedItemList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_news, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

        try {
            JSONObject feedItem = feedItemList.getJSONObject(i);
            customViewHolder.heading.setText(feedItem.getString("title"));
            if (!feedItem.getString("description").equals("null"))
                customViewHolder.description.setText(feedItem.getString("description"));
            else
                customViewHolder.description.setText("");
            customViewHolder.source.setText(feedItem.getJSONObject("source").getString("name"));
            if (!feedItem.getString("urlToImage").equals("null")) {
                Picasso.get()
                        .load(feedItem.getString("urlToImage"))
                        .into(customViewHolder.image);
                customViewHolder.image.setVisibility(View.VISIBLE);
            } else {
                customViewHolder.image.setVisibility(View.GONE);
            }
            customViewHolder.time.setText(getTimeAgo(feedItem.getString("publishedAt")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.length() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView heading, description, source, time;

        CustomViewHolder(View view) {
            super(view);
            this.image = view.findViewById(R.id.list_item_image);
            this.heading = view.findViewById(R.id.list_item_heading);
            this.description = view.findViewById(R.id.list_item_description);
            this.source = view.findViewById(R.id.list_item_source);
            this.time = view.findViewById(R.id.list_item_time);
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