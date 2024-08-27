export 'notification_overlay.dart';
import 'package:flutter/services.dart';

class NotificationOverlay {
  static const MethodChannel _channel = MethodChannel('notification_overlay');

  static Future<void> showNotification(
      String message, String imageResName) async {
    try {
      await _channel.invokeMethod('showNotification', {
        'message': message,
        'imageResName': imageResName,
      });
    } catch (e) {
      print('Error showing notification: $e');
    }
  }

  static Future<void> hideNotification() async {
    try {
      await _channel.invokeMethod('hideNotification');
    } catch (e) {
      print('Error hiding notification: $e');
    }
  }
}
