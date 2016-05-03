package vn.edu.hcmut.uddd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.entity.Meaning;

/**
 * Created by DL_PD on 4/4/2016.
 */
public class MeanAdapter extends ArrayAdapter<Meaning> {

    public MeanAdapter(Context context, int resource, List<Meaning> list){
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.text_item, null);
        }
        Meaning mean = this.getItem(position);
        TextView textView = (TextView) v.findViewById(R.id.item_text);
        if (mean.getExample().length() == 0) {
            textView.setText(CommonUtil.htmlBuilder(Constants.HTML_ADD_MEAN_NO_EX, mean.getMean(), mean.getPartOfSpeechString()));
        }
        else {
            textView.setText(CommonUtil.htmlBuilder(Constants.HTML_ADD_MEAN_FULL, mean.getMean(), mean.getPartOfSpeechString(), mean.getExample(), mean.getExampleMean()));
        }
        return v;
    }
}
