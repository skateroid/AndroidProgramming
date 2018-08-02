package com.geekbrains.weather.ui.historyFrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.base.BaseFragment;

import java.util.ArrayList;

public class HistoryFragment extends BaseFragment {
    private ArrayList<String> history_list;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private BottomAppBar bar;


    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        initHistoryList();

        recyclerView = view.findViewById(R.id.list_history);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        HistoryListAdapter history_adapter = new HistoryListAdapter(history_list, getContext());
        recyclerView.setAdapter(history_adapter);
    }

    @Override
    public void onResume() {
        bar = getBaseActivity().findViewById(R.id.bottomAppBar);
        bar.setVisibility(View.INVISIBLE);

        fab = getBaseActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //обращаемся к layout который будет содержать наш фрагмент
        return inflater.inflate(R.layout.history_fragment, container, false);
    }

    private void initHistoryList() {
        history_list = new ArrayList<>();
        history_list.add("Moscow       +23  sunny   15:15 10.07.2017");
        history_list.add("Kazan        +23  sunny   15:15 10.07.2017");
        history_list.add("Nino         +23  sunny   15:15 10.07.2017");
        history_list.add("Ufa          +23  sunny   15:15 10.07.2017");
        history_list.add("Vladivostok  +23  sunny   15:15 10.07.2017");
        history_list.add("GorodA       +23  sunny   15:15 10.07.2017");
        history_list.add("GorodB       +23  sunny   15:15 10.07.2017");
        history_list.add("GorodC       +23  sunny   15:15 10.07.2017");
        history_list.add("Ekb          +23  sunny   15:15 10.07.2017");
        history_list.add("London       +23  sunny   15:15 10.07.2017");
        history_list.add("Minsk        +23  sunny   15:15 10.07.2017");
        history_list.add("Kiev         +23  sunny   15:15 10.07.2017");
        history_list.add("Sochi        +23  sunny   15:15 10.07.2017");
    }
}
