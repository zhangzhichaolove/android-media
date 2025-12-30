package peak.chao.androidmedia.image

/**
 * Predefined image filters that can be applied to bitmaps.
 */
enum class ImageFilter {
    /**
     * No filter applied
     */
    NONE,
    
    /**
     * Converts image to grayscale using luminosity method
     */
    GRAYSCALE,
    
    /**
     * Applies sepia tone effect for a vintage look
     */
    SEPIA,
    
    /**
     * Inverts all colors in the image
     */
    INVERT
}
