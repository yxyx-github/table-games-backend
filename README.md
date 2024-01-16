# Table Games Backend

## Setup
1. Clone this project via `git clone`.
2. Open this project with an IDE (Intellij Ultimate is recommended).
3. Make sure docker / docker desktop is installed if you wish to use them
4. Execute `mvn clean install` to test if everything has been set up correctly

## Collaborate
This is a list of rules and guidelines that should be followed to allow for a successful collaboration.
- Do not push to `main` or `dev` directly
- Prefix your branch with `feature/`,`refactor/` or `fix/`
- Do not overload your merge requests as they need to be read by another developer
- Issues are linked GitHub repository 
- Please assign a reviewer for your merge requests

## Structure
This project is split into multiple modules. 
Namely, the `core` module and the `rest` module.
The `rest` module can be built into a jar, and then it can be executed.
The `core` module implements the logic of the application, but it does not have any means to be started individually.

## Execute
There are multiple ways to execute this project:
1. If you want to execute it with the compiler of your IDE then you can start all the Application classes manually.
2. If you want to use Docker than execute a `mvn clean install` and then start the `docker compose` with `docker compsoe up --build docker-compose.yaml` or `docker-compsoe up --build docker-compose.yaml`
3. If you want to build a jar with dependencies, then execute `mvn clean install` and look for a `...-bin` directory in the target directory. This directory includes a jar and all dependencies as well as a start script. 