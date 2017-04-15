package com.kumar.prince.popularmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kumar.prince.popularmovie.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by princ on 14-04-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private String [] posterURL;
    private JSONArray movieData;
    private Context context;
    final private MovieAdapter.MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
    //this.context=context;
        mClickHandler= movieAdapterOnClickHandler;
    }

    public interface MovieAdapterOnClickHandler{
        void onClick(JSONObject movieData);
    }

    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("TAG","onCreateViewHolder "+viewType);
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movieposter, parent, false);
        return new MovieAdapterViewHolder(view);


        //return null;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieAdapterViewHolder holder, int position) {
        Log.e("TAG",posterURL[position]+" "+position);
        JSONObject movieJSONObject;
        try {
            movieJSONObject=movieData.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Picasso.with(context).load(posterURL[position]).into(holder.imageMoviePoster);
    }

    @Override
    public int getItemCount() {

        if (posterURL==null){ Log.e("TAG","getItemCount ");

            return 0;
        }
        return posterURL.length;
    }


    public void setMovierURLData(String[] posterURL) {
       // Log.e("TAG"," setMovierURLData"+posterURL.toString());
        this.posterURL = posterURL;
        notifyDataSetChanged();
    }
    public void setMovieDataJSONArray(JSONArray movieData){

        this.movieData=movieData;
    }




    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public  final ImageView imageMoviePoster;
        public MovieAdapterViewHolder(View itemView) {

            super(itemView);
            Log.e("TAG"," MovieAdapterViewHolder"+posterURL.toString());
            imageMoviePoster=(ImageView) itemView.findViewById(R.id.img_moviePoster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            try {
                mClickHandler.onClick(movieData.getJSONObject(getAdapterPosition()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
