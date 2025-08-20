package pt.rvcoding.cvnotes.ui.util

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import pt.rvcoding.cvnotes.R
import pt.rvcoding.cvnotes.domain.model.SectionWithNotes
import pt.rvcoding.cvnotes.domain.model.asString
import java.io.File
import java.io.FileOutputStream


class PdfGenerator(
    val context: Context
) {

    // on below line we are creating a generate PDF method
    // which is use to generate our PDF file.
    fun generatePDF(
        fileName: String,
        sectionsWithNotes: List<SectionWithNotes>
    ) {
        // creating an object variable
        // for our PDF document.
        val pdfDocument: PdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val paint: Paint = Paint()
        val section: Paint = Paint()
        val note: Paint = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
//        canvas.drawBitmap(scaledBmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        section.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        note.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        section.textSize = 30F
        note.textSize = 15F

        // below line is sued for setting color
        // of our text inside our PDF file.
        section.color = ContextCompat.getColor(context, R.color.black)
        note.color = ContextCompat.getColor(context, R.color.black)

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
//        canvas.drawText("Geeks for Geeks", 209F, 50F, title)
//        canvas.drawText("A portal for IT professionals.", 209F, 65F, title)


        val sectionYSpace = 35F
        val noteYSpace = 15F

        val blockInterval = 100
        var block = ""

        var xPos = 56F
        var yPos = 65F
        sectionsWithNotes.onEach { sections ->
            canvas.drawText(sections.section.description, xPos, yPos, section)
            yPos += sectionYSpace
            sections.notes.onEach { noteObj ->
                val text = noteObj.asString()
                if (text.length > blockInterval) {
                    var start = 0
                    var end = blockInterval
                    repeat(text.length / blockInterval + 1) {
                        block = text.substring(start, end).trim()

                        canvas.drawText(block, xPos, yPos, note)
                        yPos += noteYSpace

                        start += blockInterval
                        end =
                            if ((end + blockInterval) < text.length) { end + blockInterval }
                            else { text.length }
                    }
                }
                else { canvas.drawText(text, xPos, yPos, note) }
                yPos += noteYSpace
            }
            yPos += sectionYSpace


        }

//        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
//        title.color = ContextCompat.getColor(context, R.color.purple_200)
//        title.textSize = 15F

        // below line is used for setting
        // our text to center of PDF.
//        title.textAlign = Paint.Align.CENTER
//        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val fileUri: Uri? = generatePdf(fileName, pdfDocument)

        // Open PDF file
        fileUri?.let {
            try {
                openFileWithUri(context, fileUri, "application/pdf")
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(context, "Fail to open PDF file..", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Fail to open PDF file..", Toast.LENGTH_SHORT).show()
            }
        }

        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()
    }

    private fun generatePdf(fileName: String, pdfDocument: PdfDocument): Uri? {
        var fileUri: Uri? = null

        try {
            // Check if we are on Android 10 (Q) or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Modern approach (API 29+)
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.pdf")
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/CVNotes")
                }

                fileUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

                fileUri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                }
            } else {
                // Legacy approach (below API 29)
                val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                val cvNotesDir = File(documentsDir, "CVNotes")
                if (!cvNotesDir.exists()) {
                    cvNotesDir.mkdirs() // Create the directory if it doesn't exist
                }
                val pdfFile = File(cvNotesDir, "$fileName.pdf")
                L.d("PdfGenerator", "PDF file path: ${pdfFile.absolutePath}")

                FileOutputStream(pdfFile).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }

                // Get a content URI using FileProvider to safely share the file
                fileUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider", // Must match your manifest
                    pdfFile
                )
            }
            L.d("PdfGenerator", "PDF file generated: $fileUri")
            Toast.makeText(context, "PDF file generated:\n${fileUri}", Toast.LENGTH_SHORT).show()
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
        }
        return fileUri
    }

    /**
     * Opens a file using an ACTION_VIEW intent.
     *
     * @param context The context needed to start the activity.
     * @param fileUri The content URI of the file to open.
     * @param mimeType The MIME type of the file (e.g., "application/pdf").
     */
    private fun openFileWithUri(context: Context, fileUri: Uri, mimeType: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            // Set the data (the URI) and the type (MIME type)
            setDataAndType(fileUri, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { putExtra(DocumentsContract.EXTRA_INITIAL_URI, fileUri) }

            // Grant temporary read permission to the receiving app
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where the user doesn't have an app to open the file
            Toast.makeText(context, "No app found to open this file.", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        const val pageHeight = 1120
        const val pageWidth = 792
    }
}
