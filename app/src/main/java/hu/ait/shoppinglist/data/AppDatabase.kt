package hu.ait.shoppinglist.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Item::class), version = 2)

abstract class AppDatabase : RoomDatabase() {



    abstract fun itemDao(): itemDAO



    companion object {

        private var INSTANCE: AppDatabase? = null



        fun getInstance(context: Context): AppDatabase {

            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),

                    AppDatabase::class.java, "item.db")

                    .fallbackToDestructiveMigration()

                    .build()

            }

            return INSTANCE!!

        }



        fun destroyInstance() {

            INSTANCE = null

        }

    }

}