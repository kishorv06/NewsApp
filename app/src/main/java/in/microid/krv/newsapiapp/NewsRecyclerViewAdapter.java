package in.microid.krv.newsapiapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            customViewHolder.description.setText(feedItem.getString("description"));
            customViewHolder.source.setText(feedItem.getJSONObject("source").getString("name"));
            if (feedItem.getString("urlToImage") != null)
                Picasso.get()
                        .load(feedItem.getString("urlToImage"))
                        .into(customViewHolder.image);
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
}