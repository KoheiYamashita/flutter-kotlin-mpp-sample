package info.kitproject.flutterkotlin.common

expect class Counter {
    suspend fun load(): Long
    suspend fun increment(): Long
}
