package com.dangxy.readhub.model;

import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dangxy.readhub.R;
import com.dangxy.readhub.base.BaseActivity;
import com.dangxy.readhub.utils.ViewUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author dangxy99
 * @description 详情页面
 * @date 2017/12/30
 */
public class DetailActivity extends BaseActivity {

    @BindView(R.id.tv_detail_title)
    TextView tvDetailTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.summary)
    TextView summaryTitle;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.rl_share)
    RelativeLayout rlShare;
    private String summary;
    private String title;

    @Override
    protected void initView() {
        title = getIntent().getStringExtra("title");
        summary = getIntent().getStringExtra("summary");
        tvDetailTitle.setText(title);
        summaryTitle.setText(summary);

//        RxView.clicks(rlShare).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                Bitmap bitmap = com.dangxy.readhub.utils.ViewUtils.createBitmapFromView(llContent);
//                String path =  ViewUtils.saveBitmap(mContext, bitmap);
//                if(!TextUtils.isEmpty(path)){
//                    Snackbar.make(rlShare, "保存成功~", Snackbar.LENGTH_SHORT).setAction("知道了", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                        }
//                    }).show();
//                }
//            }
//        });
        //请求权限并保存截图
        RxView.clicks(rlShare)
                //将流转化为请求权限
                .flatMap(new Function<Object, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Object o) throws Exception {
                        RxPermissions rxPermissions = new RxPermissions(DetailActivity.this);
                        return rxPermissions.request("android.permission.WRITE_EXTERNAL_STORAGE");
                    }
                })
                //处理结果
                .subscribe(new Consumer<Boolean>() {
                    @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                Bitmap bitmap = com.dangxy.readhub.utils.ViewUtils.createBitmapFromView(llContent);
                                String path = ViewUtils.saveBitmap(mContext, bitmap);
                                if (!TextUtils.isEmpty(path)) {
                                    Snackbar.make(rlShare, "保存成功~", Snackbar.LENGTH_SHORT).setAction("知道了", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                        }
                                    }).show();
                                }
                            } else {
                                Snackbar.make(rlShare, "保存失败，没有权限~", Snackbar.LENGTH_SHORT).setAction("知道了", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                }).show();
                            }
                        }
                    }
                );

    }


    private void savePicture() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request("android.permission.WRITE_EXTERNAL_STORAGE").subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean granted) throws Exception {
                if (granted) {
                    Bitmap bitmap = com.dangxy.readhub.utils.ViewUtils.createBitmapFromView(llContent);
                    String path = ViewUtils.saveBitmap(mContext, bitmap);
                    if (!TextUtils.isEmpty(path)) {
                        Snackbar.make(rlShare, "保存成功~", Snackbar.LENGTH_SHORT).setAction("知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        }).show();
                    }
                } else {
                    Snackbar.make(rlShare, "保存失败，没有权限~", Snackbar.LENGTH_SHORT).setAction("知道了", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    }).show();
                }
            }
        });
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_detail;
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }


}
