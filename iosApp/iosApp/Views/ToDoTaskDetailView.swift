//
//  ToDoTaskDetailView.swift
//  iosApp
//
//  Created by Martin Hristev on 25.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI


struct ToDoTaskDetailView: View {
    @State var title: String
    @State var details: String
    @State var isCompleted: Bool

    var onSave: (_ title: String, _ details: String, _ isCompleted: Bool) async -> Void

    @Environment(\.dismiss) var dismiss

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {

            TextField("Task Title", text: $title)
                .font(.title)
                .padding()
                .background(Color(UIColor.systemGray6))
                .cornerRadius(10)

            TextField("Task Details", text: $details)
                .font(.body)
                .padding()
                .background(Color(UIColor.systemGray6))
                .cornerRadius(10)

            Toggle(isOn: $isCompleted) {
                Text("Mark as Completed")
                    .font(.headline)
            }
            .padding()

            Spacer()

            Button(action: {
                Task {
                    await onSave(title, details, isCompleted)
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
        }
        .padding()
        .navigationTitle("Edit Task")
    }
}
//#Preview {
//    ToDoTaskDetailView()
//}
