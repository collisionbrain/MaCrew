package mx.cannavita.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import mx.cannavita.R;

public class DataUtils {

    public static String bitmapToBase64(Bitmap bitmap) {

        String base64 = "";
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            base64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64;
    }

    public static Bitmap base64ToBitmap(String bs64) {

        Bitmap bitmap = null;
        try {
            byte[] decodedString = Base64.decode(bs64, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Escuadron/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "MyCode.jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String objectToString(Serializable obj) {

        if (obj == null)
            return "";

        try {
            ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
            ObjectOutputStream objStream;
            objStream = new ObjectOutputStream(serialObj);
            objStream.writeObject(obj);
            objStream.close();
            return asHexStr(serialObj.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    public static Serializable stringToObject(String str) {

        if (str == null || str.length() == 0)
            return null;
        try {
            ByteArrayInputStream serialObj = new ByteArrayInputStream(asBytes(str));
            ObjectInputStream objStream;
            objStream = new ObjectInputStream(serialObj);
            return (Serializable) objStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static String asHexStr(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if ((buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString(buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static byte[] asBytes(String s) {
        String s2;
        byte[] b = new byte[s.length() / 2];
        int i;
        for (i = 0; i < s.length() / 2; i++) {
            s2 = s.substring(i * 2, i * 2 + 2);
            b[i] = (byte) (Integer.parseInt(s2, 16) & 0xff);
        }
        return b;
    }

    public static String[] getPaths(Context context) {
        String[] paths=new String[3];
        try {
            // load cascade file from application resources
            InputStream isFace = context.getResources().openRawResource(R.raw.face);
            File cascadeDirFace = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFileFace = new File(cascadeDirFace, "face.xml");

            FileOutputStream osFace = new FileOutputStream(mCascadeFileFace);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = isFace.read(buffer)) != -1) {
                osFace.write(buffer, 0, bytesRead);
            }
            isFace.close();
            osFace.close();

            InputStream isEyes = context.getResources().openRawResource(R.raw.eyes);
            File cascadeDirEyes = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFileEyes = new File(cascadeDirEyes, "eyes.xml");

            FileOutputStream osEyes = new FileOutputStream(mCascadeFileEyes);
            buffer = new byte[4096];
            while ((bytesRead = isEyes.read(buffer)) != -1) {
                osEyes.write(buffer, 0, bytesRead);
            }
            isEyes.close();
            osEyes.close();


            InputStream isSmile = context.getResources().openRawResource(R.raw.smile);
            File cascadeDirSmile = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFileSmile = new File(cascadeDirSmile, "smile.xml");

            FileOutputStream osSmile = new FileOutputStream(mCascadeFileSmile);
            buffer = new byte[4096];
            while ((bytesRead = isSmile.read(buffer)) != -1) {
                osSmile.write(buffer, 0, bytesRead);
            }
            isSmile.close();
            osSmile.close();

            paths[0]= mCascadeFileFace.getAbsolutePath();
            paths[1]= mCascadeFileEyes.getAbsolutePath();
            paths[2]= mCascadeFileSmile.getAbsolutePath();


            // mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());

        } catch (Exception e) {
            Log.e("ERROR ",e.getMessage());
        }

        return paths;
    }

    public static Bitmap matToBitmap(Mat tmp){
        Bitmap bmp = null;


        try {
            //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
             bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);
            return bmp;
        }
        catch (CvException e){Log.d("Exception",e.getMessage());}
        return null;
    }
     public static Mat rotateMat(Mat mat){

        try {

            Mat dst=new Mat();
            Point center = new Point(mat.cols()/2,mat.rows()/2);
            double angle = -90;
            double scale = 1.0;

            Mat mapMatrix = Imgproc.getRotationMatrix2D(center, angle, scale);
            Imgproc.warpAffine(mat, dst, mapMatrix, dst.size());
            return dst;



        }
        catch (CvException e){Log.d("Exception",e.getMessage());}
        return null;
    }

}
