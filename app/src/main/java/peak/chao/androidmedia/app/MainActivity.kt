package peak.chao.androidmedia.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import peak.chao.androidmedia.app.demo.AudioPlayerDemo
import peak.chao.androidmedia.app.demo.ImageViewerDemo
import peak.chao.androidmedia.app.demo.VideoPlayerDemo
import peak.chao.androidmedia.app.ui.theme.AndroidMediaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidMediaTheme {
                MediaDemoApp()
            }
        }
    }
}

enum class Screen {
    HOME, VIDEO, AUDIO, IMAGE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDemoApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    
    when (currentScreen) {
        Screen.HOME -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Android Media 组件库") }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DemoCard(
                        title = "视频播放器",
                        description = "支持播放控制、进度条、音量调节",
                        icon = Icons.Default.PlayCircle,
                        onClick = { currentScreen = Screen.VIDEO }
                    )
                    
                    DemoCard(
                        title = "音频播放器",
                        description = "支持播放控制、进度显示、专辑封面",
                        icon = Icons.Default.MusicNote,
                        onClick = { currentScreen = Screen.AUDIO }
                    )
                    
                    DemoCard(
                        title = "图片查看器",
                        description = "支持手势缩放、滤镜、旋转、裁剪",
                        icon = Icons.Default.Image,
                        onClick = { currentScreen = Screen.IMAGE }
                    )
                }
            }
        }
        Screen.VIDEO -> {
            VideoPlayerDemo(onBack = { currentScreen = Screen.HOME })
        }
        Screen.AUDIO -> {
            AudioPlayerDemo(onBack = { currentScreen = Screen.HOME })
        }
        Screen.IMAGE -> {
            ImageViewerDemo(onBack = { currentScreen = Screen.HOME })
        }
    }
}

@Composable
fun DemoCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}