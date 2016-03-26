package vn.edu.hcmut.uddd.entity;

import vn.edu.hcmut.idic.common.CommonUtil;

/**
 * Created by TRAN VAN HEN on 3/6/2016.
 */
public class Phrase extends Data{
    private String phrase;
    private String mean;

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getData() {
        return CommonUtil.joinByColon(phrase, mean);
    }

    public void setData(String data) {
        if (data != null) {
            String[] datas = CommonUtil.slipByColon(data);
            if (datas.length == 2) {
                this.phrase = datas[0];
                this.mean = datas[1];
            }
        }
    }
}
