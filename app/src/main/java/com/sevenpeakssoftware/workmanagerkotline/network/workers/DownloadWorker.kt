package com.sevenpeakssoftware.workmanagerkotline.network.workers

import android.content.Context
import androidx.core.app.NotificationCompat
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

class DownloadWorker (
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context,workerParameters) {
    override suspend fun doWork(): Result {
        startForGroundService()
        delay(5000)
        val response = FileApi.instance.downloadImageFile()
        response.body()?.let {
            body->
           return withContext(Dispatchers.IO){
                val file  = File(context.cacheDir,"downloadfile.jpg")
                val outputstream = FileOutputStream(file)
                outputstream.use {
                    stream->
                    try{
                        stream.write(body.bytes())
                    }
                    catch (e:IOException){

                        return@withContext Result.failure(
                              workDataOf(
                                  Workerkeys.ERROR_MS to e.localizedMessage
                              )
                        )
                    }
                }
               return@withContext Result.success(

                    workDataOf(
                        Workerkeys.IMAGE_URL to file.toUri().toString()
                    )
                )


            }


        }


        if(!response.isSuccessful){
            if(response.code().toString().startsWith("5")){
                return Result.retry()
            }

            return  Result.failure(
                workDataOf(
                    Workerkeys.ERROR_MS to "Network Error"
                )
            )

        }


        return  Result.failure(
            workDataOf(
                Workerkeys.ERROR_MS to "Unknown Error"
            )
        )

    }

    private suspend fun startForGroundService(){
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context,"download_chanel")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Downloading...")
                    .setContentTitle("Download in progress..")
                    .build()
            )
        )
    }
}