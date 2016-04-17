package com.kpi.imagesearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEtSearchParams;
    private Button mBtnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtSearchParams = (EditText) findViewById(R.id.et_search_param);
        mBtnSearch = (Button) findViewById(R.id.btn_search);

        if (mBtnSearch != null) {
            mBtnSearch.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                beginSearch();
                break;
        }
    }

    private void beginSearch() {
        final String searchParam = mEtSearchParams.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkManager networkManager = new NetworkManager();

                try {
                    networkManager.beginSearch(searchParam);
                } catch (IOException e) {
                    Log.e(TAG, "Failed to load images!", e);
                }
            }
        }).start();
    }
}
