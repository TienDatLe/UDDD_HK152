package vn.edu.hcmut.uddd.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.entity.Dictionary;
import vn.edu.hcmut.uddd.entity.Topic;
import vn.edu.hcmut.uddd.entity.Word;

/**
 * Created by TRAN VAN HEN on 3/2/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private SharedPreferences preferences;

    public DatabaseHelper(Context context) {
        super(context, ConstCommon.DATABASE_NAME, null, ConstCommon.DATABASE_VERSION);
        this.context = context;
        this.preferences = context.getSharedPreferences(ConstCommon.SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(ConstCommon.DATABASE_SQL), StandardCharsets.UTF_8));
            String line;
            db.beginTransaction();
            while ((line = reader.readLine()) != null){
                db.execSQL(line);
            }
            db.setTransactionSuccessful();
            reader.close();
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.DEFAULT_DICTIONARY_TABLE);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_MAX_TOPIC_ID, ConstCommon.MAX_TOPIC);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_MAX_HISTORY, ConstCommon.MAX_HISTORY);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_HISTORY, 0);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CHECK_UPDATE_APPLICATION, true);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CHECK_UPDATE_DATABASE, true);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DB_DOWNLOAD_VERSION + ConstCommon.DEFAULT_DICTIONARY_TABLE, 1);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DB_DOWNLOAD_ID_DOWNLOADED + ConstCommon.DEFAULT_DICTIONARY_TABLE, 1);
            for (int i = 0; i < ConstCommon.MAX_WORD.length; i++){
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_MAX_WORD_OF_TOPIC + ConstCommon.DEFAULT_DICTIONARY_TABLE + i, ConstCommon.MAX_WORD[i]);
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_WORD_OF_TOPIC + ConstCommon.DEFAULT_DICTIONARY_TABLE + i, 0);
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DELETED_WORD_OF_TOPIC + ConstCommon.DEFAULT_DICTIONARY_TABLE + i, CommonUtil.convertListToString(new ArrayList<Integer>()));
            }
            for (int i = 0; i < ConstCommon.DICTIONARY_TABLE.length; i++){
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_MAX_WORD_OF_TOPIC + ConstCommon.DICTIONARY_TABLE[i] + 0, 0);
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_WORD_OF_TOPIC + ConstCommon.DICTIONARY_TABLE[i] + 0, 0);
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DELETED_WORD_OF_TOPIC + ConstCommon.DICTIONARY_TABLE[i] + 0, CommonUtil.convertListToString(new ArrayList<Integer>()));
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DB_DOWNLOAD_VERSION + ConstCommon.DICTIONARY_TABLE[i], 1);
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DB_DOWNLOAD_ID_DOWNLOADED + ConstCommon.DICTIONARY_TABLE[i], 1);
            }
        }
        catch (Exception e) {
            CommonUtil.logError(this, e);
        }
        finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion + 1; i <= newVersion; i++){
            this.upgradeVersion(db, i);
        }
    }

    private void upgradeVersion(SQLiteDatabase db, int version){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(ConstCommon.DATABASE_FOLDER + version + ConstCommon.DATABASE_EXT), StandardCharsets.UTF_8));
            String line;
            db.beginTransaction();
            while ((line = reader.readLine()) != null) {
                db.execSQL(line);
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e){
            CommonUtil.logError(this, e);
        }
        finally {
            db.endTransaction();
        }
    }

    /**
     * Get word by id
     * param[id]: the word id
     * param[topic_id]: the topic id
     * return: the word
     * */
    public Word getWord(int id, int topic_id) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Word result = new Word();
        try{
            db = this.getReadableDatabase();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            cursor = db.rawQuery(CommonUtil.format(ConstCommon.SQL_GET_WORD_BY_ID, table), new String[]{String.valueOf(id), String.valueOf(topic_id)});
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
            }
            result.setId(id);
            result.setTopicId(topic_id);
            result.setWord(cursor.getString(0));
            result.setData(cursor.getString(1));
            result.setNote(cursor.getString(2));
            result.setUrl(cursor.getString(3));
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get word by word
     * param[word]: the word
     * return: the word
     * */
    public Word getWord(String word) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Word result = new Word();
        try{
            db = this.getReadableDatabase();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            cursor = db.rawQuery(CommonUtil.format(ConstCommon.SQL_GET_WORD_BY_WORD, table), new String[]{word});
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
            }
            result.setId(cursor.getInt(0));
            result.setTopicId(cursor.getInt(1));
            result.setWord(word);
            result.setData(cursor.getString(2));
            result.setNote(cursor.getString(3));
            result.setUrl(cursor.getString(4));
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Check is exist word
     * param[word]: the word to check
     * return: true if exist*/
    public boolean isExistWord(String word) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean result = false;
        try{
            db = this.getReadableDatabase();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            cursor = db.rawQuery(CommonUtil.format(ConstCommon.SQL_COUNT_WORD, table), new String[]{word});
            cursor.moveToFirst();
            result = cursor.getInt(0) != 0;
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get the list topic
     * return: the list topic
     * */
    public List<Topic> getListTopic() throws Exception{
        List<Topic> result = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_GET_LIST_TOPIC, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Topic topic = new Topic();
                topic.setId(cursor.getInt(0));
                topic.setName(cursor.getString(1));
                topic.setMean(cursor.getString(2));
                result.add(topic);
                cursor.moveToNext();
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get topic name by id
     * param[id]: the topic id
     * return: the name of topic
     * */
    public String getTopicName(int id) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String result = ConstCommon.EMPTY;
        try{
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_GET_TOPIC_NAME_BY_ID, new String[]{String.valueOf(id)});
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
            }
            result = cursor.getString(0);
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get topic id by name
     * param[name]: the topic name
     * return: the id of topic
     * */
    public int getTopicId(String name) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int result = 0;
        try{
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_GET_TOPIC_ID_BY_NAME, new String[]{name});
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
            }
            result = cursor.getInt(0);
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Update the note of word
     * param[word]: the word to update
     * param[note]: the note to update
     * return: number of row update
     * */
    public void updateNoteOfWord(Word word) throws Exception{
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            db.execSQL(CommonUtil.format(ConstCommon.SQL_UPDATE_NOTE_WORD, table), new String[]{word.getNote(), word.getWord()});
        }
        finally {
            if (db!=null) {
                db.close();
            }
        }
    }

    /**
     * Check the word is marked
     * param[word]: the word to check
     * return: true if it is marked
     * */
    public boolean getIsMarked(Word word) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_COUNT_IS_MARK, new String[]{word.getWord()});
            cursor.moveToFirst();
            result = cursor.getInt(0) != 0;
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Check the word is history
     * param[word]: the word to check
     * return: true if it is history
     * */
    public boolean getIsHistory(Word word) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_COUNT_HISTORY, new String[]{word.getWord()});
            cursor.moveToFirst();
            result = cursor.getInt(0) != 0;
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Marking a word
     * param[word]: the word to mark
     */
    public void insertMarkedWord(Word word)throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.execSQL(ConstCommon.SQL_INSERT_IS_MARK, new String[]{word.getWord()});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Delete marking word
     * param[word]: the word to delete
     * */
    public void deleteMarkedWord(Word word) throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.execSQL(ConstCommon.SQL_DELETE_IS_MARK, new String[]{word.getWord()});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * History a word
     * param[word]: the word to history
     */
    public void insertHistoryWord(Word word)throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            int max = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_MAX_HISTORY, 0);
            int current = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_HISTORY, 0);
            if (current >= max){
                cursor = db.rawQuery(ConstCommon.SQL_GET_MAX_POINT_HISTORY, null);
                cursor.moveToFirst();
                if (cursor.isAfterLast()){
                    throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
                }
                db.execSQL(ConstCommon.SQL_DELETE_HISTORY, new String[]{cursor.getString(0)});
            }
            else{
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_HISTORY, current + 1);
            }
            db.execSQL(ConstCommon.SQL_INSERT_HISTORY, new String[]{word.getWord()});
            db.setTransactionSuccessful();
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
    }

    /**
     * Delete history word
     * param[word]: the word to delete*/
    public void deleteHistoryWord(Word word) throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int current = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_HISTORY, 0);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_HISTORY, current - 1);
            db.execSQL(ConstCommon.SQL_DELETE_HISTORY, new String[]{word.getWord()});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Update point of word
     * param[word]: the word to update
     * */
    public void addPoint(Word word, int point) throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.execSQL(ConstCommon.SQL_UPDATE_POINT, new Object[]{point, word.getWord()});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Update point of word
     * param[word]: the word to update
     * */
    public void updateHistory(Word word) throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.execSQL(ConstCommon.SQL_UPDATE_HISTORY, new Object[]{word.getWord()});
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Insert new word
     * param[word]: the word insert
     * */
    public void insertWord(Word word) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            String delete = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DELETED_WORD_OF_TOPIC + table + word.getTopicId(), ConstCommon.EMPTY);
            List<Integer> deleted = CommonUtil.convertStringToList(delete);
            int id;
            if (deleted.size() > 0){
                id = deleted.remove(0);
                delete = CommonUtil.convertListToString(deleted);
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DELETED_WORD_OF_TOPIC + table + word.getTopicId(), delete);
            }
            else{
                int maxWord = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_MAX_WORD_OF_TOPIC + table + word.getTopicId(), 0);
                id = maxWord + 1;
                CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_MAX_WORD_OF_TOPIC + table + word.getTopicId(), id);
            }
            cursor = db.rawQuery(ConstCommon.SQL_GET_TOPIC_NAME_BY_ID, new String[]{String.valueOf(word.getTopicId())});
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
            }
            String topicName = cursor.getString(0);
            db.execSQL(CommonUtil.format(ConstCommon.SQL_INSERT_WORD, table), new Object[]{id, word.getTopicId(), word.getWord(), word.getData(), word.getNote(), 1, word.getUrl()});
            db.execSQL(ConstCommon.SQL_INSERT_UPLOAD_PENDING, new Object[]{word, word.getData(), table, topicName, word.getUrl()});

            ///TODO: for get data
            File dir = new File(Environment.getExternalStorageDirectory(), "idic");
            if (!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir,"idic.txt");
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file, true);
            String data = "INSERT INTO %s (id, topic_id, word, content, note, is_edited, picture_url) values (%s, %s, '%s', \"%s\", '%s', %s, '%s');\n";
            byte[] b = CommonUtil.format(data, table, String.valueOf(id), String.valueOf(word.getTopicId()), word.getWord(), word.getData(), word.getNote(), "1", word.getUrl()).getBytes(StandardCharsets.UTF_8);
            stream.write(b);
            stream.flush();
            stream.close();

            db.setTransactionSuccessful();
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
    }

    /**
     * Update word
     * param[word]: the word update
     * */
    public void updateWord(Word word) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            cursor = db.rawQuery(ConstCommon.SQL_GET_TOPIC_NAME_BY_ID, new String[]{String.valueOf(word.getTopicId())});
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
            }
            String topicName = cursor.getString(0);
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            db.execSQL(CommonUtil.format(ConstCommon.SQL_UPDATE_WORD, table), new String[]{word.getData(), word.getNote(), word.getUrl(), word.getWord()});
            db.execSQL(ConstCommon.SQL_INSERT_UPLOAD_PENDING, new Object[]{word, word.getData(), table, topicName, word.getUrl()});
            db.setTransactionSuccessful();
        }
        finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
    }

    /**
     * Get is word is edited
     * param[word]: the word to get
     * */
    public boolean getIsEdit(String word) throws Exception{
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean result = false;
        try {
            db = this.getReadableDatabase();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            cursor = db.rawQuery(CommonUtil.format(ConstCommon.SQL_GET_IS_EDITED, table), new String[]{word});
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                throw new Exception(ConstCommon.EXCEPTION_NOT_EXIST);
            }
            result = cursor.getInt(0) != 0;
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get the list word history
     * return: the list history
     * */
    public List<String> getListHistory() throws Exception{
        List<String> result = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_GET_LIST_HISTORY, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                result.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get the list word marked
     * return: the list word
     * */
    public List<String> getListMarked() throws Exception{
        List<String> result = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_GET_LIST_MARK, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                result.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get the list word for game
     * param[limit]: the limit record
     * return: the list word with point
     * */
    public List<Pair<Word, Integer>> getListWordForGame(int limit) throws Exception{
        List<Pair<Word, Integer>> result = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = this.getReadableDatabase();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            cursor = db.rawQuery(CommonUtil.format(ConstCommon.SQL_GET_LIST_WORD_FOR_GAME, table), new String[]{String.valueOf(limit)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Word word = new Word();
                word.setWord(cursor.getString(0));
                word.setData(cursor.getString(1));
                word.setUrl(cursor.getString(2));
                result.add(new Pair<>(word, cursor.getInt(3)));
                cursor.moveToNext();
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Get the list dictionary
     * return: the list dictionary
     * */
    public List<Dictionary> getListDictionary() throws Exception{
        List<Dictionary> result = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = this.getReadableDatabase();
            cursor = db.rawQuery(ConstCommon.SQL_GET_LIST_DICTIONARY, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Dictionary dic = new Dictionary();
                dic.setTableName(cursor.getString(0));
                dic.setName(cursor.getString(1));
                dic.setIsOffline(cursor.getInt(2) != 0);
                result.add(dic);
                cursor.moveToNext();
            }
        }
        finally {
            if (db != null) {
                db.close();
            }
            if (cursor != null){
                cursor.close();
            }
        }
        return result;
    }

    /**
     * Delete word
     * param[word]: the word to delete
     * */
    public void deleteWord(Word word) throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
            db.execSQL(CommonUtil.format(ConstCommon.SQL_DELETE_WORD, table), new String[]{word.getWord()});
            String delete = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DELETED_WORD_OF_TOPIC + table + word.getTopicId(), ConstCommon.EMPTY);
            List<Integer> deleted = CommonUtil.convertStringToList(delete);
            deleted.add(word.getId());
            delete = CommonUtil.convertListToString(deleted);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DELETED_WORD_OF_TOPIC + table + word.getTopicId(), delete);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Delete all history and marked word
     * */
    public void deleteAllProgressing() throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.execSQL(ConstCommon.SQL_DELETE_ALL_HISTORY_MARK);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_HISTORY, 0);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Get next word of topic
     * param[topicId]: the topic id
     * return: the word
     * */
    public Word getNextWord(int topicId)throws Exception{
        String table = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, ConstCommon.EMPTY);
        if (!table.equals(ConstCommon.DEFAULT_DICTIONARY_TABLE)){
            topicId = 0;
        }
        int max = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_MAX_WORD_OF_TOPIC + table + topicId, 0);
        int current = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_WORD_OF_TOPIC + table + topicId, 0);
        String delete = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_DELETED_WORD_OF_TOPIC + table + topicId, ConstCommon.EMPTY);
        List<Integer> deleted = CommonUtil.convertStringToList(delete);
        if (current == max){
            current = 1;
        }
        else {
            current ++;
        }
        while (deleted.contains(current)){
            current ++;
        }
        CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_WORD_OF_TOPIC + table + topicId, current);
        return this.getWord(current, topicId);
    }

    /**
     * Get next word
     * return: the next word
     * */
    public Word getNextWord() throws Exception{
        int max = CommonUtil.getSharedPreferences(this.preferences, ConstCommon.SP_MAX_TOPIC_ID, 0);
        int topic = new Random().nextInt(max);
        return this.getNextWord(topic);
    }

    /**
     * Switch dictionary
     * param[table]: the table switch to
     * */
    public void switchDictionary(String table) throws Exception{
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.execSQL(ConstCommon.SQL_DELETE_ALL_HISTORY_MARK);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_DICTIONARY_NAME, table);
            CommonUtil.setSharedPreferences(this.preferences, ConstCommon.SP_CURRENT_HISTORY, 0);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
