Flutter
=======

This project takes advantage of the Channel API provided by the `Flutter <https://flutter.io>`_ framework to create a simple chat application.


Getting Started
---------------


In your flutter application you should have a service dedicated for reading credit card.


Register Method & Event Channels
--------------------------------

Register a method and an event Channel, like the following:

.. code-block:: dart

    static const platform = MethodChannel('com.example.Brodpay_donation/payment');
    static const event = EventChannel('com.example.Brodpay_donation/card_info');



Loading Configuration
---------------------

When the application is installed, it will have to make sure that the configuration is loaded.
For that we use the following method:

..  code-block:: dart

    Future<void> _loadConfig() async {
        try {
            String result = await platform.invokeMethod('loadConfig');
            // do something with the result, if it starts with "err" then something went wrong
        } on PlatformException catch (e) {
            print(e.message);
        }
    }

You do not have to load the config every time the app is installed, so you can store a local variable to check if the config is loaded or not.


Starting the scan process
-------------------------

When you want to scan for a credit card in your application, you can use the following method:

.. code-block:: dart

   Future<void> _startScan() async {
       try {
           String result = await platform.invokeMethod('startTransaction');
           // do something with the result, if it starts with "err" then something went wrong
       } on PlatformException catch (e) {
           print(e.message);
       }
   }

This method will start the scan process.


Listening for the card info
---------------------------

Once the scan process is started, the service will start listening for the card info. To listen for the card info, you can use the following method:

..  code-block:: dart
    StreamSubscription _subscription;
    void _listenForCardInfo() {
        _subscription = event.receiveBroadcastStream().listen((data) {
            // do something with the data
        }, onError: (error) {
            print(error);
        });
    }


To see the full code example for the service, check out the example file in this repository `here <https://github.com/bitsmartinc/the-pushka-docs/blob/main/src/flutter-service.dart>`_


Reading the EMV data
--------------------

In the above example, the data received from the event channel is a string, in the format of key-value pairs.

See :docs:`emv-data` for more information on how to read the EMV data.



Read more about the Channel API `Here <https://flutter.io/platform-channels/>`_. And read more about the Flutter streamChannel class `here <https://api.flutter.dev/flutter/package-stream_channel_stream_channel/StreamChannel-class.html>`_.