package vn.edu.hcmut.uddd.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.adapter.GameListenAdapter;
import vn.edu.hcmut.uddd.adapter.TopicNameAdapter;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.common.HistoryManager;
import vn.edu.hcmut.uddd.dao.DatabaseHelper;
import vn.edu.hcmut.uddd.entity.Topic;
import vn.edu.hcmut.uddd.entity.Word;

/**
 * Created by 51201_000 on 08/04/2016.
 */
public class ScreenGameListen extends Activity {

    private DatabaseHelper db;
    private TextToSpeech textToSpeech;
    private List<Pair<Word, Integer>> gameData;

    /// Control
    private LinearLayout footerLayout;
    private LinearLayout mainLayout;
    private LinearLayout listGameData;
    private GameListenAdapter adapter;
    private ImageView speaker;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_game_listen);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        this.db = new DatabaseHelper(ScreenGameListen.this);

        /// Find control in layout
        this.listGameData = (LinearLayout) this.findViewById(R.id.game_listen_listView);
        this.footerLayout = (LinearLayout) this.findViewById(R.id.game_listen_footer);
        this.mainLayout = (LinearLayout) this.findViewById(R.id.game_listen_main);
        this.findViewById(R.id.game_listen_root).addOnLayoutChangeListener(new LayoutChangeListener());
        this.findViewById(R.id.game_listen_button_complete).setOnClickListener(new GameOkClickListener());
        this.findViewById(R.id.game_listen_button_cancel).setOnClickListener(new GameCancelClickListener());
        this.textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeechInit());
        try {
            this.gameData = this.db.getListWordForGame(5);
            if (this.gameData.size() > 0) {
                this.adapter = new GameListenAdapter(this, R.layout.game_listen_item, this.gameData);
                this.displayListView(this.listGameData, this.adapter, new SpeakerClickListener());
            }
            else{
                new NotExistListener().show();
            }
        }
        catch (Exception e){
            CommonUtil.showError(this, Constants.E001, e);
        }
    }

    private void displayListView(LinearLayout layout, ListAdapter adapter, View.OnClickListener click){
        layout.removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++){
            View v = adapter.getView(i, null, null);
            ImageView image = (ImageView) v.findViewById(R.id.game_listen_image_item);
            image.setTag(i);
            image.setOnClickListener(click);
            layout.addView(v);
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

    @Override
    public void onPause(){
        if( textToSpeech!= null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onResume(){
        this.textToSpeech = new TextToSpeech(this.getApplicationContext(), new TextToSpeechInit());
        super.onResume();
    }

    private class SearchClickListener implements MenuItem.OnMenuItemClickListener{
        private String word;
        private EditText input;
        private AlertDialog dialog;

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            this.input = new EditText(ScreenGameListen.this);
            this.input.setHint(Constants.SEARCH_POPUP_MESSAGE);
            CommonUtil.showPopup(ScreenGameListen.this, true, Constants.SEARCH_POPUP_TITLE, Constants.SEARCH_POPUP_MESSAGE, this.input,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new SearchOkClickListener(), new SearchCancelClickListener());
            return false;
        }

        public void search(String word){
            this.word = word;
            try {
                if (word.length() == 0) {
                    Toast.makeText(ScreenGameListen.this, Constants.TOAST_SEARCH_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else if (ScreenGameListen.this.db.isExistWord(this.word)){
                    this.dialog.dismiss();
                    HistoryManager.getInstance().getWord(ScreenGameListen.this.db, this.word);
                    Intent intent = new Intent(ScreenGameListen.this, ScreenMean.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ScreenGameListen.this.startActivity(intent);
                }
                else{
                    CommonUtil.showPopup(ScreenGameListen.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.SEARCH_NOT_EXIST_POPUP_MESSAGE, this.word), null,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new ExistOkClickListener(), new ExistCancelClickListener());
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameListen.this, Constants.E001, e);
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
                Intent intent = new Intent(ScreenGameListen.this, ScreenAddWord.class);
                intent.putExtra(Constants.PR_WORD, SearchClickListener.this.word);
                intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
                ScreenGameListen.this.startActivity(intent);
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
            Intent intent = new Intent(ScreenGameListen.this, ScreenAddWord.class);
            intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
            ScreenGameListen.this.startActivity(intent);
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
                View view = ScreenGameListen.this.getLayoutInflater().inflate(R.layout.menu_popup_schedule, null);
                this.scheduleNotification = (Switch) view.findViewById(R.id.notification_switch);
                this.scheduleSound = (Switch) view.findViewById(R.id.notification_sound_switch);
                this.scheduleTopic = (Spinner) view.findViewById(R.id.notification_topic_spinner);
                this.scheduleTime = (TextView) view.findViewById(R.id.notication_time_text);
                Calendar calendar = Calendar.getInstance();
                int topicId = CommonUtil.getSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_TOPIC, 0);
                this.scheduleHour = CommonUtil.getSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
                this.scheduleMinute = CommonUtil.getSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_MINUTE, calendar.get(Calendar.MINUTE));
                boolean isSound = CommonUtil.getSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_SOUND, true);
                boolean isOn = CommonUtil.getSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_NOTI, true);
                this.table = CommonUtil.getSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
                if (this.table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)) {
                    List<Topic> list = ScreenGameListen.this.db.getListTopic();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenGameListen.this, R.layout.spinner_item, list);
                    this.scheduleTopic.setAdapter(adapter);
                    this.scheduleTopic.setSelection(topicId);
                }
                else{
                    List<Topic> list = new ArrayList<>();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenGameListen.this, R.layout.spinner_item, list);
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
                CommonUtil.showPopup(ScreenGameListen.this, false, Constants.SCHEDULE_POPUP_TITLE, null, view,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ScheduleOkClickListener(), new ScheduleCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameListen.this, Constants.E001, e);
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
                CommonUtil.setSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_TOPIC, topicId);
                CommonUtil.setSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_HOUR, ScheduleClickListener.this.scheduleHour);
                CommonUtil.setSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_MINUTE, ScheduleClickListener.this.scheduleMinute);
                CommonUtil.setSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_SOUND, ScheduleClickListener.this.scheduleSound.isChecked());
                CommonUtil.setSharedPreferences(ScreenGameListen.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_NOTI, ScheduleClickListener.this.scheduleNotification.isChecked());
                if (ScheduleClickListener.this.scheduleNotification.isChecked()){
                    CommonUtil.startSchedule(ScreenGameListen.this, topicId, ScheduleClickListener.this.scheduleHour, ScheduleClickListener.this.scheduleMinute, ScheduleClickListener.this.scheduleSound.isChecked());
                }
                else{
                    CommonUtil.cancelSchedule(ScreenGameListen.this);
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
                ScheduleClickListener.this.timePicker = new TimePicker(ScreenGameListen.this);
                ScheduleClickListener.this.timePicker.setCurrentHour(ScheduleClickListener.this.scheduleHour);
                ScheduleClickListener.this.timePicker.setCurrentMinute(ScheduleClickListener.this.scheduleMinute);
                CommonUtil.showPopup(ScreenGameListen.this, false, Constants.CALENDAR_POPUP_TITLE, null, ScheduleClickListener.this.timePicker,
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
            View view = ScreenGameListen.this.getLayoutInflater().inflate(R.layout.popup_select_share, null);
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
                    CommonUtil.shareFacebook(ScreenGameListen.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenGameListen.this, Constants.E002, e);
                }
            }
        }

        private class ShareGmailClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                try {
                    ShareClickListener.this.window.dismiss();
                    CommonUtil.sendMailShare(ScreenGameListen.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenGameListen.this, Constants.E002, e);
                }
            }
        }
    }

    private class RateAppClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                CommonUtil.rateApp(ScreenGameListen.this);
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameListen.this, Constants.E002, e);
            }
            return false;
        }
    }

    private class ContactClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                String html = CommonUtil.readHtmlTemplate(ScreenGameListen.this, ConstCommon.CONTACT_TEMPLATE);
                CommonUtil.showPopup(ScreenGameListen.this, false, Constants.CONTACT_POPUP_TITTLE, CommonUtil.htmlBuilder(html), null,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ContackOkClickListener(), new ContactCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameListen.this, Constants.E001, e);
            }
            return false;
        }

        private class ContackOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    CommonUtil.sendMailContact(ScreenGameListen.this);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenGameListen.this, Constants.E002, e);
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
                ScreenGameListen.this.footerLayout.setVisibility(View.VISIBLE);
                ScreenGameListen.this.mainLayout.requestLayout();
            }
            else if (oldBottom - bottom >= ConstCommon.MIN_SIZE_LAY_OUT_CHANGE) {
                ScreenGameListen.this.footerLayout.setVisibility(View.GONE);
                ScreenGameListen.this.mainLayout.requestLayout();
            }
        }
    }

    private class GameOkClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int mark = 0;
            try {
                for (int i = 0; i < ScreenGameListen.this.gameData.size(); i++) {
                    Pair<Word, Integer> word = ScreenGameListen.this.gameData.get(i);
                    String input = ScreenGameListen.this.adapter.getTextAt(i);
                    if (word.first.getWord().equals(input)) {
                        mark++;
                        int point = word.second + 1;
                        if (point == 10) {
                            ScreenGameListen.this.db.deleteHistoryWord(word.first);
                        }
                        else {
                            ScreenGameListen.this.db.addPoint(word.first, point);
                        }
                    }
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenGameListen.this, Constants.E001, e);
            }
            CommonUtil.showPopup(ScreenGameListen.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.format(Constants.GAME_COMPLETE_POPUP_MESSAGE, String.valueOf(mark), String.valueOf(ScreenGameListen.this.adapter.getCount())), null,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new NewGameOkClickListener(), new NewGameCancelClickListener());
        }

        private class NewGameOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                try {
                    ScreenGameListen.this.gameData = ScreenGameListen.this.db.getListWordForGame(5);
                    if (ScreenGameListen.this.gameData.size() > 0) {
                        ScreenGameListen.this.adapter = new GameListenAdapter(ScreenGameListen.this, R.layout.game_listen_item, ScreenGameListen.this.gameData);
                        ScreenGameListen.this.displayListView(ScreenGameListen.this.listGameData, ScreenGameListen.this.adapter, new SpeakerClickListener());
                    }
                    else{
                        new NotExistListener().show();
                    }
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenGameListen.this, Constants.E001, e);
                }
            }
        }

        private class NewGameCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                ScreenGameListen.this.onBackPressed();
            }
        }
    }

    private class GameCancelClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ScreenGameListen.this.onBackPressed();
        }
    }

    private class NotExistListener{

        public void show(){
            CommonUtil.showPopup(ScreenGameListen.this, false, Constants.POPUP_TIT_ALERT, Constants.GAME_NOT_EXIST_POPUP_MESSAGE, null,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new EmptyOkClickListener(), new EmptyCancelClickListener());
        }

        private class EmptyOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    HistoryManager.getInstance().clearHistory();
                    HistoryManager.getInstance().getNextWord(ScreenGameListen.this.db);
                    Intent intent = new Intent(ScreenGameListen.this, ScreenMean.class);
                    ScreenGameListen.this.startActivity(intent);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenGameListen.this, Constants.E001, e);
                }
            }
        }

        private class EmptyCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                ScreenGameListen.this.onBackPressed();
            }
        }
    }

    private class TextToSpeechInit implements TextToSpeech.OnInitListener{

        @Override
        public void onInit(int status) {
            if(status != TextToSpeech.ERROR){
                ScreenGameListen.this.textToSpeech.setLanguage(Locale.US);
                ScreenGameListen.this.textToSpeech.setOnUtteranceProgressListener(new ProgressListen());
            }
        }

        private class ProgressListen extends UtteranceProgressListener {

            @Override
            public void onStart(String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ScreenGameListen.this.speaker.setImageResource(R.drawable.loa_phat);
                    }
                });
            }

            @Override
            public void onDone(String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ScreenGameListen.this.speaker.setImageResource(R.drawable.icon_loa);
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {

            }
        }
    }

    private class SpeakerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ScreenGameListen.this.speaker = (ImageView) v;
            int index = (int) v.getTag();
            ScreenGameListen.this.textToSpeech.speak(ScreenGameListen.this.gameData.get(index).first.getWord(), TextToSpeech.QUEUE_FLUSH, null, ScreenGameListen.this.gameData.get(index).first.getWord());
        }
    }
}
