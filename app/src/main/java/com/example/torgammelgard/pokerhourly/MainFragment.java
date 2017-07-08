package com.example.torgammelgard.pokerhourly;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

class MyImageButton extends  ImageButton {
    public MyImageButton(Context context) {
        super(context);
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
/*        String addButtonText = getResources().getString(R.string.addButtonText);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(TypedValue.COMPLEX_UNIT_SP * 24);
        int x = canvas.getWidth() / 2;
        int y = canvas.getHeight() / 2;

        canvas.drawText(addButtonText, x, y, paint);*/
    }
}
public class MainFragment extends Fragment {

    private static final String mLOGMESSAGE = "LOGMESSAGE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }
}
