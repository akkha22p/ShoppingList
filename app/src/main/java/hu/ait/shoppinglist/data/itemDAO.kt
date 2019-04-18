package hu.ait.shoppinglist.data

import android.arch.persistence.room.*
import android.os.FileObserver.DELETE


@Dao
interface itemDAO {
    @Query("SELECT * FROM item")
    fun getAllItem(): List<Item>

    @Insert
    fun insertItem(todo: Item): Long

    @Delete
    fun deleteItem(todo: Item)

    @Update
    fun updateItem(todo: Item)

    @Query("DELETE FROM item")
    fun deleteAll()

}