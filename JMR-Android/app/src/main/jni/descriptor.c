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

JNIEXPORT jint JNICALL
Java_com_example_alejandro_jmr_1android_jmr_MPEG7ColorStructure_quantFuncC(JNIEnv *env,
                                                                           jobject instance,
                                                                           jdouble x) {

    // TODO

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

JNIEXPORT jintArray JNICALL
Java_com_example_alejandro_jmr_1android_jmr_MPEG7ColorStructure_getStartSubspacePosC(JNIEnv *env,
                                                                                     jclass type,
                                                                                     jint offset,
                                                                                     jintArray quantArray_) {
    jint *quantArray = (*env)->GetIntArrayElements(env, quantArray_, NULL);

    // TODO

    (*env)->ReleaseIntArrayElements(env, quantArray_, quantArray, 0);

    int startP[5] = {0,0,0,0,0};
    int i = 0;
    int startPLength = 5;
    startP[0] = 0;
    for (i = 1; i < startPLength; i++) {
        startP[i] = startP[i - 1]; //Set the position of the previous subspace start
        startP[i] += quantArray[i]; //Add the length of the previous subspace
    }

    jintArray mean2 = (*env)->NewIntArray(env, 5);

    (*env)->SetIntArrayRegion(env, mean2, 0, 5, startP);
    return mean2;

}

JNIEXPORT jfloatArray JNICALL
Java_com_example_alejandro_jmr_1android_HMMDImage_fromRGBC(JNIEnv *env, jobject instance,
                                                           jfloatArray rgbVec_) {

    jfloat *rgbVec = (*env)->GetFloatArrayElements(env, rgbVec_, NULL);

    // TODO

    (*env)->ReleaseFloatArrayElements(env, rgbVec_, rgbVec, 0);

    float hmmdVec[4] = {0.0f,0.0f,0.0f,0.0f};
    float max = 0.0f;
    float min = 0.0f;
    float diff = 0.0f;
    float hue = 0.0f;
    float r = rgbVec[0];
    float g = rgbVec[1];
    float b = rgbVec[2];


    r = (r < 0.0f) ? 0.0f : ((r > 1.0f) ? 1.0f : r);
    g = (g < 0.0f) ? 0.0f : ((g > 1.0f) ? 1.0f : g);
    b = (b < 0.0f) ? 0.0f : ((b > 1.0f) ? 1.0f : b);

    if (r > b) {
        if (r > g)
            max = r;
        else
            max = g;
    }
    else {
        if(b > g)
            max = b;
        else
            max = g;
    }

    if (r < b) {
        if (r < g)
            min = r;
        else
            min = g;
    }
    else {
        if(b < g)
            min = b;
        else
            min = g;
    }

    diff = (max - min);

    if (diff == 0)
        hue = 0;
    else if (r == max && (g - b) > 0)
        hue = 60 * (g - b) / (max - min);
    else if (r == max && (g - b) <= 0)
        hue = 60 * (g - b) / (max - min) + 360;
    else if (g == max)
        hue = (float) (60 * (2.f + (b - r) / (max - min)));
    else if (b == max)
        hue = (float) (60 * (4.f + (r - g) / (max - min)));

    // set hue

    hmmdVec[0] = hue;
    hmmdVec[1] = max;
    hmmdVec[2] = min;
    hmmdVec[3] = diff;

    jfloatArray mean2 = (*env)->NewFloatArray(env, 4);

    (*env)->SetFloatArrayRegion(env, mean2, 0, 4, hmmdVec);

    return mean2;

}

JNIEXPORT jfloatArray JNICALL
Java_com_example_alejandro_jmr_1android_jmr_MPEG7ColorStructure_structureHistoC(JNIEnv *env,
                                                                                jobject instance,
                                                                                jobjectArray imQ,
                                                                                jint wImg,
                                                                                jint hImg,
                                                                                jint qLevels) {

    // TODO



}