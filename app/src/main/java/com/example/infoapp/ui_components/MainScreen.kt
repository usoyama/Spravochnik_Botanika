package com.example.infoapp.ui_components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.infoapp.utils.DrawerEvents
import com.example.infoapp.utils.IdArrayList
import com.example.infoapp.utils.ListItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "UnusedMaterialScaffoldPaddingParameter",
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition"
)
@Composable
fun MainScreen(context: Context, onClick: (ListItem) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val mainList = remember {
        mutableStateOf(getListItemsByIndex(0,context))
    }
    val topBarTitle = remember {
        mutableStateOf("Грибы")
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerMenu() { event ->
                    when (event) {
                        is DrawerEvents.OnItemClick -> {
                            topBarTitle.value = event.title
                            mainList.value = getListItemsByIndex(
                                event.index, context
                            )
                        }
                    }
                    coroutineScope.launch {
                        drawerState.close()
                    }
                }
            }
        },
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                MainTopBar(
                    title = topBarTitle.value,
                    snackbarHostState
                )
            },
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(mainList.value) { item ->
                    MainListItem(item = item){listItem ->
                        onClick(listItem)
                    }
                }
            }

        }
    }
}
private fun getListItemsByIndex(index: Int, context: Context): List<ListItem>{
    val list = ArrayList<ListItem>()
    val arrayList = context.resources.getStringArray(IdArrayList.listId[index])
    arrayList.forEach { item ->
        val itemArray = item.split("|")
        list.add(
            ListItem(
                itemArray[0],
                itemArray[1],
                itemArray[2]
            )
        )

    }
    return list
}