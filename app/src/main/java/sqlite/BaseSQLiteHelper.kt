package sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

open class BaseSQLiteHelper(context: Context, name: String, cursorFactory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, cursorFactory, version) {

    companion object {
        const val TABLE_POPIS = "Popis"
        const val COLUMN_POPIS_ID = "id"
        const val COLUMN_POPIS_DATUM = "datum"
        const val COLUMN_POPIS_NAPOMENA = "napomena"
        const val COLUMN_POPIS_ACTIVE = "active"

        const val TABLE_POPIS_STAVKA = "PopisStavka"
        const val COLUMN_STAVKA_ID = "id"
        const val COLUMN_STAVKA_ID_POPIS = "id_popis"
        const val COLUMN_STAVKA_ID_ARTIKAL = "id_artikal"
        const val COLUMN_STAVKA_ID_LOKACIJA = "id_lokacija"
        const val COLUMN_STAVKA_KOLICINA = "kolicina"
        const val COLUMN_STAVKA_ID_USER = "id_user"
        const val COLUMN_STAVKA_VREME_POPISIVANJA = "vreme_popisivanja"
        const val COLUMN_STAVKA_ID_RACUNOPOLAGAC = "id_racunopolagac"

        const val TABLE_ARTIKAL = "Artikal"
        const val COLUMN_ID = "id"
        const val COLUMN_NAZIV = "naziv"
        const val COLUMN_BARKOD = "barkod"
        const val COLUMN_SIFRA_ARTIKAL = "sifra"

        const val TABLE_LOKACIJA = "Lokacija"
        const val COLUMN_NAZIV_LOKACIJA = "naziv"
        const val COLUMN_SIFRA = "sifra"

        const val TABLE_USER = "User"
        const val COLUMN_IME = "ime"
        const val COLUMN_PREZIME = "prezime"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"

        const val TABLE_RACUNOPOLAGAC = "Racunopolagac"
        const val COLUMN_IME_RACUNOPOLAGAC = "ime"
        const val COLUMN_PREZIME_RACUNOPOLAGAC = "prezime"
        const val COLUMN_SIFRA_RACUNOPOLAGAC = "sifra"
        const val COLUMN_FUNKCIJA_RACUNOPOLAGAC = "funkcija"
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}
