package com.sevenpeakssoftware.workmanagerkotline.network.workers

import android.content.Context
import android.graphics.*
import androidx.core.app.NotificationCompat
import androidx.core.net.toFile
import androidx.core.net.toUri


import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.sevenpeakssoftware.workmanagerkotline.R
import com.sevenpeakssoftware.workmanagerkotline.network.FileApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random


class ColorFilterWorker (
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context,workerParameters) {
    override suspend fun doWork(): Result {

        val imageFile = workerParameters.inputData.getString(Workerkeys.IMAGE_URL)
            ?.toUri()
            ?.toFile()
        delay(5000)
       return imageFile?.let {
               file ->

            val bmp = BitmapFactory.decodeFile(file.absolutePath)
            val resulBitmap = bmp.copy(bmp.config, true)
            val paint = Paint()
            paint.colorFilter = LightingColorFilter(0x8FF04, 1)
            val canvas = Canvas(resulBitmap)
            canvas.drawBitmap(resulBitmap, 0f, 0f, paint)

           withContext(Dispatchers.IO) {
               val file = File(context.cacheDir, "fileoutput.jpg")
               val outputstream = FileOutputStream(file)
               val success = resulBitmap.compress(
                   Bitmap.CompressFormat.JPEG,
                   90,
                   outputstream
               )

               if (success) {
                   Result.success(
                       workDataOf(
                           Workerkeys.FILTER_URL to file.toUri().toString()
                       )
                   )
               }else{
                  Result.failure()
               }


           }





        } ?:Result.failure()



    }

}

