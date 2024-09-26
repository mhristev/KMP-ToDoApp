package org.livewall.todoapp.Views

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToDoTaskCreateNUpdateView(
    initialTitle: String,
    initialDescription: String,
    onSaveTask: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(text = "Task Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Task Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(
                onClick = { onSaveTask(title, description) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save Task")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { onCancel() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
        }
    }
}