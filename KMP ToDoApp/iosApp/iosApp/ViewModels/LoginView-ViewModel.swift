//
//  LoginView-ViewModel.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

import Shared

extension LoginView {
    
    @Observable
    class ViewModel {
        var email: String = ""
        var password: String = ""
        var errorMessage: String?
        var isAuthenticated: Bool = false
        
        private let authService: FirestoreAuthenticationService
        
        init(authService: FirestoreAuthenticationService) {
            self.authService = authService
        }
        
        
        func checkIsAuthenticated() -> Bool {
            isAuthenticated = authService.currentUser != nil
            return isAuthenticated
        }
        
        func signIn() {
            Task {
                do {
                    let appUser = try await authService.signIn(email: email, password: password)
                    print(appUser?.id)
                    print(email)
                    print(password)
                    if appUser != nil {
                        DispatchQueue.main.async {
                            self.isAuthenticated = true
                        }
                    }
                    print("User successfully loged in.")
                } catch let error {
                    print(error.self)
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        self.errorMessage = "Sign in failed:" + error.localizedDescription
                    }
                }
            }
        }
        
        func signUp() {
            Task {
                do {
                    let appUser = try await authService.signUp(email: email, password: password)
                    if appUser != nil {
                        DispatchQueue.main.async {
                            self.isAuthenticated = true
                        }
                    }
                } catch {
                    DispatchQueue.main.async {
                        self.errorMessage = "Sign up failed: \(error.localizedDescription)"
                    }
                }
            }
        }
        
        
        
        
        
    }
}
        
