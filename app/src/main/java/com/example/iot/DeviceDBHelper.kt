package com.example.iot
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DeviceDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "device.db"
        private const val TBL_DEVICE = "tbl_device"
        private const val DEV_ID = "_id"
        private const val MAKE_MODEL = "makeModel"
        private const val OWNER = "owner"
    }


    private val createTBlDevice =
        "CREATE TABLE  $TBL_DEVICE ($DEV_ID INTEGER PRIMARY KEY AUTOINCREMENT, $MAKE_MODEL TEXT, $OWNER TEXT)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTBlDevice)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_DEVICE")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun addDevice(makeMod: String, owner: String) {
        val db = this.writableDatabase

        // Find the first available ID
        var id = 1
        val cursor = db.rawQuery("SELECT $DEV_ID FROM $TBL_DEVICE ORDER BY $DEV_ID ASC", null)
        cursor.use {
            while (cursor.moveToNext()) {
                val currentId = cursor.getInt(cursor.getColumnIndex(DEV_ID))
                if (currentId != id) {
                    break
                }
                id++
            }
        }
        // Add a new row at the available ID
        val values = ContentValues().apply {
            put(DEV_ID, id)
            put(MAKE_MODEL, makeMod)
            put(OWNER, owner)
        }
        db.insert(TBL_DEVICE, null, values)

        db.close()
    }


    fun deleteDeviceById(id: Int) {
        val db = this.writableDatabase

        // Delete the specified row
        db.delete(TBL_DEVICE, "$DEV_ID = ?", arrayOf(id.toString()))

        // Shift subsequent rows to fill the gap left by the deleted row
        val cursor = db.rawQuery("SELECT * FROM $TBL_DEVICE WHERE $DEV_ID > $id", null)
        cursor.use {
            var updatedId = id
            val columnIndex = cursor.getColumnIndex(DEV_ID)
            if (columnIndex != -1) {
                while (cursor.moveToNext()) {
                    val currentId = cursor.getInt(columnIndex)
                    val values = ContentValues().apply {
                        put(DEV_ID, updatedId++)
                    }
                    db.update(TBL_DEVICE, values, "$DEV_ID = ?", arrayOf(currentId.toString()))
                }
            } else {
                // Handle case where CAM_ID column doesn't exist
            }
        }
        cursor?.close()
        db.close()
    }

    fun getAllDevices(): List<DeviceModel> {
        val deviceList = mutableListOf<DeviceModel>()
        val selectQuery = "SELECT * FROM $TBL_DEVICE"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.use {
            val idIndex = it.getColumnIndex(DEV_ID)
            val descIndex = it.getColumnIndex(MAKE_MODEL)
            val angleIndex = it.getColumnIndex(OWNER)

            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val location = it.getString(descIndex)
                val angle = it.getString(angleIndex)

                val device = DeviceModel(id, location, angle)
                deviceList.add(device)
            }
        }

        cursor?.close()
        db.close()
        return deviceList
    }














}