package icesi.i2t.leishmaniasisst.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class LargeListView  extends ListView {

    public LargeListView  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LargeListView  (Context context) {
        super(context);
    }

    public LargeListView  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
