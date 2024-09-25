package org.livewall.todoapp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.livewall.todoapp.domainimport.ToDoTask

@Composable
fun HomeView(
    userId: String,
    tasks: List<ToDoTask>?,
    onAddTaskClick: () -> Unit,
    onSignOutClick: suspend () -> Unit,
    onTaskCheckedChange: (ToDoTask, Boolean) -> Unit,
    onSaveTask: (ToDoTask) -> Unit
) {
    val scope = rememberCoroutineScope()
    val tasksNow = tasks
    var selectedTask by remember { mutableStateOf<ToDoTask?>(null) }

    // If no task is selected, show the task list, else show the TaskInputView
    if (selectedTask == null) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onAddTaskClick() },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "User: $userId",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                onSignOutClick()
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Sign Out")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        if (tasks != null) {
                            val sortedTasks = tasks.sortedBy { it.isCompleted }
                            items(count = sortedTasks.size) { index ->
                                val task = sortedTasks[index]
                                task.details?.let {
                                    TaskItem(
                                        title = task.title,
                                        description = it,
                                        isCompleted = task.isCompleted,
                                        onCheckedChange = { isChecked ->
                                            onTaskCheckedChange(task, isChecked)
                                        },
                                        onClick = { selectedTask = task }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    } else {
        // Show TaskInputView for the selected task
        TaskInputView(
            initialTitle = selectedTask!!.title,
            initialDescription = selectedTask!!.details ?: "",
            onSaveTask = { updatedTitle, updatedDescription ->
                // Save the updated task

                val updatedTask = selectedTask!!.copy(
                    title = updatedTitle,
                    details = updatedDescription,
                    id = selectedTask!!.id // Reassign the id explicitly, although it remains unchanged
                )

                onSaveTask(updatedTask)

                // Clear the selected task to return to the task list view
                if (tasks != null) {
                    tasks.map {
                        if (it.id == updatedTask.id) {
                            it.title = updatedTask.title
                            it.details = updatedTask.details
                        }
                    }
                }
                selectedTask = null
            },
            onCancel = {
                // Cancel the edit and go back to the task list
                selectedTask = null
            }
        )
    }
}