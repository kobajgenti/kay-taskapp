package com.example.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskapp.ui.theme.TaskappTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color

data class Task(
    val name: String,
    var isCompleted: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskappTheme {
                val tasks = remember { mutableStateListOf<Task>() }
                TaskListScreen(tasks)
            }
        }
    }
}

@Composable
fun TaskListScreen(tasks: MutableList<Task>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Margins around the content
    ) {
        // Title at the top
        Text(
            text = "Task List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 16.dp),
            textAlign = TextAlign.Left
        )

        // Input field and Add button
        var newTaskDescription by remember { mutableStateOf("") }

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newTaskDescription,
                onValueChange = { newTaskDescription = it },
                label = { Text("Enter Task") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Button(onClick = {
                if (newTaskDescription.isNotBlank()) {
                    tasks.add(Task(newTaskDescription))
                    newTaskDescription = ""
                }
            }) {
                Text("Add Task")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task list
        if (tasks.isEmpty()) {
            Text(
                text = "No tasks.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn {
                items(tasks) { task ->
                    TaskItem(task)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Clear Completed Button
        Button(onClick = {
            tasks.removeAll { it.isCompleted }
        }) {
            Text("Clear Completed")
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    val taskName = task.name
    var isCompleted by remember { mutableStateOf(task.isCompleted) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = { checked ->
                isCompleted = checked
                task.isCompleted = checked
            }
        )
        Text(
            text = taskName,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            style = if (isCompleted) {
                MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
            } else {
                MaterialTheme.typography.bodyMedium
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskappTheme {
        val sampleTasks = remember {
            mutableStateListOf(
                Task("Pick up laundry"),
                Task("Do laundry", isCompleted = true),
            )
        }
        TaskListScreen(sampleTasks)
    }
}