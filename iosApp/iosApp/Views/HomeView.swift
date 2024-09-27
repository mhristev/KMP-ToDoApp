import SwiftUI
import Shared
import Combine


struct HomeView: View {
    var authService: FirestoreAuthenticationService
    @StateObject private var viewModel: ViewModel
    @State private var showModal = false
    
    init(authService: FirestoreAuthenticationService) {
        self.authService = authService
        _viewModel = StateObject(wrappedValue: ViewModel(authService: authService))  
    }
    
    var showLogin: Bool {
            return !viewModel.isAuthenticated
        }
    
    var body: some View {
        
        ZStack {
            VStack {
                ToDoTaskListComponent(
                    toDoTasks: viewModel.toDoTasks,
                    onDeleteTask: { task in
                        Task {
                            await viewModel.deleteTask(task: task)
                        }
                    },
                    onToggleCompleted: { task in
                        Task {
                            await viewModel.toggleTaskCompletion(task: task)
                        }
                        
                    },
                    onUpdateTask: { task, newTitle, newDetails, newIsCompleted in
                        task.title = newTitle
                        task.details = newDetails
                        task.isCompleted = newIsCompleted
                        Task {
                            await viewModel.updateTask(task: task)
                        }
                        
                    }
                )
            }
            .padding()

            VStack {
               HStack {
                   Button(action: {
                       print("Logout pressed")
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

                   Spacer()
               }
               Spacer()
           }
            
            VStack {
                Spacer()
                HStack {
                    Spacer()
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
             viewModel.loadTasks()
        }) {
            ToDoTaskCreateView(userId: authService.currentUser!.uid)
        }
    }
}


//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        HomeView()
//    }
//}

