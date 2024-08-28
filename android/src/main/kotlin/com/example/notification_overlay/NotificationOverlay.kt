package com.example.notification_overlay

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.os.Handler
import android.os.Looper

class NotificationOverlay(private val context: Context) {

      private val windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private lateinit var layout: View
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView
    private val handler = Handler(Looper.getMainLooper())

    fun show(message: String, imageResId: Int) {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 0
            y = 100
        }

        layout = createNotificationLayout()
        textView.text = message
        imageView.setImageResource(R.drawable.mawaqitLogo)
        windowManager.addView(layout, params)

        // Schedule the notification to disappear after 10 seconds
        handler.postDelayed({
            hide()
        }, 10000) // 10000 milliseconds = 10 seconds
    }

    private fun createNotificationLayout(): View {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(Color.parseColor("#80000000"))
            setPadding(16, 16, 16, 16)

            imageView = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(144, 144).apply {
                    marginEnd = 16
                }
            }
            addView(imageView)

            textView = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setTextColor(Color.WHITE)
                textSize = 16f
            }
            addView(textView)
        }
    }

    fun hide() {
        if (::layout.isInitialized) {
            handler.removeCallbacksAndMessages(null) // Remove any pending hide operations
            windowManager.removeView(layout)
        }
    }
}