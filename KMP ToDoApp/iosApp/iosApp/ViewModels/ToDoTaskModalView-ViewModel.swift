//
//  ToDoTaskModalView-ViewModel.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Shared

extension ToDoTaskCreateView {
    
    @Observable
    class ViewModel {
        var title: String = ""
        var details: String = ""
   
        private var userId: String
        
        private var taskRepository: FirestoreToDoTaskRepository
    
        init(userId: String) {
            self.userId = userId
            self.taskRepository = FirestoreToDoTaskRepository(currentUserId: userId)
        }
        
        func onSave() {
            print("saved" + " " + title + " " + details + userId)
            let task = ToDoTask(title: title, details: details, isCompleted: false)
            
            Task {
                do {
                    try await taskRepository.addToDoTask(toDoTask: task)
                } catch {
                    print(error.localizedDescription)                }
            }
            
        }
        
        var isSaveDisabled: Bool {
            return title.isEmpty || details.isEmpty
        }
        
    }
}
