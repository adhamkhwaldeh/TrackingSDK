# Module LivetrackingSdk

# Integration Guide: [Weather Gini SDK]

## Table of Contents
1. [Introduction]
2. [Prerequisites]
3. [Setup Instructions]
    1. [Step 1: Install Dependencies]
    2. [Step 2: Configure the Integration]
    3. [Step 3: Test the Integration]
4. [Authentication]
5. [API Reference]
6. [Common Errors & Troubleshooting]
7. [FAQs]
8. [Support]

---

## Introduction

This guide provides step-by-step instructions for integrating [Weather Sdk] into your project. 
Follow the instructions to quickly get started and connect your system with [Weather Sdk].

---

## Prerequisites
Before you begin the integration process, make sure you have the following:
- A ApiKey service from [Weather Sdk]
- Access to your [system’s] configuration settings

## Setup Instructions

### Step 1: Install Dependencies
Start by installing the required dependencies to your project. 
Use one of the following commands based on your package manager.
    use Gradle:

       repositories {
           google()
           mavenCentral()
       }
       
       dependencies {
          implementation 'com.gini.weatherSdk:latest'
       }

### Step 2: Configure the Integration

Next, you’ll need to configure your integration settings. 
Add the following details in your application class .
      WeatherGiniSDKBuilder.initialize(
            this,
            "your api key"
      )
### Step 3: Test the Integration

#### Update sdk status to launch 
        WeatherGiniSDKBuilder.sdkStatus.value =
                            WeatherSdkStatus.OnLaunchForecast(cityName)

#### Observe SDK status and replace it with  ForecastScreenFragment
     WeatherGiniSDKBuilder.sdkStatus.observe(this) {
            if (it is WeatherSdkStatus.OnFinish) {
                replace(EnterCityScreenFragment(), EnterCityScreenFragment::class.java.name)
            } else if (it is WeatherSdkStatus.OnLaunchForecast) {
                replace(
                    ForecastScreenFragment.newInstance(it.cityName),
                    ForecastScreenFragment::class.java.name
                )
            }
        }

#### Observe SDK status and replace it with ForecastScreen if you are using compose 
    val sdkStatus = WeatherGiniSDKBuilder.sdkStatus.observeAsState()
    LaunchedEffect(sdkStatus.value) {
        val current = sdkStatus.value
        if (current is WeatherSdkStatus.OnFinish) {
            navController.navigateUp()
        } else if (current is WeatherSdkStatus.OnLaunchForecast) {
            navController.navigate(NavigationItem.forecastRouteWithParams(current.cityName))
        }
    }

#### You can replace ForecastScreenFragment.newInstance(it.cityName) or ForecastScreen compose directly

## Authorization
   You need to get Gini Weather Api key (e.g register to website )

## Api reference
   https://www.weatherbit.io/
   https://www.weatherbit.io/api/weather-current
   https://www.weatherbit.io/api/weather-forecast-hourly

## Common Errors & Troubleshooting
   https://github.com/adhamkhwaldeh/WeatherSdk/issues

## FAQs
   https://github.com/adhamkhwaldeh/WeatherSdk/issues

## Support
   https://github.com/adhamkhwaldeh/WeatherSdk
