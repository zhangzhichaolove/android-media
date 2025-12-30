package peak.chao.androidmedia.image

import android.graphics.Bitmap
import peak.chao.androidmedia.MediaLib

/**
 * Native image processor for applying filters and transformations.
 * Uses C++ implementation for optimal performance.
 */
object ImageProcessor {
    
    init {
        MediaLib.init()
    }
    
    /**
     * Apply grayscale filter to the bitmap.
     * @param bitmap Mutable bitmap in ARGB_8888 format
     * @return true if successful
     */
    fun applyGrayscale(bitmap: Bitmap): Boolean {
        require(bitmap.isMutable) { "Bitmap must be mutable" }
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        return nativeApplyGrayscale(bitmap)
    }
    
    /**
     * Apply sepia filter to the bitmap.
     * @param bitmap Mutable bitmap in ARGB_8888 format
     * @return true if successful
     */
    fun applySepia(bitmap: Bitmap): Boolean {
        require(bitmap.isMutable) { "Bitmap must be mutable" }
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        return nativeApplySepia(bitmap)
    }
    
    /**
     * Apply invert colors filter to the bitmap.
     * @param bitmap Mutable bitmap in ARGB_8888 format
     * @return true if successful
     */
    fun applyInvert(bitmap: Bitmap): Boolean {
        require(bitmap.isMutable) { "Bitmap must be mutable" }
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        return nativeApplyInvert(bitmap)
    }
    
    /**
     * Adjust brightness of the bitmap.
     * @param bitmap Mutable bitmap in ARGB_8888 format
     * @param factor Brightness adjustment (-255 to 255)
     * @return true if successful
     */
    fun adjustBrightness(bitmap: Bitmap, factor: Int): Boolean {
        require(bitmap.isMutable) { "Bitmap must be mutable" }
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        require(factor in -255..255) { "Factor must be between -255 and 255" }
        return nativeAdjustBrightness(bitmap, factor)
    }
    
    /**
     * Adjust contrast of the bitmap.
     * @param bitmap Mutable bitmap in ARGB_8888 format
     * @param factor Contrast adjustment (0.0 to 2.0, 1.0 = no change)
     * @return true if successful
     */
    fun adjustContrast(bitmap: Bitmap, factor: Float): Boolean {
        require(bitmap.isMutable) { "Bitmap must be mutable" }
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        require(factor in 0f..2f) { "Factor must be between 0.0 and 2.0" }
        return nativeAdjustContrast(bitmap, factor)
    }
    
    /**
     * Rotate bitmap by 180 degrees in-place.
     * @param bitmap Mutable bitmap in ARGB_8888 format
     * @return true if successful
     */
    fun rotate180(bitmap: Bitmap): Boolean {
        require(bitmap.isMutable) { "Bitmap must be mutable" }
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        return nativeRotate180(bitmap)
    }
    
    /**
     * Rotate bitmap by 90 degrees clockwise.
     * Creates a new bitmap with swapped dimensions.
     * @param bitmap Source bitmap in ARGB_8888 format
     * @return Rotated bitmap, or null if failed
     */
    fun rotate90CW(bitmap: Bitmap): Bitmap? {
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        // For 90° rotation, we need to create a new bitmap with swapped dimensions
        val matrix = android.graphics.Matrix().apply { postRotate(90f) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    /**
     * Rotate bitmap by 90 degrees counter-clockwise.
     * Creates a new bitmap with swapped dimensions.
     * @param bitmap Source bitmap in ARGB_8888 format
     * @return Rotated bitmap, or null if failed
     */
    fun rotate90CCW(bitmap: Bitmap): Bitmap? {
        require(bitmap.config == Bitmap.Config.ARGB_8888) { "Bitmap must be ARGB_8888" }
        val matrix = android.graphics.Matrix().apply { postRotate(-90f) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    /**
     * Crop bitmap to specified region.
     * @param bitmap Source bitmap
     * @param x X coordinate of crop region
     * @param y Y coordinate of crop region
     * @param width Width of crop region
     * @param height Height of crop region
     * @return Cropped bitmap, or null if failed
     */
    fun crop(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap? {
        require(x >= 0 && y >= 0) { "Crop coordinates must be non-negative" }
        require(width > 0 && height > 0) { "Crop dimensions must be positive" }
        require(x + width <= bitmap.width && y + height <= bitmap.height) { 
            "Crop region exceeds bitmap bounds" 
        }
        return Bitmap.createBitmap(bitmap, x, y, width, height)
    }
    
    // Native methods
    private external fun nativeApplyGrayscale(bitmap: Bitmap): Boolean
    private external fun nativeApplySepia(bitmap: Bitmap): Boolean
    private external fun nativeApplyInvert(bitmap: Bitmap): Boolean
    private external fun nativeAdjustBrightness(bitmap: Bitmap, factor: Int): Boolean
    private external fun nativeAdjustContrast(bitmap: Bitmap, factor: Float): Boolean
    private external fun nativeRotate180(bitmap: Bitmap): Boolean
}
