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
    
    var sortedTasks: [ToDoTask] {
        toDoTasks.sorted { !$0.isCompleted && $1.isCompleted }
    }
    var body: some View {
        List {
            ForEach(sortedTasks.indices, id: \.self) { index in
                        ToDoTaskItemComponent(
                            title: sortedTasks[index].title,
                            details: sortedTasks[index].details ?? "",
                            isCompleted: sortedTasks[index].isCompleted,
                            onToggleCompleted: {
                                onToggleCompleted(sortedTasks[index])
                            },
                            onUpdateTask: { updatedTitle, updatedDetails, updatedIsCompleted in
                                onUpdateTask(sortedTasks[index], updatedTitle, updatedDetails, updatedIsCompleted)
                            }
                        )
        }.onDelete { indexSet in
            indexSet.forEach { index in
                let task = sortedTasks[index]
                onDeleteTask(task)
            }
        }
        }.scrollContentBackground(.hidden).background(Color.white)
    }

}

//#Preview {
//    ToDoTaskListView()
//}
