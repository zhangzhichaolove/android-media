package peak.chao.androidmedia.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView

/**
 * Video Player Composable component.
 * 
 * Wraps ExoPlayer with a customizable controller UI.
 * 
 * @param state VideoPlayerState created via rememberVideoPlayerState()
 * @param modifier Modifier for the container
 * @param showController Whether to show the default controller
 * @param controller Custom controller composable, uses default if null
 */
@Composable
fun VideoPlayer(
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
    showController: Boolean = true,
    controller: (@Composable (VideoPlayerState) -> Unit)? = null
) {
    var isControllerVisible by remember { mutableStateOf(true) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Video surface
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = state.player
                    useController = false // We use custom controller
                    setOnClickListener {
                        isControllerVisible = !isControllerVisible
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { playerView ->
                playerView.player = state.player
            }
        )
        
        // Controller overlay
        if (showController && isControllerVisible) {
            if (controller != null) {
                controller(state)
            } else {
                VideoController(
                    state = state,
                    onDismiss = { isControllerVisible = false }
                )
            }
        }
    }
}
