//
//  ToDoTask.swift
//  iosApp
//
//  Created by Martin Hristev on 25.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Shared

extension ToDoTask: Hashable, Identifiable, Equatable {
    public static func == (lhs: ToDoTask, rhs: ToDoTask) -> Bool {
        lhs.id == rhs.id && lhs.title == rhs.title && lhs.isCompleted == rhs.isCompleted && lhs.details == rhs.details
        }
}
