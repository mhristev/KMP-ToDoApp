////
////  ContentVIew-ViewModel.swift
////  iosApp
////
////  Created by Martin Hristev on 23.09.24.
////  Copyright © 2024 orgName. All rights reserved.
////

import Foundation
import Shared

extension HomeView {
    
    @Observable
    class ViewModel {
        var values: [ToDoTask] = []
        private(set) var task: Task<Void, Never>?
        var isAuthenticated: Bool
        
        private let authService: FirestoreAuthenticationService
        
        init(authService: FirestoreAuthenticationService) {
            self.authService = authService
            self.isAuthenticated = authService.currentUser != nil
        }
        
        
        func startObserving() async {
            
            self.values.removeAll()
            
            task?.cancel()
            
            task = Task {
                guard let id = authService.currentUser?.uid else {
                    print("Current user does not exit")
                    return
                }
                let foundTasks = FirestoreToDoTaskRepository(currentUserId: id).getToDoTasks()
                
                do {
                    for try await taskValues in foundTasks {
                        self.updateTasks(with: taskValues)
                    }
                } catch {
                    print("Error collecting users: \(error.localizedDescription)")
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
        
        private func updateTasks(with taskValues: [Any]) {
            self.values.removeAll()
        
            for task in taskValues {
                if let task = task as? ToDoTask {
                    self.values.append(task)
                } else {
                    print("Error: task is not of type ToDoTask")
                }
            }
        }
        
        func deleteTask(atPosition: Int) {
            Task {
                do {
                    try await FirestoreToDoTaskRepository(currentUserId: authService.currentUser!.uid).deleteToDoTask(toDoTask: values[atPosition])
                } catch {
                    print(error.localizedDescription)
                }
            }
        }

    }
}

