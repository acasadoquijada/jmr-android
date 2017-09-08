#include <jni.h>

JNIEXPORT jfloatArray JNICALL
Java_com_example_alejandro_jmr_1android_jmr_SingleColorDescription_meanC(JNIEnv *env,
                                                                         jobject instance,
                                                                         jobject obj) {

    // TODO


    int width = 0;
    int height = 0;
    int x, y;

    jclass cls = (*env)->GetObjectClass(env, obj);
    jmethodID getWidth = (*env)->GetMethodID(env, cls, "getWidth", "()I");
    width = (*env)->CallIntMethod(env, obj, getWidth);

    jmethodID getHeight = (*env)->GetMethodID(env, cls, "getHeight", "()I");
    height = (*env)->CallIntMethod(env, obj, getHeight);

    jmethodID getRed = (*env)->GetMethodID
            (env, cls, "getRed", "(II)I");
    jmethodID getGreen = (*env)->GetMethodID
            (env, cls, "getGreen", "(II)I");
    jmethodID getBlue = (*env)->GetMethodID
            (env, cls, "getBlue", "(II)I");

    double imageSize = width * height;

    float mean[3] = {0.0f, 0.0f, 0.0f};

    for (x = 0; x < height; x++) {
        for (y = 0; y < width/2; y++) {

            mean[0] += (*env)->CallIntMethod(env, obj, getRed, x, y*2);
            mean[1] += (*env)->CallIntMethod(env, obj, getGreen, x, y*2);
            mean[2] += (*env)->CallIntMethod(env, obj, getBlue, x, y*2);

            mean[0] += (*env)->CallIntMethod(env, obj, getRed, x, y*2+1);
            mean[1] += (*env)->CallIntMethod(env, obj, getGreen, x, y*2+1);
            mean[2] += (*env)->CallIntMethod(env, obj, getBlue, x, y*2+1);

        }
    }
    // Calculo la media
    mean[0] /= imageSize;
    mean[1] /= imageSize;
    mean[2] /= imageSize;

    // La devuelvo

    jfloatArray mean2 = (*env)->NewFloatArray(env, 3);

    (*env)->SetFloatArrayRegion(env, mean2, 0, 3, mean);
    return mean2;


}