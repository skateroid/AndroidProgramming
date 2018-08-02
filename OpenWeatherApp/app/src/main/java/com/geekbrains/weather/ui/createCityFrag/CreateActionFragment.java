package com.geekbrains.weather.ui.createCityFrag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geekbrains.weather.R;
import com.geekbrains.weather.model.Cities;
import com.geekbrains.weather.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by shkryaba on 24/06/2018.
 */

public class CreateActionFragment extends BaseFragment {

    //объявление переменных
    private TextInputEditText editTextCountry;
    private RecyclerView recyclerView;
    OnHeadlineSelectedListener mCallback;
    OnCountrySelectedListener mCallbackCountry;
    private LinearLayout linearLayout;
    private Cities cities;
    private ArrayList<Cities> citiesCustomList;
    private FloatingActionButton fab;
    private BottomAppBar bar;
    private Pattern checkCity = Pattern.compile("[A-Z a-z]{2,}$");

    public interface OnHeadlineSelectedListener {
        void onArticleSelected(ArrayList<String> position);
    }

    public interface OnCountrySelectedListener {
        void onCountrySelected(String country);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toast.makeText(getContext(), "onAttachAction", Toast.LENGTH_SHORT).show();

        try {
            mCallbackCountry = (OnCountrySelectedListener) getBaseActivity();
            mCallback = (OnHeadlineSelectedListener) getBaseActivity().getAnotherFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getBaseActivity().getAnotherFragment().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //обращаемся к layout который будет содержать наш фрагмент
        return inflater.inflate(R.layout.create_action_fragment, container, false);
    }

    @Override
    public void onResume() {
        bar = getBaseActivity().findViewById(R.id.bottomAppBar);
        bar.setVisibility(View.INVISIBLE);

        fab = getBaseActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        super.onResume();
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        initCustomCountryList();

        recyclerView = view.findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        CustomAdapterOnCities customAdapterOnCities = new CustomAdapterOnCities(getContext(), citiesCustomList, mCallback, mCallbackCountry, getBaseActivity());
        recyclerView.setAdapter(customAdapterOnCities);

        //инициализация edittext и листенер на ключи при взаимодействии с ним, когда мы нашимаем enter у нас опускается клавиатура и запускается WeatherFragment
        editTextCountry = view.findViewById(R.id.et_country);

        editTextCountry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    if (checkCity.matcher(editTextCountry.getText().toString()).matches()) {
                        hideError(editTextCountry);
                    } else {
                        showError(editTextCountry, "Введите корректное название города");
                    }
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String country = editTextCountry.getText().toString().trim();
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(country);
                    mCallback.onArticleSelected(arrayList);
                    mCallbackCountry.onCountrySelected(country);
                    return true;
                }
                return false;
            }
        });
    }

    private void showError(TextView view, String message) {
        view.setError(message);
    }

    private void hideError(TextView view) {
        view.setError(null);
    }

    private void initCustomCountryList() {
        citiesCustomList = new ArrayList<>();
        citiesCustomList.add(new Cities(1, "Moscow"));
        citiesCustomList.add(new Cities(2, "St. Peterburg"));
        citiesCustomList.add(new Cities(3, "Kazan"));
    }
}
