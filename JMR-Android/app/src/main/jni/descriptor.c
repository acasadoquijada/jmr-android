#include <jni.h>
#include <math.h>

#ifndef NULL
#define NULL   ((void *) 0)
#endif


JNIEXPORT jfloatArray JNICALL
Java_com_example_alejandro_jmr_1android_jmr_SingleColorDescription_meanC(JNIEnv *env,
                                                                         jobject instance,
                                                                         jintArray image_) {
    jint *image = (*env)->GetIntArrayElements(env, image_, NULL);

    // TODO

    (*env)->ReleaseIntArrayElements(env, image_, image, 0);
    int size = (*env)->GetArrayLength(env, image_);

    int width = 0;
    int height = 0;
    int x, y;

    int imagesize = 64 * 64;

    float mean[3] = {0.0f, 0.0f, 0.0f};

    jint *body = (*env)->GetIntArrayElements(env, image_, 0);

    int i = 0;
    for(i = 0; i < size; i++){
        mean[0] += (body[i] >> 16) & 0xFF; // rojo
        mean[1] += (body[i] >> 8) & 0xFF; // verde
        mean[2] += body[i] & 0xFF; // azul


    }

    // Calculo la media
    mean[0] /= imagesize;
    mean[1] /= imagesize;
    mean[2] /= imagesize;


    jfloatArray mean2 = (*env)->NewFloatArray(env, 3);

    (*env)->SetFloatArrayRegion(env, mean2, 0, 3, mean);
    return mean2;
}

JNIEXPORT jdouble JNICALL
Java_com_example_alejandro_jmr_1android_jmr_SingleColorDescription_compare__IIIIII(JNIEnv *env,
                                                                                   jobject instance,
                                                                                   jint red1,
                                                                                   jint green1,
                                                                                   jint blue1,
                                                                                   jint red2,
                                                                                   jint green2,
                                                                                   jint blue2) {

    // TODO

    double rDif = pow(red1 - red2, 2);
    double gDif = pow(green1 - green2, 2);
    double bDif = pow(blue1 - blue2, 2);
    return sqrt(rDif + gDif + bDif);


}