package hu.ait.shoppinglist.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true) var itemId : Long?,
    @ColumnInfo(name = "createName") var createName: String,
    @ColumnInfo(name = "createCate") var createCate: String,
    @ColumnInfo(name = "createPrice") var createPrice: String,
    @ColumnInfo(name = "createDesc") var createDesc: String,
    @ColumnInfo(name = "done") var done: Boolean
) : Serializable