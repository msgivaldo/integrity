cmake_minimum_required(VERSION 3.10.2)

project("integrity")

add_library(
        root-detection
        SHARED
        root_detection.c)

add_library(
        virtualization-detection
        SHARED
        virtualization_detection.c)

find_library(
        log-lib
        log )

target_link_libraries(
        root-detection
        ${log-lib} )

target_link_libraries(
        virtualization-detection
        ${log-lib} )