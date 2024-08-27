package com.example.notification_overlay

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


class NotificationOverlayPlugin: FlutterPlugin, MethodCallHandler {
    private lateinit var channel : MethodChannel
    private lateinit var context: Context
    private lateinit var notificationOverlay: NotificationOverlay

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "notification_overlay")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
        notificationOverlay = NotificationOverlay(context)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "showNotification" -> {
                if (checkOverlayPermission()) {
                    val message = call.argument<String>("message") ?: ""
                    val imageResName = call.argument<String>("imageResName") ?: ""
                    val imageResId = context.resources.getIdentifier(imageResName, "drawable", context.packageName)
                    notificationOverlay.show(message, imageResId)
                    result.success(null)
                } else {
                    requestOverlayPermission()
                    result.error("PERMISSION_DENIED", "Overlay permission not granted", null)
                }
            }
            "hideNotification" -> {
                notificationOverlay.hide()
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun checkOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}