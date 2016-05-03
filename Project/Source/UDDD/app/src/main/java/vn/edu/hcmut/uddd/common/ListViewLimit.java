package vn.edu.hcmut.uddd.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by TRAN VAN HEN on 4/10/2016.
 */
public class ListViewLimit extends ListView {

    private int maxHeight;

    public ListViewLimit(Context context) {
        super(context);
    }

    public ListViewLimit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewLimit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxHeight(int maxHeight){
        this.maxHeight = maxHeight;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        if (expandSpec > heightMeasureSpec){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else{
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
        this.getLayoutParams().height = this.getMeasuredHeight();
    }

}
