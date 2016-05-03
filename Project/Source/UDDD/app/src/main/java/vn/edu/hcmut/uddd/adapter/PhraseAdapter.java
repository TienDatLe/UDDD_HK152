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
import vn.edu.hcmut.uddd.entity.Phrase;

/**
 * Created by DL_PD on 4/4/2016.
 */
public class PhraseAdapter extends ArrayAdapter<Phrase> {

    public PhraseAdapter(Context context, int resource, List<Phrase> list){
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.text_item, null);
        }
        Phrase phrase = this.getItem(position);
        TextView textView = (TextView) v.findViewById(R.id.item_text);
        textView.setText(CommonUtil.htmlBuilder(Constants.HTML_ADD_PHRASE_DETAIL, phrase.getPhrase(), phrase.getMean()));
        return v;
    }
}
