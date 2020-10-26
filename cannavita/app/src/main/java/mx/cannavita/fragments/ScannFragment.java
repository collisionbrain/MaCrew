package mx.cannavita.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import mx.cannavita.ClubActivity;
import mx.cannavita.R;


public class ScannFragment extends Fragment implements ZXingScannerView.ResultHandler{
     private ZXingScannerView mScannerView;
     private  Context context;
     private OnFragmentNextListener callback;

     public interface  OnFragmentNextListener{
         public void onNextFragment(String code);

    };
    public ScannFragment() {
        // Required empty public constructor
    }


    public static ScannFragment newInstance() {
        ScannFragment fragment = new ScannFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_scann, container, false);
        FrameLayout contentFrame = view.findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(getActivity().getApplicationContext())
        {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };

        contentFrame.addView(mScannerView);

        return view;
    }

     @Override
    public void onAttach(Context context) {
        super.onAttach(context);
         callback=(OnFragmentNextListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mScannerView.stopCamera();

    }

    @Override
    public void onResume(){
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }



    @Override
    public void handleResult(Result rawResult) {
        String text_code=rawResult.getText();
        callback.onNextFragment(text_code);



    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }

}
