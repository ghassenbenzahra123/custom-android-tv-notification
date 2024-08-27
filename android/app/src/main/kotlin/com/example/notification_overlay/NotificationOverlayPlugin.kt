package com.example.notification_overlay

import android.content.Context
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
                val message = call.argument<String>("message") ?: ""
                val imageResName = call.argument<String>("imageResName") ?: ""
                val imageResId = context.resources.getIdentifier(imageResName, "drawable", context.packageName)
                notificationOverlay.show(message, imageResId)
                result.success(null)
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
}