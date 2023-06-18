Flutter
=======

This project takes advantage of the Channel API provided by the `Flutter <https://flutter.io>`_ framework to create a simple chat application.

Read more about the Channel API `Here <https://flutter.io/platform-channels/>`_. And read more about the Flutter streamChannel class `here <https://api.flutter.dev/flutter/package-stream_channel_stream_channel/StreamChannel-class.html>`_.



Getting Started
---------------
In your flutter application you should have a service dedicated for reading credit card.


Register Method & Event Channels
--------------------------------

Register a method and an event Channel, like the following:
.. code-block:: dart
    static const platform = MethodChannel('com.example.Brodpay_donation/payment');
    static const event = EventChannel('com.example.Brodpay_donation/card_info');



Starting the scan process
-------------------------
.. code-block:: dart
    Future<void> _startScan() async {
        try {
            String result = await platform.invokeMethod('startTransaction');
            // do something with the result, if it starts with "err" then something went wrong
        } on PlatformException catch (e) {
            print(e.message);
        }
    }



Listening for the card info
---------------------------
.. code-block:: dart
    StreamSubscription _subscription;
    void _listenForCardInfo() {
        _subscription = event.receiveBroadcastStream().listen((data) {
            // do something with the data
        }, onError: (error) {
            print(error);
        });
    }


To see the full code example for the service, check out the example file in this repository `here <https://github.com/bitsmartinc/the-pushka-docs/blob/main/src/flutter-service.dart>`_