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
 * Created by tranv on 4/29/2016.
 */
public class NoteAdapter extends ArrayAdapter<String> {

    public NoteAdapter(Context context, int resource, List<String> list){
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.text_item, null);
        }
        TextView textView = (TextView) v.findViewById(R.id.item_text);
        textView.setText(this.getItem(position));
        return v;
    }
}
