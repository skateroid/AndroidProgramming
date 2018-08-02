package com.geekbrains.weather.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.createCityFrag.CreateActionFragment;

import java.util.ArrayList;

/**
 * Created by shkryaba on 07/07/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<String> cityList = new ArrayList<>();
    private CreateActionFragment.OnHeadlineSelectedListener mCallback;
    private Context context;
    private ArrayList<String> selectedCities = new ArrayList<>();
    private Boolean isRemoove = false;

    public CustomAdapter(Context context, ArrayList<String> cityList, CreateActionFragment.OnHeadlineSelectedListener mCallback) {
        this.cityList = cityList;
        this.mCallback = mCallback;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(cityList.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                for (int i = 0; i < selectedCities.size(); i++) {
                    if (selectedCities.get(i).equals(cityList.get(position))) {
                        holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                        selectedCities.remove(selectedCities.get(i));
                        mCallback.onArticleSelected(selectedCities);
                        isRemoove = true;
                    }
                }
                if (!isRemoove) {
                    holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
                    selectedCities.add(cityList.get(position));
                    mCallback.onArticleSelected(selectedCities);
                } else {
                    isRemoove = false;
                }*/

                if (isRemoove) {
                    if (selectedCities.get(0).equals(cityList.get(position))){
                        holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                        selectedCities.remove(selectedCities.get(0));
                        mCallback.onArticleSelected(selectedCities);
                        isRemoove = false;
                    } else {
                        //добавить сюда чтобы город перестал подсвечиваться
                        selectedCities.set(0, cityList.get(position));
                        holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
                        mCallback.onArticleSelected(selectedCities);
                    }
                } else {
                    selectedCities.add(cityList.get(position));
                    holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
                    mCallback.onArticleSelected(selectedCities);
                    isRemoove = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout_item);
            textView = itemView.findViewById(R.id.textview_item);
        }
    }
}
