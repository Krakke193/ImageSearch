package com.kpi.imagesearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEtSearchParams;
    private Button mBtnSearch;
    private int mSelectedQuality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtSearchParams = (EditText) findViewById(R.id.et_search_param);
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        Spinner spinner = (Spinner) findViewById(R.id.sp_quality);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.quality, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedQuality = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void beginSearch() {
        final String searchParam = mEtSearchParams.getText().toString();
        QueryManager.Builder.ImageSizes imageSize;

        switch (mSelectedQuality) {
            case 0:
                imageSize = QueryManager.Builder.ImageSizes.SMALL;
                break;
            case 1:
                imageSize = QueryManager.Builder.ImageSizes.MEDIUM;
                break;
            case 2:
                imageSize = QueryManager.Builder.ImageSizes.LARGE;
                break;
            default:
                imageSize = QueryManager.Builder.ImageSizes.MEDIUM;
                break;
        }

        ThumbnailsActivity.startActivity(this, searchParam, imageSize);
    }
}
