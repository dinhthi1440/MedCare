package com.example.medcare.utils

import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.example.medcare.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object S3UploaderUtils {

    suspend fun uploadFileToS3(folder: String, file: File): String? = withContext(Dispatchers.IO) {
        try {
            val accessKey = BuildConfig.AWS_ACCESS_KEY
            val secretKey = BuildConfig.AWS_SECRET_KEY
            val bucketName = BuildConfig.AWS_BUCKET_NAME
            val region = BuildConfig.AWS_REGION

            val credentials = BasicAWSCredentials(accessKey, secretKey)
            val s3Client = AmazonS3Client(credentials, Region.getRegion(region))

            val key = "$folder/${file.name}"
            s3Client.putObject(bucketName, key, file)

            return@withContext s3Client.getResourceUrl(bucketName, key)
        } catch (e: Exception) {
            Log.e("S3Uploader", "Upload failed", e)
            return@withContext null
        }
    }
}
enum class FolderS3(val folderName: String) {
    MEDICINES("medicines"),
    USERS("users")
}