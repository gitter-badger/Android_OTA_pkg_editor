# advanced topic

## verify task - quick start
verify an ADB connected device against an OTA package.

    $ gradle verify

## verify task details

    $ gradle verifier

This will generate ota\_verifier/build/libs/ota\_verifier-<version>.jar

    $ adb connece <deviceId>
    $ adb root
    $ adb connece <deviceId>
    $ java -jar ota_verifier/build/libs/ota_verifier-<version>.jar <ota.zip>

The "gradle verify" task does both the build and "java -jar" actions.
