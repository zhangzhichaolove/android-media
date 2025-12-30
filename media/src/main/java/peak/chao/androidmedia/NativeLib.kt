package peak.chao.androidmedia

class NativeLib {

    /**
     * A native method that is implemented by the 'androidmedia' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'androidmedia' library on application startup.
        init {
            System.loadLibrary("androidmedia")
        }
    }
}