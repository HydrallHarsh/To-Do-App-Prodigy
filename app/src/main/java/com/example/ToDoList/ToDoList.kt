package com.example.shopinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingList(val id: Int, var name: String, var isEditing: Boolean = false, val index: Int, var subscript: String = "")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingList>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemSubscript by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            // Add title at the top
            Text(
                text = "To-Do-List",
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(sItems) { item ->
                    if (item.isEditing) {
                        ToDoItemEditor(item = item, onEditComplete = { editName, editSubscript ->
                            sItems = sItems.map { it.copy(isEditing = false) }
                            val editedItem = sItems.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editName
                                it.subscript = editSubscript
                            }
                        })
                    } else {
                        TodoItemList(item = item, onEditClick = {
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        }, onDeleteClick = {
                            sItems = sItems - item
                        })
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showDialog = false }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val newItem = ShoppingList(
                                id = sItems.size + 1,
                                name = itemName,
                                index = sItems.size + 1,
                                subscript = itemSubscript
                            )
                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                            itemSubscript = ""
                        }
                    }) {
                        Text(text = "Add")
                    }
                }
            },
            title = { Text(text = "Add To-Do Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Task Name") }
                    )
                    OutlinedTextField(
                        value = itemSubscript,
                        onValueChange = { itemSubscript = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text("Subscript") }
                    )
                }
            }
        )
    }
}

@Composable
fun ToDoItemEditor(item: ShoppingList, onEditComplete: (String, String) -> Unit) {
    var editName by remember { mutableStateOf(item.name) }
    var editSubscript by remember { mutableStateOf(item.subscript) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        BasicTextField(
            value = editName,
            onValueChange = { editName = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        BasicTextField(
            value = editSubscript,
            onValueChange = { editSubscript = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(onClick = {
            isEditing = false
            onEditComplete(editName, editSubscript)
        }) {
            Text(text = "Save")
        }
    }
}

@Composable
fun TodoItemList(item: ShoppingList, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${item.index} - ${item.name}",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (item.subscript.isNotEmpty()) {
                Text(
                    text = item.subscript,
                    style = TextStyle(fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            modifier = Modifier.padding(8.dp)
                .align(Alignment.CenterVertically)
        ) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

