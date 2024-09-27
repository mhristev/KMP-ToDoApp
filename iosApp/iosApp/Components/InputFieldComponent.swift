//
//  InputFieldView.swift
//  iosApp
//
//  Created by Martin Hristev on 23.09.24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct InputFieldComponent: View {
    
    @Binding var data: String
    var title: String?
    
    var body: some View {
      ZStack {
          if title?.lowercased() == "password" {
              SecureField("", text: $data)
                  .padding(.horizontal, 10)
                  .frame(height: 42)
                  .overlay(
                      RoundedRectangle(cornerSize: CGSize(width: 4, height: 4))
                          .stroke(Color.gray, lineWidth: 1)
                  )
          } else {
              TextField("", text: $data)
                  .padding(.horizontal, 10)
                  .frame(height: 42)
                  .overlay(
                      RoundedRectangle(cornerSize: CGSize(width: 4, height: 4))
                          .stroke(Color.gray, lineWidth: 1)
                  )
          }
        HStack {
          Text(title ?? "Input")
            .font(.headline)
            .fontWeight(.thin)
            .foregroundColor(Color.gray)
            .multilineTextAlignment(.leading)
            .padding(4)
            .background(.white)
          Spacer()
        }
        .padding(.leading, 8)
        .offset(CGSize(width: 0, height: -20))
      }.padding(4)
    }
}

struct InputFieldView_Previews: PreviewProvider {
    @State static var data: String = ""
    
    static var previews: some View {
        InputFieldComponent(data: $data, title: "Password")
    }
}
