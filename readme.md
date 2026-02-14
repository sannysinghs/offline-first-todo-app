# Offline-First Todo App

A modern Android application for managing tasks with offline-first capabilities, built with Kotlin and following clean architecture principles. The app features automatic background synchronization, local data persistence, and seamless offline/online transitions.

## Features

- ✅ **Offline-First Architecture**: Work seamlessly without internet connection
- 🔄 **Automatic Background Sync**: Changes sync automatically when online
- 💾 **Local Data Persistence**: Room database for reliable local storage
- 🎨 **Modern UI**: Built with Jetpack Compose and Material Design 3
- 🏗️ **Clean Architecture**: Separation of concerns with Domain, Data, and Presentation layers
- 💉 **Dependency Injection**: Dagger Hilt for scalable dependency management
- 🔧 **Work Manager**: Background task synchronization
- 📦 **DataStore**: Preferences management for sync state


## Tech Stack

### Core
- **Language**: Kotlin 1.9.22
- **Build System**: Gradle with Kotlin DSL
- **Serialization**: Kotlinx Serialization JSON

### UI Layer
- **UI Framework**: Jetpack Compose
- **Material Design**: Material 3
- **Navigation**: Navigation Component
- **Lifecycle**: ViewModel, LiveData

### Data Layer
- **Local Database**: Room 2.6.1
- **Network**: Retrofit 2.11.0 + OkHttp 4.12.0
- **Preferences**: DataStore 1.1.4
- **Background Tasks**: WorkManager 2.10.0

### Architecture & DI
- **Dependency Injection**: Dagger Hilt 2.54
- **Architecture Pattern**: Clean Architecture + MVVM


## Project Setup

- Clone the Repository
- Open in Android Studio
- Configure Backend API (Optional)

## Offline-First Architecture

### How It Works

1. **Local-First Operations**: All user actions are immediately saved to the local Room database
2. **Background Sync**: WorkManager periodically syncs local changes with the remote server
3. **Conflict Resolution**: Server changes are merged with local data using change list versioning
4. **Seamless Experience**: App remains fully functional without internet connection

### Sync Strategy

- **Automatic Sync**: Triggered on network connectivity changes
- **Periodic Sync**: Background sync every 15 minutes (configurable)
- **Manual Sync**: Pull-to-refresh gesture in the UI
- **Change Tracking**: Uses version numbers to track synchronization state

### Data Flow

```
User Action → ViewModel → UseCase → Repository → Local DB (immediate)
                                              ↓
                                         Sync Queue
                                              ↓
                                      WorkManager (background)
                                              ↓
                                         Remote API
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Contact

For questions or support, please open an issue in the repository.

---

**Happy Coding! 🚀**
