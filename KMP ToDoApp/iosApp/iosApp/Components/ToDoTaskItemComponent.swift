//
//  ToDoTaskItemView.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ToDoTaskItemComponent: View {
    let title: String
    let details: String
    @Binding var isCompleted: Bool
    var onToggleCompleted: (_ isCompleted: Bool) async -> Void
    
    @State private var isLoading = false
    @State private var showTaskModal = false
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(title)
                    .font(.headline)
                    .foregroundColor(.primary)
                    .strikethrough(isCompleted, color: .black)
                
                Text(details)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .padding(.top, 2)
                    .strikethrough(isCompleted, color: .gray)
            }
            
            
            Spacer()

            if isLoading {
                ProgressView()
                    .frame(width: 24, height: 24)
            } else {
                Button(action: {
                    Task {
                        isLoading = true
                        isCompleted.toggle()
                        await onToggleCompleted(self.isCompleted)
                        isLoading = false
                    }
                }) {
                    Image(systemName: isCompleted ? "checkmark.circle.fill" : "circle")
                        .resizable()
                        .frame(width: 24, height: 24)
                        .foregroundColor(isCompleted ? .green : .gray)
                }
                .buttonStyle(PlainButtonStyle())
            }
        }
        .contentShape(Rectangle())
        .onTapGesture {
            showTaskModal = true
        }
        .padding()
        .sheet(isPresented: $showTaskModal) {
            ToDoTaskModalView(userId: "2uTw76whdwW37bOkqQgFCNnFEpi1")
        }
    }
}

//#Preview {
//    ToDoTaskItemView(title: "Sample Task", details: "This is a description of the task.", isCompleted: false, onToggleCompleted: {})
//}
