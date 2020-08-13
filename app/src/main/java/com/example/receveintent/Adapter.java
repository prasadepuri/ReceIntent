package com.example.receveintent;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import me.prasad.myreff.R;
//import com.orangefarm.naarinja.R;


public class Adapter extends RecyclerView.Adapter<Adapter.DataViewHolder> {
    private boolean bVideoIsBeingTouched = false;
    private Handler mHandler = new Handler();
    public static Context context;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    public ArrayList<ArticlesInfo> appData=new ArrayList<ArticlesInfo>();
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.articles_model,parent,false);
        DataViewHolder viewHolder=new DataViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DataViewHolder holder, int position) {
        final ArticlesInfo currentItem = appData.get(position);
        holder.setIsRecyclable(false);
        if (currentItem.getImageUrl().equals("")) {

        } else {
            Picasso.get().load(currentItem.getImageUrl()).into(holder.imageView);
        }
       // holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scaleanimation));
        holder.title.setText(currentItem.getTitle());
        // holder.link.setText(currentItem.getLink());
        holder.date.setText(currentItem.getTimestamp());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInNewScreen(currentItem.getLink());
                //displayAlertDialogForChoosing(currentItem.getLink());
            }
        });

    }

    private void openUrlInNewScreen(String selecteUrl) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(selecteUrl));
    }

    public Adapter(ArrayList<ArticlesInfo> appData, Context context)
    {
        this.appData=appData;
        this.context=context;

    }
    @Override
    public int getItemCount() {
        return appData.size();
    }
    public void filterList(ArrayList<ArticlesInfo> filteredList) {
        appData = filteredList;
        notifyDataSetChanged();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView;
        public TextView title,link,date;
        public ImageView imageView;
        public View customViewContainer;
        CardView cardView;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardview);
            title=itemView.findViewById(R.id.title);
            imageView=itemView.findViewById(R.id.foryouImg);
            //link=itemView.findViewById(R.id.link);
            date=itemView.findViewById(R.id.date);


        }
    }


}

