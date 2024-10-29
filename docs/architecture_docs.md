# Project Architecture Documentation

## Overview

This document provides an overview of the architecture of the Trading212 Assistant project. It outlines the main components, packages, and their responsibilities.

## Package Structure

### 1. `docs/`

Contains documentation files related to the project, including user manuals and architecture documents.

- `resources/`: Contains image resources
  - `csv_details_page.png`: [Image for CSV details page.](./resources/landing_page.png)
  - `csv_list_page.png`: [Image for CSV list page.](./resources/csv_list_page.png)
  - `landing_page.png`: [Image for the landing page.](./resources/csv_details_page.png)

- `architecture_docs.md`: [Documentation on architecture.](./architecture_docs.md)
- `user_docs.md`: [Documentation for users.](./user_docs.md)

### 2. `src/`

The main source directory for the project.

#### 2.1. `main/`

Contains the main application code.

- `java/`: The Java source files for the application.
  - **`api/`**
    - `TradingApiCommunicator.java`: Handles API communication.
    - `TradingRequestFormatter.java`: Formats trading requests for API calls.

  - **`app/`**
    - `Main.java`: Main entry point for the application.

    - `controllers/`: Contains controllers that manage user interactions and data flow.
      - `BaseController.java`: Base class for all controllers.
      - `DocumentDetailsController.java`: Manages document detail views.
      - `DocumentsController.java`: Handles document listing and actions.
      - `LandingController.java`: Manages the landing page.
      - `MainLayoutController.java`: Controls the main layout of the application.

    - **`models/`**: Contains the data models used within the application.
      - `ActionType.java`: Represents different action types.
      - `DataIncluded.java`: Represents data inclusion options.
      - `DocumentsListItem.java`: Represents individual items in document lists.
      - `Status.java`: Represents the status of various components.
      
    - **`store/`**: Contains classes responsible for data storage and management.
      - `DocumentsListItemStore.java`: Manages storage of document list items.
    
    - **`data/`**: Contains downloaded CSV files.

    - **`utils/`**: Contains utility classes.
      - **`parsers/`**: Contains parser classes for various data types.
        - `DividendsParser.java`: Parses dividend data.
        - `InterestParser.java`: Parses interest data.
        - `OrdersParser.java`: Parses order data.
        - `TransactionsParser.java`: Parses transaction data.
        - `Parser.java`: Base class for all parsers.
      - `AlertDialog.java`: Utility for displaying alert dialogs.
      - `CountDownTimer.java`: Utility for managing countdown timers.
      - `CsvManager.java`: Manages CSV file operations.
      - `KeySaver.java`: Utility for saving keys.
      - `ViewManager.java`: Manages views within the application.
      - `App.java`: Application configuration and startup.

  - **`resources/`**: Contains resources used by the application.
    - **`views/`**: Contains FXML files for UI views.
      - `DocumentDetailsView.fxml`: FXML for document details view.
      - `DocumentsView.fxml`: FXML for documents view.
      - `LandingView.fxml`: FXML for the landing page.
      - `MainLayout.fxml`: FXML for the main layout.
    - `api_key.txt`: Contains API key for authentication.

#### 2.2. `test/`

Contains test classes for various components of the application.

- `java/`
  - `DividendsParserTests.java`: Tests for dividend parser functionality.
  - `InterestParserTests.java`: Tests for interest parser functionality.
  - `OrdersParserTests.java`: Tests for orders parser functionality.
  - `TransactionsParserTests.java`: Tests for transaction parser functionality.

## Conclusion

This document provides a high-level view of the project structure and its components. For more detailed information, refer to the source code files which provide docstrings with more specific functionality descriptions.