package pratik.com.newsstand.NewsActivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import pratik.com.newsstand.R;

public class ReadArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_article);
        setTitle(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        WebView mWebView = findViewById(R.id.article_webview);

        final String url = getIntent().getExtras().getString("URL");
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                progressBar.setProgress(progress);
                if(progress == 100)
                    progressBar.setVisibility(View.GONE);

            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url2=request.getUrl().toString();
                // all links  with in ur site will be open inside the webview
                if (url != null && url.startsWith(url2)){
                    return false;
                }
                // all links that points outside the site will be open in a normal android browser
                else  {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
            }
        });

        if(url == null)
            mWebView.loadData(getIntent().getExtras().getString("CONTENT"),"text/html","UTF-8");
        else
            mWebView.loadUrl(url);

    }

    public void closeArticleActivity(View v){
        this.finish();
    }
}
