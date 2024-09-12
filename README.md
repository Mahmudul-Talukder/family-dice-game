# Family Game Application

## Overview

This project is a demo for a family game application built with Spring Boot. It includes functionality 
to manage players, start the game, roll dice, update scores, and retrieve current scores. The application interacts with an external dice roll API and handles cases where this API may not be available.
## Features

- Add New Players: Add players to the game.
- Start the Game: Initiate the game with the added players. Call an external API to get dice roll values and update player scores accordingly.
- Retrieve Current Scores: Get the current scores of all players.


## Prerequisites

- JDK 21 or later
- Maven 3.6.3
- Spring boot 3.3.0

## Build and Run

1. Clone the repository:
   git clone https://github.com/Mahmudul-Talukder/family-game.git
   cd family-game

2. Build the Application:
   mvn clean install

3. Run the Application:
   mvn spring-boot:run

4. Build the Docker image 
   docker build -t hishab-family-special-app .

5. Run the Docker container 
   docker run -d -p 8086:8082 --name=special-family-app-hishab  hishab-family-special-app

6. swagger-ui
   http://localhost:8086/swagger-ui.html
7. api documentation
   http://localhost:8086/v3/api-docs

8. Request sample
   
   Create a new player:
   curl --location 'http://localhost:8086/api/game/player' \
   --header 'Content-Type: application/json' \
   --data '{
   "name": "mahmud",
   "age": 10
   }'

   Start game:
   curl --location 'http://localhost:8086/api/game/start' \
   --header 'Content-Type: application/json'

   Retrieve current scores:
   curl --location 'http://localhost:8086/api/game/scores' \
   --header 'Content-Type: application/json' \
   --data ''
   
   
