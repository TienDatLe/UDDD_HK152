package vn.edu.hcmut.uddd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.common.CommonUtil;

/**
 * Created by DL_PD on 4/4/2016.
 */
public class PartOfSpeechAdapter extends ArrayAdapter<String> {

    public PartOfSpeechAdapter(Context context, int resource){
        super(context, resource, CommonUtil.getList());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = LayoutInflater.from(this.getContext()).inflate(R.layout.spinner_display, null);
        TextView textView = (TextView) v.findViewById(R.id.spinner_item);
        textView.setText(this.getItem(position));
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.spinner_item, null);
        }
        TextView textView = (TextView) v.findViewById(R.id.spinner_item);
        textView.setText(this.getItem(position));
        return v;
    }
}
