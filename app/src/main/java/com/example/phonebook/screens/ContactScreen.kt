package com.example.phonebook.screens


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.Contact
import com.example.phonebook.ui.theme.PhoneBookThemeSettings
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun ContactScreen(viewModel: MainViewModel) {
    val contacts by viewModel.contactsNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Contacts",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        bottomBar = {BottomNavigationBar(
            currentScreen = Screen.Contacts,
            onScreenSelected = { screen -> viewModel.onScreenSelected(screen) }
        )},
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Contacts,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewContactClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Contact Button"
                    )
                }
            )
        },

    )

    {
        if (contacts.isNotEmpty()) {
            ContactsList(
                contacts = contacts.sortedBy { it.name },
                onContactCheckedChange = {
                    viewModel.onContactCheckedChange(it)
                },
                onContactClick = { viewModel.onContactClick(it) }
            )
        }
        
    }
}
@ExperimentalMaterialApi
@Composable
private fun ContactsList(
    contacts: List<ContactModel>,
    onContactCheckedChange: (ContactModel) -> Unit,
    onContactClick: (ContactModel) -> Unit
) {
    LazyColumn {
        items(count = contacts.size) { contactIndex ->
            val contact = contacts[contactIndex]
            Contact(
                contact = contact,
                onContactClick = onContactClick,
                onContactCheckedChange = onContactCheckedChange,
                isFavorite = false
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactsListPreview() {
    ContactsList(
        contacts = listOf(
            ContactModel(1, "test1", "Content 1", null,false),
            ContactModel(2, "test2", "Content 2", false,false),
            ContactModel(3, "test3", "Content 3", true,true)
        ).sortedBy { it.name },
        onContactCheckedChange = {},
        onContactClick = {}
    )
}

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    val isDarkTheme = PhoneBookThemeSettings.isDarkThemeEnabled
    BottomNavigation(
        backgroundColor = if (isDarkTheme) MaterialTheme.colors.surface else MaterialTheme.colors.primary
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = currentScreen == Screen.Favorite,
            onClick = { onScreenSelected(Screen.Favorite) }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Contacts") },
            label = { Text("Contacts") },
            selected = currentScreen == Screen.Contacts,
            onClick = { onScreenSelected(Screen.Contacts) }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Delete, contentDescription = "Trash") },
            label = { Text("Trash") },
            selected = currentScreen == Screen.Trash,
            onClick = { onScreenSelected(Screen.Trash) }
        )
    }
}