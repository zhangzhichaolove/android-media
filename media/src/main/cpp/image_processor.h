#ifndef ANDROIDMEDIA_IMAGE_PROCESSOR_H
#define ANDROIDMEDIA_IMAGE_PROCESSOR_H

#include <cstdint>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Apply grayscale filter to bitmap
 * @param pixels Pointer to bitmap pixels (ARGB_8888)
 * @param width Bitmap width
 * @param height Bitmap height
 * @param stride Bitmap stride in bytes
 */
void applyGrayscale(void* pixels, int width, int height, int stride);

/**
 * Apply sepia filter to bitmap
 */
void applySepia(void* pixels, int width, int height, int stride);

/**
 * Apply invert colors filter to bitmap
 */
void applyInvert(void* pixels, int width, int height, int stride);

/**
 * Adjust brightness of bitmap
 * @param factor Brightness factor (-255 to 255)
 */
void adjustBrightness(void* pixels, int width, int height, int stride, int factor);

/**
 * Adjust contrast of bitmap
 * @param factor Contrast factor (0.0 to 2.0, 1.0 = no change)
 */
void adjustContrast(void* pixels, int width, int height, int stride, float factor);

/**
 * Rotate bitmap by 90 degrees clockwise
 * @param srcPixels Source bitmap pixels
 * @param dstPixels Destination bitmap pixels (must be pre-allocated)
 * @param width Source width
 * @param height Source height
 */
void rotate90CW(void* srcPixels, void* dstPixels, int width, int height, int srcStride, int dstStride);

/**
 * Rotate bitmap by 90 degrees counter-clockwise
 */
void rotate90CCW(void* srcPixels, void* dstPixels, int width, int height, int srcStride, int dstStride);

/**
 * Rotate bitmap by 180 degrees
 */
void rotate180(void* pixels, int width, int height, int stride);

/**
 * Crop bitmap
 * @param srcPixels Source bitmap pixels
 * @param dstPixels Destination bitmap pixels (must be pre-allocated)
 * @param srcWidth Source width
 * @param srcHeight Source height
 * @param cropX Crop start X
 * @param cropY Crop start Y
 * @param cropWidth Crop width
 * @param cropHeight Crop height
 */
void cropBitmap(void* srcPixels, void* dstPixels, int srcWidth, int srcHeight, 
                int srcStride, int dstStride,
                int cropX, int cropY, int cropWidth, int cropHeight);

#ifdef __cplusplus
}
#endif

#endif // ANDROIDMEDIA_IMAGE_PROCESSOR_H
