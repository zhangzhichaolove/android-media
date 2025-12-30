package peak.chao.androidmedia.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale

/**
 * Image Viewer Composable with gesture support.
 * 
 * Supports pinch zoom, pan, and double-tap to zoom.
 * 
 * @param state ImageViewerState created via rememberImageViewerState()
 * @param modifier Modifier for the container
 * @param minScale Minimum zoom scale
 * @param maxScale Maximum zoom scale
 * @param showToolbar Whether to show the default toolbar
 * @param toolbar Custom toolbar composable
 */
@Composable
fun ImageViewer(
    state: ImageViewerState,
    modifier: Modifier = Modifier,
    minScale: Float = 0.5f,
    maxScale: Float = 5f,
    showToolbar: Boolean = true,
    toolbar: (@Composable (ImageViewerState) -> Unit)? = null
) {
    var isToolbarVisible by remember { mutableStateOf(true) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Loading indicator
        if (state.isProcessing) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        // Image with gestures
        state.currentBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = state.scale
                        scaleY = state.scale
                        translationX = state.offset.x
                        translationY = state.offset.y
                        rotationZ = state.rotation
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, rotation ->
                            // Update scale with constraints
                            val newScale = (state.scale * zoom).coerceIn(minScale, maxScale)
                            state.scale = newScale
                            
                            // Update offset
                            state.offset = Offset(
                                x = state.offset.x + pan.x,
                                y = state.offset.y + pan.y
                            )
                            
                            // Note: rotation gesture is disabled by default
                            // Uncomment to enable:
                            // state.rotation += rotation
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { offset ->
                                // Double-tap to toggle zoom
                                if (state.scale > 1f) {
                                    state.resetTransform()
                                } else {
                                    state.scale = 2.5f
                                    // Center on tap point
                                    state.offset = Offset(
                                        x = (size.width / 2f - offset.x) * 1.5f,
                                        y = (size.height / 2f - offset.y) * 1.5f
                                    )
                                }
                            },
                            onTap = {
                                isToolbarVisible = !isToolbarVisible
                            }
                        )
                    },
                contentScale = ContentScale.Fit
            )
        }
        
        // Toolbar
        if (showToolbar && isToolbarVisible) {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                if (toolbar != null) {
                    toolbar(state)
                } else {
                    ImageToolbar(
                        state = state,
                        showRotateButton = true,
                        showFilterButton = true,
                        showCropButton = true
                    )
                }
            }
        }
    }
}
