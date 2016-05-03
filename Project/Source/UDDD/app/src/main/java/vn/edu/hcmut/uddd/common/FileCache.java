package vn.edu.hcmut.uddd.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import vn.edu.hcmut.uddd.R;

/**
 * Created by TRAN VAN HEN on 4/6/2016.
 */
public class FileCache extends AsyncTask<String, String, Bitmap> {

    private Context context;
    private ImageView imageView;
    private ProgressBar progressBar;
    private RelativeLayout container;
    private File fileCache;
    private boolean isHide;

    public FileCache(Context context, ImageView imageView, ProgressBar progressBar, RelativeLayout container, boolean isHide){
        this.context = context;
        this.imageView = imageView;
        this.progressBar = progressBar;
        this.container = container;
        this.isHide = isHide;
        this.fileCache = context.getCacheDir();
        if (!this.fileCache.exists()){
            this.fileCache.mkdir();
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (params[0] == null || params[0].trim().length() == 0 || !URLUtil.isValidUrl(params[0])){
            return null;
        }
        String url = params[0];
        File file = new File(this.fileCache, url.hashCode() + ".png");
        if (file.exists()){
            return BitmapFactory.decodeFile(file.getPath());
        }
        else if (CommonUtil.isNetworkConnected(this.context)){
            try {
                file.createNewFile();
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                int height, width;
                float ratio = (float)bitmap.getWidth()/bitmap.getHeight();
                if (ratio > ConstCommon.BITMAP_RATIO){
                    width = ConstCommon.BITMAP_WIDTH;
                    height = (int) (width / ratio);
                }
                else{
                    height = ConstCommon.BITMAP_HEIGHT;
                    width = (int) (height * ratio);
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                return bitmap;
            }
            catch (IOException e) {
                CommonUtil.logError(this, e);
                return null;
            }
        }
        else{
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.imageView.setVisibility(View.INVISIBLE);
        this.progressBar.setVisibility(View.VISIBLE);
        if (this.isHide) {
            this.container.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            this.imageView.setImageBitmap(bitmap);
            this.imageView.setVisibility(View.VISIBLE);
            this.progressBar.setVisibility(View.INVISIBLE);
            if (isHide) {
                this.container.setVisibility(View.VISIBLE);
            }
        }
        else {
            if (this.isHide) {
                this.container.setVisibility(View.GONE);
            }
            else{
                this.imageView.setImageResource(R.drawable.no_picture);
                this.imageView.setVisibility(View.VISIBLE);
                this.progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
