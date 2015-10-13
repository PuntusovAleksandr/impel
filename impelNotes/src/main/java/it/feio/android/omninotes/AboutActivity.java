package it.feio.android.omninotes;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;


public class AboutActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/html/about.html");

        initUI();
    }


    @Override
    public void onStart() {
        // GA tracking
        ImpelNotes.getGaTracker().set(Fields.SCREEN_NAME, getClass().getName());
        ImpelNotes.getGaTracker().send(MapBuilder.createAppView().build());
        super.onStart();
    }


    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }


    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigateUp();
            }
        });
    }

}
