# scottish-power-coding-challenge
A sample Android app which pulls a list of albums from an API and sorts them by title.

To run this project you should be using the latest version of Android Studio, Gradle and the Android Gradle Plugin.

Future Improvements:

1. Store albums locally on the device using Room. Perhaps have some sort of UI element that allows users to select which albums they'd like to download.
2. Identify when a user isn't connected to the internet and have the UI notify them e.g. if they're on airplane mode.
3. Instead of simply using the ViewBinding library on the AlbumsListFragment I'd switch to DataBinding to remove some of the logic from the fragment class
4. Utilise Jetpack Compose. Considered it for this project but due to time constraints I opted to "stick to what I know"
