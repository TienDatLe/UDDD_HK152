package vn.edu.hcmut.uddd.entity;

import vn.edu.hcmut.idic.common.ConstCommon;
import vn.edu.hcmut.idic.common.PartOfSpeech;

/**
 * Created by TRAN VAN HEN on 3/5/2016.
 */
public class WordFamily extends Data{
    private PartOfSpeech partOfSpeech;
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPartOfSpeechKey(){
        switch (partOfSpeech){
            case VERB:
                return ConstCommon.VERB_FORMAT_LABEL;
            case ADJECTIVE:
                return ConstCommon.ADJECTIVE_FORMAT_LABEL;
            case ADVERB:
                return ConstCommon.ADVERB_FORMAT_LABEL;
            case CONJUNCTION:
                return ConstCommon.CONJUNCTION_FORMAT_LABEL;
            case INTERJECTION:
                return ConstCommon.INTERJECTION_FORMAT_LABEL;
            case NOUNS:
                return ConstCommon.NOUNS_FORMAT_LABEL;
            case PREPOSITION:
                return ConstCommon.PREPOSITION_FORMAT_LABEL;
            case PRONOUNS:
                return ConstCommon.PRONOUNS_FORMAT_LABEL;
        }
        return ConstCommon.EMPTY;
    }

    public String getData(){
        return this.word;
    }

    public void setData(String partOfSpeechLabel, String data){
        this.word = data;
        switch (partOfSpeechLabel){
            case ConstCommon.NOUNS_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.NOUNS;
                break;
            case ConstCommon.PRONOUNS_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.PRONOUNS;
                break;
            case ConstCommon.ADJECTIVE_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.ADJECTIVE;
                break;
            case ConstCommon.VERB_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.VERB;
                break;
            case ConstCommon.ADVERB_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.ADVERB;
                break;
            case ConstCommon.PREPOSITION_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.PREPOSITION;
                break;
            case ConstCommon.CONJUNCTION_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.CONJUNCTION;
                break;
            case ConstCommon.INTERJECTION_FORMAT_LABEL:
                this.partOfSpeech = PartOfSpeech.INTERJECTION;
                break;
        }
    }
}
