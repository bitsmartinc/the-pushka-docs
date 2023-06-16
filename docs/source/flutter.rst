Flutter
=======

This project takes advantage of the Channel API provided by the [Flutter](https://flutter.io) framework to create a simple chat application.

Read more about the Channel API [here](https://flutter.io/platform-channels/). And read more about the Flutter streamChannel class [here](https://api.flutter.dev/flutter/package-stream_channel_stream_channel/StreamChannel-class.html).



## Getting Started
In your flutter application you register a method and an event Channel, like the following

```dart
    static const platform = MethodChannel('com.example.pushka_donation/payment');
    static const event = EventChannel('com.example.pushka_donation/card_info');
```


## Starting the scan process
```dart
    Future<void> _startScan() async {
        try {
            String result = await platform.invokeMethod('startTransaction');
            // do something with the result, if it starts with "err" then something went wrong
        } on PlatformException catch (e) {
            print(e.message);
        }
    }
```


## Listening for the card info
```dart
    StreamSubscription _subscription;
    void _listenForCardInfo() {
        _subscription = event.receiveBroadcastStream().listen((data) {
            // do something with the data
        }, onError: (error) {
            print(error);
        });
    }
```
