package com.example.exampleapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.example.exampleapplication.presentation.chronometer.Chronometer
import com.example.exampleapplication.presentation.workers.PhotoCompressWorker
import com.example.exampleapplication.ui.theme.ExampleApplicationTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager
    private val viewModel by viewModels<PhotoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            ExampleApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val workerResult = viewModel.workId?.let { id ->
                        workManager.getWorkInfoByIdLiveData(id).observeAsState().value
                    }

                    LaunchedEffect(workerResult?.outputData) {
                        if (workerResult?.outputData != null) {
                            val filePath = workerResult
                                .outputData
                                .getString(PhotoCompressWorker.KEY_RESULT_PATH)

                            filePath?.let {
                                val bitmap = BitmapFactory.decodeFile(it)
                                viewModel.updateCompressedBitmap(bitmap)
                            }

                        }

                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        viewModel.uncompressedUri?.let {
                            Text(text = "Uncompressed photo:")
                            AsyncImage(model = it, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        viewModel.compressedBitMap?.let {
                            Text(text = "Compressed photo:")
                            Image(bitmap = it.asImageBitmap(), contentDescription = null)
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        } ?: return

        viewModel.updateUncompressedUri(uri)

        val request = OneTimeWorkRequestBuilder<PhotoCompressWorker>()
            .setInputData(
                workDataOf(
                    PhotoCompressWorker.KEY_CONTENT_URI to uri.toString(),
                    PhotoCompressWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 20L
                )
            )
            .setConstraints(
                Constraints(
                    requiredNetworkType = NetworkType.CONNECTED,
                )
            ).build()
        viewModel.updateWorkId(request.id)
        workManager.enqueue(request)

    }
}

class SessionExpirationObserver : DefaultLifecycleObserver {

    val state: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val chronometer = Chronometer(
        onReached = {
            state.value = true
        },
        5000L
    )

    fun interactionReceived() {
        chronometer.restart()
    }


    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (!state.value)
            chronometer.start()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExampleApplicationTheme {
        Greeting("Android")
    }
}
