package vn.edu.hcmut.uddd.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.common.HistoryManager;
import vn.edu.hcmut.uddd.dao.DatabaseHelper;

/**
 * Created by TRAN VAN HEN on 4/22/2016.
 */
public class ScreenNotification extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int topic = this.getIntent().getIntExtra(Constants.PR_TOPIC_ID, 0);
        HistoryManager.getInstance().setTopicId(topic);
        try {
            HistoryManager.getInstance().getNextWord(new DatabaseHelper(this));
        }
        catch (Exception e) {
            CommonUtil.logError(this, e);
        }
        Intent intent = new Intent(this, ScreenMean.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
        this.finish();
    }
}
