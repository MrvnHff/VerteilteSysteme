cmake_minimum_required(VERSION 3.8)
project(server)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -pthread")

set(SOURCE_FILES main.c)
add_executable(server ${SOURCE_FILES})