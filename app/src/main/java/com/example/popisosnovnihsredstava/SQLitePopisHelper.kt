package com.example.popisosnovnihsredstava

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.popisosnovnihsredstava.entities.Popis
import com.example.popisosnovnihsredstava.entities.PopisStavka
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class SQLitePopisHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "bazaPopis.db"
        const val DATABASE_VERSION = 1

        // Table: Popis
        const val TABLE_POPIS = "Popis"
        const val COLUMN_POPIS_ID = "id"
        const val COLUMN_POPIS_DATUM = "datum"
        const val COLUMN_POPIS_NAPOMENA = "napomena"
        const val COLUMN_POPIS_ACTIVE = "active"

        // Table: PopisStavka
        const val TABLE_POPIS_STAVKA = "PopisStavka"
        const val COLUMN_STAVKA_ID = "id"
        const val COLUMN_STAVKA_ID_POPIS = "id_popis"
        const val COLUMN_STAVKA_ID_ARTIKAL = "id_artikal"
        const val COLUMN_STAVKA_ID_LOKACIJA = "id_lokacija"
        const val COLUMN_STAVKA_KOLICINA = "kolicina"
        const val COLUMN_STAVKA_ID_USER = "id_user"
        const val COLUMN_STAVKA_VREME_POPISIVANJA = "vreme_popisivanja"
        const val COLUMN_STAVKA_ID_RACUNOPOLAGAC = "id_racunopolagac" // Newly added field
    }

    fun createDB(db: SQLiteDatabase?) {
        // Create Popis Table
        val createPopisTable = """
        CREATE TABLE $TABLE_POPIS (
            $COLUMN_POPIS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_POPIS_DATUM DATE,
            $COLUMN_POPIS_NAPOMENA TEXT,
            $COLUMN_POPIS_ACTIVE INTEGER
        )
        """
        db?.execSQL(createPopisTable)

        // Create PopisStavka Table
        val createPopisStavkaTable = """
        CREATE TABLE $TABLE_POPIS_STAVKA (
            $COLUMN_STAVKA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_STAVKA_ID_POPIS INTEGER NOT NULL,
            $COLUMN_STAVKA_ID_ARTIKAL INTEGER NOT NULL,
            $COLUMN_STAVKA_ID_LOKACIJA INTEGER NOT NULL,
            $COLUMN_STAVKA_KOLICINA INTEGER NOT NULL,
            $COLUMN_STAVKA_ID_USER INTEGER NOT NULL,
            $COLUMN_STAVKA_VREME_POPISIVANJA DATETIME NOT NULL,
            $COLUMN_STAVKA_ID_RACUNOPOLAGAC INTEGER NOT NULL,
            FOREIGN KEY($COLUMN_STAVKA_ID_POPIS) REFERENCES $TABLE_POPIS($COLUMN_POPIS_ID)
        )
        """
        db?.execSQL(createPopisStavkaTable)
    }

    fun upgradeDB(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_POPIS_STAVKA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_POPIS")
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase) {
        createDB(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            upgradeDB(db, oldVersion, newVersion)
        }
    }

    fun insertPopisStavka(
        idPopis: Int,
        idArtikal: Int,
        idLokacija: Int,
        kolicina: Int,
        idUser: Int,
        vremePopisivanja: LocalDateTime,
        idRacunopolagac: Int
    ): Long {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STAVKA_ID_POPIS, idPopis)
            put(COLUMN_STAVKA_ID_ARTIKAL, idArtikal)
            put(COLUMN_STAVKA_ID_LOKACIJA, idLokacija)
            put(COLUMN_STAVKA_KOLICINA, kolicina)
            put(COLUMN_STAVKA_ID_USER, idUser)
            put(COLUMN_STAVKA_VREME_POPISIVANJA,  formatirajDateTime(vremePopisivanja.toString()).toString())
            put(COLUMN_STAVKA_ID_RACUNOPOLAGAC, idRacunopolagac)
        }
        val id = db.insert(TABLE_POPIS_STAVKA, null, values)
        db.close()
        return id
    }
    fun incrementKolicinaById(idPopisStavka: Int) {
        val db = this.writableDatabase
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val query = """
            UPDATE PopisStavka 
            SET kolicina = kolicina + 1, vremePopisivanja = ?
            WHERE id = ?
        """
        db.execSQL(query, arrayOf(currentTime, idPopisStavka))
    }

    fun getAllPopisStavka(): List<PopisStavka> {
        val db = readableDatabase
        val cursor = db.query(TABLE_POPIS_STAVKA, null, null, null, null, null, null)
        val stavkaList = mutableListOf<PopisStavka>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_STAVKA_ID))
                val idPopis = getInt(getColumnIndexOrThrow(COLUMN_STAVKA_ID_POPIS))
                val idArtikal = getInt(getColumnIndexOrThrow(COLUMN_STAVKA_ID_ARTIKAL))
                val idLokacija = getInt(getColumnIndexOrThrow(COLUMN_STAVKA_ID_LOKACIJA))
                val kolicina = getInt(getColumnIndexOrThrow(COLUMN_STAVKA_KOLICINA))
                val idUser = getInt(getColumnIndexOrThrow(COLUMN_STAVKA_ID_USER))
                val vremePopisivanja = LocalDateTime.parse(
                    getString(
                        getColumnIndexOrThrow(COLUMN_STAVKA_VREME_POPISIVANJA)
                    )
                )
                val idRacunopolagac = getInt(getColumnIndexOrThrow(COLUMN_STAVKA_ID_RACUNOPOLAGAC))
                stavkaList.add(
                    PopisStavka(
                        id,
                        idPopis,
                        idArtikal,
                        idLokacija,
                        kolicina,
                        idUser,
                        idRacunopolagac,
                        vremePopisivanja
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return stavkaList
    }

    fun getAllPopis(): List<Popis> {
        val db = readableDatabase
        val cursor = db.query(TABLE_POPIS, null, null, null, null, null, null)
        val popisList = mutableListOf<Popis>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_POPIS_ID))
                val datum =
                    getString(getColumnIndexOrThrow(COLUMN_POPIS_DATUM))?.let { LocalDate.parse(it) }
                val napomena = getString(getColumnIndexOrThrow(COLUMN_POPIS_NAPOMENA))
                val active = getInt(getColumnIndexOrThrow(COLUMN_POPIS_ACTIVE)) == 1
                popisList.add(Popis(id, datum, napomena, active))
            }
        }
        cursor.close()
        db.close()
        return popisList
    }

    fun insertIntoPopis(datum: LocalDate, napomena: String?, active: Boolean): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_POPIS_DATUM, datum.toString())
            put(COLUMN_POPIS_NAPOMENA, napomena)
            put(COLUMN_POPIS_ACTIVE, if (active) 1 else 0)
        }
        val id = db.insert(TABLE_POPIS, null, values)
        db.close()
        return id
    }

    fun unesiTestnePopise() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val popisList = listOf(
            Popis(
                id = 1,
                datum = LocalDate.parse("2024-01-15", formatter),
                napomena = "Inventar kancelarije",
                active = true
            ),
            Popis(
                id = 2,
                datum = LocalDate.parse("2024-02-10", formatter),
                napomena = "Godišnji pregled magacina",
                active = false
            ),
            Popis(
                id = 3,
                datum = LocalDate.parse("2024-03-05", formatter),
                napomena = "Provera alata",
                active = true
            ),
            Popis(
                id = 4,
                datum = LocalDate.parse("2024-04-20", formatter),
                napomena = "Popis vozila",
                active = false
            ),
            Popis(
                id = 5,
                datum = LocalDate.parse("2024-05-25", formatter),
                napomena = "Popis elektronske opreme",
                active = true
            )
        )
        for (popis in popisList) {
            popis.datum?.let {
                insertIntoPopis(
                    popis.datum ?: LocalDate.now(),
                    popis.napomena,
                    popis.active ?: false
                )
            }
        }

    }

    fun getPopisStavkeByIdPopis(popisID: Int): List<PopisStavka> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM PopisStavka WHERE id_popis = ? ORDER BY vreme_popisivanja DESC",
            arrayOf(popisID.toString())
        )
        val stavke = mutableListOf<PopisStavka>()

        with(cursor) {
            while (moveToNext()) {
                val valueFromDBRow = getColumnIndexOrThrow("vreme_popisivanja")
                val vremePopisivanja = formatirajDateTime(getString(valueFromDBRow))
                stavke.add(
                    PopisStavka(
                        getInt(getColumnIndexOrThrow("id")),
                        getInt(getColumnIndexOrThrow("id_popis")),
                        getInt(getColumnIndexOrThrow("id_artikal")),
                        getInt(getColumnIndexOrThrow("id_lokacija")),
                        getInt(getColumnIndexOrThrow("kolicina")),
                        getInt(getColumnIndexOrThrow("id_user")),
                        getInt(getColumnIndexOrThrow("id_racunopolagac")),
                        vremePopisivanja
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return stavke
    }
    fun checkIfPopisStavkaExists(artikalID: Int, popisID: Int, lokacijaID: Int, racunopolagacID: Int): Int {
        val db = this.readableDatabase

        val query = """
            SELECT id
            FROM PopisStavka 
            WHERE id_artikal = ? 
            AND id_popis = ? 
            AND id_lokacija = ? 
            AND id_racunopolagac = ? 
            LIMIT 1
        """
        val cursor = db.rawQuery(query, arrayOf(
            artikalID.toString(),
            popisID.toString(),
            lokacijaID.toString(),
            racunopolagacID.toString()
        ))

        val popisStavkaID = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        } else {
            return -1
        }

        cursor.close()

        return popisStavkaID
    }
}

