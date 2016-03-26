package vn.edu.hcmut.uddd.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import vn.edu.hcmut.idic.common.CommonUtil;
import vn.edu.hcmut.idic.common.ConstCommon;
import vn.edu.hcmut.idic.common.PartOfSpeech;

/**
 * Created by TRAN VAN HEN on 3/5/2016.
 */
public class Word extends Data {
    private int id;
    private int topicId;
    private String word;
    private PartOfSpeech partOfSpeech;
    private String pronunciation;
    private List<String> synonyms;
    private List<String> antonyms;
    private List<Meaning> meanList;
    private List<WordFamily> wordFamilyList;
    private List<Phrase> phraseList;
    private List<String> noteList;
    private String url;

    public List<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Meaning> getMeanList() {
        return meanList;
    }

    public void setMeanList(List<Meaning> meanList) {
        this.meanList = meanList;
    }

    public List<String> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<String> noteList) {
        this.noteList = noteList;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<Phrase> getPhraseList() {
        return phraseList;
    }

    public void setPhraseList(List<Phrase> phraseList) {
        this.phraseList = phraseList;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
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

    public void setWordFamilyList(List<WordFamily> wordFamilyList) {
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
                return ConstCommon.VERB_TYPE_LABEL;
            case ADJECTIVE:
                return ConstCommon.ADJECTIVE_TYPE_LABEL;
            case ADVERB:
                return ConstCommon.ADVERB_TYPE_LABEL;
            case CONJUNCTION:
                return ConstCommon.CONJUNCTION_TYPE_LABEL;
            case INTERJECTION:
                return ConstCommon.INTERJECTION_TYPE_LABEL;
            case NOUNS:
                return ConstCommon.NOUNS_TYPE_LABEL;
            case PREPOSITION:
                return ConstCommon.PREPOSITION_TYPE_LABEL;
            case PRONOUNS:
                return ConstCommon.PRONOUNS_TYPE_LABEL;
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
            if ((datas.length > 5) && (datas.length > 5 + datas[0].length())) {
                switch (datas[1]) {
                    case ConstCommon.NOUNS_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.NOUNS;
                        break;
                    case ConstCommon.PRONOUNS_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.PRONOUNS;
                        break;
                    case ConstCommon.ADJECTIVE_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.ADJECTIVE;
                        break;
                    case ConstCommon.VERB_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.VERB;
                        break;
                    case ConstCommon.ADVERB_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.ADVERB;
                        break;
                    case ConstCommon.PREPOSITION_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.PREPOSITION;
                        break;
                    case ConstCommon.CONJUNCTION_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.CONJUNCTION;
                        break;
                    case ConstCommon.INTERJECTION_TYPE_LABEL:
                        this.partOfSpeech = PartOfSpeech.INTERJECTION;
                        break;
                }
                this.pronunciation = datas[2];
                this.synonyms = Arrays.asList(CommonUtil.slipByColon(datas[3]));
                this.antonyms = Arrays.asList(CommonUtil.slipByColon(datas[4]));
                this.meanList = new ArrayList<>();
                this.wordFamilyList = new ArrayList<>();
                this.phraseList = new ArrayList<>();
                int i = 0;
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
            this.noteList = Arrays.asList(CommonUtil.splitBySemicolon(data));
        }
    }
}
