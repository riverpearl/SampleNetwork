package com.tacademy.samplenetwork;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tacademy.samplenetwork.autodata.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tacademy on 2016-08-05.
 */
public class ProductView extends FrameLayout {

    Product product;

    @BindView(R.id.image_thumbnail)
    ImageView thumbView;
    @BindView(R.id.text_name)
    TextView nameView;
    @BindView(R.id.text_like)
    TextView likeView;
    @BindView(R.id.text_download)
    TextView downloadView;
    @BindView(R.id.text_description)
    TextView descView;

    public ProductView(Context context) {
        this(context, null);
    }

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_product, this);
        ButterKnife.bind(this);
    }

    public void setProduct(Product product) {
        this.product = product;

        nameView.setText(product.getName());
        likeView.setText(product.getScore() + "");
        downloadView.setText(product.getDownloadCount() + "");
        descView.setText(product.getDescription());
    }
}
