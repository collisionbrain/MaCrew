package mx.cannavita.utils;

public class SelfieNativeCore {
    long pointerDetector=0;
    long faces=0;


    public void createCore(String []paths){
        pointerDetector=createNativeObject(paths);

    }
    public void destroyCore(){
        destroyNativeObject(pointerDetector);

    }
    public boolean isSelfie(long mat){
        faces=validateSelfie(pointerDetector,mat);

        if(faces>0) {

            return true;
        }else{

            return false;
        }

    }


    private static native long createNativeObject(String[] paths);
    private static native void destroyNativeObject(long pointer);
    private static native long validateSelfie(long thiz, long inputImage);




}
