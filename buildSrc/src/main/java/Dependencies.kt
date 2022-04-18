import Versions.kotlin_version
import Versions.lifecycle_version
import Versions.work_version

object Versions {
  const val work_version = "2.3.4"
  const val lifecycle_version = "2.2.0"
  const val kotlin_version = "1.3.70"
}

object Dependencies {
  const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
  const val appCompat = "androidx.appcompat:appcompat:1.1.0"
  const val coreKtx = "androidx.core:core-ktx:1.2.0"
  const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
  const val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
  const val jUnit = "junit:junit:4.12"
  const val androidTestJunit = "androidx.test.ext:junit:1.1.1"
  const val androidTestEspresso = "androidx.test.espresso:espresso-core:3.4.0"
  const val workRuntime = "androidx.work:work-runtime-ktx:$work_version"
  const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
  const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
  const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
  const val material = "com.google.android.material:material:1.1.0"
  const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
  const val anrWatchDog = "com.github.anrwatchdog:anrwatchdog:1.4.0"
  const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
  const val mpAndroidCharts = "com.github.PhilJay:MPAndroidChart:3.1.0"
}