package vn.edu.hcmut.uddd.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import vn.edu.hcmut.uddd.adapter.NoteAdapter;
import vn.edu.hcmut.uddd.adapter.TopicNameAdapter;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.common.FileCache;
import vn.edu.hcmut.uddd.common.HistoryManager;
import vn.edu.hcmut.uddd.common.SwipeDetector;
import vn.edu.hcmut.uddd.common.SwipeDirection;
import vn.edu.hcmut.uddd.dao.DatabaseHelper;
import vn.edu.hcmut.uddd.entity.Meaning;
import vn.edu.hcmut.uddd.entity.Phrase;
import vn.edu.hcmut.uddd.entity.Topic;
import vn.edu.hcmut.uddd.entity.Word;
import vn.edu.hcmut.uddd.entity.WordFamily;

public class ScreenMean extends Activity {

    private DatabaseHelper db;
    private Word word;
    private SwipeDetector detector;
    private TextToSpeech textToSpeech;
    private boolean isMark = false;
    private boolean isUpdateNote = false;

    /// Control
    private TextView textWord;
    private TextView textPronunciation;
    private ImageView imagePronunciation;
    private CheckBox checkMarked;
    private ImageView imagePicture;
    private ProgressBar progressBarPicture;
    private TextView textHtml;
    private ScrollView scrollFrame;
    private ListView listNote;
    private LinearLayout animationFrame;
    private RelativeLayout relativePicture;

    /// Animation
    private Animation animationLeftToRight;
    private Animation animationRightToLeft;
    private Animation animationLeftToRightBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_screen_mean);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        this.db = new DatabaseHelper(this);
        this.word = HistoryManager.getInstance().getCurrent();

        this.textWord = (TextView) this.findViewById(R.id.mean_textView_word);
        this.textPronunciation = (TextView) this.findViewById(R.id.mean_textView_pronunciation);
        this.imagePronunciation = (ImageView) this.findViewById(R.id.mean_imageView_pronunciation);
        this.checkMarked = (CheckBox) this.findViewById(R.id.mean_checkbox_marked);
        this.imagePicture = (ImageView) this.findViewById(R.id.mean_imageView_picture);
        this.progressBarPicture = (ProgressBar) this.findViewById(R.id.mean_progressBar_picture);
        this.textHtml = (TextView) this.findViewById(R.id.mean_textView_html);
        this.scrollFrame = (ScrollView) this.findViewById(R.id.mean_scroll_frame);
        this.listNote = (ListView) this.findViewById(R.id.mean_listView_list_note);
        this.animationFrame = (LinearLayout) this.findViewById(R.id.mean_animation_frame);
        this.relativePicture = (RelativeLayout) this.findViewById(R.id.mean_relative_picture);
        this.animationRightToLeft = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.move_right_to_left);
        this.animationLeftToRight = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.move_left_to_right);
        this.animationLeftToRightBack = AnimationUtils.loadAnimation(this.getApplicationContext(), R.anim.left_to_right_one_half);

        try {
            this.fillDataToLayout();
        }
        catch (Exception e){
            CommonUtil.logError(this, e);
        }

        this.findViewById(R.id.mean_button_edit).setOnClickListener(new EditClickListener());
        this.findViewById(R.id.mean_button_delete).setOnClickListener(new DeleteClickListener());
        this.findViewById(R.id.mean_imageView_add_note).setOnClickListener(new AddNoteClickListener());
        this.listNote.setOnItemLongClickListener(new NoteLongClickListener());
        this.imagePronunciation.setOnClickListener(new SpeechClickListener());
        this.textToSpeech = new TextToSpeech(getApplicationContext(), new SpeechInitListener());
        this.textHtml.setLinksClickable(true);
        this.textHtml.setOnTouchListener(new HtmlTouchListener());
        this.detector = new SwipeDetector(this.getWindow().getDecorView().getRootView());
        this.detector.setOnSwipeListener(new SwipeDetectorListener());
    }

    @Override
    public void onResume(){
        try {
            this.fillDataToLayout();
            this.textToSpeech = new TextToSpeech(this.getApplicationContext(), new SpeechInitListener());
        }
        catch (Exception e){
            CommonUtil.logError(this, e);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        HistoryManager.getInstance().clearHistory();
        super.onBackPressed();
    }

    @Override
    public void onPause(){
        if(this.textToSpeech!= null){
            this.textToSpeech.stop();
            this.textToSpeech.shutdown();
        }
        try {
            if (this.isMark && !this.checkMarked.isChecked()) {
                this.db.deleteMarkedWord(this.word);
            }
            else if (!this.isMark && this.checkMarked.isChecked()){
                this.db.insertMarkedWord(this.word);
            }
            if (this.isUpdateNote){
                this.db.updateNoteOfWord(this.word);
            }
        }
        catch (Exception e){
            CommonUtil.logError(this, e);
        }
        super.onPause();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return this.detector.onTouch(null, ev);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            this.clearAllLayout(false);
            this.word = HistoryManager.getInstance().getCurrent();
            this.animationFrame.startAnimation(this.animationRightToLeft);
            this.fillDataToLayout();
        }
        catch (Exception e){
            CommonUtil.showError(this, Constants.E001, e);
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

        if (id == android.R.id.home){
            HistoryManager.getInstance().clearHistory();
        }
        else if (id == R.id.actionbar_search) {
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

    private void fillDataToLayout() throws Exception{
        if(this.db.getIsMarked(this.word)){
            this.checkMarked.setChecked(true);
            this.isMark = true;
        }
        else{
            this.isMark = false;
        }
        this.isUpdateNote = false;
        this.textWord.setText(CommonUtil.htmlBuilder(Constants.HTML_MEAN_WORD_STRING, this.word.getWord(), this.word.getPartOfSpeechString()));
        this.textPronunciation.setText(CommonUtil.htmlBuilder(Constants.HTML_MEAN_WORD_PRONUNCIATION, this.word.getPronunciation()));
        this.textHtml.setText(Html.fromHtml(builHtml()));
        new FileCache(this, this.imagePicture, this.progressBarPicture, this.relativePicture, true).execute(this.word.getUrl());
        this.displayListView();
        this.scrollFrame.fullScroll(ScrollView.FOCUS_UP);
    }

    private void clearAllLayout(boolean isDelete) throws Exception {
        if (!isDelete) {
            if (this.isMark && !this.checkMarked.isChecked()) {
                this.db.deleteMarkedWord(word);
            } else if (!this.isMark && this.checkMarked.isChecked()) {
                this.db.insertMarkedWord(this.word);
            }
            if (this.isUpdateNote) {
                this.db.updateNoteOfWord(this.word);
            }
        }
        this.textWord.setText(ConstCommon.EMPTY);
        this.textPronunciation.setText(ConstCommon.EMPTY);
        this.checkMarked.setChecked(false);
        this.imagePicture.setVisibility(View.GONE);
        this.textHtml.setText(ConstCommon.EMPTY);
        this.listNote.setAdapter(new NoteAdapter(this, R.layout.text_item, new ArrayList<String>()));
    }

    private void displayListView(){
        this.listNote.setAdapter(new NoteAdapter(listNote.getContext(), R.layout.text_item, word.getNoteList()));
    }

    private String builHtml() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.HTML_MEAN_MEAN_HEADER);
        for (int i = 0; i < this.word.getMeanList().size(); i++) {
            Meaning mean = this.word.getMeanList().get(i);
            if (mean.getExample().equals(ConstCommon.EMPTY)) {
                builder.append(CommonUtil.format(Constants.HTML_MEAN_MEAN_DETAIL_NO_EX, mean.getMean(), mean.getPartOfSpeechString()));
            } else {
                builder.append(CommonUtil.format(Constants.HTML_MEAN_MEAN_DETAIL_FULL, mean.getMean(), mean.getPartOfSpeechString(), mean.getExample(), mean.getExampleMean()));
            }
        }
        if (this.word.getSynonyms().size() > 0) {
            builder.append(Constants.HTML_MEAN_SYNONYMS_HEADER);
            for (int i = 0; i < this.word.getSynonyms().size(); i++) {
                String synonyms = this.word.getSynonyms().get(i);
                if (this.db.isExistWord(synonyms)) {
                    builder.append(CommonUtil.format(Constants.HTML_MEAN_SYNONYMS_LINK, synonyms, synonyms));
                } else {
                    builder.append(CommonUtil.format(Constants.HTML_MEAN_SYNONYMS_NO_LINK, synonyms));
                }
                if (i < this.word.getSynonyms().size() - 1) {
                    builder.append(Constants.HTML_MEAN_COMMA_SEPARATOR);
                }
            }
            builder.append(Constants.HTML_NEW_LINE);
        }
        if (this.word.getAntonyms().size() > 0) {
            builder.append(Constants.HTML_MEAN_ANTONYMS_HEADER);
            for (int i = 0; i < this.word.getAntonyms().size(); i++) {
                String antonyms = this.word.getAntonyms().get(i);
                if (this.db.isExistWord(antonyms)) {
                    builder.append(CommonUtil.format(Constants.HTML_MEAN_ANTONYMS_LINK, antonyms, antonyms));
                } else {
                    builder.append(CommonUtil.format(Constants.HTML_MEAN_ANTONYMS_NO_LINK, antonyms));
                }
                if (i < this.word.getAntonyms().size() - 1) {
                    builder.append(Constants.HTML_MEAN_COMMA_SEPARATOR);
                }
            }
            builder.append(Constants.HTML_NEW_LINE);
        }
        if (this.word.getWordFamilyList().size() > 0) {
            builder.append(Constants.HTML_MEAN_FAMILY_HEADER);
            for (int i = 0; i < this.word.getWordFamilyList().size(); i++) {
                WordFamily family = this.word.getWordFamilyList().get(i);
                if (this.db.isExistWord(family.getWord())) {
                    builder.append(CommonUtil.format(Constants.HTML_MEAN_WORD_FAMILY_LINK, family.getWord(), family.getWord(), family.getPartOfSpeechString()));
                } else {
                    builder.append(CommonUtil.format(Constants.HTML_MEAN_WORD_FAMILY_NO_LINK, family.getWord(), family.getPartOfSpeechString()));
                }
                if (i < this.word.getWordFamilyList().size() - 1) {
                    builder.append(Constants.HTML_MEAN_COMMA_SEPARATOR);
                }
            }
            builder.append(Constants.HTML_NEW_LINE);
        }
        if (this.word.getPhraseList().size() > 0) {
            builder.append(Constants.HTML_MEAN_PHRASE_HEADER);
            for (int i = 0; i < this.word.getPhraseList().size(); i++) {
                Phrase phrase = this.word.getPhraseList().get(i);
                builder.append(CommonUtil.format(Constants.HTML_MEAN_PHRASE_DETAIL, phrase.getPhrase(), phrase.getMean()));
            }
        }
        return builder.toString();
    }

    private class SearchClickListener implements MenuItem.OnMenuItemClickListener{
        private String word;
        private EditText input;
        private AlertDialog dialog;

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            this.input = new EditText(ScreenMean.this);
            this.input.setHint(Constants.SEARCH_POPUP_MESSAGE);
            CommonUtil.showPopup(ScreenMean.this, true, Constants.SEARCH_POPUP_TITLE, Constants.SEARCH_POPUP_MESSAGE, this.input,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new SearchOkClickListener(), new SearchCancelClickListener());
            return false;
        }

        public void search(String word){
            this.word = word;
            try {
                if (word.length() == 0) {
                    Toast.makeText(ScreenMean.this, Constants.TOAST_SEARCH_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else if (ScreenMean.this.db.isExistWord(this.word)){
                    this.dialog.dismiss();
                    ScreenMean.this.clearAllLayout(false);
                    HistoryManager.getInstance().getWord(ScreenMean.this.db, this.word);
                    ScreenMean.this.animationFrame.startAnimation(ScreenMean.this.animationRightToLeft);
                    ScreenMean.this.word = HistoryManager.getInstance().getCurrent();
                    ScreenMean.this.fillDataToLayout();
                }
                else{
                    CommonUtil.showPopup(ScreenMean.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.SEARCH_NOT_EXIST_POPUP_MESSAGE, this.word), null,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new ExistOkClickListener(), new ExistCancelClickListener());
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenMean.this, Constants.E001, e);
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
                Intent intent = new Intent(ScreenMean.this, ScreenAddWord.class);
                intent.putExtra(Constants.PR_WORD, SearchClickListener.this.word);
                intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
                ScreenMean.this.startActivity(intent);
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
            Intent intent = new Intent(ScreenMean.this, ScreenAddWord.class);
            intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
            ScreenMean.this.startActivity(intent);
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
                View view = ScreenMean.this.getLayoutInflater().inflate(R.layout.menu_popup_schedule, null);
                this.scheduleNotification = (Switch) view.findViewById(R.id.notification_switch);
                this.scheduleSound = (Switch) view.findViewById(R.id.notification_sound_switch);
                this.scheduleTopic = (Spinner) view.findViewById(R.id.notification_topic_spinner);
                this.scheduleTime = (TextView) view.findViewById(R.id.notication_time_text);
                Calendar calendar = Calendar.getInstance();
                int topicId = CommonUtil.getSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_TOPIC, 0);
                this.scheduleHour = CommonUtil.getSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
                this.scheduleMinute = CommonUtil.getSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_MINUTE, calendar.get(Calendar.MINUTE));
                boolean isSound = CommonUtil.getSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_SOUND, true);
                boolean isOn = CommonUtil.getSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_NOTI, true);
                this.table = CommonUtil.getSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
                if (this.table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)) {
                    List<Topic> list = ScreenMean.this.db.getListTopic();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenMean.this, R.layout.spinner_item, list);
                    this.scheduleTopic.setAdapter(adapter);
                    this.scheduleTopic.setSelection(topicId);
                }
                else{
                    List<Topic> list = new ArrayList<>();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenMean.this, R.layout.spinner_item, list);
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
                CommonUtil.showPopup(ScreenMean.this, false, Constants.SCHEDULE_POPUP_TITLE, null, view,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ScheduleOkClickListener(), new ScheduleCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenMean.this, Constants.E001, e);
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
                CommonUtil.setSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_TOPIC, topicId);
                CommonUtil.setSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_HOUR, ScheduleClickListener.this.scheduleHour);
                CommonUtil.setSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_MINUTE, ScheduleClickListener.this.scheduleMinute);
                CommonUtil.setSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_SOUND, ScheduleClickListener.this.scheduleSound.isChecked());
                CommonUtil.setSharedPreferences(ScreenMean.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_NOTI, ScheduleClickListener.this.scheduleNotification.isChecked());
                if (ScheduleClickListener.this.scheduleNotification.isChecked()){
                    CommonUtil.startSchedule(ScreenMean.this, topicId, ScheduleClickListener.this.scheduleHour, ScheduleClickListener.this.scheduleMinute, ScheduleClickListener.this.scheduleSound.isChecked());
                }
                else{
                    CommonUtil.cancelSchedule(ScreenMean.this);
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
                ScheduleClickListener.this.timePicker = new TimePicker(ScreenMean.this);
                ScheduleClickListener.this.timePicker.setCurrentHour(ScheduleClickListener.this.scheduleHour);
                ScheduleClickListener.this.timePicker.setCurrentMinute(ScheduleClickListener.this.scheduleMinute);
                CommonUtil.showPopup(ScreenMean.this, false, Constants.CALENDAR_POPUP_TITLE, null, ScheduleClickListener.this.timePicker,
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
            View view = ScreenMean.this.getLayoutInflater().inflate(R.layout.popup_select_share, null);
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
                    CommonUtil.shareFacebook(ScreenMean.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenMean.this, Constants.E002, e);
                }
            }
        }

        private class ShareGmailClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                try {
                    ShareClickListener.this.window.dismiss();
                    CommonUtil.sendMailShare(ScreenMean.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenMean.this, Constants.E002, e);
                }
            }
        }
    }

    private class RateAppClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                CommonUtil.rateApp(ScreenMean.this);
            }
            catch (Exception e){
                CommonUtil.showError(ScreenMean.this, Constants.E002, e);
            }
            return false;
        }
    }

    private class ContactClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                String html = CommonUtil.readHtmlTemplate(ScreenMean.this, ConstCommon.CONTACT_TEMPLATE);
                CommonUtil.showPopup(ScreenMean.this, false, Constants.CONTACT_POPUP_TITTLE, CommonUtil.htmlBuilder(html), null,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ContackOkClickListener(), new ContactCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenMean.this, Constants.E001, e);
            }
            return false;
        }

        private class ContackOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    CommonUtil.sendMailContact(ScreenMean.this);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenMean.this, Constants.E002, e);
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

    private class NoteLongClickListener implements AdapterView.OnItemLongClickListener{

        private int position;

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            this.position = position;
            PopupMenu popupMenu = new PopupMenu(ScreenMean.this, view);
            popupMenu.setOnMenuItemClickListener(new ContexItemClickListener());
            popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
            popupMenu.show();
            return false;
        }

        private class ContexItemClickListener implements PopupMenu.OnMenuItemClickListener {

            private EditText input;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.context_edit) {
                    this.input = new EditText(ScreenMean.this);
                    this.input.setText(ScreenMean.this.word.getNoteList().get(NoteLongClickListener.this.position));
                    this.input.setHint(Constants.NOTE_POPUP_HINT);
                    CommonUtil.showPopup(ScreenMean.this, true, Constants.EDIT_NOTE_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.EDIT_NOTE_POPUP_MESSAGE, ScreenMean.this.word.getWord()), this.input,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new OkUpdateClickListener(), new CancelUpdateClickListener());
                    this.input.setSelection(this.input.getText().length());
                }
                else if (id == R.id.context_delete) {
                    ScreenMean.this.word.deleteNote(NoteLongClickListener.this.position);
                    ScreenMean.this.isUpdateNote = true;
                    ScreenMean.this.displayListView();
                }
                return false;
            }

            private class CancelUpdateClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    dialog.dismiss();
                }
            }

            private class OkUpdateClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    String inputString = CommonUtil.replaceString(ContexItemClickListener.this.input.getText().toString());
                    if (inputString.length() == 0) {
                        Toast.makeText(ScreenMean.this, CommonUtil.htmlBuilder(Constants.TOAST_NOTE_INPUT_EMPTY, ScreenMean.this.word.getWord()), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.dismiss();
                        ScreenMean.this.word.updateNote(NoteLongClickListener.this.position, inputString);
                        ScreenMean.this.isUpdateNote = true;
                        ScreenMean.this.displayListView();
                    }
                }
            }
        }
    }

    private class EditClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ScreenMean.this, ScreenAddWord.class);
            intent.putExtra(Constants.PR_MODE, Constants.UPDATE_MODE);
            ScreenMean.this.startActivity(intent);
        }
    }

    private class DeleteClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            CommonUtil.showPopup(ScreenMean.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.DELETE_WORD_POPUP_MESSAGE, word.getWord()), null,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new DeleteOkClickListener(), new DeleteCancelClickListener());
        }

        private class DeleteOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    ScreenMean.this.db.deleteWord(ScreenMean.this.word);
                    ScreenMean.this.db.deleteMarkedWord(ScreenMean.this.word);
                    ScreenMean.this.db.deleteHistoryWord(ScreenMean.this.word);
                    if (HistoryManager.getInstance().isEmpty()){
                        ScreenMean.this.onBackPressed();
                    }
                    else{
                        ScreenMean.this.clearAllLayout(true);
                        ScreenMean.this.word = HistoryManager.getInstance().getPeriod();
                        ScreenMean.this.animationFrame.startAnimation(ScreenMean.this.animationLeftToRight);
                        ScreenMean.this.fillDataToLayout();
                    }
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenMean.this, Constants.E001, e);
                }
            }
        }

        private class DeleteCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }

    private class AddNoteClickListener implements View.OnClickListener{

        private EditText input;

        @Override
        public void onClick(View v) {
            this.input = new EditText(ScreenMean.this);
            this.input.setHint(Constants.NOTE_POPUP_HINT);
            CommonUtil.showPopup(ScreenMean.this, true, Constants.ADD_NOTE_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.ADD_NOTE_POPUP_MESSAGE, ScreenMean.this.word.getWord()), this.input,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new OkAddClickListener(), new CancelAddClickListener());
        }


        private class CancelAddClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }

        private class OkAddClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                String inputString = CommonUtil.replaceString(AddNoteClickListener.this.input.getText().toString());
                if (inputString.length() == 0) {
                    Toast.makeText(ScreenMean.this, CommonUtil.htmlBuilder(Constants.TOAST_NOTE_INPUT_EMPTY, ScreenMean.this.word.getWord()), Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    ScreenMean.this.word.addNote(inputString);
                    ScreenMean.this.isUpdateNote = true;
                    ScreenMean.this.displayListView();
                }
            }
        }
    }

    private class HtmlTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TextView widget = (TextView) v;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int x = (int) event.getX() - widget.getTotalPaddingLeft() + widget.getScrollX();
                int y = (int) event.getY() - widget.getTotalPaddingTop() + widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                URLSpan[] spans = ((Spanned) widget.getText()).getSpans(off, off, URLSpan.class);
                if (spans.length > 0) {
                    try {
                        HistoryManager.getInstance().getWord(ScreenMean.this.db, spans[0].getURL());
                        ScreenMean.this.clearAllLayout(false);
                        ScreenMean.this.word = HistoryManager.getInstance().getCurrent();
                        ScreenMean.this.animationFrame.startAnimation(ScreenMean.this.animationRightToLeft);
                        ScreenMean.this.fillDataToLayout();
                    }
                    catch (Exception e) {
                        CommonUtil.showError(ScreenMean.this, Constants.E001, e);
                    }
                }
            }
            return true;
        }
    }

    private class SwipeDetectorListener implements SwipeDetector.onSwipeEvent{

        @Override
        public void SwipeEventDetected(View v, SwipeDirection SwipeType) {
            if (SwipeType == SwipeDirection.RIGHT_TO_LEFT) {
                try {
                    HistoryManager.getInstance().getNextWord(ScreenMean.this.db);
                    ScreenMean.this.clearAllLayout(false);
                    ScreenMean.this.word = HistoryManager.getInstance().getCurrent();
                    ScreenMean.this.animationFrame.startAnimation(ScreenMean.this.animationRightToLeft);
                    ScreenMean.this.fillDataToLayout();
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenMean.this, Constants.E001, e);
                }
            }
            else if (SwipeType == SwipeDirection.LEFT_TO_RIGHT) {
                if (HistoryManager.getInstance().isEmpty()){
                    ScreenMean.this.animationFrame.startAnimation(ScreenMean.this.animationLeftToRightBack);
                }
                else {
                    try {
                        ScreenMean.this.clearAllLayout(false);
                        ScreenMean.this.word = HistoryManager.getInstance().getPeriod();
                        ScreenMean.this.animationFrame.startAnimation(ScreenMean.this.animationLeftToRight);
                        ScreenMean.this.fillDataToLayout();
                    }
                    catch (Exception e){
                        CommonUtil.showError(ScreenMean.this, Constants.E001, e);
                    }
                }
            }
        }
    }

    private class SpeechClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ScreenMean.this.textToSpeech.speak(ScreenMean.this.word.getWord(), TextToSpeech.QUEUE_FLUSH, null, ScreenMean.this.word.getWord());
        }
    }

    private class SpeechInitListener implements TextToSpeech.OnInitListener{

        @Override
        public void onInit(int status) {
            if(status != TextToSpeech.ERROR){
                ScreenMean.this.textToSpeech.setLanguage(Locale.US);
                ScreenMean.this.textToSpeech.setOnUtteranceProgressListener(new SpeechProgressListen());
            }
        }

        private class SpeechProgressListen extends UtteranceProgressListener {

            @Override
            public void onStart(String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ScreenMean.this.imagePronunciation.setImageResource(R.drawable.loa_phat);
                    }
                });
            }

            @Override
            public void onDone(String utteranceId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ScreenMean.this.imagePronunciation.setImageResource(R.drawable.icon_loa);
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {

            }
        }
    }
}