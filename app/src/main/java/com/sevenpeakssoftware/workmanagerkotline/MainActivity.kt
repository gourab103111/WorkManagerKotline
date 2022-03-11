package com.sevenpeakssoftware.workmanagerkotline

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import androidx.work.impl.model.WorkTypeConverters.StateIds.SUCCEEDED
import coil.load
import com.sevenpeakssoftware.workmanagerkotline.network.workers.ColorFilterWorker
import com.sevenpeakssoftware.workmanagerkotline.network.workers.DownloadWorker
import com.sevenpeakssoftware.workmanagerkotline.network.workers.Workerkeys



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(
                    NetworkType.CONNECTED
                ).build()
            )
            .build()
        val colorFilterRequest = OneTimeWorkRequestBuilder<ColorFilterWorker>()
            .build()
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.getWorkInfoByIdLiveData(downloadRequest.id).observe(this, Observer {
            workinfo->

          //  Log.d("ImageUrl","Imageurl2:"+workinfo.state.name)
          //   Log.d("ImageUrl","Imageurl3:"+workinfo.outputData.toString())


            workinfo.outputData?.let {

                findViewById<ImageView>(R.id.privieImage).load( it.getString(Workerkeys.IMAGE_URL) )


            }

        })


        workManager.getWorkInfoByIdLiveData(colorFilterRequest.id).observe(this, Observer {

        workinfo->



            workinfo.outputData?.let {


                findViewById<ImageView>(R.id.privieImage).load( it.getString(Workerkeys.FILTER_URL) )


            }

        })

        findViewById<Button>(R.id.downloadimage).setOnClickListener {
            workManager.beginUniqueWork("download",
            ExistingWorkPolicy.KEEP,
            downloadRequest).then(colorFilterRequest).enqueue()
        }

    }

}