package com.example.phonebook.database

import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.TagModel

class DbMapper {
    // Create list of ContactModels by pairing each Contact with a tag
    fun mapContacts(
        contactDbModels: List<ContactDbModel>,
        tagDbModels: Map<Long, TagDbModel>
    ): List<ContactModel> = contactDbModels.map {
        val tagDbModel = tagDbModels[it.tagId]
            ?: throw RuntimeException("Color for colorId: ${it.tagId} was not found. Make sure that all colors are passed to this method")

        mapContact(it, tagDbModel)
    }

    // convert ContactDbModel to ContactModel
    fun mapContact(contactDbModel: ContactDbModel, tagDbModel: TagDbModel): ContactModel {
        val tag = mapTag(tagDbModel)
        val isCheckedOff = with(contactDbModel) { if (canBeCheckedOff) isCheckedOff else null }
        return with(contactDbModel) { ContactModel(id, name, content, isCheckedOff,  isFavorite , tag,) }
    }

    // convert list of TagDdModels to list of TagModels
    fun mapTags(tagDbModels: List<TagDbModel>): List<TagModel> =
        tagDbModels.map { mapTag(it) }

    // convert TagDbModel to TagModel
    private fun mapTag(tagDbModel: TagDbModel): TagModel =
        with(tagDbModel) { TagModel(id, name, icon) }

    // convert ContactModel back to ContactDbModel
    fun mapDbContact(contact: ContactModel): ContactDbModel =
        with(contact) {
            val canBeCheckedOff = isCheckedOff != null
            val isCheckedOff = isCheckedOff ?: false
            val isFavorite = isFavorite
            if (id == NEW_CONTACT_ID)
                ContactDbModel(
                    name = name,
                    content = content,
                    canBeCheckedOff = canBeCheckedOff,
                    isCheckedOff = isCheckedOff,
                    tagId = tag.id,
                    isInTrash = false,
                    isFavorite = isFavorite
                )
            else
                ContactDbModel(id, name, content, canBeCheckedOff, isCheckedOff, tag.id, false, isFavorite)
        }
}