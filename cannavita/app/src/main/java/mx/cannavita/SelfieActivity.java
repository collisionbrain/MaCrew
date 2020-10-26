package mx.cannavita;

  import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
  import android.os.Handler;
  import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
  import org.opencv.imgproc.Imgproc;

  import mx.cannavita.utils.DataUtils;

public class SelfieActivity extends Activity implements  CvCameraViewListener2 ,FloatingActionButton.OnClickListener{
    private static final String  TAG              = "SelfieActivity";

     private Mat mRgba,mGray,mRgbaRotated,mGrayRotated;

    Bitmap selfie;
    boolean isSelfie=false;
    private CameraBridgeViewBase mOpenCvCameraView;
    private TextView textViewSelfie;
    private FloatingActionButton floatingActionButtonCamera;
    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                 } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    private static int CAMERA_REQUEST_CODE=101;
    static {
        System.loadLibrary("native-lib");
    }

    public SelfieActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_selfie);
        mOpenCvCameraView =  findViewById(R.id.camera_preview);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
        mOpenCvCameraView.setCvCameraViewListener(this);
        textViewSelfie=findViewById(R.id.txt_selfie);
        floatingActionButtonCamera=findViewById(R.id.btn_camera_selfie);
        floatingActionButtonCamera.setOnClickListener(this);
        String text=stringFromJNI();
        String []paths= DataUtils.getPaths(this);
        pointerDetector=createNativeObject(paths);

        Log.i("OPN CV VERSION :",text);
        Log.i("OPN CV POINTER :",": "+pointerDetector);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(60);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textViewSelfie.startAnimation(anim);
        nativeStart(pointerDetector);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
           //destroyNativeObject(pointerDetector);
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaRotated=new Mat(height, width, CvType.CV_8UC4);

    }

    public void onCameraViewStopped() {
        mRgba.release();
    }


    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        Core.flip(mGray, mGray, 1);
        mGrayRotated=DataUtils.rotateMat(mGray);
       // mRgbaRotated=DataUtils.rotateMat(mRgba);

    long faces = validateSelfie(pointerDetector,mGrayRotated.getNativeObjAddr());
    Log.i("NUMBER O FACES: ",""+faces);

    if(faces>0){

        updateViewForSelfie();
    }


          return mRgba;
    }

    private void  updateViewForSelfie(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                textViewSelfie.clearAnimation();
                textViewSelfie.setText("");
                textViewSelfie.setVisibility(View.GONE);
                floatingActionButtonCamera.show();
            }
        });

    }


    long pointerDetector=0;
    public static native String stringFromJNI();
    private static native long createNativeObject(String[] paths);
    private static native void nativeStart(long pointer);
    private static native void destroyNativeObject(long pointer);
    private static native long validateSelfie(long thiz, long inputImage);


    @Override
    public void onClick(View v) {

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                isSelfie=true;
                selfie=DataUtils.matToBitmap(mGrayRotated);

                String b64=DataUtils.bitmapToBase64(selfie);
                Intent intent=new Intent();
                intent.setData(Uri.parse(b64));

                setResult(RESULT_OK, intent);

                finish();

            }
        }, 2000);

    }

   }
