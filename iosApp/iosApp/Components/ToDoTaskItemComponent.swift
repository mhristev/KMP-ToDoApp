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
    var isCompleted: Bool
    
    var onToggleCompleted: () -> Void
    var onUpdateTask: (_ updatedTitle: String, _ updatedDetails: String, _ updatedIsCompleted: Bool) -> Void
    
    @State private var showTaskModal = false

    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(title)
                    .font(.headline)
                    .strikethrough(isCompleted, color: .gray)
                
                Text(details)
                    .font(.subheadline)
                    .strikethrough(isCompleted, color: .gray)
            }
            .contentShape(Rectangle())
            .onTapGesture {
                showTaskModal = true
            }
            
            Spacer()
            
            Button(action: {
                onToggleCompleted()
            }) {
                Image(systemName: isCompleted ? "checkmark.circle.fill" : "circle")
                    .resizable()
                    .frame(width: 24, height: 24)
                    .foregroundColor(isCompleted ? .green : .gray)
            }
            .buttonStyle(PlainButtonStyle())
        }
        .padding()
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(Color.gray, lineWidth: 1)
        )
        .sheet(isPresented: $showTaskModal) {
            ToDoTaskDetailView(
                title: title,
                details: details,
                isCompleted: isCompleted,
                onSave: { newTitle, newDetails, newIsCompleted in
                    onUpdateTask(newTitle, newDetails, newIsCompleted)
                }
            )
        }
        
    }
}

//#Preview {
//    ToDoTaskItemView(title: "Sample Task", details: "This is a description of the task.", isCompleted: false, onToggleCompleted: {})
//}
