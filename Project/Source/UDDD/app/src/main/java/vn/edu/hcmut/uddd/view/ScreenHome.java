package vn.edu.hcmut.uddd.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import vn.edu.hcmut.uddd.adapter.TopicAdapter;
import vn.edu.hcmut.uddd.adapter.TopicNameAdapter;
import vn.edu.hcmut.uddd.adapter.WordAdapter;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.common.HistoryManager;
import vn.edu.hcmut.uddd.common.ListViewLimit;
import vn.edu.hcmut.uddd.dao.DatabaseHelper;
import vn.edu.hcmut.uddd.entity.Topic;

public class ScreenHome extends Activity {

    DatabaseHelper db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_screen_home);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        this.db = new DatabaseHelper(ScreenHome.this);

        this.findViewById(R.id.linear_menu_new_word).setOnClickListener(new NewWordClickListener());
        this.findViewById(R.id.linear_menu_topics).setOnClickListener(new TopicClickListener());
        this.findViewById(R.id.linear_menu_micro).setOnClickListener(new MicroClickListener());
        this.findViewById(R.id.linear_menu_test).setOnClickListener(new TestClickListener());
        this.findViewById(R.id.linear_menu_marked).setOnClickListener(new MarkClickListener());
        this.findViewById(R.id.linear_menu_history).setOnClickListener(new HistoryClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstCommon.VOICE_TO_TEXT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                new SearchClickListener().search(text.get(0).trim().toLowerCase());
            }
            catch (Exception e){
                CommonUtil.showError(this, Constants.E001, e);
            }
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
            this.input = new EditText(ScreenHome.this);
            this.input.setHint(Constants.SEARCH_POPUP_MESSAGE);
            CommonUtil.showPopup(ScreenHome.this, true, Constants.SEARCH_POPUP_TITLE, Constants.SEARCH_POPUP_MESSAGE, this.input,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new SearchOkClickListener(), new SearchCancelClickListener());
            return false;
        }

        public void search(String word){
            this.word = word;
            try {
                if (word.length() == 0) {
                    Toast.makeText(ScreenHome.this, Constants.TOAST_SEARCH_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else if (ScreenHome.this.db.isExistWord(this.word)){
                    this.dialog.dismiss();
                    HistoryManager.getInstance().getWord(ScreenHome.this.db, this.word);
                    Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ScreenHome.this.startActivity(intent);
                }
                else{
                    CommonUtil.showPopup(ScreenHome.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.SEARCH_NOT_EXIST_POPUP_MESSAGE, this.word), null,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new ExistOkClickListener(), new ExistCancelClickListener());
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenHome.this, Constants.E001, e);
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
                Intent intent = new Intent(ScreenHome.this, ScreenAddWord.class);
                intent.putExtra(Constants.PR_WORD, SearchClickListener.this.word);
                intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
                ScreenHome.this.startActivity(intent);
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
            Intent intent = new Intent(ScreenHome.this, ScreenAddWord.class);
            intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
            ScreenHome.this.startActivity(intent);
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
                View view = ScreenHome.this.getLayoutInflater().inflate(R.layout.menu_popup_schedule, null);
                this.scheduleNotification = (Switch) view.findViewById(R.id.notification_switch);
                this.scheduleSound = (Switch) view.findViewById(R.id.notification_sound_switch);
                this.scheduleTopic = (Spinner) view.findViewById(R.id.notification_topic_spinner);
                this.scheduleTime = (TextView) view.findViewById(R.id.notication_time_text);
                Calendar calendar = Calendar.getInstance();
                int topicId = CommonUtil.getSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_TOPIC, 0);
                this.scheduleHour = CommonUtil.getSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
                this.scheduleMinute = CommonUtil.getSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_MINUTE, calendar.get(Calendar.MINUTE));
                boolean isSound = CommonUtil.getSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_SOUND, true);
                boolean isOn = CommonUtil.getSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_NOTI, true);
                this.table = CommonUtil.getSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
                if (this.table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)) {
                    List<Topic> list = ScreenHome.this.db.getListTopic();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenHome.this, R.layout.spinner_item, list);
                    this.scheduleTopic.setAdapter(adapter);
                    this.scheduleTopic.setSelection(topicId);
                }
                else{
                    List<Topic> list = new ArrayList<>();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenHome.this, R.layout.spinner_item, list);
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
                CommonUtil.showPopup(ScreenHome.this, false, Constants.SCHEDULE_POPUP_TITLE, null, view,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ScheduleOkClickListener(), new ScheduleCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenHome.this, Constants.E001, e);
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
                CommonUtil.setSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_TOPIC, topicId);
                CommonUtil.setSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_HOUR, ScheduleClickListener.this.scheduleHour);
                CommonUtil.setSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_MINUTE, ScheduleClickListener.this.scheduleMinute);
                CommonUtil.setSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_SOUND, ScheduleClickListener.this.scheduleSound.isChecked());
                CommonUtil.setSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_NOTI, ScheduleClickListener.this.scheduleNotification.isChecked());
                if (ScheduleClickListener.this.scheduleNotification.isChecked()){
                    CommonUtil.startSchedule(ScreenHome.this, topicId, ScheduleClickListener.this.scheduleHour, ScheduleClickListener.this.scheduleMinute, ScheduleClickListener.this.scheduleSound.isChecked());
                }
                else{
                    CommonUtil.cancelSchedule(ScreenHome.this);
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
                ScheduleClickListener.this.timePicker = new TimePicker(ScreenHome.this);
                ScheduleClickListener.this.timePicker.setCurrentHour(ScheduleClickListener.this.scheduleHour);
                ScheduleClickListener.this.timePicker.setCurrentMinute(ScheduleClickListener.this.scheduleMinute);
                CommonUtil.showPopup(ScreenHome.this, false, Constants.CALENDAR_POPUP_TITLE, null, ScheduleClickListener.this.timePicker,
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
            View view = ScreenHome.this.getLayoutInflater().inflate(R.layout.popup_select_share, null);
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
                    CommonUtil.shareFacebook(ScreenHome.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenHome.this, Constants.E002, e);
                }
            }
        }

        private class ShareGmailClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                try {
                    ShareClickListener.this.window.dismiss();
                    CommonUtil.sendMailShare(ScreenHome.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenHome.this, Constants.E002, e);
                }
            }
        }
    }

    private class RateAppClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                CommonUtil.rateApp(ScreenHome.this);
            }
            catch (Exception e){
                CommonUtil.showError(ScreenHome.this, Constants.E002, e);
            }
            return false;
        }
    }

    private class ContactClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                String html = CommonUtil.readHtmlTemplate(ScreenHome.this, ConstCommon.CONTACT_TEMPLATE);
                CommonUtil.showPopup(ScreenHome.this, false, Constants.CONTACT_POPUP_TITTLE, CommonUtil.htmlBuilder(html), null,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ContackOkClickListener(), new ContactCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenHome.this, Constants.E001, e);
            }
            return false;
        }

        private class ContackOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    CommonUtil.sendMailContact(ScreenHome.this);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenHome.this, Constants.E002, e);
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

    private class NewWordClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                HistoryManager.getInstance().clearHistory();
                HistoryManager.getInstance().getNextWord(ScreenHome.this.db);
                Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                ScreenHome.this.startActivity(intent);
            }
            catch (Exception e) {
                CommonUtil.showError(ScreenHome.this, Constants.E001, e);
            }
        }
    }

    private class TopicClickListener implements View.OnClickListener{
        private PopupWindow window;

        @Override
        public void onClick(View v) {
            try {
                String table = CommonUtil.getSharedPreferences(ScreenHome.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
                if (table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)) {
                    List<Topic> topics = ScreenHome.this.db.getListTopic();
                    ListAdapter adapter = new TopicAdapter(ScreenHome.this, R.layout.home_topic_item, topics);
                    View view = ScreenHome.this.getLayoutInflater().inflate(R.layout.popup_select_topic, null);
                    ListViewLimit listView = (ListViewLimit) view.findViewById(R.id.home_list_topic);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new TopicItemClickListener());
                    TextView title = (TextView) view.findViewById(R.id.home_topic_title);
                    title.measure(0, 0);
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ScreenHome.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int statusBarHeight = ScreenHome.this.getResources().getDimensionPixelSize(ScreenHome.this.getResources().getIdentifier("status_bar_height", "dimen", "android"));
                    int actionBarHeight = ScreenHome.this.getActionBar().getHeight();
                    listView.setMaxHeight(displaymetrics.heightPixels - statusBarHeight - actionBarHeight - title.getMeasuredHeight());
                    this.window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                    this.window.setFocusable(true);
                    this.window.setBackgroundDrawable(new BitmapDrawable());
                    this.window.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, statusBarHeight + actionBarHeight);
                }
                else{
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().getNextWord(ScreenHome.this.db);
                    Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                    ScreenHome.this.startActivity(intent);
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenHome.this, Constants.E001, e);
            }
        }

        private class TopicItemClickListener implements AdapterView.OnItemClickListener{

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().setTopicId((int) id);
                    HistoryManager.getInstance().getNextWord(ScreenHome.this.db);
                    TopicClickListener.this.window.dismiss();
                    Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                    ScreenHome.this.startActivity(intent);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenHome.this, Constants.E001, e);
                }
            }
        }
    }

    private class MicroClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                ScreenHome.this.startActivityForResult(intent, ConstCommon.VOICE_TO_TEXT_REQUEST_CODE);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(ScreenHome.this.getApplicationContext(), Constants.TOAST_VTT_NOT_SUPPORT, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class TestClickListener implements View.OnClickListener{

        private PopupWindow window;

        @Override
        public void onClick(View v) {
            View view = ScreenHome.this.getLayoutInflater().inflate(R.layout.popup_select_game, null);
            view.findViewById(R.id.popup_game_fill).setOnClickListener(new GameFillClickListener());
            view.findViewById(R.id.popup_game_listen).setOnClickListener(new GameListenClickListener());
            this.window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
            this.window.setFocusable(true);
            this.window.setBackgroundDrawable(new BitmapDrawable());
            this.window.showAtLocation(view, Gravity.CENTER, 0, 0);
        }

        private class GameFillClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                TestClickListener.this.window.dismiss();
                Intent intent = new Intent(ScreenHome.this, ScreenGameFill.class);
                ScreenHome.this.startActivity(intent);
            }
        }

        private class GameListenClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                TestClickListener.this.window.dismiss();
                Intent intent = new Intent(ScreenHome.this, ScreenGameListen.class);
                ScreenHome.this.startActivity(intent);
            }
        }
    }

    private class HistoryClickListener implements View.OnClickListener{

        private PopupWindow window;
        private List<String> history;

        @Override
        public void onClick(View v) {
            try {
                this.history = ScreenHome.this.db.getListHistory();
                if (this.history.size() > 0) {
                    ListAdapter adapter = new WordAdapter(ScreenHome.this, R.layout.home_word_item, this.history);
                    View view = ScreenHome.this.getLayoutInflater().inflate(R.layout.popup_select_history, null);
                    ListViewLimit listView = (ListViewLimit) view.findViewById(R.id.home_list_history);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new HistoryItemClickListener());
                    TextView title = (TextView) view.findViewById(R.id.home_history_title);
                    title.measure(0, 0);
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ScreenHome.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int statusBarHeight = ScreenHome.this.getResources().getDimensionPixelSize(ScreenHome.this.getResources().getIdentifier("status_bar_height", "dimen", "android"));
                    int actionBarHeight = ScreenHome.this.getActionBar().getHeight();
                    listView.setMaxHeight(displaymetrics.heightPixels - statusBarHeight - actionBarHeight - title.getMeasuredHeight());
                    this.window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                    this.window.setFocusable(true);
                    this.window.setBackgroundDrawable(new BitmapDrawable());
                    this.window.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, statusBarHeight + actionBarHeight);
                }
                else{
                    CommonUtil.showPopup(ScreenHome.this, false, Constants.POPUP_TIT_ALERT, Constants.HISTORY_NOT_EXIST_POPUP_MESSAGE, null,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new EmptyOkClickListener(), new EmptyCancelClickListener());
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenHome.this, Constants.E001, e);
            }
        }

        private class HistoryItemClickListener implements AdapterView.OnItemClickListener{

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().getWord(ScreenHome.this.db, HistoryClickListener.this.history.get((int) id));
                    HistoryClickListener.this.window.dismiss();
                    Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                    ScreenHome.this.startActivity(intent);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenHome.this, Constants.E001, e);
                }
            }
        }

        private class EmptyOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().getNextWord(ScreenHome.this.db);
                    Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                    ScreenHome.this.startActivity(intent);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenHome.this, Constants.E001, e);
                }
            }
        }

        private class EmptyCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }

    private class MarkClickListener implements View.OnClickListener{

        private PopupWindow window;
        private List<String> mark;

        @Override
        public void onClick(View v) {
            try {
                this.mark = ScreenHome.this.db.getListMarked();
                if (this.mark.size() > 0) {
                    ListAdapter adapter = new WordAdapter(ScreenHome.this, R.layout.home_word_item, this.mark);
                    View view = ScreenHome.this.getLayoutInflater().inflate(R.layout.popup_select_marked, null);
                    ListViewLimit listView = (ListViewLimit) view.findViewById(R.id.home_list_marked);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new MarkItemClickListener());
                    TextView title = (TextView) view.findViewById(R.id.home_marked_title);
                    title.measure(0, 0);
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ScreenHome.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    int statusBarHeight = ScreenHome.this.getResources().getDimensionPixelSize(ScreenHome.this.getResources().getIdentifier("status_bar_height", "dimen", "android"));
                    int actionBarHeight = ScreenHome.this.getActionBar().getHeight();
                    listView.setMaxHeight(displaymetrics.heightPixels - statusBarHeight - actionBarHeight - title.getMeasuredHeight());
                    this.window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                    this.window.setFocusable(true);
                    this.window.setBackgroundDrawable(new BitmapDrawable());
                    this.window.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, statusBarHeight + actionBarHeight);
                }
                else{
                    CommonUtil.showPopup(ScreenHome.this, false, Constants.POPUP_TIT_ALERT, Constants.MARKED_NOT_EXIST_POPUP_MESSAGE, null,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new EmptyOkClickListener(), new EmptyCancelClickListener());
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenHome.this, Constants.E001, e);
            }
        }

        private class MarkItemClickListener implements AdapterView.OnItemClickListener{

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().getWord(ScreenHome.this.db, MarkClickListener.this.mark.get((int) id));
                    MarkClickListener.this.window.dismiss();
                    Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                    ScreenHome.this.startActivity(intent);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenHome.this, Constants.E001, e);
                }
            }
        }

        private class EmptyOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().getNextWord(ScreenHome.this.db);
                    Intent intent = new Intent(ScreenHome.this, ScreenMean.class);
                    ScreenHome.this.startActivity(intent);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenHome.this, Constants.E001, e);
                }
            }
        }

        private class EmptyCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }
}