# Cookwriter

A libre Android application to write and store cooking recipes.

This app aim to force writers to add precision to their recipes and respect conventions such as
time, technique and seasonality of ingredients.

Recipe steps will also have parsed content such as time, ingredients, quantities, etc. to be able
to check for recipe consistency and avoid errors, as well as providing useful tools for the user
(a timer for example).

Currently, the app is at its most crude state and doesn't have such feature.

## Motivations

This project aims to provide a solution to theses problems, which motivate me the develop it:
- The lack of a free, open source and libre Android app to store personal recipes.
- Errors or inconsistencies are frequent when writing a recipe from scratch without a pair review.
- Recipes that lack precision and conventions in their steps are frustrating.
- Seasonality of ingredients in recipes are rarely a concern and deserve to be addressed more.
- The ability to reference other recipes when writing a new one is non-existent. This is to avoid
  repetition or to extends other recipes.

## Installation

APK files are available in releases but there are not signed (yet ?). Don't forget to allow unknown
sources in your Android system or browser app settings.

Android 12 (API level 31) or later is required.

## Build

This project has been developed with android studio, so I recommend using it to build the app.

You can also use `gradlew` (I haven't tested it):

```sh
gradlew installDebug
```
