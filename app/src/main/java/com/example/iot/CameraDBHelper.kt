package com.example.iot
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class CameraDBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "camera.db"
        private const val TBL_CAMERA = "tbl_camera"
        private const val CAM_ID = "_id"
        private const val DESCRIPTION = "description"
        private const val ANGLE = "angle"
    }

    private val createTBlCamera =
        "CREATE TABLE $TBL_CAMERA ($CAM_ID INTEGER PRIMARY KEY AUTOINCREMENT, $DESCRIPTION TEXT, $ANGLE TEXT)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTBlCamera)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_CAMERA")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun addCamera(location: String, angle: String) {
        val db = this.writableDatabase

        // Find the first available ID
        var id = 1
        val cursor = db.rawQuery("SELECT $CAM_ID FROM $TBL_CAMERA ORDER BY $CAM_ID ASC", null)
        cursor.use {
            while (cursor.moveToNext()) {
                val currentId = cursor.getInt(cursor.getColumnIndex(CAM_ID))
                if (currentId != id) {
                    break
                }
                id++
            }
        }

        // Add a new row at the available ID
        val values = ContentValues().apply {
            put(CAM_ID, id)
            put(DESCRIPTION, location)
            put(ANGLE, angle)
        }
        db.insert(TBL_CAMERA, null, values)

        db.close()
    }





    fun deleteCameraById(id: Int) {
        val db = this.writableDatabase

        // Delete the specified row
        db.delete(TBL_CAMERA, "$CAM_ID = ?", arrayOf(id.toString()))

        // Shift subsequent rows to fill the gap left by the deleted row
        val cursor = db.rawQuery("SELECT * FROM $TBL_CAMERA WHERE $CAM_ID > $id", null)
        cursor.use {
            var updatedId = id
            val columnIndex = cursor.getColumnIndex(CAM_ID)
            if (columnIndex != -1) {
                while (cursor.moveToNext()) {
                    val currentId = cursor.getInt(columnIndex)
                    val values = ContentValues().apply {
                        put(CAM_ID, updatedId++)
                    }
                    db.update(TBL_CAMERA, values, "$CAM_ID = ?", arrayOf(currentId.toString()))
                }
            } else {
                // Handle case where CAM_ID column doesn't exist
            }
        }
        cursor?.close()
        db.close()
    }



    fun getAllCameras(): List<CameraModel> {
        val cameraList = mutableListOf<CameraModel>()
        val selectQuery = "SELECT * FROM $TBL_CAMERA"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.use {
            val idIndex = it.getColumnIndex(CAM_ID)
            val descIndex = it.getColumnIndex(DESCRIPTION)
            val angleIndex = it.getColumnIndex(ANGLE)

            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val location = it.getString(descIndex)
                val angle = it.getString(angleIndex)

                val camera = CameraModel(id, location, angle)
                cameraList.add(camera)
            }
        }

        cursor?.close()
        db.close()
        return cameraList
    }



}