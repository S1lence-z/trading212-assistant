# Trading212 Assistant

Trading212 Assistant is a JavaFX application that interacts with the Trading212 API to manage and analyze trading data, including orders, dividends, and interest.

## Features

- Fetch and manage trading data
- Generate reports for orders, dividends, and interest
- Export trading history
- User-friendly interface built with JavaFX

## Prerequisites

Make sure you have the following installed on your machine:

- Java Development Kit (JDK) 21 or later
- Maven 3.6.0 or later

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/Trading212Assistant.git
cd Trading212Assistant
```

### Build the Project

To build the project, run the following command:

```bash
mvn clean package
```

### Run the Application

To run the JavaFX application, use the following command:

```bash
mvn javafx:run
```

> Note: Ensure that you have the necessary JavaFX SDK configured in your `pom.xml`.

## Usage

1. Start the application using the command mentioned above.
2. Use the UI to interact with the Trading212 API.
3. Follow the prompts to fetch and manage your trading data.

## API Key

Before using the application, make sure you have generate you unique Trading212 API key. You can do this in the Trading212 mobile app, by goind to settings, clicking on API (beta) and generating an api key. You have to have export history enabled.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Trading212 API](https://t212public-api-docs.redoc.ly/)
- [JavaFX](https://openjfx.io/)