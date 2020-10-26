#include <jni.h>
#include <string>
#include <opencv2/core.hpp>

#include <opencv2/opencv.hpp>
#include <opencv2/objdetect.hpp>
#include <opencv2/dnn.hpp>
#include <android/log.h>

#define LOG_TAG "LOG FROM NATIVE: "
#define LOG_TAG_TYPE "%s"

#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_INFO, LOG_TAG,LOG_TAG_TYPE, __VA_ARGS__))

using namespace cv;
using namespace cv::dnn;
using namespace std;


class CascadeDetectorAdapter: public DetectionBasedTracker::IDetector
{
public:

    CascadeDetectorAdapter(cv::Ptr<cv::CascadeClassifier> detector):
            IDetector(),
            Detector(detector)
    {
        CV_Assert(detector);
    }

    void detect(const cv::Mat &Image, std::vector<cv::Rect> &objects) CV_OVERRIDE
    {
        Detector->detectMultiScale(Image, objects, scaleFactor, minNeighbours, 0, minObjSize, maxObjSize);
    }

    virtual ~CascadeDetectorAdapter() CV_OVERRIDE
    {}

private:
    CascadeDetectorAdapter();
    cv::Ptr<cv::CascadeClassifier> Detector;
};

struct DetectorCreation
{
    cv::Ptr<CascadeDetectorAdapter> mainDetector;
    cv::Ptr<CascadeDetectorAdapter> trackingDetector;

    cv::Ptr<CascadeDetectorAdapter> mainDetectorEyes;
    cv::Ptr<CascadeDetectorAdapter> trackingDetectorEyes;

    cv::Ptr<CascadeDetectorAdapter> mainDetectorSmile;
    cv::Ptr<CascadeDetectorAdapter> trackingDetectorSmile;

    cv::Ptr<DetectionBasedTracker> trackerFace;
    cv::Ptr<DetectionBasedTracker> trackerEyes;
    cv::Ptr<DetectionBasedTracker> trackerSmile;

    DetectorCreation(
            cv::Ptr<CascadeDetectorAdapter>& _mainDetector,
            cv::Ptr<CascadeDetectorAdapter>& _trackingDetector,

            cv::Ptr<CascadeDetectorAdapter>& _mainDetectorEyes,
            cv::Ptr<CascadeDetectorAdapter>& _trackingDetectorEyes,

            cv::Ptr<CascadeDetectorAdapter>& _mainDetectorSmile,
            cv::Ptr<CascadeDetectorAdapter>& _trackingDetectorSmile
    ):
            mainDetector(_mainDetector),
            trackingDetector(_trackingDetector),
            mainDetectorEyes(_mainDetectorEyes),
            trackingDetectorEyes(_trackingDetectorEyes),
            mainDetectorSmile(_mainDetectorSmile),
            trackingDetectorSmile(_trackingDetectorSmile)


    {
        CV_Assert(_mainDetector);
        CV_Assert(_trackingDetector);
        CV_Assert(_mainDetectorEyes);
        CV_Assert(_trackingDetectorEyes);

        CV_Assert(_mainDetectorSmile);
        CV_Assert(_trackingDetectorSmile);

        DetectionBasedTracker::Parameters DetectorParamsFace;
        DetectionBasedTracker::Parameters DetectorParamsEyes;
        DetectionBasedTracker::Parameters DetectorParamsSmile;
        trackerFace = makePtr<DetectionBasedTracker>(mainDetector, trackingDetector, DetectorParamsFace);
        trackerEyes = makePtr<DetectionBasedTracker>(mainDetectorEyes, trackingDetectorEyes, DetectorParamsEyes);
        trackerSmile = makePtr<DetectionBasedTracker>(mainDetectorSmile, trackingDetectorSmile, DetectorParamsSmile);

    }
};

string setValueToString(string value){

    string final=value+"+++++++";
    return final;


}


extern "C" JNIEXPORT jstring JNICALL
Java_mx_cannavita_SelfieActivity_stringFromJNI(
        JNIEnv* env,
        jclass /* this */) {
        string hello = CV_VERSION;
        string hola=setValueToString(hello);

    return env->NewStringUTF(hola.c_str());
}

extern "C" JNIEXPORT jlong JNICALL
Java_mx_cannavita_SelfieActivity_createNativeObject(
        JNIEnv* env,
        jclass ,jobjectArray paths) {
   // LOGD("CREATING OBJECT");
    jstring stringFace = (jstring) (env->GetObjectArrayElement(paths, 0));
    jstring stringEyes = (jstring) (env->GetObjectArrayElement(paths, 1));
    jstring stringSmile = (jstring) (env->GetObjectArrayElement(paths, 2));

    const char *face = env->GetStringUTFChars(stringFace, NULL);
    const char *eyes = env->GetStringUTFChars(stringEyes, NULL);
    const char *smile = env->GetStringUTFChars(stringSmile, NULL);


    string face_cascade_name(face);
    string eyes_cascade_name(eyes);
    string smile_cascade_name(smile);

    jlong result = 0;

    Ptr<CascadeDetectorAdapter>     mainDetectorFace = makePtr<CascadeDetectorAdapter>(makePtr<CascadeClassifier>(face_cascade_name));
    Ptr<CascadeDetectorAdapter>     trackingDetectorFace = makePtr<CascadeDetectorAdapter>(makePtr<CascadeClassifier>(face_cascade_name));

    Ptr<CascadeDetectorAdapter>     mainDetectorEyes = makePtr<CascadeDetectorAdapter>(makePtr<CascadeClassifier>(eyes_cascade_name));
    Ptr<CascadeDetectorAdapter>     trackingDetectorEyes = makePtr<CascadeDetectorAdapter>(makePtr<CascadeClassifier>(eyes_cascade_name));

    Ptr<CascadeDetectorAdapter>     mainDetectorSmile = makePtr<CascadeDetectorAdapter>(makePtr<CascadeClassifier>(smile_cascade_name));
    Ptr<CascadeDetectorAdapter>     trackingDetectorSmile = makePtr<CascadeDetectorAdapter>(makePtr<CascadeClassifier>(smile_cascade_name));

    try
    {
        result = (jlong)new DetectorCreation(
                mainDetectorFace,
                trackingDetectorFace,
                mainDetectorEyes,
                trackingDetectorEyes,
                mainDetectorSmile,
                trackingDetectorSmile);

    }
    catch(const cv::Exception& e)
    {
        return 0;

    }
    catch (...)
    {
         return 0;
    }
    //const char *rawString = env->GetStringUTFChars(stringFace, NULL);
   // LOGD(rawString);


//    __android_log_print(ANDROID_LOG_INFO, "MyTag", "The value is %s", rawString);
    return result;



}


extern "C" JNIEXPORT void JNICALL
Java_mx_cannavita_SelfieActivity_nativeStart(JNIEnv * jenv, jclass, jlong pointer)
{
    try
    {
        if(pointer != 0)
        {
            ((DetectorCreation*)pointer)->trackerFace->run();
           // ((DetectorCreation*)pointer)->trackerEyes->run();
           // ((DetectorCreation*)pointer)->trackerSmile->run();

         }
    }
    catch(const cv::Exception& e)
    {
    }
    catch (...)
    {

    }
}

extern "C" JNIEXPORT void JNICALL
Java_mx_cannavita_SelfieActivity_destroyNativeObject(JNIEnv * jenv, jclass, jlong pointer)
{
    try
    {
        if(pointer != 0)
        {

            ((DetectorCreation*)pointer)->trackerFace->stop();
   //         ((DetectorCreation*)pointer)->trackerEyes->stop();
   //         ((DetectorCreation*)pointer)->trackerSmile->stop();
            LOGD("DESTRY OBJECT");
            delete (DetectorCreation*)pointer;
        }
    }
    catch(const cv::Exception& e)
    {
    }
    catch (...)
    {

    }
}


extern "C" JNIEXPORT jlong JNICALL
Java_mx_cannavita_SelfieActivity_validateSelfie(JNIEnv * jenv, jclass, jlong pointer,jlong imageGray) {
    vector<Rect> faces;

    try
    {
        if(pointer != 0)
        {
            ((DetectorCreation*)pointer)->trackerFace->process(*((Mat*)imageGray));
            ((DetectorCreation*)pointer)->trackerFace->getObjects(faces);

            if(faces.size()>0){
                return faces.size();

            }else{
                return -1;
            }

        }
    }
    catch(const cv::Exception& e)
    {
        return 0;
    }
    catch (...)
    {
        return 0;
    }
    return 0;
}
