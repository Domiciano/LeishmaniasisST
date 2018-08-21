package icesi.i2t.leishmaniasisst.calendario;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Domiciano on 18/05/2016.
 */
public class CustomGrid extends GridView {

    public CustomGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGrid(Context context) {
        super(context);
    }

    public CustomGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
