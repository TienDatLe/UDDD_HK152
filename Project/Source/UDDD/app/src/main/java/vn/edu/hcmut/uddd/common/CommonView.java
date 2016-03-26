package vn.edu.hcmut.uddd.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

/**
 * Created by TRAN VAN HEN on 3/26/2016.
 */
public class CommonView {
    private Context context;
    public CommonView(Context context){
        this.context = context;
    }

    public void sendMail(List<String> address){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address.toArray(new String[address.size()]));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
    }
}
