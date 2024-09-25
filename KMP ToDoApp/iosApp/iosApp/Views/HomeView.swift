import SwiftUI
import Shared
import Combine


struct HomeView: View {
    var authService: FirestoreAuthenticationService
    @State private var viewModel: ViewModel
    @State private var showModal = false
    
    init(authService: FirestoreAuthenticationService) {
        self.authService = authService
        self.viewModel = ViewModel(authService: authService)
    }
    
    var showLogin: Bool {
            return !viewModel.isAuthenticated
        }
    
    var body: some View {
        
        ZStack {
            VStack {
                Button("Start Receiving") {
                    Task {
                        await viewModel.startObserving()
                    }
                }
                
                ToDoTaskListComponent(toDoTasks: viewModel.values, userId: authService.currentUser!.uid, onDelete: {  atPosition in
                    viewModel.deleteTask(atPosition: atPosition)
                    
                    Task {
                        await viewModel.startObserving()
                    }
                })
            }
            .padding()

            VStack {
                           HStack {
                               Button(action: {
                                   // Logout action here
                                   print("Logout pressed")
//                                   Task {
//                                       do {
//                                           try await authService.signOut()
//                                           
//                                           //LoginView()
//                                           
//                                       } catch {
//                                           print(error.localizedDescription)
//                                       }
//                                   }
                                   viewModel.signOut()
                               }) {
                                   Image(systemName: "arrow.left.square.fill")
                                       .font(.system(size: 24))
                                       .frame(width: 40, height: 40)
                                       .foregroundColor(.white)
                                       .background(Color.purple)
                                       .clipShape(RoundedRectangle(cornerRadius: 10))
                                       .shadow(radius: 5)
                               }
                               .padding(.leading)
                               .padding(.top, 20)

                               Spacer() // Keep the button at the top left
                           }
                           Spacer() // Push the "+" button to the bottom
                       }
            
            VStack {
                Spacer() // button to the bottom
                HStack {
                    Spacer() // the button to the right
                    Button(action: {
                        showModal = true
                    }) {
                        Image(systemName: "plus")
                            .font(.system(size: 24))
                            .frame(width: 60, height: 60)
                            .foregroundColor(.white)
                            .background(Color.purple)
                            .clipShape(Circle())
                            .shadow(radius: 10)
                    }
                    .padding()
                }
            }
        }
        .fullScreenCover(isPresented: .constant(showLogin)) {
            LoginView(authService: authService)
        }
         .sheet(isPresented: $showModal, onDismiss: {
            Task {
                await viewModel.startObserving() 
            }
        }) {
            ToDoTaskModalView(userId: authService.currentUser!.uid)
        }
    }
}


//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        HomeView()
//    }
//}

