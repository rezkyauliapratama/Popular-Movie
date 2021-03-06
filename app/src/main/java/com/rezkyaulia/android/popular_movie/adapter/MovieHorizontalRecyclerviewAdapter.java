package com.rezkyaulia.android.popular_movie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rezkyaulia.android.popular_movie.R;
import com.rezkyaulia.android.popular_movie.databinding.ItemMovieSmallBinding;
import com.rezkyaulia.android.popular_movie.fragment.MovieFragment;
import com.rezkyaulia.android.popular_movie.model.Movie;
import com.rezkyaulia.android.popular_movie.util.ApiClient;
import com.rezkyaulia.android.popular_movie.util.Common;
import com.rezkyaulia.android.popular_movie.util.ImageSize;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Rezky Aulia Pratama on 8/6/2017.
 */

public class MovieHorizontalRecyclerviewAdapter extends RecyclerView.Adapter<MovieHorizontalRecyclerviewAdapter.ViewHolder> {
    List<Movie> mItems;
    Context mContext;
    MovieFragment.OnRecyclerViewInteraction mListener;


    private int animationCount = 0;
    private int lastPosition = -1;
    public MovieHorizontalRecyclerviewAdapter(Context mContext,List<Movie> mItems, MovieFragment.OnRecyclerViewInteraction mListener) {
        this.mItems = mItems;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public MovieHorizontalRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_small, parent, false);
        return new MovieHorizontalRecyclerviewAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(MovieHorizontalRecyclerviewAdapter.ViewHolder holder, int position) {
        final Movie mItem = mItems.get(position);

        holder.binding.textviewTitle.setText(mItem.getTitle());
        holder.binding.textViewPoint.setText(String.valueOf(mItem.getVoteAverage()));

        String year = String.valueOf(Common.getInstance().parseDate(mItem.getReleaseDate()).get(Calendar.YEAR));
        holder.binding.textviewReleaseDate.setText(year);

        Picasso.with(mContext)
                .load(ApiClient.getInstance().URL_IMAGE.concat(ImageSize.getInstance().NORMAL).concat(mItem.getPosterPath()))
                .placeholder(R.drawable.ic_movie) //this is optional the image to display while the url image is downloading
                .error(R.drawable.ic_error_sing)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                .into(holder.binding.imagePoster);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnListItemInteraction(mItem);
            }
        });

        setAnimation(holder.binding.getRoot(), position);

        holder.binding.executePendingBindings();   // update the view now
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(final View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            final Animation animation = AnimationUtils.loadAnimation(
                    viewToAnimate.getContext(), R.anim.slide_in_right);
            animationCount++;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animationCount--;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(300);
            viewToAnimate.setVisibility(View.GONE);
            viewToAnimate.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewToAnimate.setVisibility(View.VISIBLE);
                    viewToAnimate.startAnimation(animation);

                }
            }, animationCount * 100);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.binding.getRoot().setVisibility(View.VISIBLE);
        holder.binding.getRoot().clearAnimation();
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieSmallBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemMovieSmallBinding.bind(itemView);
        }
    }
}
