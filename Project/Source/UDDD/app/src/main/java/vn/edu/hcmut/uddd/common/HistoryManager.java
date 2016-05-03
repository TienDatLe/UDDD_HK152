package vn.edu.hcmut.uddd.common;

import java.util.Stack;

import vn.edu.hcmut.uddd.dao.DatabaseHelper;
import vn.edu.hcmut.uddd.entity.Word;

/**
 * Created by TRAN VAN HEN on 4/2/2016.
 */
public class HistoryManager {

    private static HistoryManager instance;
    private Stack<Word> previoid;
    private Stack<Word> next;
    private Word current;
    private int topicId;

    private HistoryManager(){
        this.previoid = new Stack<>();
        this.next = new Stack<>();
    }

    public static HistoryManager getInstance(){
        if (instance == null){
            instance = new HistoryManager();
        }
        return instance;
    }

    public boolean isEmpty(){
        return this.previoid.empty();
    }

    public Word getCurrent(){
        return this.current;
    }

    public Word getPeriod(){
        this.next.push(this.current);
        this.current = this.previoid.pop();
        return this.current;
    }

    public Word removeCurrent(){
        return new Word(this.current);
    }

    public void removeWord(Word word){
        if (word != null) {
            this.previoid.remove(word);
            this.next.remove(word);
        }
    }

    public void getNextWord(DatabaseHelper db) throws Exception {
        if (this.current != null) {
            this.previoid.push(this.current);
        }
        if (!this.next.empty()){
            this.current = this.next.pop();
        }
        else {
            if (this.topicId == 0) {
                this.current = db.getNextWord();
            }
            else {
                this.current = db.getNextWord(this.topicId);
            }
            if (db.getIsHistory(this.current)) {
                db.updateHistory(this.current);
            }
            else {
                db.insertHistoryWord(this.current);
            }
        }
    }

    public void getWord(DatabaseHelper db, String string) throws Exception{
        if (this.current != null) {
            this.previoid.push(this.current);
        }
        this.current = db.getWord(string);
        if (db.getIsHistory(this.current)){
            db.updateHistory(this.current);
        }
        else {
            db.insertHistoryWord(this.current);
        }
    }

    public void clearHistory(){
        this.previoid.clear();
        this.next.clear();
        this.current = null;
        this.topicId = 0;
    }

    public void setTopicId(int topicId){
        this.topicId = topicId;
    }
}
