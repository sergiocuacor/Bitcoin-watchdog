# Bitcoin Watchdog 🔍

Bitcoin Watchdog is a monitoring system built with Spring Boot that tracks key Bitcoin network metrics and sends alerts when specific conditions are met. The application currently monitors three essential market indicators daily:
- Puell Multiple
- MVRV-Z Score
- NUPL (Net Unrealized Profit/Loss)

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
  - [Email Settings](#email-settings)
  - [Alert Thresholds](#alert-thresholds)
- [Scheduling](#scheduling)
  - [macOS](#macos)
  - [Linux](#linux)
  - [Windows](#windows)
- [Usage](#usage)
  - [Manual Execution](#manual-execution)
- [Architecture](#architecture)
  - [Core Components](#core-components)
- [Configuration](#configuration-1)
  - [Application Properties](#application-properties)
  - [Environment Variables](#environment-variables-env)

## Features 
- Customizable alert thresholds
- Email notifications for significant market conditions
- Automated daily execution (macOS → Launchd, Linux → cron, Windows → Task Scheduler)
- Detailed logging

## Prerequisites
- Java 17 or higher
- Maven
- Gmail account (for notifications)

## Installation 
1. **Clone the repository**
```bash
git clone https://github.com/sergiocuacor/bitcoin-watchdog.git
cd bitcoin-watchdog
```

2. **Configure environment variables**
```bash
# Copy the template and edit with your values
cp .env.template .env
```

3. **Build the application**
```bash
mvn clean package
```

## Configuration

### Email Settings
Create a Gmail App Password and configure it in your `.env` file (see `.env.template`):
```properties
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_app_specific_password
```

### Alert Thresholds
Current default thresholds:
```
Puell Multiple: < 0.6
NUPL: < 0.2
MVRV-Z Score: < 0.5
```

## Scheduling

> **Note:** To avoid consuming unnecessary resources, we configure a schedule for the program to execute.
> By default, it runs once a day, with a 1-day delay to avoid inaccurate values from the external API.
> This shouldn't be a concern for decision-making given that the conditions evaluated often give market participants weeks or months to act on them.

### macOS
Using launchd:
```bash
# Create a .plist file within ~/Library/LaunchAgents/
# Then configure it with your paths
```
```xml
<!-- ~/Library/LaunchAgents/com.bitcoinwatchdog.plist -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN">
<plist version="1.0">
<dict>
    <key>Label</key>
    <string>com.bitcoinwatchdog</string>
    <key>ProgramArguments</key>
    <array>
        <string>/path/to/Bitcoin-watchdog/scripts/run-watchdog.sh</string>
    </array>
    <key>StartCalendarInterval</key>
    <dict>
        <key>Hour</key>
        <integer>21</integer>
        <key>Minute</key>
        <integer>0</integer>
    </dict>
</dict>
</plist>
```
```bash
# Lastly, load the service:
launchctl load ~/Library/LaunchAgents/com.bitcoinwatchdog.plist
```

### Linux
Using cron:
```bash
# Add to crontab (runs at 21:00 daily)
0 21 * * * /path/to/bitcoin-watchdog/scripts/run-watchdog.sh
```

### Windows
Using Task Scheduler:
1. Open Task Scheduler
2. Create Basic Task
3. Set trigger: Daily at your preferred time (21:00 by default)
4. Action: Start Program
5. Program/script: path to run-watchdog.bat

## Usage

### Manual Execution
```bash
# Unix-like systems
./scripts/run-watchdog.sh

# Windows
scripts\run-watchdog.bat
```

## Architecture

### Core Components

1. **Models** (`com.bitcoinwatchdog.metrics.model`)
- `BaseMetric`: Abstract base class for all metrics
- `PuellMultiple`: Puell Multiple indicator data
- `NUPL`: Net Unrealized Profit/Loss data
- `MVRVZScore`: MVRV-Z Score data

2. **API Client** (`com.bitcoinwatchdog.metrics.client`)
- `MetricsApiClient`: Handles HTTP calls to an external API, in this case bitcoin-data.com/v1
- Endpoints:
  - `/puell-multiple`
  - `/nupl`
  - `/mvrv-zscore`

3. **Service Layer** (`com.bitcoinwatchdog.metrics.service`)
- `MetricsService`: Core business logic
  - Fetches daily metrics
  - Evaluates conditions
  - Triggers alerts
- Alert Thresholds:
  ```java
  PUELL_MULTIPLE_THRESHOLD = 0.6
  NUPL_THRESHOLD = 0.2
  MVRVZ_THRESHOLD = -0.5
  ```

4. **Notification** (`com.bitcoinwatchdog.notification`)
- `EmailService`: Handles email notifications using Gmail SMTP

## Configuration

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

### Environment Variables (.env)
```properties
EMAIL_USERNAME=app_email@gmail.com
EMAIL_PASSWORD=gmail_app_specific_password
```

---

<div align="center">

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>
