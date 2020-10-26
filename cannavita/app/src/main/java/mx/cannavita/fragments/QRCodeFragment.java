package mx.cannavita.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import mx.cannavita.R;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class QRCodeFragment extends Fragment {

    private Bitmap bitmapCode;
    private int smallerDimension;
    private static int width ;
    private static int height ;


    public QRCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivacyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRCodeFragment newInstance(int param1, int param2) {
        QRCodeFragment fragment = new QRCodeFragment();

        width=param1;
        height=param2;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_codeqr, container, false);

        smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        ImageView qrImage=v.findViewById(R.id.imgCode);

        QRGEncoder qrgEncoder = new QRGEncoder("USER _GUID", null,
                QRGContents.Type.TEXT, smallerDimension);
         try {
            // Getting QR-Code as Bitmap
            bitmapCode = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            qrImage.setImageBitmap(bitmapCode);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }





}
