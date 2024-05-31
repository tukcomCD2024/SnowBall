package com.snowball.memetory.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.snowball.memetory.BuildConfig
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream


class S3Util(private val context: Context) {
    private var accessKey = BuildConfig.S3ACCESSKEY // IAM AccessKey
    private var secretKey = BuildConfig.S3SECRETKEY // IAM SecretKey
    private var bucketName = BuildConfig.S3BUCKET
    private var region: Region = Region.getRegion(Regions.AP_NORTHEAST_2)

    fun uploadFile(fileName: String, file: File) {
        val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        val s3Client = AmazonS3Client(awsCredentials, region)

        val transferUtility = TransferUtility.builder()
            .s3Client(s3Client)
            .context(context)
            .build()
        TransferNetworkLossHandler.getInstance(context)

        val uploadObserver = transferUtility.upload(bucketName, fileName, file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    // 업로드가 완료되었을 때의 처리
                    Log.d("S3Util", "업로드 끝")
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = ((current.toDouble() / total) * 100.0).toInt()
                Log.d("S3Util", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("S3Util", "UPLOAD ERROR - - ID: $id - - EX: ${ex.toString()} ")
            }
        })
    }

    suspend fun uploadAudioFile(fileName: String, file: File): Boolean = withContext(Dispatchers.IO) {
        val credentials = BasicAWSCredentials(accessKey, secretKey)
        val s3Client = AmazonS3Client(credentials, region)
        val transferUtility = TransferUtility.builder()
            .s3Client(s3Client)
            .context(context)
            .build()
        val deferred = CompletableDeferred<Boolean>()

        val uploadObserver = transferUtility.upload(bucketName, fileName, file)
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    deferred.complete(true)
                } else if (state == TransferState.FAILED) {
                    deferred.complete(false)
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) { }

            override fun onError(id: Int, ex: Exception) {
                deferred.completeExceptionally(ex)
            }
        })

        deferred.await()
    }

    // 파일을 S3Url로 변환
    fun getFileUrl(fileName: String): String {
        return "https://$bucketName.s3.$region.amazonaws.com/$fileName"
    }
//    fun uploadAudioFile(fileName: String, file: File) {
//        val awsCredentials = BasicAWSCredentials(accessKey, secretKey)
//        val s3Client = AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2))
//        val transferUtility = TransferUtility.builder()
//            .context(context)
//            .s3Client(s3Client)
//            .build()
//
//        val uploadObserver = transferUtility.upload(bucketName, fileName, file)
//
//        uploadObserver.setTransferListener(object : TransferListener {
//            override fun onStateChanged(id: Int, state: TransferState?) {
//                if (state == TransferState.COMPLETED) {
//                    Log.d("S3Util", "Upload completed")
//                }
//            }
//
//            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
//                val percentDone = ((bytesCurrent.toDouble() / bytesTotal) * 100.0).toInt()
//                Log.d("S3Util", "Upload progress: $percentDone%")
//            }
//
//            override fun onError(id: Int, ex: Exception) {
//                Log.e("S3Util", "Upload error: $ex")
//            }
//        })
//    }



    suspend fun uploadImageWithPresignedUrl(file: File, signedUrl: String) = withContext(Dispatchers.IO) {
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(signedUrl)
            .put(requestBody)
            .build()

        try {
            val response = OkHttpClient().newCall(request).execute()

            if (response.isSuccessful) {
                Log.d("S3Upload", "File uploaded successfully: ${file.name}")
            } else {
                val errorBody = response.body?.string() ?: "No error details"
                Log.e("S3Upload", "Failed to upload file: ${file.name}, Response: $errorBody")
                throw IOException("Failed to upload file: ${file.name}, Response: $errorBody")
            }
        }catch (e:Exception){
            Log.e("S3Upload", "File upload failed: ${file.name}", e)

        }

    }

}