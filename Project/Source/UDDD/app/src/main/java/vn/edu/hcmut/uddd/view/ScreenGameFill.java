package vn.edu.hcmut.uddd.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.adapter.GameFillAdapter;
import vn.edu.hcmut.uddd.adapter.TopicNameAdapter;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.common.HistoryManager;
import vn.edu.hcmut.uddd.dao.DatabaseHelper;
import vn.edu.hcmut.uddd.entity.Topic;
import vn.edu.hcmut.uddd.entity.Word;

/**
 * Created by 51201_000 on 06/04/2016.
 */
public class ScreenGameFill extends Activity {

    private DatabaseHelper db;
    private List<Pair<Word, Integer>> gameData;

    /// Control
    private LinearLayout listGameData;
    private LinearLayout footerLayout;
    private LinearLayout mainLayout;
    private GameFillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_game_fill);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        this.db = new DatabaseHelper(ScreenGameFill.this);

        /// Find control in layout
        this.listGameData = (LinearLayout) this.findViewById(R.id.game_fill_listView);
        this.footerLayout = (LinearLayout) this.findViewById(R.id.game_fill_footer);
        this.mainLayout = (LinearLayout) this.findViewById(R.id.game_fill_main);
        this.findViewById(R.id.game_fill_root).addOnLayoutChangeListener(new LayoutChangeListener());
        this.findViewById(R.id.game_fill_button_complete).setOnClickListener(new GameOkClickListener());
        this.findViewById(R.id.game_fill_button_cancel).setOnClickListener(new GameCancelClickListener());

        try {
            this.gameData = this.db.getListWordForGame(5);
            if (this.gameData.size() > 0) {
                this.adapter = new GameFillAdapter(this, R.layout.game_fill_item, this.gameData);
                this.displayListView(this.listGameData, this.adapter);
            }
            else{
                new NotExistListener().show();
            }
        }
        catch (Exception e){
            CommonUtil.showError(this, Constants.E001, e);
        }
    }

    private void displayListView(LinearLayout layout, ListAdapter adapter){
        layout.removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++){
            layout.addView(adapter.getView(i, null, null));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionbar_search) {
            new SearchClickListener().onMenuItemClick(item);
        }
        else if (id == R.id.actionbar_add){
            new AddWordClickListener().onMenuItemClick(item);
        }
        else if (id == R.id.actionbar_notification){
            new ScheduleClickListener().onMenuItemClick(item);
        }
        else if(id == R.id.actionbar_share){
            new ShareClickListener().onMenuItemClick(item);
        }
        else if(id == R.id.actionbar_review){
            new RateAppClickListener().onMenuItemClick(item);
        }
        else if (id == R.id.actionbar_contact){
            new ContactClickListener().onMenuItemClick(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private class SearchClickListener implements MenuItem.OnMenuItemClickListener{
        private String word;
        private EditText input;
        private AlertDialog dialog;

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            this.input = new EditText(ScreenGameFill.this);
            this.input.setHint(Constants.SEARCH_POPUP_MESSAGE);
            CommonUtil.showPopup(ScreenGameFill.this, true, Constants.SEARCH_POPUP_TITLE, Constants.SEARCH_POPUP_MESSAGE, this.input,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new SearchOkClickListener(), new SearchCancelClickListener());
            return false;
        }

        public void search(String word){
            this.word = word;
            try {
                if (word.length() == 0) {
                    Toast.makeText(ScreenGameFill.this, Constants.TOAST_SEARCH_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else if (ScreenGameFill.this.db.isExistWord(this.word)){
                    this.dialog.dismiss();
                    HistoryManager.getInstance().getWord(ScreenGameFill.this.db, this.word);
                    Intent intent = new Intent(ScreenGameFill.this, ScreenMean.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ScreenGameFill.this.startActivity(intent);
                }
                else{
                    CommonUtil.showPopup(ScreenGameFill.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.SEARCH_NOT_EXIST_POPUP_MESSAGE, this.word), null,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new ExistOkClickListener(), new ExistCancelClickListener());
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameFill.this, Constants.E001, e);
            }
        }

        private class SearchOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                SearchClickListener.this.dialog = dialog;
                SearchClickListener.this.search(SearchClickListener.this.input.getText().toString().trim().toLowerCase());
            }
        }

        private class SearchCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }

        private class ExistOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                Intent intent = new Intent(ScreenGameFill.this, ScreenAddWord.class);
                intent.putExtra(Constants.PR_WORD, SearchClickListener.this.word);
                intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
                ScreenGameFill.this.startActivity(intent);
            }
        }

        private class ExistCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }

    private class AddWordClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(ScreenGameFill.this, ScreenAddWord.class);
            intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
            ScreenGameFill.this.startActivity(intent);
            return false;
        }
    }

    private class ScheduleClickListener implements MenuItem.OnMenuItemClickListener{
        private Switch scheduleNotification;
        private Switch scheduleSound;
        private Spinner scheduleTopic;
        private TextView scheduleTime;
        private TimePicker timePicker;
        private int scheduleHour;
        private int scheduleMinute;
        private String table;

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                View view = ScreenGameFill.this.getLayoutInflater().inflate(R.layout.menu_popup_schedule, null);
                this.scheduleNotification = (Switch) view.findViewById(R.id.notification_switch);
                this.scheduleSound = (Switch) view.findViewById(R.id.notification_sound_switch);
                this.scheduleTopic = (Spinner) view.findViewById(R.id.notification_topic_spinner);
                this.scheduleTime = (TextView) view.findViewById(R.id.notication_time_text);
                Calendar calendar = Calendar.getInstance();
                int topicId = CommonUtil.getSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_TOPIC, 0);
                this.scheduleHour = CommonUtil.getSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
                this.scheduleMinute = CommonUtil.getSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_MINUTE, calendar.get(Calendar.MINUTE));
                boolean isSound = CommonUtil.getSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_SOUND, true);
                boolean isOn = CommonUtil.getSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_NOTI, true);
                this.table = CommonUtil.getSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
                if (this.table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)) {
                    List<Topic> list = ScreenGameFill.this.db.getListTopic();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenGameFill.this, R.layout.spinner_item, list);
                    this.scheduleTopic.setAdapter(adapter);
                    this.scheduleTopic.setSelection(topicId);
                }
                else{
                    List<Topic> list = new ArrayList<>();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenGameFill.this, R.layout.spinner_item, list);
                    this.scheduleTopic.setAdapter(adapter);
                    this.scheduleTopic.setSelection(0);
                    this.scheduleTopic.setEnabled(false);
                }
                this.scheduleNotification.setChecked(isOn);
                this.scheduleTime.setText(CommonUtil.timeDisplay(this.scheduleHour, this.scheduleMinute));
                this.scheduleSound.setChecked(isSound);
                this.scheduleTime.setOnClickListener(new TimeClickListener());
                this.scheduleNotification.setOnCheckedChangeListener(new NotificationSwitchListener());
                if (!isOn) {
                    this.scheduleSound.setEnabled(false);
                    this.scheduleTopic.setEnabled(false);
                    this.scheduleTime.setEnabled(false);
                }
                CommonUtil.showPopup(ScreenGameFill.this, false, Constants.SCHEDULE_POPUP_TITLE, null, view,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ScheduleOkClickListener(), new ScheduleCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameFill.this, Constants.E001, e);
            }
            return false;
        }

        private class NotificationSwitchListener implements CompoundButton.OnCheckedChangeListener{

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ScheduleClickListener.this.scheduleSound.setEnabled(isChecked);
                ScheduleClickListener.this.scheduleTopic.setEnabled(isChecked && ScheduleClickListener.this.table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE));
                ScheduleClickListener.this.scheduleTime.setEnabled(isChecked);
            }
        }

        private class ScheduleOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                int topicId = ((Topic)ScheduleClickListener.this.scheduleTopic.getSelectedItem()).getId();
                CommonUtil.setSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_TOPIC, topicId);
                CommonUtil.setSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_HOUR, ScheduleClickListener.this.scheduleHour);
                CommonUtil.setSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_MINUTE, ScheduleClickListener.this.scheduleMinute);
                CommonUtil.setSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_SOUND, ScheduleClickListener.this.scheduleSound.isChecked());
                CommonUtil.setSharedPreferences(ScreenGameFill.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_NOTI, ScheduleClickListener.this.scheduleNotification.isChecked());
                if (ScheduleClickListener.this.scheduleNotification.isChecked()){
                    CommonUtil.startSchedule(ScreenGameFill.this, topicId, ScheduleClickListener.this.scheduleHour, ScheduleClickListener.this.scheduleMinute, ScheduleClickListener.this.scheduleSound.isChecked());
                }
                else{
                    CommonUtil.cancelSchedule(ScreenGameFill.this);
                }
            }
        }

        private class ScheduleCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }

        private class TimeClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                ScheduleClickListener.this.timePicker = new TimePicker(ScreenGameFill.this);
                ScheduleClickListener.this.timePicker.setCurrentHour(ScheduleClickListener.this.scheduleHour);
                ScheduleClickListener.this.timePicker.setCurrentMinute(ScheduleClickListener.this.scheduleMinute);
                CommonUtil.showPopup(ScreenGameFill.this, false, Constants.CALENDAR_POPUP_TITLE, null, ScheduleClickListener.this.timePicker,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new CalendarOkClickListener(), new CalendarCancelClickListener());
            }

            private class CalendarOkClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    ScheduleClickListener.this.scheduleHour = ScheduleClickListener.this.timePicker.getCurrentHour();
                    ScheduleClickListener.this.scheduleMinute = ScheduleClickListener.this.timePicker.getCurrentMinute();
                    ScheduleClickListener.this.scheduleTime.setText(CommonUtil.timeDisplay(ScheduleClickListener.this.scheduleHour, ScheduleClickListener.this.scheduleMinute));
                }
            }

            private class CalendarCancelClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    dialog.dismiss();
                }
            }
        }
    }

    private class ShareClickListener implements MenuItem.OnMenuItemClickListener{
        private PopupWindow window;

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            View view = ScreenGameFill.this.getLayoutInflater().inflate(R.layout.popup_select_share, null);
            view.findViewById(R.id.popup_share_face).setOnClickListener(new ShareFacebookClickListener());
            view.findViewById(R.id.popup_share_mail).setOnClickListener(new ShareGmailClickListener());
            this.window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            this.window.setFocusable(true);
            this.window.setBackgroundDrawable(new BitmapDrawable());
            this.window.showAtLocation(view, Gravity.CENTER, 0, 0);
            return false;
        }

        private class ShareFacebookClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                try {
                    ShareClickListener.this.window.dismiss();
                    CommonUtil.shareFacebook(ScreenGameFill.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenGameFill.this, Constants.E002, e);
                }
            }
        }

        private class ShareGmailClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                try {
                    ShareClickListener.this.window.dismiss();
                    CommonUtil.sendMailShare(ScreenGameFill.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenGameFill.this, Constants.E002, e);
                }
            }
        }
    }

    private class RateAppClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                CommonUtil.rateApp(ScreenGameFill.this);
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameFill.this, Constants.E002, e);
            }
            return false;
        }
    }

    private class ContactClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                String html = CommonUtil.readHtmlTemplate(ScreenGameFill.this, ConstCommon.CONTACT_TEMPLATE);
                CommonUtil.showPopup(ScreenGameFill.this, false, Constants.CONTACT_POPUP_TITTLE, CommonUtil.htmlBuilder(html), null,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ContackOkClickListener(), new ContactCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameFill.this, Constants.E001, e);
            }
            return false;
        }

        private class ContackOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    CommonUtil.sendMailContact(ScreenGameFill.this);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenGameFill.this, Constants.E002, e);
                }
            }
        }

        private class ContactCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }

    private class LayoutChangeListener implements View.OnLayoutChangeListener{

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (bottom - oldBottom >= ConstCommon.MIN_SIZE_LAY_OUT_CHANGE) {
                ScreenGameFill.this.footerLayout.setVisibility(View.VISIBLE);
                ScreenGameFill.this.mainLayout.requestLayout();
            }
            else if (oldBottom - bottom >= ConstCommon.MIN_SIZE_LAY_OUT_CHANGE) {
                ScreenGameFill.this.footerLayout.setVisibility(View.GONE);
                ScreenGameFill.this.mainLayout.requestLayout();
            }
        }
    }

    private class GameOkClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int mark = 0;
            try {
                for (int i = 0; i < ScreenGameFill.this.gameData.size(); i++) {
                    Pair<Word, Integer> word = ScreenGameFill.this.gameData.get(i);
                    String input = ScreenGameFill.this.adapter.getTextAt(i);
                    if (word.first.getWord().equals(input)) {
                        mark++;
                        int point = word.second + 1;
                        if (point == 10) {
                            ScreenGameFill.this.db.deleteHistoryWord(word.first);
                        }
                        else {
                            ScreenGameFill.this.db.addPoint(word.first, point);
                        }
                    }
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameFill.this, Constants.E001, e);
            }
            CommonUtil.showPopup(ScreenGameFill.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.format(Constants.GAME_COMPLETE_POPUP_MESSAGE, String.valueOf(mark), String.valueOf(ScreenGameFill.this.adapter.getCount())), null,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new NewGameOkClickListener(), new NewGameCancelClickListener());
        }

        private class NewGameOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                try {
                    ScreenGameFill.this.gameData = ScreenGameFill.this.db.getListWordForGame(5);
                    if (ScreenGameFill.this.gameData.size() > 0) {
                        ScreenGameFill.this.adapter = new GameFillAdapter(ScreenGameFill.this, R.layout.game_fill_item, ScreenGameFill.this.gameData);
                        ScreenGameFill.this.displayListView(ScreenGameFill.this.listGameData, ScreenGameFill.this.adapter);
                    }
                    else{
                        new NotExistListener().show();
                    }
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenGameFill.this, Constants.E001, e);
                }
            }
        }

        private class NewGameCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                ScreenGameFill.this.onBackPressed();
            }
        }
    }

    private class GameCancelClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ScreenGameFill.this.onBackPressed();
        }
    }

    private class NotExistListener{

        public void show(){
            CommonUtil.showPopup(ScreenGameFill.this, false, Constants.POPUP_TIT_ALERT, Constants.GAME_NOT_EXIST_POPUP_MESSAGE, null,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new EmptyOkClickListener(), new EmptyCancelClickListener());
        }

        private class EmptyOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().getNextWord(ScreenGameFill.this.db);
                    Intent intent = new Intent(ScreenGameFill.this, ScreenMean.class);
                    ScreenGameFill.this.startActivity(intent);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenGameFill.this, Constants.E001, e);
                }
            }
        }

        private class EmptyCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                ScreenGameFill.this.onBackPressed();
            }
        }
    }
}
