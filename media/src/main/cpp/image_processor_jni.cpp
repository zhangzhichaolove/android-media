#include <jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include "image_processor.h"

#define LOG_TAG "ImageProcessor"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jboolean JNICALL
Java_peak_chao_androidmedia_image_ImageProcessor_nativeApplyGrayscale(
        JNIEnv *env, jobject /* this */, jobject bitmap) {
    
    AndroidBitmapInfo info;
    void* pixels;
    
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return JNI_FALSE;
    }
    
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888");
        return JNI_FALSE;
    }
    
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock pixels");
        return JNI_FALSE;
    }
    
    applyGrayscale(pixels, info.width, info.height, info.stride);
    
    AndroidBitmap_unlockPixels(env, bitmap);
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_peak_chao_androidmedia_image_ImageProcessor_nativeApplySepia(
        JNIEnv *env, jobject /* this */, jobject bitmap) {
    
    AndroidBitmapInfo info;
    void* pixels;
    
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return JNI_FALSE;
    }
    
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888");
        return JNI_FALSE;
    }
    
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock pixels");
        return JNI_FALSE;
    }
    
    applySepia(pixels, info.width, info.height, info.stride);
    
    AndroidBitmap_unlockPixels(env, bitmap);
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_peak_chao_androidmedia_image_ImageProcessor_nativeApplyInvert(
        JNIEnv *env, jobject /* this */, jobject bitmap) {
    
    AndroidBitmapInfo info;
    void* pixels;
    
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return JNI_FALSE;
    }
    
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888");
        return JNI_FALSE;
    }
    
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock pixels");
        return JNI_FALSE;
    }
    
    applyInvert(pixels, info.width, info.height, info.stride);
    
    AndroidBitmap_unlockPixels(env, bitmap);
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_peak_chao_androidmedia_image_ImageProcessor_nativeAdjustBrightness(
        JNIEnv *env, jobject /* this */, jobject bitmap, jint factor) {
    
    AndroidBitmapInfo info;
    void* pixels;
    
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return JNI_FALSE;
    }
    
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888");
        return JNI_FALSE;
    }
    
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock pixels");
        return JNI_FALSE;
    }
    
    adjustBrightness(pixels, info.width, info.height, info.stride, factor);
    
    AndroidBitmap_unlockPixels(env, bitmap);
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_peak_chao_androidmedia_image_ImageProcessor_nativeAdjustContrast(
        JNIEnv *env, jobject /* this */, jobject bitmap, jfloat factor) {
    
    AndroidBitmapInfo info;
    void* pixels;
    
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return JNI_FALSE;
    }
    
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888");
        return JNI_FALSE;
    }
    
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock pixels");
        return JNI_FALSE;
    }
    
    adjustContrast(pixels, info.width, info.height, info.stride, factor);
    
    AndroidBitmap_unlockPixels(env, bitmap);
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_peak_chao_androidmedia_image_ImageProcessor_nativeRotate180(
        JNIEnv *env, jobject /* this */, jobject bitmap) {
    
    AndroidBitmapInfo info;
    void* pixels;
    
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        LOGE("Failed to get bitmap info");
        return JNI_FALSE;
    }
    
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888");
        return JNI_FALSE;
    }
    
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        LOGE("Failed to lock pixels");
        return JNI_FALSE;
    }
    
    rotate180(pixels, info.width, info.height, info.stride);
    
    AndroidBitmap_unlockPixels(env, bitmap);
    return JNI_TRUE;
}

} // extern "C"
