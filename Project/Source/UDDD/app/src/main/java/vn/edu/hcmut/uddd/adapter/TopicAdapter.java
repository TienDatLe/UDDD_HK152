package vn.edu.hcmut.uddd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.edu.hcmut.uddd.R;
import vn.edu.hcmut.uddd.common.CommonUtil;
import vn.edu.hcmut.uddd.common.Constants;
import vn.edu.hcmut.uddd.entity.Topic;

/**
 * Created by DL_PD on 4/4/2016.
 */
public class TopicAdapter extends ArrayAdapter<Topic> {

    public TopicAdapter(Context context, int resource, List<Topic> list){
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.getContext()).inflate(R.layout.home_topic_item, null);
        }
        Topic topic = this.getItem(position);
        ImageView image = (ImageView) v.findViewById(R.id.home_topic_image);
        TextView text = (TextView) v.findViewById(R.id.home_topic_text);
        image.setImageResource(CommonUtil.getDrawableByName(this.getContext(), Constants.TOPIC_DRAWABLE + String.valueOf(topic.getId())));
        text.setText(CommonUtil.htmlBuilder(Constants.HTML_TOPIC_ITEM, topic.getName(), topic.getMean()));
        return v;
    }

    @Override
    public long getItemId(int position){
        return this.getItem(position).getId();
    }
}
