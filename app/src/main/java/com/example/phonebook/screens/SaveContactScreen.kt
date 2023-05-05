package com.example.phonebook.screens

import ContactColor
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.R
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.TagModel
import com.example.phonebook.routing.ContactRouter
import com.example.phonebook.routing.Screen

import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SaveContactScreen(viewModel: MainViewModel) {
    val contactEntry by viewModel.contactEntry.observeAsState(ContactModel())

    val tags: List<TagModel> by viewModel.tags.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val moveContactToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            ContactRouter.navigateTo(Screen.Contacts)
        }
    }

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = contactEntry.id != NEW_CONTACT_ID
            SaveContactTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = { ContactRouter.navigateTo(Screen.Contacts) },
                onSaveContactClick = { viewModel.saveContact(contactEntry) },
                onOpenColorPickerClick = {
                    coroutineScope.launch { bottomDrawerState.open() }
                },
                onDeleteContactClick = {
                    moveContactToTrashDialogShownState.value = true
                }
            )
        }
    ) {
        BottomDrawer(
            drawerState = bottomDrawerState,
            drawerContent = {
                TagPicker(
                    tags = tags,
                    onTagSelect = { tag ->
                        viewModel.onContactEntryChange(contactEntry.copy(tag = tag))
                    }
                )
            }
        ) {
            SaveContactContent(
                contact = contactEntry,
                onContactChange = { updateContactEntry ->
                    viewModel.onContactEntryChange(updateContactEntry)
                }
            )
        }

        if (moveContactToTrashDialogShownState.value) {
            AlertDialog(
                onDismissRequest = {
                    moveContactToTrashDialogShownState.value = false
                },
                title = {
                    Text("Move to the trash?")
                },
                text = {
                    Text(
                        "Are you sure you want to " +
                                "move this Contact to the trash?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.moveContactToTrash(contactEntry)
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        moveContactToTrashDialogShownState.value = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun SaveContactTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSaveContactClick: () -> Unit,
    onOpenColorPickerClick: () -> Unit,
    onDeleteContactClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Save Contact",
                color = MaterialTheme.colors.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveContactClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Contact Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            IconButton(onClick = onOpenColorPickerClick) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_tag_24),
                    contentDescription = "Open Color Picker Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeleteContactClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun SaveContactContent(
    contact: ContactModel,
    onContactChange: (ContactModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val isChecked: Boolean = contact.isFavorite

        FavoriteCheckOption(
            isChecked = isChecked,
            onCheckedChange = { FavoriteNewValue ->
                val isCheckedOff: Boolean = FavoriteNewValue

                onContactChange.invoke(contact.copy(isFavorite = isCheckedOff))
            }
        )
        ContentTextField(
            label = "Name",
            text = contact.name,
            onTextChange = { newTitle ->
                onContactChange.invoke(contact.copy(name = newTitle))
            }
        )

        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "PhoneNumber",
            text = contact.content,
            onTextChange = { newContent ->
                onContactChange.invoke(contact.copy(content = newContent))
            }
        )


        PickedTag(icon = contact.tag)
    }
}

@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        )
    )
}
@Composable
private fun FavoriteCheckOption(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Favorite Number",
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Composable
private fun ContactCheckOption(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Hide Number",
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun PickedTag(icon: TagModel) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Picked Tag : " + icon.name,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        ContactColor(
            modifier = Modifier.padding(4.dp),
            icon = icon.icon,
            size = 40.dp,
            border = 2.dp
        )

    }
}

@Composable
private fun TagPicker(
    tags: List<TagModel>,
    onTagSelect: (TagModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tag picker",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)

        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(tags.size) { itemIndex ->
                val icon = tags[itemIndex]
                TagItem(

                    icon = icon,
                    onColorSelect = onTagSelect

                )
            }
        }
    }
}

@Composable
fun TagItem(
    icon: TagModel,
    onColorSelect: (TagModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onColorSelect(icon)
                }
            )
    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.baseline_restore_from_trash_24),
//            contentDescription = "Restore Contacts Button",
//            tint = MaterialTheme.colors.onPrimary
//        )
        ContactColor(
            modifier = Modifier.padding(10.dp),
            icon = icon.icon,
            size = 80.dp,
            border = 2.dp
        )
        Text(
            text = icon.name,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun TagItemPreview() {
    TagItem(TagModel.DEFAULT) {}
}

@Preview
@Composable
fun TagPickerPreview() {
    TagPicker(
        tags = listOf(
            TagModel.DEFAULT,
            TagModel.DEFAULT,
            TagModel.DEFAULT
        )
    ) { }
}

@Preview
@Composable
fun PickedTagPreview() {
    PickedTag(TagModel.DEFAULT)
}