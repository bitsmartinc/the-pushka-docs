import 'dart:async';
import 'package:flutter/services.dart';

class ScanCard {
  static const platform = MethodChannel('com.example.pushka_donation/payment');
  static const event = EventChannel('com.example.pushka_donation/card_info');

  static String? paymentData;
  static CardData? cardData;

  static StreamSubscription<dynamic>? subscription;

  static Future<void> cardRead() async {
    paymentData = null;
    cardData = null;

    try {
      String cnlRes = await platform.invokeMethod('startTransaction');
      if (cnlRes.startsWith('err')) {
        paymentData = 'error';
      } else {
        subscription = event.receiveBroadcastStream().listen((value) {
          paymentData = value;
          cardData = parseScanData(value);
          subscription?.cancel();
        });
      }
    } catch (e) {
      // Handle exceptions thrown by invokeMethod here
      paymentData = 'error';
      subscription?.cancel();
    } finally {
      // Cancel the stream subscription when done
    }
  }

  static void cancel() {
    paymentData = null;
    subscription?.cancel();
  }

  static CardData parseScanData(String scanData) {
    return CardData.fromMap(Uri.splitQueryString(scanData));
  }
}

// this is a class for the card data
class CardData {
  final String? code;
  final String? codeText;
  final String? _isSuccess;

  CardData(this.code, this.codeText, this._isSuccess);
  bool get isSuccess {
    return _isSuccess == 'true';
  }

  static CardData fromMap(Map<String, String?> map) {
    return CardData(map['code'], map['codeText'], map['isSuccess']);
  }
}
