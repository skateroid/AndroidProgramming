package com.geekbrains.weather.ui.createCityFrag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.prefs.PrefsData;
import com.geekbrains.weather.prefs.PrefsHelper;
import com.geekbrains.weather.R;
import com.geekbrains.weather.model.Cities;
import com.geekbrains.weather.ui.base.BaseActivity;

import java.util.ArrayList;

public class CustomAdapterOnCities extends RecyclerView.Adapter<CustomAdapterOnCities.ViewHolder> {

    private ArrayList<Cities> citiesCustomList = new ArrayList<>();
    private CreateActionFragment.OnHeadlineSelectedListener mCallback;
    private CreateActionFragment.OnCountrySelectedListener mCallbackCountry;
    private ArrayList<String> selectedCities;
    private Context context;
    private PrefsHelper prefsHelper;

    public CustomAdapterOnCities(Context context, ArrayList<Cities> citiesCustomList,
                                 CreateActionFragment.OnHeadlineSelectedListener mCallback,
                                 CreateActionFragment.OnCountrySelectedListener mCallbackCountry,
                                 BaseActivity baseActivity) {
        this.citiesCustomList = citiesCustomList;
        this.mCallback = mCallback;
        this.context = context;
        this.mCallbackCountry = mCallbackCountry;
        selectedCities = new ArrayList<>();
        selectedCities.add("");
        prefsHelper = new PrefsData(baseActivity);
    }

    @NonNull
    @Override
    public CustomAdapterOnCities.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapterOnCities.ViewHolder holder, final int position) {
        holder.textView.setText(citiesCustomList.get(position).getCity());

        if (citiesCustomList.get(position).isChecked()) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (citiesCustomList.get(position).isChecked()) {
                    isChecked(position, false);
                    selectedCities.set(0, "Weather");
                    mCallbackCountry.onCountrySelected("Weather");
                    mCallback.onArticleSelected(selectedCities);
                    deleteInPref();
                } else {
                    isChecked(position, true);
                    selectedCities.set(0, citiesCustomList.get(position).getCity());
                    mCallbackCountry.onCountrySelected(selectedCities.get(0));
                    mCallback.onArticleSelected(selectedCities);
                    saveInPref(selectedCities.get(0));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return citiesCustomList.size();
    }

    private void isChecked(int position, boolean isChecked) {
        ArrayList<Cities> customList = new ArrayList<>();
        for (int i = 0; i < citiesCustomList.size(); i++) {
            if (i == position) {
                Cities cities = citiesCustomList.get(position);
                cities.setChecked(isChecked);
                customList.add(cities);
            } else {
                Cities cities = citiesCustomList.get(i);
                cities.setChecked(false);
                customList.add(cities);
            }
        }
        citiesCustomList.clear();
        citiesCustomList = customList;
        notifyDataSetChanged();
    }

    private void deleteInPref() {
        prefsHelper.deleteSharedPreferences(Constants.CITY);
    }

    private void saveInPref(String city) {
        prefsHelper.saveSharedPreferences(Constants.CITY, city);
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
