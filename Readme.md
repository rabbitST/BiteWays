# BiteWays API

*With BiteWays, you can effortlessly store all your favorite recipes in one place.</br>
BiteWays planning your weekly menu and provide a convenient shopping list feature.</br> 
Simply select the menu for the week, and BiteWays will automatically generate a</br> 
shopping list for you, ensuring you have all the necessary ingredients on hand.*

### Backend
Menu controller  
http://localhost:8080/api/biteways/menu    
Recipe controller  
http://localhost:8080/api/biteways/recipe    
Ingredient controller  
http://localhost:8080/api/biteways/ingredient  

### Frontend
*First run the API locally and then visit the link for the frontend.*  
Online on github.io: [BiteWaysAPI](https://rabbitst.github.io/biteways.github.io/index.html)  
FrontEnd folder: Biteways/FrontEndVue  

### API documentation / Swagger
http://localhost:8080/swagger-ui/index.html  

### Postman collection  
Biteways root folder  
[BitewaysDev.postman_collection.json](BitewaysDev.postman_collection.json)

### Docker
Biteways root folder  

**Solution 1.**  
Create a single Docker container with the database inside it.
1. [docker_build.sh](docker_build.sh): This file will contain the commands for building the Docker image.
2. [docker_run.sh](docker_run.sh): This file will contain the command for running the Docker container with the database.

**Solution 2.**  
[docker_network_postgres.sh](docker_network_postgres.sh): Create a network with a Docker container for the database and another Docker container for the application.

### API uml  
Biteways root folder  
[BiteWaysUML.plantuml](BiteWaysUML.plantuml)

