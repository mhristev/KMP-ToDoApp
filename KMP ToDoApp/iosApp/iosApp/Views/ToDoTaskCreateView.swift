//
//  ToDoTaskModal.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ToDoTaskCreateView: View {
    @State private var viewModel: ViewModel
    @Environment(\.dismiss) var dismiss
    
    init(userId: String) {
        viewModel = ViewModel(userId: userId)
    }

    var body: some View {
        VStack(spacing: 20) {
            Text("Add New Task")
                .font(.title)
                .fontWeight(.bold)
            
            TextField("Task Title", text: $viewModel.title)
                .padding()
                .background(Color(UIColor.systemGray6))
                .cornerRadius(10)
                .padding(.horizontal)
            
            TextField("Task Description", text: $viewModel.details)
                .padding()
                .background(Color(UIColor.systemGray6))
                .cornerRadius(10)
                .padding(.horizontal)

            
            Button(action: {
                if !viewModel.isSaveDisabled {
                    viewModel.onSave()
                    dismiss()
                }
            }) {
                Text("Save")
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .foregroundColor(.white)
                    .background(Color.blue)
                    .cornerRadius(10)
                    .padding(.horizontal)
            }
            .disabled(viewModel.isSaveDisabled)
        }
        .padding()
    }
}
