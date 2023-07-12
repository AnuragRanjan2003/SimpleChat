package com.example.simplechat.utils

import com.example.simplechat.utils.Resource.Failure
import com.example.simplechat.utils.Resource.Success
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException

object SafeCall {

    suspend fun <T> fireStore(value: T, task: suspend () -> Unit): Resource<T> {
        return try {
            task()
            Success(value)
        } catch (e: FirebaseFirestoreException) {
            Failure(e.message.toString())
        } catch (e: FirebaseException) {
            Failure(e.message.toString())
        } catch (e: Exception) {
            Failure(e.message.toString())
        }
    }

    suspend fun <T> auth(task: suspend () -> T?): Resource<T> {
        return try {
            val res = task()
            if (res != null) Success(res)
            else Failure("something went wrong")
        } catch (e: FirebaseAuthException) {
            Failure(e.message.toString())
        } catch (e: FirebaseException) {
            Failure(e.message.toString())
        } catch (e: Exception) {
            Failure(e.message.toString())
        }
    }
}