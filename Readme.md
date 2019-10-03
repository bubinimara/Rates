# Rates
This is a use case application to show the commons technique and patterns to build a Clean, Solid and Testable application.

An Android application show currency exchange rates in real time.
The user is able to change the value and the currency to be change.

The release apk can be found [here](https://github.com/bubinimara/Rates/releases)

<img src="https://raw.githubusercontent.com/bubinimara/bubinimara.github.io/master/Rates/home.png" width="200">


## Architecture
The Clean architecture is used to structure the entire application and
MVVM is choice for implements the Android related code

## Modules structure ( packages ) and responsibility
 - `app` => android related UI and Components to show data that came from Domain package
 - `domain` => Interactors  with all logic reside here ( independent from framework)
 - `data` => fetch data
 - `di` => dependency injection

## MVVM in details
 - `Activity and Fragments` => show data that came from ViewModel
 - `*ViewModel` => Get data from *Interactors and handle the android lifecycle
 - `*Interactor` => fetch data and handle logic


## A Video
<img src="https://github.com/bubinimara/bubinimara.github.io/raw/master/Rates/video.gif" width="250">