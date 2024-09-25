//
//  ToDoTaskListView.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct ToDoTaskListComponent: View {
    var toDoTasks: Array<ToDoTask> = []
    let taskRepostiory: FirestoreToDoTaskRepository
    
    var onDelete: (_ atPosition: Int) -> Void
//    var onToggleCompleted: (_ isCompleted: Bool) async -> Void
//    var onUpdateTask: (_ title: String, _ details: String, _ isCompleted: Bool) async -> Void
//
//    
    init(toDoTasks: Array<ToDoTask>, userId: String, onDelete: @escaping (_ atPosition: Int) -> Void) {
        self.toDoTasks = toDoTasks
        self.taskRepostiory = FirestoreToDoTaskRepository(currentUserId: userId)
        self.onDelete = onDelete
    }
    var body: some View {
        List {
        ForEach(toDoTasks.indices, id: \.self) { index in
                        ToDoTaskItemComponent(
                            title: toDoTasks[index].title,
                            details: toDoTasks[index].details ?? "",
                            isCompleted:  Binding(
                                get: { toDoTasks[index].isCompleted }, // Get the current value
                                set: { newValue in
                                    toDoTasks[index].isCompleted = newValue // Update the value when changed
                                }
                            ),
                            onToggleCompleted: { isCompleted in
                                var task = toDoTasks[index]
                                task.isCompleted = isCompleted
                                try? await taskRepostiory.updateToDoTask(toDoTask: task)
                            }
                        )
        }.onDelete(perform: deleteTask)
        }
    }
    
//    func loadToDoTasks() {
//        Task {
//            let foundTasks = taskRepostiory.getToDoTasks()
//            do {
//                for try await taskValues in foundTasks {
//                    self.updateTasks(with: taskValues)
//                }
//            } catch {
//                print("Error collecting users: \(error.localizedDescription)")
//            }
//        }
//    }
//    private func updateTasks(with taskValues: [Any]) {
//        self.toD.removeAll()
//    
//        for task in taskValues {
//            if let task = task as? ToDoTask {
//                self.values.append(task)
//            } else {
//                print("Error: task is not of type ToDoTask")
//            }
//        }
//    }
    func deleteTask(at offsets: IndexSet) {
            for index in offsets {
                onDelete(index)
//                let taskToDelete = toDoTasks[index]
//                
//                Task {
//                    do {
//                        try await taskRepostiory.deleteToDoTask(toDoTask: taskToDelete)
//                    
//                    } catch {
//                        print("Failed to delete task: \(error)")
//                    }
//                }
            }
        
        }
}

//#Preview {
//    ToDoTaskListView()
//}
