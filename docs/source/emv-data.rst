EMV data
========

The emv data is encoded as a TLV (tag-length-value) structure. If you use the code we provided, you will get it in key=value pairs.

Here is the important payment information that you can get from the emv data:

- `HEX_57`: This tag contains the credit card number and the expiration date seperated by a `D` character.
For example, `1234567890123456D1219` means the credit card number is `1234567890123456` and the expiration date is `12/19`

- `HEX_5F20`: This tag contains the cardholder name.
