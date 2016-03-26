package vn.edu.hcmut.uddd.entity;

import vn.edu.hcmut.idic.common.CommonUtil;
import vn.edu.hcmut.idic.common.ConstCommon;
import vn.edu.hcmut.idic.common.PartOfSpeech;

/**
 * Created by TRAN VAN HEN on 3/5/2016.
 */
public class Meaning extends Data {
    private PartOfSpeech partOfSpeech;
    private String mean;
    private String example;
    private String exampleMean;

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExampleMean() {
        return exampleMean;
    }

    public void setExampleMean(String exampleMean) {
        this.exampleMean = exampleMean;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getPartOfSpeechKey(){
        switch (partOfSpeech){
            case VERB:
                return ConstCommon.VERB_MEAN_LABEL;
            case ADJECTIVE:
                return ConstCommon.ADJECTIVE_MEAN_LABEL;
            case ADVERB:
                return ConstCommon.ADVERB_MEAN_LABEL;
            case CONJUNCTION:
                return ConstCommon.CONJUNCTION_MEAN_LABEL;
            case INTERJECTION:
                return ConstCommon.INTERJECTION_MEAN_LABEL;
            case NOUNS:
                return ConstCommon.NOUNS_MEAN_LABEL;
            case PREPOSITION:
                return ConstCommon.PREPOSITION_MEAN_LABEL;
            case PRONOUNS:
                return ConstCommon.PRONOUNS_MEAN_LABEL;
        }
        return ConstCommon.EMPTY;
    }

    public String getData(){
        return CommonUtil.joinByColon(this.mean, this.example, this.exampleMean);
    }

    public void setData(String partOfSpeechLabel, String data){
        if (data != null) {
            String[] datas = CommonUtil.slipByColon(data);
            if (datas.length == 3) {
                this.mean = datas[0];
                this.example = datas[1];
                this.exampleMean = datas[2];
            }
        }
        switch (partOfSpeechLabel){
            case ConstCommon.NOUNS_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.NOUNS;
                break;
            case ConstCommon.PRONOUNS_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.PRONOUNS;
                break;
            case ConstCommon.ADJECTIVE_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.ADJECTIVE;
                break;
            case ConstCommon.VERB_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.VERB;
                break;
            case ConstCommon.ADVERB_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.ADVERB;
                break;
            case ConstCommon.PREPOSITION_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.PREPOSITION;
                break;
            case ConstCommon.CONJUNCTION_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.CONJUNCTION;
                break;
            case ConstCommon.INTERJECTION_MEAN_LABEL:
                this.partOfSpeech = PartOfSpeech.INTERJECTION;
                break;
        }
    }
}
