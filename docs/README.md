# Battleships Mobile - Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [Architecture](#architecture)
3. [Application](#application)
4. [Connection to the API](#connection-to-the-api)
5. [Authors](#authors)
6. [Critical Analysis](#critical-analysis)

## Introduction

> This document describes the implementation details of the Battleships Mobile application.

This is a simple battleship game for Android, which is a client-server application, where the server
is implemented in Kotlin using the Spring framework. The game is multiplayer and each player plays
on a different device.

The application has the following features:

* Login and Registration
* Ranking of players
* Quick Game
* Custom Game

---

## Architecture

The application is implemented using Android with Kotlin and Jetpack Compose. The application
architecture is based on the **MVVM pattern**, which is a Model-View-ViewModel pattern. The MVVM
pattern is a variation of the MVC pattern, where the controller is replaced by the ViewModel.

The ViewModel is responsible for exposing data to the view and handling the view's events. The
ViewModel is also responsible for communicating with the model, which is the service layer that
communicates with the API.

The `src/main/kotlin/pt/isel/pdm/battleships` folder contains the source code of the application,
which is divided into the following packages:

* `domain`: contains the domain models of the application;
* `service`: contains the service layer of the application, which is responsible for communicating
  with the HTTP API;
* `session`: contains the session manager, which is responsible for managing the user session;
* `ui`: contains the user interface of the application, which is divided into the following
  packages:
  * `screens`: contains the screens of the application;
  * `theme`: contains the application theme.

In the screens package, there is a package for each screen of the application, which can have three
files and a folder:

* `<ScreenName>Activity.kt`: contains the activity of the screen;
* `<ScreenName>ViewModel.kt`: contains the ViewModel of the screen;
* `<ScreenName>Screen.kt`: contains the screen (Jetpack Compose UI);
* `components`: contains the components of the screen.

This means that there is an **activity for each screen** of the application.

---

## Application

The application has the following screens:

* Home Screen
* About Screen
* Login Screen
* Registration Screen
* Ranking Screen
* GameMenu Screen
* CreateGame Screen
* QuickGame Screen
* Lobby Screen
* BoardSetup Screen
* Gameplay Screen

The game is available in three languages: English, Portuguese and Spanish.

The application screen orientation is portrait (vertical), but in the future it will be possible to
play in landscape (horizontal) mode.

---

## Connection to the API

The application communicates with the API using the [OkHttp](https://square.github.io/okhttp/)
library, which is a HTTP client for Android and Java.

The service layer of the application is responsible for communicating with the API.

The HTTP API is implemented using the Siren Hypermedia specification, which is a specification for
hypermedia-driven REST APIs, so in order to abstract the communication with the API, there are two
types of services implemented in the application:

* `BattleShipsService`: contains the methods that communicate with the API, which each method
  receives the link where the request should be sent;
* `LinkBattleShipsService`: contains the methods that communicate with the API, and each method does
  not receive a link; this instance stores the links of the API journey, and calls the methods of
  the `BattleShipsService` instance.

---

## Authors

- 48089 [André Páscoa](https://github.com/devandrepascoa)
- 48280 [André Jesus](https://github.com/andre-j3sus)
- 48287 [Nyckollas Brandão](https://github.com/Nyckoka)

Professor: Eng. Paulo Pereira

@ISEL<br>
Bachelor in Computer Science and Computer Engineering<br>
Mobile Devices Programming - LEIC51D - Group 13<br>
Winter Semester of 2022/2023

---

## Critical Analysis

We believe that the application is well structured and that the code is easy to understand. The
documentation could be improved, but we believe that it is sufficient for the purpose of this
project.

We think that the main challenge of this project was to implement the communication with the API,
because it was the first time we used the Siren Hypermedia specification.

What can be improved/added in the future:

* Implement the landscape (horizontal) mode;
* Improve the documentation;
* Improve the application design;
* Add a profile screen;
* Add more tests.
