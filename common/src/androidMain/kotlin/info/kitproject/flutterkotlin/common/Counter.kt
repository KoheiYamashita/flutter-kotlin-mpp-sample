package info.kitproject.flutterkotlin.common

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class Counter {
    private var count = 0L
    private var database = FirebaseDatabase.getInstance().reference.child("count")
    actual suspend fun load(): Long {
        return GlobalScope.async {
            suspendCoroutine<Long> { continuation ->
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        count = if (p0.value != null) {
                            p0.value as Long
                        } else {
                            0
                        }
                        database.setValue(count)
                        continuation.resume(count)
                    }
                })
            }
        }.await()
    }

    actual suspend fun increment(): Long {
        database.setValue(++count)
        return GlobalScope.async {
            suspendCoroutine<Long> { continuation ->
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        count = p0.value as Long
                        continuation.resume(count)
                    }
                })
            }
        }.await()
    }
}
