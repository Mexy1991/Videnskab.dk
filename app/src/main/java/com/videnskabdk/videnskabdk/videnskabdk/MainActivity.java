package com.videnskabdk.videnskabdk.videnskabdk;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity {

    private WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView) findViewById(R.id.webView);


        /*
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webview.loadUrl("https://videnskab.dk");


         */


        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               /*
                * Opens all external links in browser
                if (url.contains("videnskab.dk")) {
                    view.loadUrl(url);
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
                 */

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

                return false;
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl("https://videnskab.dk");
    }


    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
