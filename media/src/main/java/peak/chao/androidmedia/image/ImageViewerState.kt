package peak.chao.androidmedia.image

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

/**
 * State holder for image viewer.
 * Manages zoom, pan, rotation, and image processing state.
 */
class ImageViewerState(
    initialBitmap: Bitmap?
) {
    var currentBitmap by mutableStateOf(initialBitmap)
        internal set
    
    var originalBitmap by mutableStateOf(initialBitmap)
        internal set
    
    var scale by mutableFloatStateOf(1f)
        internal set
    
    var offset by mutableStateOf(Offset.Zero)
        internal set
    
    var rotation by mutableFloatStateOf(0f)
        internal set
    
    var currentFilter by mutableStateOf(ImageFilter.NONE)
        internal set
    
    var isProcessing by mutableStateOf(false)
        internal set
    
    /**
     * Reset zoom and pan to default.
     */
    fun resetTransform() {
        scale = 1f
        offset = Offset.Zero
        rotation = 0f
    }
    
    /**
     * Rotate image by 90 degrees clockwise.
     */
    fun rotate90CW() {
        currentBitmap?.let { bitmap ->
            isProcessing = true
            val rotated = ImageProcessor.rotate90CW(bitmap)
            if (rotated != null) {
                currentBitmap = rotated
            }
            isProcessing = false
        }
    }
    
    /**
     * Rotate image by 90 degrees counter-clockwise.
     */
    fun rotate90CCW() {
        currentBitmap?.let { bitmap ->
            isProcessing = true
            val rotated = ImageProcessor.rotate90CCW(bitmap)
            if (rotated != null) {
                currentBitmap = rotated
            }
            isProcessing = false
        }
    }
    
    /**
     * Apply filter to image.
     */
    fun applyFilter(filter: ImageFilter) {
        originalBitmap?.let { original ->
            isProcessing = true
            
            // Create mutable copy from original
            val mutableBitmap = original.copy(Bitmap.Config.ARGB_8888, true)
            
            val success = when (filter) {
                ImageFilter.NONE -> {
                    currentBitmap = original.copy(Bitmap.Config.ARGB_8888, true)
                    true
                }
                ImageFilter.GRAYSCALE -> {
                    ImageProcessor.applyGrayscale(mutableBitmap)
                    currentBitmap = mutableBitmap
                    true
                }
                ImageFilter.SEPIA -> {
                    ImageProcessor.applySepia(mutableBitmap)
                    currentBitmap = mutableBitmap
                    true
                }
                ImageFilter.INVERT -> {
                    ImageProcessor.applyInvert(mutableBitmap)
                    currentBitmap = mutableBitmap
                    true
                }
            }
            
            if (success) {
                currentFilter = filter
            }
            isProcessing = false
        }
    }
    
    /**
     * Adjust brightness of image.
     * @param factor -255 to 255
     */
    fun adjustBrightness(factor: Int) {
        currentBitmap?.let { bitmap ->
            val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            if (ImageProcessor.adjustBrightness(mutableBitmap, factor)) {
                currentBitmap = mutableBitmap
            }
        }
    }
    
    /**
     * Adjust contrast of image.
     * @param factor 0.0 to 2.0
     */
    fun adjustContrast(factor: Float) {
        currentBitmap?.let { bitmap ->
            val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            if (ImageProcessor.adjustContrast(mutableBitmap, factor)) {
                currentBitmap = mutableBitmap
            }
        }
    }
    
    /**
     * Crop image to specified region.
     */
    fun crop(x: Int, y: Int, width: Int, height: Int) {
        currentBitmap?.let { bitmap ->
            isProcessing = true
            val cropped = ImageProcessor.crop(bitmap, x, y, width, height)
            if (cropped != null) {
                currentBitmap = cropped
                originalBitmap = cropped.copy(Bitmap.Config.ARGB_8888, true)
            }
            isProcessing = false
        }
    }
    
    /**
     * Reset to original image.
     */
    fun resetToOriginal() {
        originalBitmap?.let { original ->
            currentBitmap = original.copy(Bitmap.Config.ARGB_8888, true)
            currentFilter = ImageFilter.NONE
            resetTransform()
        }
    }
}

/**
 * Remember and create an ImageViewerState.
 * 
 * @param bitmap Bitmap to display
 * @return ImageViewerState instance
 */
@Composable
fun rememberImageViewerState(bitmap: Bitmap?): ImageViewerState {
    return remember(bitmap) {
        ImageViewerState(bitmap?.copy(Bitmap.Config.ARGB_8888, true))
    }
}
