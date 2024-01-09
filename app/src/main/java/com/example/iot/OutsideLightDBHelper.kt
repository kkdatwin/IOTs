package com.example.iot

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OutsideLightDBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "outside_light.db"
        private const val TBL_LIGHT = "tbl_light"
        private const val LIGHT_ID = "_id"
        private const val ROOM = "room"
        private const val TYPE = "type"
    }


    private val createTBlInsidelight =
        "CREATE TABLE  $TBL_LIGHT ($LIGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT, $ROOM TEXT, $TYPE TEXT)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTBlInsidelight)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_LIGHT")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun addOutsideLights(room: String, type: String) {
        val db = this.writableDatabase

        // Find the first available ID
        var id = 1
        val cursor = db.rawQuery("SELECT $LIGHT_ID FROM $TBL_LIGHT ORDER BY $LIGHT_ID ASC", null)
        cursor.use {
            while (cursor.moveToNext()) {
                val currentId = cursor.getInt(cursor.getColumnIndex(LIGHT_ID))
                if (currentId != id) {
                    break
                }
                id++
            }
        }
        // Add a new row at the available ID
        val values = ContentValues().apply {
            put(LIGHT_ID, id)
            put(ROOM, room)
            put(TYPE, type)
        }
        db.insert(TBL_LIGHT, null, values)

        db.close()
    }


    fun deleteOutsideLightById(id: Int) {
        val db = this.writableDatabase

        // Delete the specified row
        db.delete(TBL_LIGHT, "$LIGHT_ID = ?", arrayOf(id.toString()))

        // Shift subsequent rows to fill the gap left by the deleted row
        val cursor = db.rawQuery("SELECT * FROM $TBL_LIGHT WHERE $LIGHT_ID > $id", null)
        cursor.use {
            var updatedId = id
            val columnIndex = cursor.getColumnIndex(LIGHT_ID)
            if (columnIndex != -1) {
                while (cursor.moveToNext()) {
                    val currentId = cursor.getInt(columnIndex)
                    val values = ContentValues().apply {
                        put(LIGHT_ID, updatedId++)
                    }
                    db.update(TBL_LIGHT, values, "$LIGHT_ID = ?", arrayOf(currentId.toString()))
                }
            } else {
                // Handle case where CAM_ID column doesn't exist
            }
        }
        cursor?.close()
        db.close()
    }

    fun getAllOutsideLights(): List<OutsideLightsModel> {
        val lightList = mutableListOf<OutsideLightsModel>()
        val selectQuery = "SELECT * FROM $TBL_LIGHT"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.use {
            val idIndex = it.getColumnIndex(LIGHT_ID)
            val descIndex = it.getColumnIndex(ROOM)
            val angleIndex = it.getColumnIndex(TYPE)

            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val location = it.getString(descIndex)
                val angle = it.getString(angleIndex)

                val light = OutsideLightsModel(id, location, angle)
                lightList.add(light)
            }
        }

        cursor?.close()
        db.close()
        return lightList
    }
}