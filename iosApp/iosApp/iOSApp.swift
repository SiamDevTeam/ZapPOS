import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        MainViewControllerKt.initializeDatabase()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}