package com.any.weatherapp.model.repo

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.any.weatherapp.App
import com.any.weatherapp.model.pojo.Town
import com.any.weatherapp.model.pojo.Weather
import com.any.weatherapp.model.repo.imp.IDatabaseRepo
import com.any.weatherapp.model.service.DBContract
import com.any.weatherapp.model.service.DBContract.TownEntry.TOWN_TABLE_NAME
import com.any.weatherapp.model.service.DBContract.WeatherEntry.WEATHER_TABLE_NAME
import com.any.weatherapp.utils.DateTimeUtils.getTodaySeconds


/**
 * Created by Aleksey Vishnyakov on 12.07.2018.
 * Telegram: http://t.me/vishnyakov_aleks
 */

class DatabaseRepo: SQLiteOpenHelper(App.appContext, DB_NAME, null, DB_VERSION), IDatabaseRepo {

    companion object {
        const val DB_NAME = "weather_app.db"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_WEATHER_TABLE = ("CREATE TABLE $WEATHER_TABLE_NAME ("
                + "${DBContract.WeatherEntry.COLUMNS.DATE} INTEGER, "
                + "${DBContract.WeatherEntry.COLUMNS.OWNER_TOWN} INTEGER NOT NULL, "
                + "${DBContract.WeatherEntry.COLUMNS.VALUE_C} INTEGER NOT NULL, "
                + "${DBContract.WeatherEntry.COLUMNS.UPDATED_TIME} INTEGER NOT NULL," +
                "  PRIMARY KEY (${DBContract.WeatherEntry.COLUMNS.DATE}, ${DBContract.WeatherEntry.COLUMNS.OWNER_TOWN}));")

        val SQL_CREATE_TOWNS_TABLE = ("CREATE TABLE $TOWN_TABLE_NAME ("
                + "${DBContract.TownEntry.COLUMNS.TOWN_ID} INTEGER NOT NULL, "
                + "${DBContract.TownEntry.COLUMNS.TOWN_NAME} TEXT NOT NULL);")

        db.execSQL(SQL_CREATE_WEATHER_TABLE)
        db.execSQL(SQL_CREATE_TOWNS_TABLE)

    }

    fun postInitDatabase() {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF IT EXISTS $DB_NAME")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF IT EXISTS $DB_NAME")
        onCreate(db)
    }

    override fun getCachedWeather(townId: Int): ArrayList<Weather> {
        val listToReturn = ArrayList<Weather>(7)
        val cursor = readableDatabase.query(
                WEATHER_TABLE_NAME,
                null,
                "${DBContract.WeatherEntry.COLUMNS.OWNER_TOWN} = ? AND ${DBContract.WeatherEntry.COLUMNS.DATE} >= ?",
                arrayOf(townId.toString(), getTodaySeconds().toString()),
                null,
                null,
                null)



        while (cursor.moveToNext()) {
            listToReturn.add(Weather(
                    cursor.getInt(DBContract.WeatherEntry.COLUMNS.OWNER_TOWN.ordinal),
                    cursor.getInt(DBContract.WeatherEntry.COLUMNS.VALUE_C.ordinal),
                    cursor.getInt(DBContract.WeatherEntry.COLUMNS.DATE.ordinal),
                    cursor.getInt(DBContract.WeatherEntry.COLUMNS.UPDATED_TIME.ordinal)))
        }

        cursor.close()
        return listToReturn
    }

    override fun addWeather(weather: Weather): Int {
        if (weather.date < getTodaySeconds())
            return -1

        val values = ContentValues()
        values.put(DBContract.WeatherEntry.COLUMNS.OWNER_TOWN.name, weather.townId)
        values.put(DBContract.WeatherEntry.COLUMNS.VALUE_C.name, weather.cValue)
        values.put(DBContract.WeatherEntry.COLUMNS.DATE.name, weather.date)
        values.put(DBContract.WeatherEntry.COLUMNS.UPDATED_TIME.name, weather.lastUpdate)

        writableDatabase.insertWithOnConflict(WEATHER_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        return 0
    }

    override fun clearOldWeather() {
        writableDatabase.delete(
                WEATHER_TABLE_NAME,
                "${DBContract.WeatherEntry.COLUMNS.DATE} < ${getTodaySeconds()}",
                null)
    }

    override fun getActiveTownList(): ArrayList<Town> {
        val listToReturn = ArrayList<Town>()
        val cursor = readableDatabase.query(
                TOWN_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null)

        while (cursor.moveToNext()) {
            listToReturn.add(Town(
                    cursor.getInt(DBContract.TownEntry.COLUMNS.TOWN_ID.ordinal),
                    cursor.getString(DBContract.TownEntry.COLUMNS.TOWN_NAME.ordinal)))
        }

        cursor.close()
        return listToReturn
    }

    override fun activateTown(town: Town) = activateTown(town, writableDatabase)

    private fun activateTown(town: Town, db: SQLiteDatabase) {
        val values = ContentValues()
        values.put(DBContract.TownEntry.COLUMNS.TOWN_ID.name, town.id)
        values.put(DBContract.TownEntry.COLUMNS.TOWN_NAME.name, town.name)

        db.insertWithOnConflict(TOWN_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)

    }

    override fun deactivateTown(townId: Int) = deactivateTown(townId, writableDatabase)

    private fun deactivateTown(townId: Int, db: SQLiteDatabase = writableDatabase) {
        db.delete(DBContract.TownEntry.TOWN_TABLE_NAME,
                "${DBContract.TownEntry.COLUMNS.TOWN_ID} = ?",
                arrayOf(townId.toString()))
    }
}