package vn.edu.hcmut.uddd.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import vn.edu.hcmut.uddd.adapter.FamilyAdapter;
import vn.edu.hcmut.uddd.adapter.MeanAdapter;
import vn.edu.hcmut.uddd.adapter.PartOfSpeechAdapter;
import vn.edu.hcmut.uddd.adapter.PhraseAdapter;
import vn.edu.hcmut.uddd.adapter.TopicNameAdapter;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.common.HistoryManager;
import vn.edu.hcmut.uddd.dao.DatabaseHelper;
import vn.edu.hcmut.uddd.entity.Meaning;
import vn.edu.hcmut.uddd.entity.Phrase;
import vn.edu.hcmut.uddd.entity.Topic;
import vn.edu.hcmut.uddd.entity.Word;
import vn.edu.hcmut.uddd.entity.WordFamily;

public class ScreenAddWord extends Activity {

    private DatabaseHelper db;
    private Word word;
    private Word old;

    private LinearLayout footerLayout;
    private LinearLayout mainLayout;
    private EditText wordEdit;
    private EditText pronunciation;
    private EditText url;
    private Spinner part;
    private Spinner topic;
    private EditText synonyms;
    private EditText antonyms;
    private ListView meanList;
    private ListView familyList;
    private ListView phraseList;
    private KeyboardView keyboard;

    private int mode;
    private boolean isBack;
    private boolean isHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_screen_add_word);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        this.isBack = false;
        this.isHome = false;

        this.db = new DatabaseHelper(ScreenAddWord.this);
        this.mode = this.getIntent().getIntExtra(Constants.PR_MODE, 1);
        if (mode == Constants.CREATE_MODE){
            this.word = new Word();
            String prWord = this.getIntent().getStringExtra(Constants.PR_WORD);
            this.word.setWord(prWord == null ? ConstCommon.EMPTY : prWord);
        }
        else if (mode == Constants.UPDATE_MODE){
            this.word = HistoryManager.getInstance().removeCurrent();
            this.old = HistoryManager.getInstance().getCurrent();
        }

        this.footerLayout = (LinearLayout) this.findViewById(R.id.add_word_footer);
        this.mainLayout = (LinearLayout) this.findViewById(R.id.add_word_main);
        this.meanList = (ListView) this.findViewById(R.id.add_word_list_mean);
        this.familyList = (ListView) this.findViewById(R.id.add_word_list_family);
        this.phraseList = (ListView) this.findViewById(R.id.add_word_list_phrase);
        this.wordEdit = (EditText) this.findViewById(R.id.add_word_word);
        this.pronunciation = (EditText) this.findViewById(R.id.add_word_pronunciation);
        this.url = (EditText) this.findViewById(R.id.add_word_url);
        this.synonyms = (EditText) this.findViewById(R.id.add_word_synonyms);
        this.antonyms = (EditText) this.findViewById(R.id.add_word_antonyms);
        this.part = (Spinner) this.findViewById(R.id.add_word_spiner_part);
        this.topic = (Spinner) this.findViewById(R.id.add_word_spiner_topic);
        if (!CommonUtil.getSharedPreferences(this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY).equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)){
            this.topic.setEnabled(false);
        }
        this.keyboard = (KeyboardView) this.findViewById(R.id.customkeyboard);

        this.findViewById(R.id.add_word_add_mean).setOnClickListener(new AddMeanClick());
        this.findViewById(R.id.add_word_add_family).setOnClickListener(new AddFamilyClick());
        this.findViewById(R.id.add_word_add_phrase).setOnClickListener(new AddPhraseClick());
        this.findViewById(R.id.add_word_root).addOnLayoutChangeListener(new LayoutChangeListener());
        this.findViewById(R.id.add_word_button_complete).setOnClickListener(new SaveClickListener());
        this.findViewById(R.id.add_word_button_cancel).setOnClickListener(new CancelClickListener());

        this.meanList.setOnItemLongClickListener(new MeanItemLongClickListener());
        this.familyList.setOnItemLongClickListener(new FamilyItemLongClickListener());
        this.phraseList.setOnItemLongClickListener(new PhraseItemLongClickListener());

        this.fillDataToLayout();

        this.keyboard.setKeyboard(new Keyboard(ScreenAddWord.this, R.layout.keyboad));
        this.keyboard.setOnKeyboardActionListener(new KeyboardActionListener());
        this.keyboard.setPreviewEnabled(false);
        this.pronunciation.setOnFocusChangeListener(new FocusChangeListener());
        this.pronunciation.setOnClickListener(new PronunctionClickListener());
        this.pronunciation.setShowSoftInputOnFocus(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            this.isHome = true;
            new ExitClickListener().onClick();
            return true;
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

    @Override
    public void onBackPressed() {
        if (this.keyboard.getVisibility() == View.VISIBLE) {
            this.keyboard.setVisibility(View.GONE);
        } else {
            this.isBack = true;
            new ExitClickListener().onClick();
        }
    }

    private void fillDataToLayout() {
        try {
            this.wordEdit.setText(this.word.getWord());
            this.pronunciation.setText(this.word.getPronunciation());
            this.url.setText(this.word.getUrl());
            List<Topic> list = this.db.getListTopic();
            list.add(0, CommonUtil.defaultTopic());
            ArrayAdapter topicAdapter = new TopicNameAdapter(ScreenAddWord.this, R.layout.spinner_item, list);
            ArrayAdapter partAdapter = new PartOfSpeechAdapter(ScreenAddWord.this, R.layout.spinner_item);
            this.topic.setAdapter(topicAdapter);
            this.topic.setSelection(this.word.getTopicId());
            this.part.setAdapter(partAdapter);
            this.part.setSelection(CommonUtil.getPosition(this.word.getPartOfSpeech()));
            this.synonyms.setText(CommonUtil.join(ConstCommon.COMMA, this.word.getSynonyms().toArray(new String[this.word.getSynonyms().size()])));
            this.antonyms.setText(CommonUtil.join(ConstCommon.COMMA, this.word.getAntonyms().toArray(new String[this.word.getAntonyms().size()])));
            this.displayMean();
            this.displayFamily();
            this.displayPhrase();
        }
        catch (Exception e){
            CommonUtil.showError(this, Constants.E001, e);
        }
    }

    private void displayMean(){
        ListAdapter adapter = new MeanAdapter(this, R.layout.text_item, this.word.getMeanList());
        this.meanList.setAdapter(adapter);
    }

    private void displayFamily(){
        ListAdapter adapter = new FamilyAdapter(this, R.layout.text_item, this.word.getWordFamilyList());
        this.familyList.setAdapter(adapter);
    }

    private void displayPhrase(){
        ListAdapter adapter = new PhraseAdapter(this, R.layout.text_item, this.word.getPhraseList());
        this.phraseList.setAdapter(adapter);
    }

    private class SearchClickListener implements MenuItem.OnMenuItemClickListener{
        private String word;
        private EditText input;
        private AlertDialog dialog;

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            this.input = new EditText(ScreenAddWord.this);
            this.input.setHint(Constants.SEARCH_POPUP_MESSAGE);
            CommonUtil.showPopup(ScreenAddWord.this, true, Constants.SEARCH_POPUP_TITLE, Constants.SEARCH_POPUP_MESSAGE, this.input,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new SearchOkClickListener(), new SearchCancelClickListener());
            return false;
        }

        public void search(String word){
            this.word = word;
            try {
                if (word.length() == 0) {
                    Toast.makeText(ScreenAddWord.this, Constants.TOAST_SEARCH_INPUT_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else if (ScreenAddWord.this.db.isExistWord(this.word)){
                    this.dialog.dismiss();
                    HistoryManager.getInstance().getWord(ScreenAddWord.this.db, this.word);
                    Intent intent = new Intent(ScreenAddWord.this, ScreenMean.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ScreenAddWord.this.startActivity(intent);
                }
                else{
                    CommonUtil.showPopup(ScreenAddWord.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.SEARCH_NOT_EXIST_POPUP_MESSAGE, this.word), null,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new ExistOkClickListener(), new ExistCancelClickListener());
                }
            }
            catch (Exception e){
                CommonUtil.showError(ScreenAddWord.this, Constants.E001, e);
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
                Intent intent = new Intent(ScreenAddWord.this, ScreenAddWord.class);
                intent.putExtra(Constants.PR_WORD, SearchClickListener.this.word);
                intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
                ScreenAddWord.this.startActivity(intent);
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
            Intent intent = new Intent(ScreenAddWord.this, ScreenAddWord.class);
            intent.putExtra(Constants.PR_MODE, Constants.CREATE_MODE);
            ScreenAddWord.this.startActivity(intent);
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
                View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.menu_popup_schedule, null);
                this.scheduleNotification = (Switch) view.findViewById(R.id.notification_switch);
                this.scheduleSound = (Switch) view.findViewById(R.id.notification_sound_switch);
                this.scheduleTopic = (Spinner) view.findViewById(R.id.notification_topic_spinner);
                this.scheduleTime = (TextView) view.findViewById(R.id.notication_time_text);
                Calendar calendar = Calendar.getInstance();
                int topicId = CommonUtil.getSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_TOPIC, 0);
                this.scheduleHour = CommonUtil.getSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
                this.scheduleMinute = CommonUtil.getSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_MINUTE, calendar.get(Calendar.MINUTE));
                boolean isSound = CommonUtil.getSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_SOUND, true);
                boolean isOn = CommonUtil.getSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_SCHEDULE_NOTI, true);
                this.table = CommonUtil.getSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE), ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
                if (this.table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)) {
                    List<Topic> list = ScreenAddWord.this.db.getListTopic();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenAddWord.this, R.layout.spinner_item, list);
                    this.scheduleTopic.setAdapter(adapter);
                    this.scheduleTopic.setSelection(topicId);
                }
                else{
                    List<Topic> list = new ArrayList<>();
                    list.add(0, CommonUtil.defaultTopic());
                    SpinnerAdapter adapter = new TopicNameAdapter(ScreenAddWord.this, R.layout.spinner_item, list);
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
                CommonUtil.showPopup(ScreenAddWord.this, false, Constants.SCHEDULE_POPUP_TITLE, null, view,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ScheduleOkClickListener(), new ScheduleCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenAddWord.this, Constants.E001, e);
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
                CommonUtil.setSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_TOPIC, topicId);
                CommonUtil.setSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_HOUR, ScheduleClickListener.this.scheduleHour);
                CommonUtil.setSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_MINUTE, ScheduleClickListener.this.scheduleMinute);
                CommonUtil.setSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_SOUND, ScheduleClickListener.this.scheduleSound.isChecked());
                CommonUtil.setSharedPreferences(ScreenAddWord.this.getSharedPreferences(ConstCommon.SP_FILE_NAME, MODE_PRIVATE),ConstCommon.SP_SCHEDULE_NOTI, ScheduleClickListener.this.scheduleNotification.isChecked());
                if (ScheduleClickListener.this.scheduleNotification.isChecked()){
                    CommonUtil.startSchedule(ScreenAddWord.this, topicId, ScheduleClickListener.this.scheduleHour, ScheduleClickListener.this.scheduleMinute, ScheduleClickListener.this.scheduleSound.isChecked());
                }
                else{
                    CommonUtil.cancelSchedule(ScreenAddWord.this);
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
                ScheduleClickListener.this.timePicker = new TimePicker(ScreenAddWord.this);
                ScheduleClickListener.this.timePicker.setCurrentHour(ScheduleClickListener.this.scheduleHour);
                ScheduleClickListener.this.timePicker.setCurrentMinute(ScheduleClickListener.this.scheduleMinute);
                CommonUtil.showPopup(ScreenAddWord.this, false, Constants.CALENDAR_POPUP_TITLE, null, ScheduleClickListener.this.timePicker,
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
            View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.popup_select_share, null);
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
                    CommonUtil.shareFacebook(ScreenAddWord.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenAddWord.this, Constants.E002, e);
                }
            }
        }

        private class ShareGmailClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                try {
                    ShareClickListener.this.window.dismiss();
                    CommonUtil.sendMailShare(ScreenAddWord.this);
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenAddWord.this, Constants.E002, e);
                }
            }
        }
    }

    private class RateAppClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                CommonUtil.rateApp(ScreenAddWord.this);
            }
            catch (Exception e){
                CommonUtil.showError(ScreenAddWord.this, Constants.E002, e);
            }
            return false;
        }
    }

    private class ContactClickListener implements MenuItem.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            try {
                String html = CommonUtil.readHtmlTemplate(ScreenAddWord.this, ConstCommon.CONTACT_TEMPLATE);
                CommonUtil.showPopup(ScreenAddWord.this, false, Constants.CONTACT_POPUP_TITTLE, CommonUtil.htmlBuilder(html), null,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new ContackOkClickListener(), new ContactCancelClickListener());
            }
            catch (Exception e){
                CommonUtil.showError(ScreenAddWord.this, Constants.E001, e);
            }
            return false;
        }

        private class ContackOkClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                try {
                    dialog.dismiss();
                    CommonUtil.sendMailContact(ScreenAddWord.this);
                }
                catch (Exception e) {
                    CommonUtil.showError(ScreenAddWord.this, Constants.E002, e);
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

    private class AddMeanClick implements View.OnClickListener{
        private EditText mean;
        private EditText ex;
        private EditText exMean;
        private Spinner part;

        @Override
        public void onClick(View v) {
            View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.popup_add_mean, null);
            this.mean = (EditText) view.findViewById(R.id.add_mean_mean);
            this.ex = (EditText) view.findViewById(R.id.add_mean_ex);
            this.exMean = (EditText) view.findViewById(R.id.add_mean_ex_mean);
            this.part = (Spinner) view.findViewById(R.id.add_mean_part);
            ArrayAdapter adapter = new PartOfSpeechAdapter(ScreenAddWord.this, R.layout.spinner_item);
            this.part.setAdapter(adapter);
            CommonUtil.showPopup(ScreenAddWord.this, true, Constants.ADD_MEAN_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.ADD_MEAN_POPUP_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), view,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new OkAddClickListener(), new CancelAddClickListener());
        }

        private class OkAddClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                String mean = CommonUtil.replaceString(AddMeanClick.this.mean.getText().toString());
                String ex = CommonUtil.replaceString(AddMeanClick.this.ex.getText().toString());
                String exMean = CommonUtil.replaceString(AddMeanClick.this.exMean.getText().toString());
                if (mean.equals(ConstCommon.EMPTY)){
                    Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_MEAN_MEAN_EMPTY, ScreenAddWord.this.wordEdit.getText().toString()), Toast.LENGTH_SHORT).show();
                }
                else if (!ex.equals(ConstCommon.EMPTY) && exMean.equals(ConstCommon.EMPTY)){
                    Toast.makeText(ScreenAddWord.this, Constants.TOAST_MEAN_EX_MEAN_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    Meaning meaning = new Meaning();
                    meaning.setMean(mean);
                    meaning.setExample(ex);
                    meaning.setExampleMean(exMean);
                    meaning.setPartOfSpeech(CommonUtil.getPartOfSpeech(AddMeanClick.this.part.getSelectedItem().toString()));
                    ScreenAddWord.this.word.addMean(meaning);
                    ScreenAddWord.this.displayMean();
                }
            }
        }

        private class CancelAddClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }

    private class AddFamilyClick implements View.OnClickListener{
        private EditText wordFamily;
        private Spinner part;

        @Override
        public void onClick(View v) {
            View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.popup_add_family, null);
            this.wordFamily = (EditText) view.findViewById(R.id.add_family_word);
            this.part = (Spinner) view.findViewById(R.id.add_family_part);
            ArrayAdapter adapter = new PartOfSpeechAdapter(ScreenAddWord.this, R.layout.spinner_item);
            this.part.setAdapter(adapter);
            CommonUtil.showPopup(ScreenAddWord.this, true, Constants.ADD_FAMILY_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.ADD_FAMILY_POPUP_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), view,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new OkAddClickListener(), new CancelAddClickListener());
        }

        private class OkAddClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                String wordFamily = CommonUtil.replaceString(AddFamilyClick.this.wordFamily.getText().toString());
                if (wordFamily.equals(ConstCommon.EMPTY)){
                    Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_FAMILY_WORD_EMPTY, ScreenAddWord.this.wordEdit.getText().toString()), Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    WordFamily family = new WordFamily();
                    family.setWord(CommonUtil.replaceString(AddFamilyClick.this.wordFamily.getText().toString()));
                    family.setPartOfSpeech(CommonUtil.getPartOfSpeech(AddFamilyClick.this.part.getSelectedItem().toString()));
                    ScreenAddWord.this.word.addWordFamily(family);
                    ScreenAddWord.this.displayFamily();
                }
            }
        }

        private class CancelAddClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }

    private class AddPhraseClick implements View.OnClickListener{
        private EditText phrase;
        private EditText mean;

        @Override
        public void onClick(View v) {
            View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.popup_add_phrase, null);
            this.phrase = (EditText) view.findViewById(R.id.add_phrase_word);
            this.mean = (EditText) view.findViewById(R.id.add_phrase_mean);
            CommonUtil.showPopup(ScreenAddWord.this, true, Constants.ADD_PHRASE_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.ADD_PHRASE_POPUP_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), view,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new OkAddClickListener(), new CancelAddClickListener());
        }

        private class OkAddClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                String phraseWord = CommonUtil.replaceString(AddPhraseClick.this.phrase.getText().toString());
                String mean = CommonUtil.replaceString(AddPhraseClick.this.mean.getText().toString());
                if (phraseWord.equals(ConstCommon.EMPTY)) {
                    Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_PHRASE_WORD_EMPTY, ScreenAddWord.this.wordEdit.getText().toString()), Toast.LENGTH_SHORT).show();
                }
                else if (mean.equals(ConstCommon.EMPTY)) {
                    Toast.makeText(ScreenAddWord.this, Constants.TOAST_PHRASE_MEAN_EMPTY, Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    Phrase phrase = new Phrase();
                    phrase.setPhrase(CommonUtil.replaceString(AddPhraseClick.this.phrase.getText().toString()));
                    phrase.setMean(CommonUtil.replaceString(AddPhraseClick.this.mean.getText().toString()));
                    ScreenAddWord.this.word.addPhrase(phrase);
                    ScreenAddWord.this.displayPhrase();
                }
            }
        }

        private class CancelAddClickListener implements CommonUtil.OnClickListener{

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
                ScreenAddWord.this.footerLayout.setVisibility(View.VISIBLE);
                ScreenAddWord.this.mainLayout.requestLayout();
            } else if (oldBottom - bottom >= ConstCommon.MIN_SIZE_LAY_OUT_CHANGE) {
                ScreenAddWord.this.footerLayout.setVisibility(View.GONE);
                ScreenAddWord.this.mainLayout.requestLayout();
            }
        }
    }

    private class MeanItemLongClickListener implements AdapterView.OnItemLongClickListener{
        private int position;

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            this.position = position;
            PopupMenu popupMenu = new PopupMenu(ScreenAddWord.this, view);
            popupMenu.setOnMenuItemClickListener(new ContextMenuItemClickListener());
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popupMenu.getMenu());
            popupMenu.show();
            return false;
        }

        private class ContextMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            private EditText mean;
            private EditText ex;
            private EditText exMean;
            private Spinner part;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.context_edit) {
                    Meaning meaning = (Meaning) ScreenAddWord.this.meanList.getAdapter().getItem(MeanItemLongClickListener.this.position);
                    View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.popup_add_mean, null);
                    this.mean = (EditText) view.findViewById(R.id.add_mean_mean);
                    this.ex = (EditText) view.findViewById(R.id.add_mean_ex);
                    this.exMean = (EditText) view.findViewById(R.id.add_mean_ex_mean);
                    this.part = (Spinner) view.findViewById(R.id.add_mean_part);
                    ArrayAdapter adapter = new PartOfSpeechAdapter(ScreenAddWord.this, R.layout.spinner_item);
                    this.part.setAdapter(adapter);
                    this.mean.setText(meaning.getMean());
                    this.ex.setText(meaning.getExample());
                    this.exMean.setText(meaning.getExampleMean());
                    this.part.setSelection(CommonUtil.getPosition(meaning.getPartOfSpeech()));
                    CommonUtil.showPopup(ScreenAddWord.this, true, Constants.EDIT_MEAN_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.EDIT_MEAN_POPUP_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), view,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new OkUpdateClickListener(), new CancelUpdateClickListener());
                    this.mean.setSelection(this.mean.getText().length());
                }
                else if (id == R.id.context_delete) {
                    ScreenAddWord.this.word.deleteMean(MeanItemLongClickListener.this.position);
                    ScreenAddWord.this.displayMean();
                }
                return false;
            }

            private class OkUpdateClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    String mean = CommonUtil.replaceString(ContextMenuItemClickListener.this.mean.getText().toString());
                    String ex = CommonUtil.replaceString(ContextMenuItemClickListener.this.ex.getText().toString());
                    String exMean = CommonUtil.replaceString(ContextMenuItemClickListener.this.exMean.getText().toString());
                    if (mean.equals(ConstCommon.EMPTY)){
                        Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_MEAN_MEAN_EMPTY, ScreenAddWord.this.wordEdit.getText().toString()), Toast.LENGTH_SHORT).show();
                    }
                    else if (!ex.equals(ConstCommon.EMPTY) && exMean.equals(ConstCommon.EMPTY)){
                        Toast.makeText(ScreenAddWord.this, Constants.TOAST_MEAN_EX_MEAN_EMPTY, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.dismiss();
                        Meaning meaning = new Meaning();
                        meaning.setMean(mean);
                        meaning.setExample(ex);
                        meaning.setExampleMean(exMean);
                        meaning.setPartOfSpeech(CommonUtil.getPartOfSpeech(ContextMenuItemClickListener.this.part.getSelectedItem().toString()));
                        ScreenAddWord.this.word.updateMean(MeanItemLongClickListener.this.position, meaning);
                        ScreenAddWord.this.displayMean();
                    }
                }
            }

            private class CancelUpdateClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    dialog.dismiss();
                }
            }
        }
    }

    private class FamilyItemLongClickListener implements AdapterView.OnItemLongClickListener{
        private int position;

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            this.position = position;
            PopupMenu popupMenu = new PopupMenu(ScreenAddWord.this, view);
            popupMenu.setOnMenuItemClickListener(new ContextMenuItemClickListener());
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popupMenu.getMenu());
            popupMenu.show();
            return false;
        }

        private class ContextMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            private EditText family;
            private Spinner part;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.context_edit) {
                    WordFamily wordFamily = (WordFamily) ScreenAddWord.this.familyList.getAdapter().getItem(FamilyItemLongClickListener.this.position);
                    View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.popup_add_family, null);
                    this.family = (EditText) view.findViewById(R.id.add_family_word);
                    this.part = (Spinner) view.findViewById(R.id.add_family_part);
                    ArrayAdapter adapter = new PartOfSpeechAdapter(ScreenAddWord.this, R.layout.spinner_item);
                    this.part.setAdapter(adapter);
                    this.family.setText(wordFamily.getWord());
                    this.part.setSelection(CommonUtil.getPosition(wordFamily.getPartOfSpeech()));
                    CommonUtil.showPopup(ScreenAddWord.this, true, Constants.EDIT_FAMILY_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.EDIT_FAMILY_POPUP_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), view,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new OkUpdateClickListener(), new CancelUpdateClickListener());
                    this.family.setSelection(this.family.getText().length());
                }
                else if (id == R.id.context_delete) {
                    ScreenAddWord.this.word.deleteWordFamily(FamilyItemLongClickListener.this.position);
                    ScreenAddWord.this.displayFamily();
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
                    String family = CommonUtil.replaceString(ContextMenuItemClickListener.this.family.getText().toString());
                    if (family.equals(ConstCommon.EMPTY)){
                        Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_FAMILY_WORD_EMPTY, ScreenAddWord.this.wordEdit.getText().toString()), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.dismiss();
                        WordFamily wordFamily = new WordFamily();
                        wordFamily.setWord(family);
                        wordFamily.setPartOfSpeech(CommonUtil.getPartOfSpeech(ContextMenuItemClickListener.this.part.getSelectedItem().toString()));
                        ScreenAddWord.this.word.updateWordFamily(FamilyItemLongClickListener.this.position, wordFamily);
                        ScreenAddWord.this.displayFamily();
                    }
                }
            }
        }
    }

    private class PhraseItemLongClickListener implements AdapterView.OnItemLongClickListener{
        private int position;

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            this.position = position;
            PopupMenu popupMenu = new PopupMenu(ScreenAddWord.this, view);
            popupMenu.setOnMenuItemClickListener(new ContextMenuItemClickListener());
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popupMenu.getMenu());
            popupMenu.show();
            return false;
        }

        private class ContextMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            private EditText phrase;
            private EditText mean;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.context_edit) {
                    Phrase phrase = (Phrase) ScreenAddWord.this.phraseList.getAdapter().getItem(PhraseItemLongClickListener.this.position);
                    View view = ScreenAddWord.this.getLayoutInflater().inflate(R.layout.popup_add_phrase, null);
                    this.phrase = (EditText) view.findViewById(R.id.add_phrase_word);
                    this.mean = (EditText) view.findViewById(R.id.add_phrase_mean);
                    this.phrase.setText(phrase.getPhrase());
                    this.mean.setText(phrase.getMean());
                    CommonUtil.showPopup(ScreenAddWord.this, true, Constants.EDIT_PHRASE_POPUP_TITLE, CommonUtil.htmlBuilder(Constants.EDIT_PHRASE_POPUP_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), view,
                            Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                            new OkUpdateClickListener(), new CancelUpdateClickListener());
                    this.phrase.setSelection(this.phrase.getText().length());
                }
                else if (id == R.id.context_delete) {
                    ScreenAddWord.this.word.deletePhrase(PhraseItemLongClickListener.this.position);
                    ScreenAddWord.this.displayPhrase();
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
                    String phrase = CommonUtil.replaceString(ContextMenuItemClickListener.this.phrase.getText().toString());
                    String mean = CommonUtil.replaceString(ContextMenuItemClickListener.this.mean.getText().toString());
                    if (phrase.equals(ConstCommon.EMPTY)){
                        Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_PHRASE_WORD_EMPTY, ScreenAddWord.this.wordEdit.getText().toString()), Toast.LENGTH_SHORT).show();
                    }
                    else if (mean.equals(ConstCommon.EMPTY)){
                        Toast.makeText(ScreenAddWord.this, Constants.TOAST_PHRASE_MEAN_EMPTY, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dialog.dismiss();
                        Phrase phraseWord = new Phrase();
                        phraseWord.setPhrase(phrase);
                        phraseWord.setMean(mean);
                        ScreenAddWord.this.word.updatePhrase(PhraseItemLongClickListener.this.position, phraseWord);
                        ScreenAddWord.this.displayPhrase();
                    }
                }
            }
        }
    }

    private class SaveClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String wordEdit = ScreenAddWord.this.wordEdit.getText().toString().trim().toLowerCase();
            String pronunciation = CommonUtil.replaceString(ScreenAddWord.this.pronunciation.getText().toString());
            if (wordEdit.length() == 0){
                Toast.makeText(ScreenAddWord.this, Constants.TOAST_ADD_WORD_EMPTY, Toast.LENGTH_SHORT).show();
            }
            else if (pronunciation.equals(ConstCommon.EMPTY)){
                Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_ADD_PRONUNCIATION_EMPTY, wordEdit), Toast.LENGTH_SHORT).show();
            }
            else if (ScreenAddWord.this.word.getMeanList().size() == 0){
                Toast.makeText(ScreenAddWord.this, CommonUtil.htmlBuilder(Constants.TOAST_ADD_MEAN_EMPTY, wordEdit), Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    if (ScreenAddWord.this.mode == Constants.CREATE_MODE){
                        if (ScreenAddWord.this.db.isExistWord(wordEdit)){
                            new SaveWordListener().Save(CommonUtil.htmlBuilder(Constants.SAVE_WORD_ESXIST_POPUP_MESSAGE, wordEdit), true);
                        }
                        else{
                            new SaveWordListener().Save(CommonUtil.htmlBuilder(Constants.SAVE_WORD_POPUP_MESSAGE, wordEdit), false);
                        }
                    }
                    else if (ScreenAddWord.this.mode == Constants.UPDATE_MODE){
                        if (ScreenAddWord.this.db.isExistWord(wordEdit)){
                            new SaveWordListener().Save(CommonUtil.htmlBuilder(Constants.UPDATE_WORD_POPUP_MESSAGE, wordEdit), true);
                        }
                        else{
                            new SaveWordListener().Save(CommonUtil.htmlBuilder(Constants.UPDATE_WORD_NOT_EXIST_POPUP_MESSAGE, wordEdit), false);
                        }
                    }
                }
                catch (Exception e){
                    CommonUtil.showError(ScreenAddWord.this, Constants.E001, e);
                }
            }
        }

        private class SaveWordListener {

            private boolean isExist;

            public void Save(CharSequence message, boolean isExist) throws Exception{
                this.isExist = isExist;
                CommonUtil.showPopup(ScreenAddWord.this, false, Constants.POPUP_TIT_ALERT, message, null,
                        Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                        new OkSaveClickListener(), new CancelSaveClickListener());
            }

            private class OkSaveClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    try {
                        dialog.dismiss();
                        ScreenAddWord.this.word.setWord(ScreenAddWord.this.wordEdit.getText().toString().trim().toLowerCase());
                        ScreenAddWord.this.word.setPronunciation(CommonUtil.replaceString(ScreenAddWord.this.pronunciation.getText().toString()));
                        ScreenAddWord.this.word.setUrl(ScreenAddWord.this.url.getText().toString().trim());
                        ScreenAddWord.this.word.setTopicId(((Topic) ScreenAddWord.this.topic.getSelectedItem()).getId());
                        ScreenAddWord.this.word.setPartOfSpeech(CommonUtil.getPartOfSpeech(ScreenAddWord.this.part.getSelectedItem().toString()));
                        ScreenAddWord.this.word.setSynonyms(CommonUtil.convertFromArrayToList(CommonUtil.split(CommonUtil.replaceString(ScreenAddWord.this.synonyms.getText().toString()), ConstCommon.COMMA)));
                        ScreenAddWord.this.word.setAntonyms(CommonUtil.convertFromArrayToList(CommonUtil.split(CommonUtil.replaceString(ScreenAddWord.this.antonyms.getText().toString()), ConstCommon.COMMA)));
                        if (SaveWordListener.this.isExist) {
                            ScreenAddWord.this.db.updateWord(ScreenAddWord.this.word);
                        }
                        else{
                            ScreenAddWord.this.db.insertWord(ScreenAddWord.this.word);
                        }
                        if (ScreenAddWord.this.isBack){
                            ScreenAddWord.super.onBackPressed();
                        }
                        else if (ScreenAddWord.this.isHome){
                            HistoryManager.getInstance().clearHistory();
                            Intent intent = new Intent(ScreenAddWord.this, ScreenHome.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            ScreenAddWord.this.startActivity(intent);
                        }
                        else {
                            if (ScreenAddWord.this.old != null && !ScreenAddWord.this.old.getWord().equals(ScreenAddWord.this.word.getWord())){
                                ScreenAddWord.this.old = ScreenAddWord.this.db.getWord(ScreenAddWord.this.old.getWord());
                            }
                            else{
                                HistoryManager.getInstance().removeWord(ScreenAddWord.this.old);
                            }
                            HistoryManager.getInstance().getWord(ScreenAddWord.this.db, ScreenAddWord.this.word.getWord());
                            Intent intent = new Intent(ScreenAddWord.this, ScreenMean.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            ScreenAddWord.this.startActivity(intent);
                            ScreenAddWord.this.finish();
                        }
                    }
                    catch (Exception e){
                        CommonUtil.showError(ScreenAddWord.this, Constants.E001, e);
                    }
                }
            }

            private class CancelSaveClickListener implements CommonUtil.OnClickListener{

                @Override
                public void onClick(AlertDialog dialog) {
                    dialog.dismiss();
                }
            }
        }
    }

    private class CancelClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            CommonUtil.showPopup(ScreenAddWord.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.ADD_CANCEL_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), null,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEGATIVE_CANCEL,
                    new OkCancelClickListener(), new CancelCancelClickListener());
        }

        private class OkCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                ScreenAddWord.super.onBackPressed();
            }
        }

        private class CancelCancelClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }
    }

    private class ExitClickListener{

        public void onClick() {
            CommonUtil.showPopup(ScreenAddWord.this, false, Constants.POPUP_TIT_ALERT, CommonUtil.htmlBuilder(Constants.ADD_CANCEL_MESSAGE, ScreenAddWord.this.wordEdit.getText().toString()), null,
                    Constants.BUTTON_POSITIVE_OK, Constants.BUTTON_NEUTRAL_SAVE, Constants.BUTTON_NEGATIVE_CANCEL,
                    new OkExitClickListener(), new ContinueExistClickListener(), new CancelExitClickListener());
        }

        private class OkExitClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                ScreenAddWord.super.onBackPressed();
            }
        }

        private class CancelExitClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
            }
        }

        private class ContinueExistClickListener implements CommonUtil.OnClickListener{

            @Override
            public void onClick(AlertDialog dialog) {
                dialog.dismiss();
                new SaveClickListener().onClick(dialog.getListView());
            }
        }
    }

    private class KeyboardActionListener implements KeyboardView.OnKeyboardActionListener{

        @Override
        public void onPress(int primaryCode) {}

        @Override
        public void onRelease(int primaryCode) {}

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            int start = ScreenAddWord.this.pronunciation.getSelectionStart();
            int end = ScreenAddWord.this.pronunciation.getSelectionEnd();
            if (primaryCode == -1){
                if (start < end) {
                    ScreenAddWord.this.pronunciation.getText().replace(start, end, ConstCommon.EMPTY);
                }
                else if (start > 0){
                    ScreenAddWord.this.pronunciation.getText().replace(start - 1, start, ConstCommon.EMPTY);
                }
            }
            else{
                if (start < end){
                    ScreenAddWord.this.pronunciation.getText().replace(start, end, String.valueOf(ConstCommon.KEY_BOARD.charAt(primaryCode)));
                }
                else{
                    ScreenAddWord.this.pronunciation.getText().replace(start, start, String.valueOf(ConstCommon.KEY_BOARD.charAt(primaryCode)));
                }
            }
        }

        @Override
        public void onText(CharSequence text) {}

        @Override
        public void swipeLeft() {}

        @Override
        public void swipeRight() {}

        @Override
        public void swipeDown() {}

        @Override
        public void swipeUp() {}
    }

    private class FocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                InputMethodManager imm = (InputMethodManager) ScreenAddWord.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                ScreenAddWord.this.keyboard.setVisibility(View.VISIBLE);
            }
            else{
                ScreenAddWord.this.keyboard.setVisibility(View.GONE);
            }
        }
    }

    private class PronunctionClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) ScreenAddWord.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            ScreenAddWord.this.keyboard.setVisibility(View.VISIBLE);
        }
    }
}