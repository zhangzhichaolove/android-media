package peak.chao.androidmedia.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay

/**
 * State holder for video player.
 * Manages playback state and provides control functions.
 */
class VideoPlayerState(
    val player: ExoPlayer
) {
    var isPlaying by mutableStateOf(false)
        internal set
    
    var currentPosition by mutableLongStateOf(0L)
        internal set
    
    var duration by mutableLongStateOf(0L)
        internal set
    
    var bufferedPercentage by mutableFloatStateOf(0f)
        internal set
    
    var isLoading by mutableStateOf(true)
        internal set
    
    var volume by mutableFloatStateOf(1f)
        internal set
    
    var isMuted by mutableStateOf(false)
        internal set
    
    /**
     * Play or resume playback.
     */
    fun play() {
        player.play()
    }
    
    /**
     * Pause playback.
     */
    fun pause() {
        player.pause()
    }
    
    /**
     * Toggle play/pause.
     */
    fun togglePlayPause() {
        if (isPlaying) pause() else play()
    }
    
    /**
     * Seek to position in milliseconds.
     */
    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }
    
    /**
     * Set volume (0.0 to 1.0).
     */
    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
        player.volume = if (isMuted) 0f else this.volume
    }
    
    /**
     * Toggle mute state.
     */
    fun toggleMute() {
        isMuted = !isMuted
        player.volume = if (isMuted) 0f else volume
    }
    
    /**
     * Skip forward by specified milliseconds.
     */
    fun skipForward(ms: Long = 10000) {
        val newPosition = (currentPosition + ms).coerceAtMost(duration)
        seekTo(newPosition)
    }
    
    /**
     * Skip backward by specified milliseconds.
     */
    fun skipBackward(ms: Long = 10000) {
        val newPosition = (currentPosition - ms).coerceAtLeast(0)
        seekTo(newPosition)
    }
}

/**
 * Remember and create a VideoPlayerState.
 * 
 * @param mediaUri URI of the media to play
 * @return VideoPlayerState instance
 */
@Composable
fun rememberVideoPlayerState(mediaUri: String): VideoPlayerState {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(mediaUri))
            prepare()
        }
    }
    
    val state = remember(player) { VideoPlayerState(player) }
    
    // Update state based on player events
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                state.isPlaying = isPlaying
            }
            
            override fun onPlaybackStateChanged(playbackState: Int) {
                state.isLoading = playbackState == Player.STATE_BUFFERING
                if (playbackState == Player.STATE_READY) {
                    state.duration = player.duration
                }
            }
        }
        
        player.addListener(listener)
        
        onDispose {
            player.removeListener(listener)
        }
    }
    
    // Lifecycle handling
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_RESUME -> {
                    // Don't auto-play on resume
                }
                Lifecycle.Event.ON_DESTROY -> player.release()
                else -> {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }
    
    // Position update coroutine
    LaunchedEffect(player) {
        while (true) {
            state.currentPosition = player.currentPosition
            state.bufferedPercentage = player.bufferedPercentage / 100f
            delay(500)
        }
    }
    
    return state
}
