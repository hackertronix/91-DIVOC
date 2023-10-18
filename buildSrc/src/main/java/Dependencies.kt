import Versions.koin_version
import Versions.kotlin_version
import Versions.lifecycle_version
import Versions.retrofit_version
import Versions.room_version
import Versions.work_version

object Versions {
  const val work_version = "2.3.4"
  const val lifecycle_version = "2.2.0"
  const val kotlin_version = "1.3.70"
  const val retrofit_version = "2.7.2"
  const val arch_version = "2.1.0"
  const val room_version = "2.2.5"
  const val koin_version= "2.1.4"
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
  const val workRx = "android.arch.work:work-rxjava2:$work_version"
  const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
  const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
  const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
  const val material = "com.google.android.material:material:1.1.0"
  const val recyclerView = "androidx.recyclerview:recyclerview:1.3.2"
  const val anrWatchDog = "com.github.anrwatchdog:anrwatchdog:1.4.0"
  const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
  const val mpAndroidCharts = "com.github.PhilJay:MPAndroidChart:3.1.0"
  const val room = "androidx.room:room-runtime:$room_version"
  const val roomRx = "androidx.room:room-rxjava2:$room_version"
  const val roomKtx = "androidx.room:room-ktx:$room_version"
  const val roomCompiler = "androidx.room:room-compiler:$room_version"
  const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.0"
  const val rx2 = "io.reactivex.rxjava2:rxjava:2.2.21"
  const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.2.0"
  const val koinViewModel = "org.koin:koin-androidx-viewmodel:$koin_version"
  const val koinAndroid = "org.koin:koin-android:$koin_version"
  const val koinCore = "org.koin:koin-core:$koin_version"
  const val jodaTime = "joda-time:joda-time:2.10.14"
  const val moshi = "com.squareup.moshi:moshi-kotlin:1.9.2"
  const val moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:1.9.2"
  const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:$retrofit_version"
  const val retrofitGson = "com.squareup.retrofit2:converter-gson:$retrofit_version"
  const val retrofitRx = "com.squareup.retrofit2:adapter-rxjava2:$retrofit_version"
  const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4"
  const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:2.1.1"
}