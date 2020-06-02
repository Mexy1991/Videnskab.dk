package com.videnskabdk.videnskabdk.videnskabdk;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);

        /* Pull to refresh */
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        pullToRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.equals("mailto:sv@videnskab.dk")) {
                    MailTo mt = MailTo.parse(url);
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Spørg Videnskaben");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mt.getTo()});
                    startActivity(sendIntent);
                    return true;
                }

                if (url.startsWith("mailto:")) {
                    MailTo mt = MailTo.parse(url);
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mt.getTo()});
                    startActivity(sendIntent);
                    return true;
                }

                if (url.contains(".pdf")) {
                    Uri path = Uri.parse(url);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(pdfIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Ingen PDF-kompatibel app fundet", Toast.LENGTH_SHORT).show();
                    } catch (Exception otherException) {
                        Toast.makeText(MainActivity.this, "Ukendt fejl", Toast.LENGTH_SHORT).show();
                    }
                }

                if(!url.contains("videnskab.dk")){
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });


        webView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                //Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                //Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("https://videnskab.dk");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

