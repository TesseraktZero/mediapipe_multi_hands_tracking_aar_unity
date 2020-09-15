This is a sub-project for building of the apk used in [UnityHandTrackingWithMediapipe](https://github.com/TesseraktZero/UnityHandTrackingWithMediapipe). 
It is a fork of [MediaPipe AAR Example Project](https://github.com/jiuqiant/mediapipe_multi_hands_tracking_aar_example), with extra logic on unity data transfer.

The steps to build and use MediaPipe AAR is documented in MediaPipe's [android_archive_library.md](https://google.github.io/mediapipe/getting_started/android_archive_library.html).
The source code is copied from MediaPipe's [multi-hand tracking gpu demo](https://github.com/google/mediapipe/tree/master/mediapipe/examples/android/src/java/com/google/mediapipe/apps/multihandtrackinggpu).

A built .aar file is included in release. Please place the file to `mediapipe_multi_hands_tracking_aar_unity/app/libs`.

If you want to build the aar file on your own, please [follow the changes to include handedness output](https://github.com/cm-yamamoto-hiroki/mediapipe/compare/ffd3821f2bcfcee7c84c8f503f6928f2307f4661...6cd3d8a0356c63d65c98118c2962e6669b2cde4b) (credits: Hiroki YAMAMOTO).
