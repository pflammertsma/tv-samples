load(
    "//third_party/java/android/android_sdk_linux/extras/android/compatibility/jetify:jetify.bzl",
    "jetify_android_library",
    "kt_jetify_android_library",
)
load("//third_party/android/androidx_test/build_extensions:kt_android_library.bzl", "kt_android_library")
load("//devtools/blaze/android:generate_build_config.bzl", "generate_build_config")
load("//tools/build_defs/android:rules.bzl", "android_binary")

package(default_visibility = ["//visibility:private"])

generate_build_config(
    name = "build_config",
    custom_package = "com.example.android.tvleanback",
)

kt_jetify_android_library(
    name = "app_lib",
    srcs = glob([
        "src/main/java/**/*.java",
        "src/main/java/**/*.kt",
    ]),
    custom_package = "com.example.android.tvleanback",
    jetify_sources = True,
    manifest = "src/main/AndroidManifest.xml",
    resource_files = glob(["src/main/res/**"]),
    deps = [
        ":build_config",
        "//third_party/java/android_libs/glide",
        "//third_party/java/android_libs/media:common",
        "//third_party/java/android_libs/media:exoplayer",
        "//third_party/java/android_libs/media:ui",
        "//third_party/java/android_libs/media:ui_compose",
        "//third_party/java/android_libs/media:ui_leanback",
        "//third_party/java/androidx/activity/compose",
        "//third_party/java/androidx/annotation",
        "//third_party/java/androidx/compose/foundation",
        "//third_party/java/androidx/compose/foundation/layout",
        "//third_party/java/androidx/compose/runtime",
        "//third_party/java/androidx/compose/ui",
        "//third_party/java/androidx/core",
        "//third_party/java/androidx/core/hack_for_licenses:core",
        "//third_party/java/androidx/fragment",
        "//third_party/java/androidx/leanback",
        "//third_party/java/androidx/leanback/preference",
        "//third_party/java/androidx/loader",
        "//third_party/java/androidx/navigation/compose",
        "//third_party/java/androidx/preference",
        "//third_party/java/androidx/recommendation",
        "//third_party/java/androidx/recyclerview",
        "//third_party/java/androidx/tv/foundation",
        "//third_party/java/androidx/tv/material",
        "//third_party/java/coil:compose_base",
        "//third_party/java/coil:compose_singleton",
        "//third_party/java/coil:singleton",
    ],
)

android_binary(
    name = "app",
    custom_package = "com.example.android.tvleanback",
    manifest = "src/main/AndroidManifest.xml",
    manifest_values = {
        "applicationId": "com.example.android.tvleanback",
        "minSdkVersion": "21",
        "packageName": "com.example.android.tvleanback",
        "targetSdkVersion": "34",
    },
    deps = [
        ":app_lib",
    ],
)
