#include <jni.h>
#include <math.h>

#ifndef NULL
#define NULL   ((void *) 0)
#endif

JNIEXPORT jfloatArray JNICALL
Java_com_jmr_1android_jmr_SingleColorDescription_meanC(JNIEnv *env, jobject instance,
                                                       jintArray image_) {
    jint *image = (*env)->GetIntArrayElements(env, image_, NULL);

    (*env)->ReleaseIntArrayElements(env, image_, image, 0);
    int size = (*env)->GetArrayLength(env, image_);

    int width = 0;
    int height = 0;
    int x, y;

    int imagesize = 32 * 32;

    float mean[3] = {0.0f, 0.0f, 0.0f};

    jint *body = (*env)->GetIntArrayElements(env, image_, 0);

    int i = 0;
    for(i = 0; i < size; i++){
        mean[0] += (body[i] >> 16) & 0xFF;
        mean[1] += (body[i] >> 8) & 0xFF;
        mean[2] += body[i] & 0xFF;
    }

    mean[0] /= imagesize;
    mean[1] /= imagesize;
    mean[2] /= imagesize;


    jfloatArray mean2 = (*env)->NewFloatArray(env, 3);

    (*env)->SetFloatArrayRegion(env, mean2, 0, 3, mean);
    return mean2;
}

JNIEXPORT jint JNICALL
Java_com_jmr_1android_jmr_MPEG7ColorStructure_quantFuncC(JNIEnv *env, jobject instance, jdouble x) {

    int i = 0;
    double stepIn[] = {0.000000001, 0.037, 0.08, 0.195, 0.32, 1};
    int stepInLength = 6;
    int stepOut[] = {-1, 0, 25, 45, 80, 115};
    int y = 0;
    if (x <= 0) {
        y = 0;
    } else if (x >= 1) {
        y = 255;
    } else {
        y = (int) round(((x - 0.32) / (1 - 0.32)) * 140.0);
        for (i = 0; i < stepInLength; i++) {
            if (x < stepIn[i]) {
                y += stepOut[i];
                break;
            }
        }
        // Since there is a bug in Caliph & emir version the data
        // are between -66 and 255
        y = (int) (255.0 * ((double) y + 66) / (255.0 + 66.0));
    }
    return y;

}

JNIEXPORT jdouble JNICALL
Java_com_jmr_1android_jmr_SingleColorDescription_compare__IIIIII(JNIEnv *env, jobject instance,
                                                                 jint red1, jint green1, jint blue1,
                                                                 jint red2, jint green2,
                                                                 jint blue2) {

    double rDif = pow(red1 - red2, 2);
    double gDif = pow(green1 - green2, 2);
    double bDif = pow(blue1 - blue2, 2);
    return sqrt(rDif + gDif + bDif);

}