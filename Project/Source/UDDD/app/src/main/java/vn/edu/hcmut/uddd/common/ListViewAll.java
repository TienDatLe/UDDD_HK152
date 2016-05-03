package vn.edu.hcmut.uddd.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by TRAN VAN HEN on 4/10/2016.
 */
public class ListViewAll extends ListView {

    public ListViewAll(Context context) {
        super(context);
    }

    public ListViewAll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewAll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        this.getLayoutParams().height = this.getMeasuredHeight();
    }

}
