//
//  LoginView.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Shared

struct LoginView: View {
    
    @State var email: String = ""
    @State var password: String = ""
    
    @State private var viewModel: ViewModel
    private var authService: FirestoreAuthenticationService
    
    init(authService: FirestoreAuthenticationService) {
        self.viewModel = ViewModel(authService: authService)
        self.authService = authService
    }
    
    
    var body: some View {
        NavigationStack {
            VStack {
                Text("Welcome")
                    .font(.largeTitle)
                    .fontWeight(.black)
                    .padding(.bottom, 42)
                VStack(spacing: 15.0) {
                    InputFieldComponent(data: $viewModel.email, title: "Email")
                    InputFieldComponent(data: $viewModel.password, title: "Password")
                }.padding(.bottom, 16)
                Button(action: {
                    viewModel.signIn()
                }) {
                    Text("Sign In")
                        .fontWeight(.heavy)
                        .font(.title2)
                        .frame(maxWidth: .infinity)
                        .cornerRadius(40)
                }
                .buttonStyle(.borderedProminent)
                .tint(.purple)
                
                if let errorMessage = viewModel.errorMessage {
                    Text(errorMessage)
                        .foregroundColor(.red)
                        .padding()
                }
                
            }.padding()
                .fullScreenCover(isPresented: $viewModel.isAuthenticated) {
                    HomeView(authService: authService)
                }
        }
    }
}
