package com.example.apple.android_yunyin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sogou.speech.listener.RecognizerListener;
import com.sogou.speech.ui.RecognizerDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Button btn;
    private TextView mResultsText;
    private RecognizerDialog dialog = null;

    // applied APP ID
    private String appId = "DF465202";
    // applied Access Key
    private String accessKey = "EA382D6A";
    // set isAutoStop true in order to stop recording automatically,
    // default value is true
    private boolean isAutoStop = true;
    // set isContinuous true in order to get real-time partial recognized
    // results, default value is false
    private boolean isContinuous = false;

    // Save the results of recognizing
    private List<List<String>> wholeResult;

    private RecognizerListener listener = new RecognizerListener() {

        @Override
        // call it when the last package has received valid results
        public void onResults(List<List<String>> results) {
            addToWholeResult(results);
            showResults(mResultsText, results);
        }

        @Override
        // call it when some error occurs
        public void onError(int err) {
            // TODO: show error code for users when necessary
        }

        @Override
        // call it when the former packages have received valid results
        public void onPartResults(List<List<String>> results) {
            addToWholeResult(results);
            showResults(mResultsText, results);
        }

        @Override
        // call it when the last package has no valid result but the former
        // packages have some valid results
        public void onQuitQuietly(int err) {
            // TODO: this is not an error, no need to show it for users
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new RecognizerDialog(MainActivity.this, appId, accessKey,
                isAutoStop, isContinuous);
        dialog.setlistener(listener);
        setContentView(R.layout.activity_main);
        bindViews();
        setListeners();
    }

    @Override
    protected void onDestroy() {
        if (dialog != null)
            dialog.dismiss();
        super.onDestroy();
    }

    private void bindViews() {
        mResultsText = (TextView) findViewById(R.id.text);
        btn = (Button) findViewById(R.id.button1);
    }

    private void setListeners() {
        // Click Listener
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeResult = new ArrayList<List<String>>();
                mResultsText.setText("");
                dialog.setAutoStop(isAutoStop);
                dialog.setContinuous(isContinuous);
                dialog.show();
            }
        });

    }

    // add recognized results to the wholeResult
    private void addToWholeResult(List<List<String>> ds) {
        if (ds != null) {
            int tmpAmount = ds.size();
            for (int i = 0; i < tmpAmount; i++) {
                List<String> tmpS = new ArrayList<String>();

                for (int j = 0; j < ds.get(i).size(); j++) {
                    tmpS.add(ds.get(i).get(j));
                }
                wholeResult.add(tmpS);
            }
        }
    }

    // show wholeResult for users when necessary
    private void showResults(TextView textView, List<List<String>> results) {
        String result = "";
        for (List<String> list : results) {
            result += list.get(0);
        }
        textView.append(result);
    }

}

