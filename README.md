Smart Pantry Manager
--------------------

A Java Android app that helps to reduce food waste. You add the ingredients you already have at home and the app suggests recipes you can cook using only those ingredients without needing to go shopping.

How it works
-------------
The app only suggests a recipe if i have every ingredient it needs and the quantity it requires. If a recipe needs 5 ingredients and i have 4 of them, it will not show up. Also if the quantity for an ingredient is not enough, it will not show up. Expired items are also never counted when it comes to matching recipes.

Features
---------
- Add, edit and delete items from the pantry
- Pantry list screen that shows everything in your pantry
- 16 recipes that are loaded into the database the first time the app is run
- Suggested recipes screen that only shows recipes that you can actually make now
- Recipe detail screen with the full ingredient list for a recipe and the steps to make it
- Settings screen that lets you sort the pantry by name, expiry date or quantity and in ascending or descending order
- Input validation on the add/edit form
- A message when no recipes match what is currently in your pantry

Database choice:SQLite
-----------------------
I chose the SQLite option because it runs on the phone, so the app works without internet and i don't need to build or host a backend. The data is stored on the device, so it is still there after i close and reopen the app.

There are three tables: pantry_items, recipes and recipe_ingredients. The recipes are added to the database when it is first created.

Setup and run instructions
--------------------------
You need Android studio and an emulator or physical device running android 9 or higher.

Steps
-----
1. Clone the repository
2. Open Android studio and choose open, then select the project folder
3. Wait for the Gradle to finish syncing
4. Select an emulator or connect a phone with USB debugging turned on
5. Click the green Run button.

When the app opens for the first time, the pantry will be open. Add a few ingredients, then open the recipes screen to see what you can make.
