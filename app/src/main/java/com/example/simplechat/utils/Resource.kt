package com.example.simplechat.utils

sealed class Resource<T>(private val data: T?, private val error: String?) {
    class Success<T>(val data: T) : Resource<T>(data, null)
    class Failure<T>(val error: String) : Resource<T>(null, error)
}