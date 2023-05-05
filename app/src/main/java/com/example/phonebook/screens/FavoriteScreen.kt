package com.example.phonebook.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun FavoriteScreen(viewModel: MainViewModel) {
    val contacts by viewModel.contactsNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Favorite",
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
        }
    ) {
        if (contacts.isNotEmpty()) {
            FavoritesList(
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
private fun FavoritesList(
    contacts: List<ContactModel>,
    onContactCheckedChange: (ContactModel) -> Unit,
    onContactClick: (ContactModel) -> Unit
) {
    val favorite = contacts.filter{ it.isFavorite }
    LazyColumn {
        items(count = favorite.size) { contactIndex ->
            val contact = favorite[contactIndex]
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
private fun FavoritesListPreview() {
    FavoritesList(
        contacts = listOf(
            ContactModel(1, "Test1", "Content 1", null,false),
            ContactModel(2, "Test2", "Content 2", false, false),
            ContactModel(3, "Test3", "Content 3", true,true)
        ).sortedBy { it.name },
        onContactCheckedChange = {},
        onContactClick = {}
    )
}