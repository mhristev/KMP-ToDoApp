//
//  ContentView2.swift
//  iosApp
//
//  Created by Martin Hristev on 24.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct ContentView: View {
    private var authService = FirestoreAuthenticationService()
    
    var body: some View {
        guard let _ = authService.currentUser else {
            return AnyView(LoginView(authService: authService))
        }
        
        return AnyView(HomeView(authService: authService))
    }
}

#Preview {
    ContentView()
}
