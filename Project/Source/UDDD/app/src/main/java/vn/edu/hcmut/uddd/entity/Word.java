package vn.edu.hcmut.uddd.entity;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.PartOfSpeech;

/**
 * Created by TRAN VAN HEN on 3/5/2016.
 */
public class Word extends Data implements Cloneable {
    private int id;
    private int topicId;
    private String word;
    private PartOfSpeech partOfSpeech;
    private String pronunciation;
    private ArrayList<String> synonyms;
    private ArrayList<String> antonyms;
    private ArrayList<Meaning> meanList;
    private ArrayList<WordFamily> wordFamilyList;
    private ArrayList<Phrase> phraseList;
    private ArrayList<String> noteList;
    private String url;

    public Word(){
        this.synonyms = new ArrayList<>();
        this.antonyms = new ArrayList<>();
        this.meanList = new ArrayList<>();
        this.wordFamilyList = new ArrayList<>();
        this.phraseList = new ArrayList<>();
        this.noteList = new ArrayList<>();
    }

    public Word(Word obj){
        this.id = obj.id;
        this.topicId = obj.topicId;
        this.word = obj.word;
        this.partOfSpeech = obj.partOfSpeech;
        this.pronunciation = obj.pronunciation;
        this.url = obj.url;
        this.synonyms = (ArrayList<String>) obj.synonyms.clone();
        this.antonyms = (ArrayList<String>) obj.antonyms.clone();
        this.meanList = (ArrayList<Meaning>) obj.meanList.clone();
        this.wordFamilyList = (ArrayList<WordFamily>) obj.wordFamilyList.clone();
        this.phraseList = (ArrayList<Phrase>) obj.phraseList.clone();
        this.noteList = (ArrayList<String>) obj.noteList.clone();
    }

    public ArrayList<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(ArrayList<String> antonyms) {
        this.antonyms = antonyms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Meaning> getMeanList() {
        return meanList;
    }

    public void setMeanList(ArrayList<Meaning> meanList) {
        this.meanList = meanList;
    }

    public ArrayList<String> getNoteList() {
        return noteList;
    }

    public void setNoteList(ArrayList<String> noteList) {
        this.noteList = noteList;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public ArrayList<Phrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(ArrayList<Phrase> phraseList) {
        this.phraseList = phraseList;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public ArrayList<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<String> synonyms) {
        this.synonyms = synonyms;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<WordFamily> getWordFamilyList() {
        return wordFamilyList;
    }

    public void setWordFamilyList(ArrayList<WordFamily> wordFamilyList) {
        this.wordFamilyList = wordFamilyList;
    }

    public String getPartOfSpeechString(){
        switch (partOfSpeech){
            case VERB:
                return ConstCommon.VERB_STRING;
            case ADJECTIVE:
                return ConstCommon.ADJECTIVE_STRING;
            case ADVERB:
                return ConstCommon.ADVERB_STRING;
            case CONJUNCTION:
                return ConstCommon.CONJUNCTION_STRING;
            case INTERJECTION:
                return ConstCommon.INTERJECTION_STRING;
            case NOUNS:
                return ConstCommon.NOUNS_STRING;
            case PREPOSITION:
                return ConstCommon.PREPOSITION_STRING;
            case PRONOUNS:
                return ConstCommon.PRONOUNS_STRING;
        }
        return ConstCommon.EMPTY;
    }

    public String getPartOfSpeechKey(){
        switch (partOfSpeech){
            case VERB:
                return ConstCommon.VERB_PART_LABEL;
            case ADJECTIVE:
                return ConstCommon.ADJECTIVE_PART_LABEL;
            case ADVERB:
                return ConstCommon.ADVERB_PART_LABEL;
            case CONJUNCTION:
                return ConstCommon.CONJUNCTION_PART_LABEL;
            case INTERJECTION:
                return ConstCommon.INTERJECTION_PART_LABEL;
            case NOUNS:
                return ConstCommon.NOUNS_PART_LABEL;
            case PREPOSITION:
                return ConstCommon.PREPOSITION_PART_LABEL;
            case PRONOUNS:
                return ConstCommon.PRONOUNS_PART_LABEL;
        }
        return ConstCommon.EMPTY;
    }

    public String getData(){
        List<String> list = new ArrayList<>();
        StringBuilder header = new StringBuilder();
        list.add(getPartOfSpeechKey());
        list.add(pronunciation);
        list.add(CommonUtil.joinByColon((synonyms.toArray(new String[synonyms.size()]))));
        list.add(CommonUtil.joinByColon((antonyms.toArray(new String[antonyms.size()]))));
        for (WordFamily wordFamily : wordFamilyList){
            header.append(wordFamily.getPartOfSpeechKey());
            list.add(wordFamily.getData());
        }
        for (Meaning mean : meanList){
            header.append(mean.getPartOfSpeechKey());
            list.add(mean.getData());
        }
        for(Phrase phrase : phraseList){
            list.add(phrase.getData());
        }
        list.add(0, header.toString());
        return CommonUtil.joinBySemicolon(list.toArray(new String[list.size()]));
    }

    public String getNote(){
        return CommonUtil.joinBySemicolon(noteList.toArray(new String[noteList.size()]));
    }

    public void setData(String data) {
        if (data != null) {
            String[] datas = CommonUtil.splitBySemicolon(data);
            if ((datas.length > 5) && (datas.length > 4 + datas[0].length())) {
                switch (datas[1]) {
                    case ConstCommon.NOUNS_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.NOUNS;
                        break;
                    case ConstCommon.PRONOUNS_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.PRONOUNS;
                        break;
                    case ConstCommon.ADJECTIVE_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.ADJECTIVE;
                        break;
                    case ConstCommon.VERB_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.VERB;
                        break;
                    case ConstCommon.ADVERB_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.ADVERB;
                        break;
                    case ConstCommon.PREPOSITION_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.PREPOSITION;
                        break;
                    case ConstCommon.CONJUNCTION_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.CONJUNCTION;
                        break;
                    case ConstCommon.INTERJECTION_PART_LABEL:
                        this.partOfSpeech = PartOfSpeech.INTERJECTION;
                        break;
                }
                this.pronunciation = datas[2];
                this.synonyms = CommonUtil.convertFromArrayToList(CommonUtil.slipByColon(datas[3]));
                this.antonyms = CommonUtil.convertFromArrayToList(CommonUtil.slipByColon(datas[4]));
                this.meanList = new ArrayList<>();
                this.wordFamilyList = new ArrayList<>();
                this.phraseList = new ArrayList<>();
                int i;
                for (i = 0; i < datas[0].length(); i++) {
                    char c = datas[0].charAt(i);
                    if (Character.isUpperCase(c)) {
                        Meaning mean = new Meaning();
                        mean.setData(String.valueOf(c), datas[i + 5]);
                        meanList.add(mean);
                    }
                    else {
                        WordFamily wordFamily = new WordFamily();
                        wordFamily.setData(String.valueOf(c), datas[i + 5]);
                        wordFamilyList.add(wordFamily);
                    }
                }
                i+=5;
                while (i < datas.length) {
                    Phrase phrase = new Phrase();
                    phrase.setData(datas[i]);
                    phraseList.add(phrase);
                    i++;
                }
            }
        }
    }

    public void setNote(String data){
        if (data != null){
            this.noteList = CommonUtil.convertFromArrayToList(CommonUtil.splitBySemicolon(data));
        }
    }

    public void addNote(String newNote){
        this.noteList.add(newNote);
    }

    public void deleteNote(int index){
        if (this.noteList.size() > index){
            this.noteList.remove(index);
        }
    }

    public void updateNote(int index, String newNote){
        if (this.noteList.size() > index){
            this.noteList.remove(index);
            this.noteList.add(index, newNote);
        }
    }

    public void addMean(Meaning mean){
        this.meanList.add(mean);
    }

    public void deleteMean(int index){
        if (this.meanList.size() > index){
            this.meanList.remove(index);
        }
    }

    public void updateMean(int index, Meaning mean){
        if (this.meanList.size() > index){
            this.meanList.remove(index);
            this.meanList.add(index, mean);
        }
    }

    public void addWordFamily(WordFamily word){
        this.wordFamilyList.add(word);
    }

    public void deleteWordFamily(int index){
        if (this.wordFamilyList.size() > index){
            this.wordFamilyList.remove(index);
        }
    }

    public void updateWordFamily(int index, WordFamily word){
        if (this.wordFamilyList.size() > index){
            this.wordFamilyList.remove(index);
            this.wordFamilyList.add(index, word);
        }
    }

    public void addPhrase(Phrase phrase){
        this.phraseList.add(phrase);
    }

    public void deletePhrase(int index){
        if (this.phraseList.size() > index){
            this.phraseList.remove(index);
        }
    }

    public void updatePhrase(int index, Phrase phrase){
        if (this.phraseList.size() > index){
            this.phraseList.remove(index);
            this.phraseList.add(index, phrase);
        }
    }
}
