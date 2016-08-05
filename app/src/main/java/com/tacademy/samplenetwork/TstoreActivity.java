package com.tacademy.samplenetwork;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tacademy.samplenetwork.autodata.Product;
import com.tacademy.samplenetwork.autodata.TstoreResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class TstoreActivity extends AppCompatActivity {



    @BindView(R.id.edit_keyword)
    EditText keywordView;
    @BindView(R.id.list_tstore)
    ListView listView;

    ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tstore);

        ButterKnife.bind(this);

        mAdapter = new ProductAdapter();
        listView.setAdapter(mAdapter);
    }

    @OnClick(R.id.btn_search)
    public void onSearch(View view) {
        String keyword = keywordView.getText().toString();

        if (!TextUtils.isEmpty(keyword))
            new TstoreSearchTask().execute(keyword);
    }

    @OnItemClick(R.id.list_tstore)
    public void onProductItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product p = (Product)listView.getItemAtPosition(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.getTinyUrl()));
        startActivity(intent);
    }

    private static final String TSTORE_URL = "http://apis.skplanetx.com/tstore/products?version=1&page=1&count=10&searchKeyword=%s&order=L";
    private static final String SORT_ACCURACY = "R";
    private static final String SORT_LATEST = "L";
    private static final String SORT_DOWNLOAD = "D";

    class TstoreSearchTask extends AsyncTask<String, Integer, TstoreResult> {
        @Override
        protected TstoreResult doInBackground(String... strings) {
            String keyword = strings[0];

            try {
                String urlText = String.format(TSTORE_URL, URLEncoder.encode(keyword, "utf-8"));
                URL url = new URL(urlText);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("appKey", "b9982d77-eefc-30ce-8e7e-25a52cc10bb6");
                int code = conn.getResponseCode();

                if (code >= 200 && code < 300) {
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    Gson gson = new Gson();
                    TstoreResult result = gson.fromJson(br, TstoreResult.class);
                    return result;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(TstoreResult tstoreResult) {
            super.onPostExecute(tstoreResult);

            if (tstoreResult != null)
                mAdapter.addAll(tstoreResult.getTstore().getProducts().getProduct());
            else Toast.makeText(TstoreActivity.this, "error", Toast.LENGTH_SHORT).show();
        }
    }
}
