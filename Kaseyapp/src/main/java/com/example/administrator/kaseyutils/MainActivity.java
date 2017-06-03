package com.example.administrator.kaseyutils;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.kaseyutils.utils.KSPermissionsUtils;
import com.example.administrator.kaseyutils.utils.KSPhotoUtils;
import com.example.administrator.kaseyutils.utils.KSSpUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {;
    ImageView mIvAvatar;


    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForChoose();
            }
        });
        initView();
    }

    protected void initView() {
        Resources r = this.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.mipmap.ic_launcher) + "/"
                + r.getResourceTypeName(R.mipmap.ic_launcher) + "/"
                + r.getResourceEntryName(R.mipmap.ic_launcher));


    }


    protected void showDialogForChoose() {
        final AlertDialog mDialog = new AlertDialog.Builder(this).create();
        setFullScreen(mDialog);
        Log.e("TAG", "11111111111111111111111111");

        LinearLayout mChoosenImage = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_picker_pictrue, null);

        mDialog.setView(mChoosenImage);

        mDialog.show();
        TextView mTvCamera = (TextView) mChoosenImage.findViewById(R.id.tv_camera);
        TextView mTvFile = (TextView) mChoosenImage.findViewById(R.id.tv_file);
        TextView mTvCancel = (TextView) mChoosenImage.findViewById(R.id.tv_cancel);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mTvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KSPermissionsUtils.requestCamera(MainActivity.this, new onRequestPermissionsListener() {
                    @Override
                    public void onRequestBefore() {
                        Toast.makeText(MainActivity.this, "请先获得相机权限", Toast.LENGTH_LONG);
                        mDialog.cancel();
                        Log.e("TAG", "jdfslkgjs;gjd;g");
                    }

                    @Override
                    public void onRequestLater() {
                        KSPhotoUtils.openCameraImage(MainActivity.this);
                        Log.e("TAG", "hallelelel");
                        mDialog.cancel();
                    }
                });
            }
        });
        mTvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KSPermissionsUtils.requestReadExternalStorage(MainActivity.this, new onRequestPermissionsListener() {
                    @Override
                    public void onRequestBefore() {
                        mDialog.cancel();
                    }

                    @Override
                    public void onRequestLater() {
                        KSPhotoUtils.openLocalImage(MainActivity.this);
                        mDialog.cancel();
                    }
                });
            }
        });

    }

    /**
     * 设置AlertDilog显示的位置
     */
    public void setFullScreen(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(80);
        window.setBackgroundDrawableResource(R.drawable.transparent_bg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case KSPhotoUtils.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                    RxPhotoUtils.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case KSPhotoUtils.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
                    // KSPhotoUtils.cropImage(MainActivity.this, KSPhotoUtils.imageUriFromCamera);// 裁剪图片
                    initUCrop(KSPhotoUtils.imageUriFromCamera);
                }

                break;
            case KSPhotoUtils.CROP_IMAGE://普通裁剪后的处理
                Glide.with(MainActivity.this).
                        load(KSPhotoUtils.cropImageUri).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        bitmapTransform(new CropCircleTransformation(MainActivity.this)).
                        thumbnail(0.5f).
                        placeholder(R.mipmap.ic_launcher).
                        priority(Priority.LOW).
                        error(R.mipmap.ic_launcher).
                        fallback(R.mipmap.ic_launcher).
                        dontAnimate().
                        into(mIvAvatar);
//                RequestUpdateAvatar(new File(RxPhotoUtils.getRealFilePath(mContext, RxPhotoUtils.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    roadImageView(resultUri, mIvAvatar);
                    KSSpUtils.putContent(MainActivity.this, "KASEY", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(MainActivity.this).
                load(uri).
                diskCacheStrategy(DiskCacheStrategy.RESULT).
                bitmapTransform(new CropCircleTransformation(MainActivity.this)).
                thumbnail(0.5f).
                placeholder(R.mipmap.ic_launcher).
                priority(Priority.LOW).
                error(R.mipmap.ic_launcher).
                fallback(R.mipmap.ic_launcher).
                dontAnimate().
                into(imageView);

        return (new File(KSPhotoUtils.getImageAbsolutePath(this, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoUtils.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
//        options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
//        options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
//        options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
//        options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
//        options.setCropGridColumnCount(2);
        //设置横线的数量
//        options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

}
