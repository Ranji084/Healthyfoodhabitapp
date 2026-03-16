package com.simats.Healthyfoodhabitapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.OutputStream

object PdfHelper {

    fun generateAndSaveWellnessPdf(
        context: Context,
        username: String,
        email: String,
        water: String,
        sleep: String,
        score: String
    ): Boolean {
        val pdfDocument = PdfDocument()
        // Use A4 size (595 x 842 points)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        val waterIntake = water.toIntOrNull() ?: 0
        val sleepHours = sleep.toDoubleOrNull() ?: 0.0

        // Background
        canvas.drawColor(Color.WHITE)

        // Header Title
        paint.color = Color.parseColor("#1B5E20") // Dark Green
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("HEALTHY HABIT APP - WELLNESS REPORT", 50f, 60f, paint)

        // Decorative Line
        paint.strokeWidth = 2f
        canvas.drawLine(50f, 80f, 545f, 80f, paint)

        // User Info Section
        paint.color = Color.BLACK
        paint.isFakeBoldText = false
        paint.textSize = 14f
        
        var currentY = 120f
        val lineSpacing = 30f

        paint.isFakeBoldText = true
        canvas.drawText("User Details:", 50f, currentY, paint)
        paint.isFakeBoldText = false
        currentY += lineSpacing
        canvas.drawText("Name: $username", 60f, currentY, paint)
        currentY += lineSpacing
        canvas.drawText("Email: $email", 60f, currentY, paint)
        currentY += lineSpacing
        canvas.drawText("Date: ${java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())}", 60f, currentY, paint)
        
        currentY += lineSpacing * 1.5f

        // Pie Chart Section (Donut style)
        val scoreValue = score.toFloatOrNull() ?: 0f
        paint.isFakeBoldText = true
        paint.textSize = 18f
        canvas.drawText("Overall Wellness Score:", 50f, currentY, paint)
        
        currentY += 40f
        val chartSize = 180f
        val chartRect = RectF(
            (595f - chartSize) / 2f, 
            currentY, 
            (595f + chartSize) / 2f, 
            currentY + chartSize
        )
        
        // Background Circle (Light Gray)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.color = Color.parseColor("#EEEEEE")
        canvas.drawArc(chartRect, 0f, 360f, false, paint)
        
        // Score Arc (Green)
        paint.color = Color.parseColor("#4CAF50")
        val sweepAngle = (scoreValue / 100f) * 360f
        canvas.drawArc(chartRect, -90f, sweepAngle, false, paint)
        
        // Score Text inside Donut
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 36f
        paint.isFakeBoldText = true
        canvas.drawText("$score%", 595f / 2f, currentY + (chartSize / 2f) + 12f, paint)
        
        currentY += chartSize + 60f
        paint.textAlign = Paint.Align.LEFT // Reset alignment

        // Analysis Breakdown Section
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Analysis Breakdown:", 50f, currentY, paint)
        
        paint.isFakeBoldText = false
        paint.textSize = 16f
        currentY += lineSpacing + 10f

        canvas.drawCircle(60f, currentY - 5f, 4f, paint) // Bullet point
        canvas.drawText("Water Intake: $water glasses", 75f, currentY, paint)
        currentY += lineSpacing
        
        canvas.drawCircle(60f, currentY - 5f, 4f, paint) // Bullet point
        canvas.drawText("Sleep Duration: $sleep hours", 75f, currentY, paint)
        
        currentY += lineSpacing * 1.5f

        // AI Recommendations / Alerts Section
        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText("Smart Recommendations:", 50f, currentY, paint)
        
        paint.textSize = 14f
        paint.isFakeBoldText = false
        currentY += lineSpacing + 5f

        // Dynamic Recommendations based on stats
        if (waterIntake < 8) {
            paint.color = Color.parseColor("#EF5350") // Red-ish for warning
            canvas.drawText("• WARNING: Dehydration alert! Aim for at least 8 glasses daily.", 60f, currentY, paint)
            currentY += 25f
            paint.color = Color.BLACK
            canvas.drawText("  Recommendation: Drink one glass every 2 hours.", 60f, currentY, paint)
            currentY += lineSpacing
        } else {
            paint.color = Color.parseColor("#4CAF50") // Green for good
            canvas.drawText("• Excellent hydration! Keep maintaining this level.", 60f, currentY, paint)
            currentY += lineSpacing
        }

        if (sleepHours < 7.5) {
            paint.color = Color.parseColor("#FBC02D") // Amber for warning
            canvas.drawText("• ALERT: Sleep duration is below ideal (7.5h - 9h).", 60f, currentY, paint)
            currentY += 25f
            paint.color = Color.BLACK
            canvas.drawText("  Recommendation: Try going to bed 30 mins earlier tonight.", 60f, currentY, paint)
            currentY += lineSpacing
        } else {
            paint.color = Color.parseColor("#4CAF50")
            canvas.drawText("• Great rest! Your sleep duration is perfect for recovery.", 60f, currentY, paint)
            currentY += lineSpacing
        }

        // Static Professional Tips
        paint.color = Color.BLACK
        canvas.drawText("• Boost: Practice 5 mins of deep breathing to improve oxygen flow.", 60f, currentY, paint)
        currentY += lineSpacing
        canvas.drawText("• Activity: Take a 10-minute walk after your next meal.", 60f, currentY, paint)

        pdfDocument.finishPage(page)

        // SECOND PAGE for Footer/Disclaimer to avoid overlapping
        val pageInfo2 = PdfDocument.PageInfo.Builder(595, 842, 2).create()
        val page2 = pdfDocument.startPage(pageInfo2)
        val canvas2 = page2.canvas
        
        paint.textSize = 12f
        paint.color = Color.GRAY
        paint.isFakeBoldText = false
        canvas2.drawText("This report is generated based on your wellness checkup inputs.", 50f, 50f, paint)
        canvas2.drawText("Stay Healthy! Team Healthy Habit.", 50f, 70f, paint)
        
        pdfDocument.finishPage(page2)

        // Sanitize filename
        val safeUsername = username.replace(Regex("[^a-zA-Z0-9]"), "_")
        val timestamp = System.currentTimeMillis()
        val fileName = "WellnessReport_${safeUsername}_$timestamp.pdf"
        
        var success = false
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    resolver.openOutputStream(it)?.use { os ->
                        pdfDocument.writeTo(os)
                        os.flush()
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                    resolver.update(it, contentValues, null, null)
                    Log.d("PdfHelper", "Saved to Downloads: $fileName")
                    success = true
                }
            } else {
                val targetDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!targetDir.exists()) targetDir.mkdirs()
                val targetFile = java.io.File(targetDir, fileName)
                targetFile.outputStream().use { os ->
                    pdfDocument.writeTo(os)
                }
                success = true
            }
        } catch (e: Exception) {
            Log.e("PdfHelper", "Error saving PDF", e)
        } finally {
            pdfDocument.close()
        }
        return success
    }
}
