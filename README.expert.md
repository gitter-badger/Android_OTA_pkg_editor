# advanced topic

## verify task
verify an ADB connected device against an OTA package.

    $ gradle verifier

This will generate ota\_verifier/build/libs/ota\_verifier-<version>.jar

    $ adb connece <deviceId>
    $ adb root
    $ adb connece <deviceId>
    $ java -jar ota_verifier/build/libs/ota_verifier-<version>.jar <ota.zip>

