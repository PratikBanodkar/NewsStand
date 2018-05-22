package pratik.com.newsstand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pratik.com.newsstand.AllArticlesActivites.AllArticlesActivity;

/**
 * Created by Pratz on 30-01-2018.
 */

public class LibraryRecyclerAdapter extends RecyclerView.Adapter<LibraryRecyclerAdapter.ViewHolder>{

    private ArrayList<Category> allCategories;
    private Context context;

    public LibraryRecyclerAdapter(Context c,ArrayList<Category> list){
        this.allCategories = list;
        this.context = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_article_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cardHeader.setText(allCategories.get(position).getCategoryName());
        final ArrayList sn = allCategories.get(position).getCategorySourceNames();
        final ArrayList sl = allCategories.get(position).getCategorySourceLogos();
        final ArrayList sid = allCategories.get(position).getCategorySourceIDs();

        if(position == 0 || position == 6){
            holder.layout_inside_hsv.removeAllViewsInLayout();
        }

        for(int i=0; i<sn.size();i++){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newView  = layoutInflater.inflate(R.layout.layout_inside_hsv,null);
            final ImageView sourceLogo = newView.findViewById(R.id.source_logo);
            final int finalI = i;
            final int finalI1 = i;
            final int finalI2 = i;
            sourceLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent allArticlesIntent = new Intent(context, AllArticlesActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("Source",sn.get(finalI).toString());
                    mBundle.putInt("Drawable ID",Integer.parseInt(sl.get(finalI1).toString()));
                    mBundle.putString("Source ID",sid.get(finalI2).toString());
                    allArticlesIntent.putExtras(mBundle);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)context,sourceLogo,"libraryToAllArticles");
                    context.startActivity(allArticlesIntent);
                }
            });
            TextView sourceName = newView.findViewById(R.id.source_header);
            sourceLogo.setImageDrawable(context.getResources().getDrawable(Integer.parseInt(sl.get(i).toString())));
            sourceName.setText(sn.get(i).toString());
            holder.layout_inside_hsv.addView(newView);
        }

    }

    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        protected TextView cardHeader;
        protected HorizontalScrollView hsv;
        protected LinearLayout layout_inside_hsv;
        public ViewHolder(View itemView) {
            super(itemView);
            cardHeader =  (TextView) itemView.findViewById(R.id.card_header);
            hsv =  (HorizontalScrollView) itemView.findViewById(R.id.sources_scrollview);
            layout_inside_hsv = (LinearLayout) hsv.findViewById(R.id.layout_inside_hsv);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
