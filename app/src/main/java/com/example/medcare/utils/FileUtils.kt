package com.example.medcare.utils

import android.content.Context
import android.net.Uri
import java.io.File

class FileUtils {
    companion object {
        fun uriToFile(context: Context, uri: Uri): File {
            val inputStream = context.contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("Can't open input stream from URI")
            val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return tempFile
        }
    }
}