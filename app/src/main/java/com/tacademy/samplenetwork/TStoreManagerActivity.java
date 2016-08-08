package com.tacademy.samplenetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tacademy.samplenetwork.Manager.NetworkManager;
import com.tacademy.samplenetwork.Manager.NetworkRequest;
import com.tacademy.samplenetwork.Manager.TStoreSearchRequest;
import com.tacademy.samplenetwork.autodata.Tstore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TStoreManagerActivity extends AppCompatActivity {

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
        if (!TextUtils.isEmpty(keyword)) {
            TStoreSearchRequest request = new TStoreSearchRequest(keyword);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<Tstore>() {
                @Override
                public void onSuccess(NetworkRequest<Tstore> request, Tstore result) {
                    mAdapter.addAll(result.getProducts().getProduct());
                }

                @Override
                public void onFail(NetworkRequest<Tstore> request, int errorCode, String errorMessage) {
                    Toast.makeText(TStoreManagerActivity.this, "fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
