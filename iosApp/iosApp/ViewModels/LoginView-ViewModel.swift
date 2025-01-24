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
    
    @MainActor
    @Observable
    class ViewModel {
        var email: String = ""
        var password: String = ""
        var errorMessage: String?
        var isAuthenticated: Bool = false
        var isSignUp: Bool = false
        
        private let authService: FirestoreAuthenticationService
        
        init(authService: FirestoreAuthenticationService) {
            self.authService = authService
        }
        
        func checkIsAuthenticated() -> Bool {
            isAuthenticated = authService.currentUser != nil
            return isAuthenticated
        }
        
        func toggleSignUpLogin() {
            isSignUp.toggle()
        }
        
        func signIn() {
            Task {
                do {
                    let appUser = try await authService.signIn(email: email, password: password)

                    if appUser != nil {
                        isAuthenticated = true
                    }
                    print("User successfully loged in.")
                } catch let error {
                    print(error.self)
                    print(error.localizedDescription)
                    errorMessage = "Sign in failed:" + error.localizedDescription
                }
            }
        }
        
        func signUp() {
            Task {
                do {
                    let appUser = try await authService.signUp(email: email, password: password)
                    if appUser != nil {
                        isAuthenticated = true
                    }
                } catch {
                    self.errorMessage = "Sign up failed: \(error.localizedDescription)"
                }
            }
        }
        
    }
}
        
