# Android_OTA_pkg_editor
An convenient command line tool to manipulate Android OTA(Over-The-Air) update packages. Current features include unpack/repack/resign, etc.

[![Build Status](https://travis-ci.org/cfig/Android_OTA_pkg_editor.svg?branch=master)](https://travis-ci.org/cfig/Android_OTA_pkg_editor)

## Compatible Android versions

This tool is known to work for Nexus (or Nexus compatible) OTA package for the following Android releases:
 - Oreo
 - Nougat
 - Marshmallow (API Level 23)
 - Lollipop (API Level 21,22)
 - Kitkat (API Level 19)

You can get a full [Android version list](https://source.android.com/source/build-numbers.html) here.

## usage

copy your Android OTA package to current directory:

    cp <your_package> signed.zip

extract it:

    ./gradlew unpack

modify it according to your needs, then repack and sign it with default Android keys:

    ./gradlew pack

You will get a modified **signed.zip**.

## advanced topic
Please read [advanced topic guide](https://github.com/cfig/Android_OTA_pkg_editor/blob/master/README.expert.md).
