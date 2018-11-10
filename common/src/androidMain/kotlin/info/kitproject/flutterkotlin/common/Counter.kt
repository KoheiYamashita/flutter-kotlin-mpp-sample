package info.kitproject.flutterkotlin.common

import android.content.ContentValues
import android.content.Context

actual class Counter(context: Context) {
    private var count = 0
    private val database = DatabaseOpenHelper(context).writableDatabase

    actual fun load(): Int {
        database.query(
                DatabaseOpenHelper.TABLE_NAME,
                arrayOf(DatabaseOpenHelper.COLUMN_NAME_COUNT),
                null, null, null, null, null
        ).use { cursor ->
            if (cursor.count == 0) {
                val contentValues = ContentValues()
                contentValues.put(DatabaseOpenHelper.COLUMN_NAME_COUNT, 0)
                database.insert(DatabaseOpenHelper.TABLE_NAME,
                        DatabaseOpenHelper.COLUMN_NAME_COUNT + " = ?", contentValues)
                return 0
            }
            cursor.moveToFirst()
            count = cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME_COUNT))
        }
        return count
    }

    actual fun increment(): Int {
        val contentValues = ContentValues()
        contentValues.put(DatabaseOpenHelper.COLUMN_NAME_COUNT, ++count)
        if (database.update(DatabaseOpenHelper.TABLE_NAME, contentValues, null, null) == 0) {
            return --count
        }
        return count
    }
}
