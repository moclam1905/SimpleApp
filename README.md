# SimpleApp - Modern Android Application

## Overview
SimpleApp is a modern Android application built with the latest Android development practices and architecture patterns. It demonstrates the implementation of a modular, scalable, and maintainable Android application.

## Project Structure
The project follows a modular architecture with the following modules:

```
├── app                     # Main application module
├── simpleapp-data          # Data layer implementation
├── simpleapp-database      # Local database implementation
├── simpleapp-model         # Domain models and entities
├── simpleapp-test          # Testing utilities and shared test resources
```

## Architecture
The application follows Clean Architecture principles with MVVM (Model-View-ViewModel) pattern:

### Layers
- **Presentation Layer (app module)**
  - Activities and Fragments
  - ViewModels
  - UI Components
  - Data Binding

- **Domain Layer (simpleapp-model)**
  - Business Logic
  - Domain Models
  - Use Cases

- **Data Layer (simpleapp-data, simpleapp-database)**
  - Repositories with Kotlin Flow
  - Local Data Sources with Room
  - Asset-based sample data
  - Repository implementations with coroutines
  - Dependency injection with Hilt
  - Data mapping between layers

## Key Features

### Modern UI Components
- Material Design Components
- TransformationLayout for smooth transitions
- Custom CardView implementations
- Responsive layouts with ConstraintLayout

### Data Binding
- Two-way data binding
- Custom binding adapters
- Layout variables and expressions

### Database
Implemented using Room Persistence Library:
- Entity definitions
- Data Access Objects (DAOs)
- Database migrations

## Technical Stack

### Core Libraries
- AndroidX
- Material Design Components
- ConstraintLayout
- Data Binding
- Room Database
- Dagger Hilt for dependency injection
- Kotlin Coroutines & Flow
- Moshi for JSON parsing

### Architecture Components
- Repository Pattern with Clean Architecture
- Coroutines for asynchronous operations
- Flow for reactive data streams
- Hilt for dependency injection
- WorkerThread annotations for background operations

### UI Components
- TransformationLayout
- MaterialCardView
- ShapeableImageView
- Custom Views

## Project Setup

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK 21 or later

### Build Configuration
- Gradle with Kotlin DSL
- BuildSrc for dependency management
- Multi-module project structure

### Gradle Properties
```properties
# Enable AndroidX
android.useAndroidX=true

# Kotlin code style
kotlin.code.style=official

# Build optimization
org.gradle.caching=true
```

## UI Implementation

### Item Layout
The item layout (`item_simple.xml`) implements a card-based design with:
- Responsive image display
- Title with dynamic text color
- Index display
- Date information
- Smooth transitions

### Detail View
The detail view (`activity_detail.xml`) features:
- Header image with custom shape
- Elevation and translation effects
- Responsive layout adaptation

## Development Practices

### Code Style
- Kotlin coding conventions
- Clean Architecture principles
- SOLID principles

### Testing
Dedicated test module (`simpleapp-test`) for:
- Unit tests
- Integration tests
- UI tests

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---

*Note: This README is a living document and will be updated as the project evolves.*