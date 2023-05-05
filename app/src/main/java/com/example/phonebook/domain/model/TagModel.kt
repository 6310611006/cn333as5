package com.example.phonebook.domain.model

import com.example.phonebook.database.TagDbModel

data class TagModel(
    val id: Long,
    val name: String,
    val icon: Int
) {
    companion object {
        val DEFAULT = with(TagDbModel.DEFAULT_TAG) { TagModel(id, name, icon)}
    }
}