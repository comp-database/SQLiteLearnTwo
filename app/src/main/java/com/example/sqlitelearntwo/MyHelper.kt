package com.example.sqlitelearntwo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context) : SQLiteOpenHelper(context,"NUM_REG",null,1) {


    override fun onCreate(db: SQLiteDatabase?) {
        // primary key autoincrement increments the id key
        db?.execSQL("CREATE TABLE ACTABLE (_id integer primary key autoincrement , NAME TEXT , MEANING TEXT)")
        // id Name Meaning are columns in db
        db?.execSQL("INSERT INTO ACTABLE (NAME,MEANING)VALUES('ONE','FIRST')")
        db?.execSQL("INSERT INTO ACTABLE (NAME,MEANING)VALUES('TWO','SECOND')")
        db?.execSQL("INSERT INTO ACTABLE (NAME,MEANING)VALUES('THREE','THIRD')")
        db?.execSQL("INSERT INTO ACTABLE (NAME,MEANING)VALUES('FOUR','FOURTH')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}