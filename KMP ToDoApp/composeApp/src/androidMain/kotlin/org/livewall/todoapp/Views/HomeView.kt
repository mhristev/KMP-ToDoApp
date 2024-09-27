package org.livewall.todoapp.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.livewall.todoapp.Components.ToDoTaskItemComponent
import org.livewall.todoapp.ViewModels.HomeViewModel
import org.livewall.todoapp.domain.models.ToDoTask

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeView(
    homeViewModel: HomeViewModel,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (ToDoTask) -> Unit,
    onSignOutClick: suspend () -> Unit,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddTaskClick() },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    scope.launch {
                        onSignOutClick()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            ) {
                Text(text = "Sign Out")
            }

            Spacer(modifier = Modifier.height(16.dp))

            val sortedTasks = homeViewModel.toDoTasks.sortedBy { it.isCompleted }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(sortedTasks) { task ->
                    ToDoTaskItemComponent (
                        title = task.title,
                        description = task.details.orEmpty(),
                        isCompleted = task.isCompleted,
                        onCheckedChange = { homeViewModel.toggleTaskCompletion(task) },
                        onClick = { onEditTaskClick(task) },
                        onDeleteClick = { homeViewModel.deleteTask(task)}
                    )
                }
            }
        }
    }
}