buildscript {
    val compose_ui_version by extra("1.1.1")
}
plugins {
    id ("com.android.application") version "7.4.1" apply false
    id ("com.android.library") version "7.4.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.6.10" apply false

    //dagger
    id("com.google.dagger.hilt.android") version "2.44" apply false
}