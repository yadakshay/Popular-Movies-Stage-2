package com.example.android.popularmovies1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Akshay on 16-09-2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private int mNoOfTrailers;
    final private ListItemClickListener mOnClickListener;
    //interface for click listener
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    //constructor
    public TrailerAdapter(int noOfTrailers, ListItemClickListener listener){
        mNoOfTrailers = noOfTrailers;
        mOnClickListener = listener;
    }

    // this class defines the View holder for out Adapter
    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView listItemNumberView;
        public TrailerAdapterViewHolder(View view){
            super(view);
            listItemNumberView = (TextView) view.findViewById(R.id.trailer_text);
            view.setOnClickListener(this);
        }

        void bind(int listIndex) {
            listItemNumberView.setText("Trailer " + String.valueOf(listIndex + 1));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrailerAdapterViewHolder viewHolder = new TrailerAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNoOfTrailers;
    }


}
