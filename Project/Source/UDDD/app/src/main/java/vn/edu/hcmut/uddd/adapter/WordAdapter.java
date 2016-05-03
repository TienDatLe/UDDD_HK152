package vn.edu.hcmut.uddd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.edu.hcmut.uddd.R;

/**
 * Created by DL_PD on 4/4/2016.
 */
public class WordAdapter extends ArrayAdapter<String> {

    public WordAdapter(Context context, int resource, List<String> list){
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.home_word_item, null);
        }
        TextView homeTextViewIsMark= (TextView) v.findViewById(R.id.home_word_text);
        homeTextViewIsMark.setText(this.getItem(position));
        return v;
    }
}
