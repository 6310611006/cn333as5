package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.phonebook.R

@Entity
data class TagDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "icon") val icon: Int, // changed to resource ID
    @ColumnInfo(name = "name") val name: String

) {
    companion object {
        val DEFAULT_TAGS = listOf(
            TagDbModel(1, R.drawable.baseline_phone_android_24, "Mobile"),
            TagDbModel(2, R.drawable.baseline_home_24, "Home"),
            TagDbModel(3, R.drawable.baseline_family_restroom_24, "Family"),
            TagDbModel(4, R.drawable.baseline_work_24, "Work"),
            TagDbModel(5, R.drawable.baseline_office_24, "Office"),
            TagDbModel(6, R.drawable.baseline_local_hospital_24, "Emergency"),
        )
        val DEFAULT_TAG = DEFAULT_TAGS[0]
    }
}