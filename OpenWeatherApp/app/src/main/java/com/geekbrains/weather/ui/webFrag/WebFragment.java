package com.geekbrains.weather.ui.webFrag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.base.BaseFragment;

public class WebFragment extends BaseFragment {
    private EditText inputUrl;
    private WebView webView;
    private Button goButton;
    private FloatingActionButton fab;
    private BottomAppBar bar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_geo_layout, container, false);
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
        inputUrl = view.findViewById(R.id.status);
        webView = view.findViewById(R.id.webView);
        goButton = view.findViewById(R.id.btn_go_to);

        final OkHttpRequester requester = new OkHttpRequester(new OkHttpRequester.OnResponseCompleted() {
            @Override
            public void onCompleted(String content) {
                webView.loadData(content, "text/html; charset=utf-8", "utf-8");
            }
        });
        requester.run("https://www.gismeteo.ru/weather-moscow-4368/3-days/");

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requester.run(inputUrl.getText().toString());
            }
        });
    }
}
