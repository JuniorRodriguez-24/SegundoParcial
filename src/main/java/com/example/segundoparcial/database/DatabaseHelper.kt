package com.example.segundoparcial.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.segundoparcial.model.Cita

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "CitasMedicas.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_CITAS = "citas"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_TELEFONO = "telefono"
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_HORA = "hora"

        private const val CREATE_TABLE = """
            CREATE TABLE $TABLE_CITAS (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NOMBRE TEXT NOT NULL,
            $COLUMN_TELEFONO TEXT NOT NULL,
            $COLUMN_FECHA TEXT NOT NULL,
            $COLUMN_HORA TEXT NOT NULL)
       """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CITAS")
        onCreate(db)
    }

    // CREAR
    fun insertarCita(cita: Cita): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, cita.nombre)
            put(COLUMN_TELEFONO, cita.telefono)
            put(COLUMN_FECHA, cita.fecha)
            put(COLUMN_HORA, cita.hora)
        }
        val id = db.insert(TABLE_CITAS, null, values)
        db.close()
        return id
    }

    // LEER
    fun obtenerCitas(): List<Cita> {
        val listaCitas = mutableListOf<Cita>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_CITAS ORDER BY $COLUMN_ID DESC"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val cita = Cita(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA))
                )
                listaCitas.add(cita)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaCitas
    }
    // LEER (Individual por ID)
    fun obtenerCitaPorId(id: Int): Cita? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CITAS, null, "$COLUMN_ID = ?", arrayOf(id.toString()),
            null, null, null
        )

        var cita: Cita? = null
        if (cursor != null && cursor.moveToFirst()) {
            cita = Cita(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)),
                hora = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA))
            )
            cursor.close()
        }
        db.close()
        return cita
    }

    // ACTUALIZAR
    fun actualizarCita(cita: Cita): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, cita.nombre)
            put(COLUMN_TELEFONO, cita.telefono)
            put(COLUMN_FECHA, cita.fecha)
            put(COLUMN_HORA, cita.hora)
        }
        val filasAfectadas = db.update(TABLE_CITAS, values, "$COLUMN_ID = ?", arrayOf(cita.id.toString()))
        db.close()
        return filasAfectadas
    }

    // ELIMINAR
    fun eliminarCita(id: Int): Int {
        val db = writableDatabase
        val filasAfectadas = db.delete(TABLE_CITAS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        return filasAfectadas
    }
}