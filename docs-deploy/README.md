# Karage App API Documentation

This directory contains the auto-generated API documentation for the Karage Android application.

## Viewing the Documentation

Open `index.html` in your web browser to view the complete API documentation.

## Features

- **Complete API Coverage**: Documentation for all public classes, methods, and properties
- **Source Links**: Direct links to source code on GitHub
- **Search Functionality**: Built-in search to quickly find specific APIs
- **Dark/Light Theme**: Toggle between themes for better readability
- **Mobile Responsive**: Optimized for viewing on all devices

## Generation

The documentation is automatically generated using [Dokka](https://kotlinlang.org/docs/dokka-introduction.html) and deployed via GitHub Actions.

### Manual Generation

To generate the documentation locally:

```bash
./gradlew dokkaGenerate
```

The generated documentation will be available in `app/build/dokka/html/`.

## Structure

- **Packages**: Organized by package structure
- **Classes**: All public classes with their methods and properties
- **Source Links**: Click any class or method name to view the source code
- **Navigation**: Use the sidebar for quick navigation between packages and classes
