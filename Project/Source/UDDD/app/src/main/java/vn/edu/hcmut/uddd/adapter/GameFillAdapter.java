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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.ConstCommon;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.common.FileCache;
import vn.edu.hcmut.uddd.entity.Word;

/**
 * Created by 51201_000 on 06/04/2016.
 */
public class GameFillAdapter extends ArrayAdapter<Pair<Word, Integer>> {

    private HashMap<Integer, String> value;

    public GameFillAdapter(Context context, int resource, List<Pair<Word, Integer>> list){
        super(context, resource, list);
        this.value = new LinkedHashMap<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.game_fill_item, null);
        }
        Pair<Word, Integer> word = this.getItem(position);
        ImageView imageView = (ImageView) v.findViewById(R.id.game_fill_image_item);
        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.game_progressBar_picture);
        new FileCache(this.getContext(), imageView, progressBar, null, false).execute(word.first.getUrl());
        TextView textView = (TextView) v.findViewById(R.id.game_fill_textView_item);
        EditText editText = (EditText) v.findViewById(R.id.game_fill_editText_item);
        textView.setText(CommonUtil.htmlBuilder(Constants.HTML_GAME_FILL, word.first.getMeanList().get(0).getMean(), word.first.getPartOfSpeechString()));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                GameFillAdapter.this.value.put(position, s.toString().toLowerCase().trim());
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
