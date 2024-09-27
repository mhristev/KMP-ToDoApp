package org.livewall.todoapp.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ToDoTaskItemComponent(
    title: String,
    description: String,
    isCompleted: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    var checkedState by remember { mutableStateOf(isCompleted) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.body2)
        }

        Checkbox(
            checked = isCompleted,
            onCheckedChange = {
                checkedState = it
                onCheckedChange(it)
            }
        )
        // Delete button (optional)
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }

    }
}