//
//  ContentView2.swift
//  iosApp
//
//  Created by Martin Hristev on 24.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared
import FirebaseAuth

// Do I inject the model view or initialize it in the view? (testing easier when its injected?)


struct ContentView: View {
    @State private var authService = FirestoreAuthenticationService()
    @State private var isAuthenticated = false

    var body: some View {
        VStack {
            if isAuthenticated {
                HomeView(authService: authService)
            } else {
                LoginView(authService: authService)
            }
        }
        .onAppear {
            setupAuthStateListener()
        }
    }
    
    // This function will listen for changes in the authentication state
    private func setupAuthStateListener() {
        Auth.auth().addStateDidChangeListener { auth, user in
            if let _ = user {
                isAuthenticated = true
            } else {
                isAuthenticated = false
            }
        }
    }
}

#Preview {
    ContentView()
}
