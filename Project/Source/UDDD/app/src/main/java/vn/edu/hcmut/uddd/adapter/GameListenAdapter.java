package vn.edu.hcmut.uddd.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.entity.Word;

/**
 * Created by 51201_000 on 08/04/2016.
 */
public class GameListenAdapter extends ArrayAdapter<Pair<Word, Integer>> {

    private HashMap<Integer, String> value;

    public GameListenAdapter(Context context, int resource, List<Pair<Word, Integer>> list){
        super(context, resource, list);
        this.value = new LinkedHashMap<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.game_listen_item, null);
        }
        EditText editText = (EditText) v.findViewById(R.id.game_listen_editText_item);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                GameListenAdapter.this.value.put(position, s.toString().toLowerCase().trim());
            }
        });
        return v;
    }

    public String getTextAt(int position){
        if (this.value.containsKey(position)){
            return this.value.get(position);
        }
        else{
            return ConstCommon.EMPTY;
        }
    }
}
