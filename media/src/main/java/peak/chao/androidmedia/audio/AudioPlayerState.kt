package peak.chao.androidmedia.audio

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
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay

/**
 * State holder for audio player.
 * Manages playback state and provides control functions.
 */
class AudioPlayerState(
    val player: ExoPlayer
) {
    var isPlaying by mutableStateOf(false)
        internal set
    
    var currentPosition by mutableLongStateOf(0L)
        internal set
    
    var duration by mutableLongStateOf(0L)
        internal set
    
    var isLoading by mutableStateOf(true)
        internal set
    
    var volume by mutableFloatStateOf(1f)
        internal set
    
    var isMuted by mutableStateOf(false)
        internal set
    
    var title by mutableStateOf<String?>(null)
        internal set
    
    var artist by mutableStateOf<String?>(null)
        internal set
    
    var albumArtUri by mutableStateOf<String?>(null)
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
    fun skipForward(ms: Long = 15000) {
        val newPosition = (currentPosition + ms).coerceAtMost(duration)
        seekTo(newPosition)
    }
    
    /**
     * Skip backward by specified milliseconds.
     */
    fun skipBackward(ms: Long = 15000) {
        val newPosition = (currentPosition - ms).coerceAtLeast(0)
        seekTo(newPosition)
    }
}

/**
 * Remember and create an AudioPlayerState.
 * 
 * @param mediaUri URI of the audio to play
 * @param title Optional title to display
 * @param artist Optional artist name to display
 * @param albumArtUri Optional album art URI
 * @return AudioPlayerState instance
 */
@Composable
fun rememberAudioPlayerState(
    mediaUri: String,
    title: String? = null,
    artist: String? = null,
    albumArtUri: String? = null
): AudioPlayerState {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.Builder()
                .setUri(mediaUri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(title)
                        .setArtist(artist)
                        .build()
                )
                .build()
            setMediaItem(mediaItem)
            prepare()
        }
    }
    
    val state = remember(player) { 
        AudioPlayerState(player).apply {
            this.title = title
            this.artist = artist
            this.albumArtUri = albumArtUri
        }
    }
    
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
            
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                if (state.title == null) {
                    state.title = mediaMetadata.title?.toString()
                }
                if (state.artist == null) {
                    state.artist = mediaMetadata.artist?.toString()
                }
                if (state.albumArtUri == null && mediaMetadata.artworkUri != null) {
                    state.albumArtUri = mediaMetadata.artworkUri.toString()
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
            delay(500)
        }
    }
    
    return state
}
