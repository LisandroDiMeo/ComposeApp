package com.example.exampleapplication.presentation.fragments

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkManager
import coil.compose.AsyncImage
import com.example.exampleapplication.PhotoViewModel
import com.example.exampleapplication.presentation.workers.PhotoCompressWorker

@Composable
fun ImageCompressor() {
    val viewModel = viewModel(modelClass = PhotoViewModel::class.java)
    val workManager = WorkManager.getInstance(LocalContext.current.applicationContext)

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
