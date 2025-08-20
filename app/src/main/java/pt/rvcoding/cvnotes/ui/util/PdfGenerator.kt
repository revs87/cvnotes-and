package pt.rvcoding.cvnotes.ui.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import pt.rvcoding.cvnotes.domain.model.SectionWithNotes
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.ui.util.component.SectionListCard
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume


class PdfGenerator(
    val context: Context
) {
    suspend fun generatePDF(
        fileName: String,
        sectionsWithNotes: List<SectionWithNotes>,
        pageWidthInPixels: Int
    ) {
        withContext(Dispatchers.IO) { // Perform heavy work on a background thread
            val pdfDocument = PdfDocument()
            try {
                // Define A4 aspect ratio
                val a4AspectRatio = 1.415f
                val pageHeightInPixels = (pageWidthInPixels * a4AspectRatio).toInt()

                // Use the dynamic dimensions to create the page info
                val pageInfo = PdfDocument.PageInfo
                    .Builder(
                        /* pageWidth = */ pageWidthInPixels,
                        /* pageHeight = */ pageHeightInPixels,
                        /* pageNumber = */ 1
                    )
                    .create()

                // Start the first page
                var currentPage = pdfDocument.startPage(pageInfo)
                var canvas = currentPage.canvas
                var yPosition = 20f // Starting Y position with some margin

                sectionsWithNotes.onEachIndexed { index, sectionWithNotes ->
                    val bitmap = withContext(Dispatchers.Main) { // Capturing must be on the main thread
                        captureComposableAsBitmap(context) {
                            MyTheme {
                                SectionListCard(
                                    modifier = Modifier,
                                    index = index,
                                    description = sectionWithNotes.section.description,
                                    hasSelected = false,
                                    isSelected = sectionWithNotes.section.isSelected,
                                    colorId = sectionWithNotes.section.colorId,
                                    notes = sectionWithNotes.notes
                                )
                            }
                        }
                    }

                    // Check if there is enough space on the current page
                    if (yPosition + bitmap.height > canvas.height) {
                        // Not enough space, finish this page and start a new one
                        pdfDocument.finishPage(currentPage)
                        currentPage = pdfDocument.startPage(pageInfo)
                        canvas = currentPage.canvas
                        yPosition = 20f // Reset Y position
                    }

                    // Draw the bitmap on the canvas
                    canvas.drawBitmap(bitmap, 0f, yPosition, null)
                    yPosition += bitmap.height
                    bitmap.recycle() // Recycle bitmap to save memory
                }

                // Finish the last page
                pdfDocument.finishPage(currentPage)

                // Write PDF file
                val fileUri: Uri? = generatePdf(fileName, pdfDocument)

                // Close PDF document
                pdfDocument.close()

                // Open PDF file
                fileUri?.let {
                    openFileWithUri(context, fileUri, "application/pdf")
                }
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Fail to open PDF file..", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Fail to open PDF file..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Calculates the path of a file and returns its URI and File.
     *
     * @param fileName The file name to use.
     * @return fileUri The content URI of the file to be written.
     * @return file The File object representing the file.
     */
    private suspend fun getUri(fileName: String): Pair<Uri?, File?> {
        var fileUri: Uri? = null
        var file: File? = null

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
            } else {
                // Legacy approach (below API 29)
                val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                val cvNotesDir = File(documentsDir, "CVNotes")
                if (!cvNotesDir.exists()) {
                    cvNotesDir.mkdirs() // Create the directory if it doesn't exist
                }
                file = File(cvNotesDir, "$fileName.pdf")
                L.d(TAG, "PDF file path: ${file.absolutePath}")

                // Get a content URI using FileProvider to safely share the file
                fileUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider", // Must match your manifest
                    file
                )
            }
            L.d(TAG, "PDF file generated: $fileUri")

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PDF file generated:\n${fileUri}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
            }
        }
        return Pair(fileUri, file)
    }

    /**
     * Captures a Composable by temporarily attaching it to the Activity's window.
     *
     * @param context The context, which must be from an Activity.
     * @param content The @Composable function to capture.
     * @return A Bitmap of the rendered composable.
     */
    suspend fun captureComposableAsBitmap(
        context: Context,
        content: @Composable () -> Unit
    ): Bitmap = withContext(Dispatchers.Main) { // All view operations must be on the Main thread
        // 1. Find the root view of the Activity
        val activity = context.findActivity()
        val rootView = activity.window.decorView.rootView as ViewGroup

        // 2. Create the ComposeView to be captured
        val composeView = ComposeView(context).apply {
            // Set the content of the view
            setContent(content)

            // Make it completely invisible
            alpha = 0f
        }

        // 3. Add the view to the screen and wait for it to be laid out
        val bitmap = suspendCancellableCoroutine<Bitmap> { continuation ->
            val onLayoutListener = View.OnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
                // This is called when the view has been measured and laid out
                val bitmap = createBitmap(view.width, view.height)
                val canvas = Canvas(bitmap)
                view.draw(canvas)
                continuation.resume(bitmap)
            }
            composeView.addOnLayoutChangeListener(onLayoutListener)
            rootView.addView(
                composeView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }

        // 4. Clean up by removing the view from the screen
        rootView.removeView(composeView)

        return@withContext bitmap
    }


    // Helper function to find the Activity from a Context
    private fun Context.findActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("You need to use an Activity context to capture a Composable")
    }

    /**
     * Generates a file and returns its URI.
     *
     * @param fileName The file name to use.
     * @param pdfDocument The PDF document to write to the file.
     * @return fileUri The content URI of the file to open.
     */
    private suspend fun generatePdf(fileName: String, pdfDocument: PdfDocument): Uri? {
        val (fileUri, file) = getUri(fileName)

        try {
            fileUri?.let {
                // Check if we are on Android 10 (Q) or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Modern approach (API 29+)
                    context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    L.d(TAG, "PDF file generated: ${fileUri.path}")
                } else {
                    // Legacy approach (below API 29)
                    FileOutputStream(file).use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                    }
                    L.d(TAG, "PDF file generated: ${file?.absolutePath}")
                }
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "PDF file generated:\n${fileUri}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
            }
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
    private suspend fun openFileWithUri(context: Context, fileUri: Uri, mimeType: String) {
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
            withContext(Dispatchers.Main) {
                // Handle the case where the user doesn't have an app to open the file
                Toast.makeText(context, "No app found to open this file.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        const val TAG = "PdfGenerator"
    }
}
