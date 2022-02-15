project(ocrlibrary)

cmake_minimum_required(VERSION 3.4.1)

set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-mobile-4.5.4-android/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc)

set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn-20211208-android-vulkan/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(ocrlibrary SHARED ocr.cpp common.cpp clipper.cpp)

target_link_libraries(ocrlibrary ncnn ${OpenCV_LIBS} jnigraphics)
