# Rates
Application that show currency exchange rates in real time.
The user is able to change the value and the currency to be change.

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
