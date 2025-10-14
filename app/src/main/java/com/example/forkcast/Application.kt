package com.example.forkcast

import android.app.Application
import android.widget.Toast
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
                val timestamp = sdf.format(Date())

                // Use app's external files dir (safe, no permissions needed)
                val folder = getExternalFilesDir("CrashLogs")
                folder?.mkdirs()
                val file = File(folder, "crash_log.txt")

                FileWriter(file, true).use { fw ->
                    PrintWriter(fw).use { pw ->
                        pw.println("===== Crash at $timestamp =====")
                        throwable.printStackTrace(pw)
                        pw.println()
                        pw.flush()
                    }
                }

                Toast.makeText(this, "App crashed! Log saved in CrashLogs folder.", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                // ignore
            }

            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }
}
