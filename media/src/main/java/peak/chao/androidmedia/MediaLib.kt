package peak.chao.androidmedia

/**
 * Android Media Library - Main entry point
 * 
 * Provides video, audio, and image components with native C++ processing.
 */
object MediaLib {
    
    private var isInitialized = false
    
    /**
     * Initialize the media library.
     * This loads the native library and should be called before using any components.
     */
    @JvmStatic
    fun init() {
        if (!isInitialized) {
            System.loadLibrary("androidmedia")
            isInitialized = true
        }
    }
    
    /**
     * Check if the library is initialized.
     */
    @JvmStatic
    fun isInitialized(): Boolean = isInitialized
}
