package com.example.test.utils

fun<T> className(javaClass: Class<T>?) = javaClass?.name ?: "null"
