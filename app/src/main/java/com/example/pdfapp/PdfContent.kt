package com.example.pdfapp

import android.content.Context
import android.net.Uri
import android.util.Log
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class PdfContent {

    suspend fun extractData(context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            var extractedText = ""
            try {
                val contentResolver = context.contentResolver
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val pdfReader = PdfReader(inputStream)
                    val pageCount = pdfReader.numberOfPages

                    for (i in 1..pageCount) {
                        extractedText += PdfTextExtractor.getTextFromPage(pdfReader, i).trim() + "\n"
                    }

                    extractedText = extractedText.trim()
                    pdfReader.close()
                } ?: Log.e("PdfContent", "Failed to open input stream for URI: $uri")
            } catch (e: IOException) {
                Log.e("PdfContent", "IOException occurred: ${e.message}")
            } catch (e: Exception) {
                Log.e("PdfContent", "Exception occurred: ${e.message}")
            }
            extractedText
        }
    }
}
