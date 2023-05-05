package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "can_be_checked_off") val canBeCheckedOff: Boolean,
    @ColumnInfo(name = "is_checked_off") val isCheckedOff: Boolean,
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,




    ) {
    companion object {
        val DEFAULT_CONTACTS = listOf(
            ContactDbModel(1, "John", "0927866309", false, false, 1, false,false),
            ContactDbModel(2, "Wick", "0835532054", false, false, 2, false,false),
            ContactDbModel(3, "Bensin", "0888889999", false, false, 3, false,false),
            ContactDbModel(4, "Diesel", "0875822468", false, false, 4, false, false),
            ContactDbModel(5, "Erdal", "0819119111", false, false, 5, false, false),
            ContactDbModel(6, "Hospital", "027745555", false, false, 6, false, true),


            )
    }
}
