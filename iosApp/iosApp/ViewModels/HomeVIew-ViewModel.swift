//
//  ContentVIew-ViewModel.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Shared

extension HomeView {
    
    class ViewModel: ObservableObject {
        @Published var toDoTasks: [ToDoTask] = []
        private(set) var task: Task<Void, Never>?
        @Published var isAuthenticated: Bool
        
        private let authService: FirestoreAuthenticationService
        private let taskRepository: FirestoreToDoTaskRepository
        
        init(authService: FirestoreAuthenticationService) {
            self.authService = authService
            self.isAuthenticated = authService.currentUser != nil
            guard let currentUserId = authService.currentUser?.uid else {
                fatalError("User is not authenticated. Cannot initialize task repository.")
            }
            
            self.taskRepository = FirestoreToDoTaskRepository(currentUserId: currentUserId)
            loadTasks()
             
        }

        func loadTasks() {
            Task {
                for try await taskList in taskRepository.getToDoTasks() {
                    DispatchQueue.main.async {
                        self.toDoTasks = taskList
                    }
                }
            }
        }
        
        
        func signOut() {
            Task {
                do {
                    try await authService.signOut()
                    DispatchQueue.main.async {
                        self.isAuthenticated = false
                    }
                } catch {
                    print(error.localizedDescription)
                }
            }
        }
        
        func toggleTaskCompletion(task: ToDoTask) async {
                let updatedTask = task
                updatedTask.isCompleted.toggle()
                
                do {
                    try await taskRepository.updateToDoTask(toDoTask: updatedTask)
                    if let index = toDoTasks.firstIndex(where: { $0.id == updatedTask.id }) {
                        DispatchQueue.main.async {
                            self.toDoTasks[index] = updatedTask
                        }
                    }
                } catch {
                    print("Failed to update task: \(error)")
                }
            }
        
        
        func updateTask(task: ToDoTask) async {
                do {
                    try await taskRepository.updateToDoTask(toDoTask: task)
                    if let index = toDoTasks.firstIndex(where: { $0.id == task.id }) {
                        DispatchQueue.main.async {
                            self.toDoTasks[index] = task
                        }
                    }
                } catch {
                    print("Failed to update task: \(error)")
                }
            }
        
        func deleteTask(task: ToDoTask) async {
                do {
                    try await taskRepository.deleteToDoTask(toDoTask: task)
                    DispatchQueue.main.async {
                        self.toDoTasks.removeAll { $0.id == task.id }
                    }
                } catch {
                    print("Failed to delete task: \(error)")
                }
            }

    }
}

