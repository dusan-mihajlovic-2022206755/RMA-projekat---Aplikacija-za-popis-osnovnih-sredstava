package com.example.popisosnovnihsredstava.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.popisosnovnihsredstava.entities.Artikal
import com.example.popisosnovnihsredstava.entities.Lokacija
import com.example.popisosnovnihsredstava.entities.Racunopolagac
import com.example.popisosnovnihsredstava.entities.User

class SQLiteSifarnikHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create Artikal table
        db.execSQL("""
            CREATE TABLE $TABLE_ARTIKAL (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAZIV TEXT NOT NULL,
                $COLUMN_BARKOD TEXT NOT NULL,
                $COLUMN_SIFRA TEXT NOT NULL
            )
        """)

        // Create Lokacija table
        db.execSQL("""
            CREATE TABLE $TABLE_LOKACIJA (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAZIV_LOKACIJA TEXT,
                $COLUMN_SIFRA TEXT
            )
        """)

        // Create User table
        db.execSQL("""
            CREATE TABLE $TABLE_USER (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_IME TEXT NOT NULL,
                $COLUMN_PREZIME TEXT NOT NULL,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL
            )
        """)

        db.execSQL("""
    CREATE TABLE $TABLE_RACUNOPOLAGAC (
        $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_SIFRA_RACUNOPOLAGAC TEXT NOT NULL,
        $COLUMN_IME_RACUNOPOLAGAC TEXT NOT NULL,
        $COLUMN_PREZIME_RACUNOPOLAGAC TEXT NOT NULL,
        $COLUMN_FUNKCIJA_RACUNOPOLAGAC TEXT NOT NULL
    )
""")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ARTIKAL")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOKACIJA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RACUNOPOLAGAC")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "Sifarnik.db"
        private const val DATABASE_VERSION = 1

        // Artikal table
        const val TABLE_ARTIKAL = "Artikal"
        const val COLUMN_ID = "id"
        const val COLUMN_NAZIV = "naziv"
        const val COLUMN_BARKOD = "barkod"
        const val COLUMN_SIFRA_ARTIKAL = "sifra"

        // Lokacija table
        const val TABLE_LOKACIJA = "Lokacija"
        const val COLUMN_NAZIV_LOKACIJA = "naziv"
        const val COLUMN_SIFRA = "sifra"

        // User table
        const val TABLE_USER = "User"
        const val COLUMN_IME = "ime"
        const val COLUMN_PREZIME = "prezime"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"

        const val TABLE_RACUNOPOLAGAC = "User"
        const val COLUMN_IME_RACUNOPOLAGAC = "ime"
        const val COLUMN_PREZIME_RACUNOPOLAGAC = "prezime"
        const val COLUMN_SIFRA_RACUNOPOLAGAC = "sifra"
        const val COLUMN_FUNKCIJA_RACUNOPOLAGAC = "funkcija"
    }

    // CRUD Operations for Artikal
    fun insertArtikal(artikal: Artikal): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAZIV, artikal.naziv)
            put(COLUMN_BARKOD, artikal.barkod)
            put(COLUMN_SIFRA_ARTIKAL, artikal.sifra)
        }
        return db.insert(TABLE_ARTIKAL, null, values).also { db.close() }
    }

    fun getArtikli(): List<Artikal> {
        val db = readableDatabase
        val cursor = db.query(TABLE_ARTIKAL, null, null, null, null, null, null)
        val artikli = mutableListOf<Artikal>()
        with(cursor) {
            while (moveToNext()) {
                artikli.add(
                    Artikal(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        naziv = getString(getColumnIndexOrThrow(COLUMN_NAZIV)),
                        barkod = getString(getColumnIndexOrThrow(COLUMN_BARKOD)),
                        sifra = getString(getColumnIndexOrThrow(COLUMN_SIFRA))
                    )
                )
            }
            close()
        }
        db.close()
        return artikli
    }
    fun searchArtikli(query: String): List<Artikal> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_ARTIKAL,
            null,
            "$COLUMN_SIFRA LIKE ? OR $COLUMN_BARKOD LIKE ? OR $COLUMN_NAZIV LIKE ?",
            arrayOf("%$query%", "%$query%", "%$query%"),
            null,
            null,
            null
        )
        val artikli = mutableListOf<Artikal>()
        with(cursor) {
            while (moveToNext()) {
                artikli.add(
                    Artikal(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        naziv = getString(getColumnIndexOrThrow(COLUMN_NAZIV)),
                        barkod = getString(getColumnIndexOrThrow(COLUMN_BARKOD)),
                        sifra = getString(getColumnIndexOrThrow(COLUMN_SIFRA))
                    )
                )
            }
            close()
        }
        db.close()
        return artikli
    }
    // CRUD Operations for Lokacija
    fun insertLokacija(lokacija: Lokacija): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAZIV, lokacija.naziv)
            put(COLUMN_SIFRA, lokacija.sifra)
        }
        return db.insert(TABLE_LOKACIJA, null, values).also { db.close() }
    }

    fun getLokacije(): List<Lokacija> {
        val db = readableDatabase
        val cursor = db.query(TABLE_LOKACIJA, null, null, null, null, null, null)
        val lokacije = mutableListOf<Lokacija>()
        with(cursor) {
            while (moveToNext()) {
                lokacije.add(
                    Lokacija(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        naziv = getString(getColumnIndexOrThrow(COLUMN_NAZIV)),
                        sifra = getString(getColumnIndexOrThrow(COLUMN_SIFRA))
                    )
                )
            }
            close()
        }
        db.close()
        return lokacije
    }

    fun searchLokacije(query: String): List<Lokacija> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LOKACIJA,
            null,
            "$COLUMN_NAZIV LIKE ? OR $COLUMN_SIFRA LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null,
            null,
            null
        )
        val lokacije = mutableListOf<Lokacija>()
        with(cursor) {
            while (moveToNext()) {
                lokacije.add(
                    Lokacija(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        naziv = getString(getColumnIndexOrThrow(COLUMN_NAZIV)),
                        sifra = getString(getColumnIndexOrThrow(COLUMN_SIFRA))
                    )
                )
            }
            close()
        }
        db.close()
        return lokacije
    }

    // CRUD Operations for User
    fun insertUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IME, user.ime)
            put(COLUMN_PREZIME, user.prezime)
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_EMAIL, user.email)
        }
        return db.insert(TABLE_USER, null, values).also { db.close() }
    }

    fun getUsers(): List<User> {
        val db = readableDatabase
        val cursor = db.query(TABLE_USER, null, null, null, null, null, null)
        val users = mutableListOf<User>()
        with(cursor) {
            while (moveToNext()) {
                users.add(
                    User(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        ime = getString(getColumnIndexOrThrow(COLUMN_IME)),
                        prezime = getString(getColumnIndexOrThrow(COLUMN_PREZIME)),
                        username = getString(getColumnIndexOrThrow(COLUMN_USERNAME)),
                        email = getString(getColumnIndexOrThrow(COLUMN_EMAIL))
                    )
                )
            }
            close()
        }
        db.close()
        return users
    }
    fun insertRacunopolagac(racunopolagac: Racunopolagac): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SIFRA_RACUNOPOLAGAC, racunopolagac.sifra)
            put(COLUMN_IME_RACUNOPOLAGAC, racunopolagac.ime)
            put(COLUMN_PREZIME_RACUNOPOLAGAC, racunopolagac.prezime)
            put(COLUMN_FUNKCIJA_RACUNOPOLAGAC, racunopolagac.funkcija)
        }
        return db.insert(TABLE_RACUNOPOLAGAC, null, values).also { db.close() }
    }
    fun getRacunopolagaci(): List<Racunopolagac> {
        val db = readableDatabase
        val cursor = db.query(TABLE_RACUNOPOLAGAC, null, null, null, null, null, null)
        val racunopolagaci = mutableListOf<Racunopolagac>()
        with(cursor) {
            while (moveToNext()) {
                racunopolagaci.add(
                    Racunopolagac(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        sifra = getString(getColumnIndexOrThrow(COLUMN_SIFRA_RACUNOPOLAGAC)),
                        ime = getString(getColumnIndexOrThrow(COLUMN_IME_RACUNOPOLAGAC)),
                        prezime = getString(getColumnIndexOrThrow(COLUMN_PREZIME_RACUNOPOLAGAC)),
                        funkcija = getString(getColumnIndexOrThrow(COLUMN_FUNKCIJA_RACUNOPOLAGAC))
                    )
                )
            }
            close()
        }
        db.close()
        return racunopolagaci
    }

    fun popuniTestnimPodacima(db: SQLiteDatabase){

        val artikli = listOf(
            Artikal(id = 1, naziv = "Šećer", barkod = "1234567890123", sifra = "A001"),
            Artikal(id = 2, naziv = "Mleko", barkod = "2345678901234", sifra = "A002"),
            Artikal(id = 3, naziv = "Hleb", barkod = "3456789012345", sifra = "A003"),
            Artikal(id = 4, naziv = "Ulje", barkod = "4567890123456", sifra = "A004"),
            Artikal(id = 5, naziv = "Jaja", barkod = "5678901234567", sifra = "A005"),
            Artikal(id = 6, naziv = "Pavlaka", barkod = "6789012345678", sifra = "A006"),
            Artikal(id = 7, naziv = "Sir", barkod = "7890123456789", sifra = "A007"),
            Artikal(id = 8, naziv = "Kafa", barkod = "8901234567890", sifra = "A008"),
            Artikal(id = 9, naziv = "Čokolada", barkod = "9012345678901", sifra = "A009"),
            Artikal(id = 10, naziv = "Brašno", barkod = "0123456789012", sifra = "A010"),
            Artikal(id = 11, naziv = "So", barkod = "1123456789013", sifra = "A011"),
            Artikal(id = 12, naziv = "Biber", barkod = "2123456789014", sifra = "A012"),
            Artikal(id = 13, naziv = "Pirinač", barkod = "3123456789015", sifra = "A013"),
            Artikal(id = 14, naziv = "Testenina", barkod = "4123456789016", sifra = "A014"),
            Artikal(id = 15, naziv = "Sok", barkod = "5123456789017", sifra = "A015")
        )

        val lokacije = listOf(
            Lokacija(id = 1, naziv = "Magacin 1", sifra = "L001"),
            Lokacija(id = 2, naziv = "Prodavnica A", sifra = "L002"),
            Lokacija(id = 3, naziv = "Magacin 2", sifra = "L003"),
            Lokacija(id = 4, naziv = "Kancelarija", sifra = "L004"),
            Lokacija(id = 5, naziv = "Prodavnica B", sifra = "L005"),
            Lokacija(id = 6, naziv = "Skladište", sifra = "L006"),
            Lokacija(id = 7, naziv = "Pogonska hala", sifra = "L007"),
            Lokacija(id = 8, naziv = "Magacin 3", sifra = "L008"),
            Lokacija(id = 9, naziv = "Prodavnica C", sifra = "L009"),
            Lokacija(id = 10, naziv = "Distributivni centar", sifra = "L010"),
            Lokacija(id = 11, naziv = "Maloprodaja 1", sifra = "L011"),
            Lokacija(id = 12, naziv = "Maloprodaja 2", sifra = "L012"),
            Lokacija(id = 13, naziv = "Maloprodaja 3", sifra = "L013"),
            Lokacija(id = 14, naziv = "Regionalni magacin", sifra = "L014"),
            Lokacija(id = 15, naziv = "Centralno skladište", sifra = "L015")
        )

        val racunopolagaci = listOf(
            Racunopolagac(id = 1, sifra = "RP001", ime = "Marko", prezime = "Marković", funkcija = "Direktor"),
            Racunopolagac(id = 2, sifra = "RP002", ime = "Jelena", prezime = "Jovanović", funkcija = "Računovođa"),
            Racunopolagac(id = 3, sifra = "RP003", ime = "Nikola", prezime = "Petrović", funkcija = "Menadžer"),
            Racunopolagac(id = 4, sifra = "RP004", ime = "Ana", prezime = "Milić", funkcija = "Sekretar"),
            Racunopolagac(id = 5, sifra = "RP005", ime = "Ivan", prezime = "Ilić", funkcija = "Magacioner"),
            Racunopolagac(id = 6, sifra = "RP006", ime = "Maja", prezime = "Stanković", funkcija = "Asistent"),
            Racunopolagac(id = 7, sifra = "RP007", ime = "Petar", prezime = "Đorđević", funkcija = "Koordinator"),
            Racunopolagac(id = 8, sifra = "RP008", ime = "Milica", prezime = "Nikolić", funkcija = "Konsultant"),
            Racunopolagac(id = 9, sifra = "RP009", ime = "Vladimir", prezime = "Popović", funkcija = "Finansijski analitičar"),
            Racunopolagac(id = 10, sifra = "RP010", ime = "Dragana", prezime = "Marić", funkcija = "Administrator"),
            Racunopolagac(id = 11, sifra = "RP011", ime = "Luka", prezime = "Stevanović", funkcija = "Saradnik"),
            Racunopolagac(id = 12, sifra = "RP012", ime = "Sara", prezime = "Pavlović", funkcija = "Referent"),
            Racunopolagac(id = 13, sifra = "RP013", ime = "Filip", prezime = "Živanović", funkcija = "Operater"),
            Racunopolagac(id = 14, sifra = "RP014", ime = "Tamara", prezime = "Mitrović", funkcija = "Planer"),
            Racunopolagac(id = 15, sifra = "RP015", ime = "Nemanja", prezime = "Vučić", funkcija = "Kontrolor"))

            for (artikal in artikli) {
                insertArtikal(artikal)
            }
            for (lokacija in lokacije) {
                insertLokacija(lokacija)
            }
            for (racunopolagac in racunopolagaci) {
                insertRacunopolagac(racunopolagac)
            }
    }

}
