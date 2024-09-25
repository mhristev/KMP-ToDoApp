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
    var toDoTasks: [ToDoTask]
    var onDeleteTask: (ToDoTask) -> Void
    var onToggleCompleted: (ToDoTask) -> Void
    var onUpdateTask: (ToDoTask, String, String, Bool) -> Void


    var body: some View {
        List {
        ForEach(toDoTasks.indices, id: \.self) { index in
                        ToDoTaskItemComponent(
                            title: toDoTasks[index].title,
                            details: toDoTasks[index].details ?? "",
                            isCompleted: toDoTasks[index].isCompleted,
                            onToggleCompleted: {
                                onToggleCompleted(toDoTasks[index])
                            },
                            onUpdateTask: { updatedTitle, updatedDetails, updatedIsCompleted in
                                onUpdateTask(toDoTasks[index], updatedTitle, updatedDetails, updatedIsCompleted)
                            }
                        )
        }.onDelete { indexSet in
            indexSet.forEach { index in
                let task = toDoTasks[index]
                onDeleteTask(task)
            }
        }
        }.scrollContentBackground(.hidden).background(Color.white)
    }

}

//#Preview {
//    ToDoTaskListView()
//}
