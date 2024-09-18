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
    onTaskCheckedChange: (ToDoTask, Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val tasksNow = tasks

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddTaskClick() },
                backgroundColor = MaterialTheme.colors.primary,
//                modifier = Modifier.size(70.dp)
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
                // Button to sign out
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
                        items(count = tasks.size) { index ->
                            val task = tasks[index] // Access each task by index
                            task.description?.let {
                                TaskItem(
                                    title = task.title,
                                    description = it,
                                    isCompleted = task.isCompleted,
                                    onCheckedChange = { isChecked ->
                                        onTaskCheckedChange(task, isChecked)
                                    }
                                )
                            }
                        }
                    }
                }




            }
        }
    )
}