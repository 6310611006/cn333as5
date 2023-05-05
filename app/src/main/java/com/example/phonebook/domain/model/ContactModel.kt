package com.example.phonebook.domain.model

const val NEW_CONTACT_ID = -1L

data class ContactModel(
    val id: Long = NEW_CONTACT_ID, // This value is used for new Contacts
    val name: String = "",
    val content: String = "",
    val isCheckedOff: Boolean? = null, // null represents that the Contact can't be checked off
    val isFavorite: Boolean = false,
    val tag: TagModel = TagModel.DEFAULT

)