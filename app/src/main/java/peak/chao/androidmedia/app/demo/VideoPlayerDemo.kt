package peak.chao.androidmedia.app.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import peak.chao.androidmedia.video.VideoPlayer
import peak.chao.androidmedia.video.rememberVideoPlayerState

/**
 * Demo screen for VideoPlayer component.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerDemo(
    onBack: () -> Unit = {}
) {
    // Sample video URL (Big Buck Bunny)
    val sampleVideoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    val videoState = rememberVideoPlayerState(sampleVideoUrl)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("视频播放器") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            VideoPlayer(
                state = videoState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
