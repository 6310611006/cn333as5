

package com.example.phonebook.ui.components

import ContactColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ContactModel

import com.example.phonebook.util.fromHex

@ExperimentalMaterialApi
@Composable
fun Contact(
    modifier: Modifier = Modifier,
    contact: ContactModel,
    onContactClick: (ContactModel) -> Unit = {},
    onContactCheckedChange: (ContactModel) -> Unit = {},
    isFavorite: Boolean

) {
    val background = if (isFavorite)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = { Text(text = contact.name, maxLines = 1) },
            secondaryText = {
                Text(text = contact.tag.name, maxLines = 1)
            },
            icon = {

                ContactColor(
                    icon = contact.tag.icon,
                    size = 40.dp,
                    border = 2.dp
                )
            },
            trailing = {
                if (contact.isFavorite != null) {
                    IconButton(
                        onClick = {
                            val newContact = contact.copy(isFavorite = !contact.isFavorite)
                            onContactCheckedChange.invoke(newContact)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (contact.isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            },
            modifier = Modifier.clickable {
                onContactClick.invoke(contact)
            }
        )
    }
}
@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactPreview() {
    Contact(contact = ContactModel(1, "Contact 1", "Content 1", false), isFavorite = true)
    Contact(contact = ContactModel(2, "Contact 2", "Content 2", false), isFavorite = false)
}