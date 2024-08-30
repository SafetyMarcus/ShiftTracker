//
//  AppDelegate.swift
//  iosApp
//
//  Created by Marcus Hooper on 30/8/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import Foundation
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    FirebaseApp.configure()

    return true
  }
}
