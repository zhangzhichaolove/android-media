package peak.chao.androidmedia.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Default video controller UI.
 * 
 * Provides play/pause, seek, skip, and volume controls with a modern design.
 * 
 * @param state VideoPlayerState to control
 * @param onDismiss Callback when controller should be hidden
 */
@Composable
fun VideoController(
    state: VideoPlayerState,
    onDismiss: () -> Unit = {}
) {
    var showVolumeSlider by remember { mutableStateOf(false) }
    
    // Auto-hide controller after 3 seconds of inactivity
    LaunchedEffect(state.isPlaying) {
        if (state.isPlaying) {
            delay(3000)
            onDismiss()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() }
    ) {
        // Top gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        // Center controls
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Skip backward
            IconButton(
                onClick = { state.skipBackward() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = "Skip backward 10s",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
            
            // Play/Pause button
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { state.togglePlayPause() },
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            
            // Skip forward
            IconButton(
                onClick = { state.skipForward() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = "Skip forward 10s",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        
        // Bottom controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            // Progress bar
            Box(modifier = Modifier.fillMaxWidth()) {
                // Buffered progress
                LinearProgressIndicator(
                    progress = { state.bufferedPercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color.White.copy(alpha = 0.3f),
                    trackColor = Color.White.copy(alpha = 0.1f)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Seek slider
            Slider(
                value = if (state.duration > 0) {
                    state.currentPosition.toFloat() / state.duration.toFloat()
                } else 0f,
                onValueChange = { progress ->
                    state.seekTo((progress * state.duration).toLong())
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )
            
            // Time and volume row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time display
                Text(
                    text = "${formatTime(state.currentPosition)} / ${formatTime(state.duration)}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
                
                // Volume control
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { 
                            if (showVolumeSlider) {
                                state.toggleMute()
                            } else {
                                showVolumeSlider = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (state.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "Volume",
                            tint = Color.White
                        )
                    }
                    
                    AnimatedVisibility(
                        visible = showVolumeSlider,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Slider(
                            value = state.volume,
                            onValueChange = { state.setVolume(it) },
                            modifier = Modifier.width(100.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.White,
                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Format milliseconds to MM:SS or HH:MM:SS format.
 */
private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}
