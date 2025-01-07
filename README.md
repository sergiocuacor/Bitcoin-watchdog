# Bitcoin Watchdog 🔍
Bitcoin Watchdog is a Java application that analyzes key Bitcoin market metrics daily and alerts users when specific market conditions are met. Built with Spring Boot and scheduled through GitHub Actions, it evaluates three essential market indicators:
- Puell Multiple
- MVRV-Z Score
- NUPL (Net Unrealized Profit/Loss)

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [GitHub Actions Workflow](#github-actions-workflow)
- [Configuration](#configuration)
  - [Email Settings](#email-settings)
  - [Alert Thresholds](#alert-thresholds)
  - [Application Properties](#application-properties)
- [Usage](#usage)
  - [Cloud Execution](#cloud-execution)
  - [Local Development](#local-development)
- [Architecture](#architecture)
  - [Core Components](#core-components)


## Features 
- Daily analysis of Bitcoin network metrics
- Customizable alert thresholds
- Email alerts when metrics fall below defined thresholds
- Automated daily execution through GitHub Actions
- Detailed logging


## Prerequisites
- Java 17 or higher
- Maven
- Gmail account (for notifications)
- GitHub account


## Setup

1. **Clone the repository**
```bash
git clone https://github.com/sergiocuacor/bitcoin-watchdog.git
cd bitcoin-watchdog
```


2. **Configure GitHub Secrets**
- Fork this repository
- Go to Settings → Secrets and variables → Actions
- Add the following secrets:
  ```
  EMAIL_USERNAME: your Gmail address
  EMAIL_PASSWORD: your Gmail app password
  ```


3. **Build for local testing** (optional)
```bash
cp .env.template .env
# Edit .env with your credentials
mvn clean package
```


## GitHub Actions Workflow

The application uses GitHub Actions for automated execution. Have a look at `.github/workflows/daily-run.yml`:

```yaml
name: Daily Bitcoin Watchdog Run

on:
  schedule:
    - cron: '0 19 * * *'  # Runs daily at 19:00 UTC
  workflow_dispatch:       # Allows manual execution

jobs:
  run-watchdog:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Build with Maven
      run: mvn clean package
        
    - name: List target directory
      run: |
        echo "Contents of target directory:"
        ls -la target/
        
    - name: Find JAR file
      run: |
        echo "Looking for JAR files:"
        find . -name "*.jar"
        
    - name: Run Watchdog
      env:
        EMAIL_USERNAME: ${{ secrets.EMAIL_USERNAME }}
        EMAIL_PASSWORD: ${{ secrets.EMAIL_PASSWORD }}
      run: |
        JAR_FILE=$(find target/ -name "*.jar" ! -name "*sources.jar" ! -name "*javadoc.jar" | head -1)
        echo "Found JAR file: $JAR_FILE"
        java -jar "$JAR_FILE"
```

This workflow:
- Runs automatically at 19:00 UTC daily
- Can be triggered manually through the Actions tab
- Uses Ubuntu as the execution environment
- Sets up Java 17 and builds the project
- Uses GitHub Secrets for secure credential management


## Configuration

### Email Settings
For cloud execution, configure through GitHub Secrets as described in the Setup section.

For local development, create a `.env` file based on the template:
```properties
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_app_specific_password
```

### Alert Thresholds
Default thresholds:
```
Puell Multiple: < 0.6
NUPL: < 0.2
MVRV-Z Score: < 0.5
```

### Application Properties
```yaml
# application.yml
api:
  base:
    url: "https://bitcoin-data.com/v1"

spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```


## Usage

### Cloud Execution
The application runs automatically through GitHub Actions:
- Scheduled daily at 19:00 UTC
- View execution history in the Actions tab
- Email notifications for significant market conditions
- Can be manually triggered from the Actions tab for testing

### Local Development
For testing and development purposes:
```bash
./scripts/run-watchdog.sh
```
The script will:
- Check for environment variables
- Find and execute the correct JAR file
- Display relevant logs


## Architecture

### Core Components

1. **Models** (`com.bitcoinwatchdog.metrics.model`)
- `BaseMetric`: Abstract base class for all metrics
- `PuellMultiple`: Puell Multiple indicator data
- `NUPL`: Net Unrealized Profit/Loss data
- `MVRVZScore`: MVRV-Z Score data
  
2. **API Client** (`com.bitcoinwatchdog.metrics.client`)
- `MetricsApiClient`: Handles HTTP calls to the external API
- Endpoints:
  - `/puell-multiple`
  - `/nupl`
  - `/mvrv-zscore`

3. **Service Layer** (`com.bitcoinwatchdog.metrics.service`)
- `MetricsService`: Core business logic
  - Fetches daily metrics
  - Evaluates conditions
  - Triggers alerts

4. **Notification** (`com.bitcoinwatchdog.notification`)
- `EmailService`: Handles email notifications using Gmail SMTP


---

<div align="center">

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>
