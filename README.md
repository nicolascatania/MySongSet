# MySongSet
This project is a web application for managing song sets, like the ones that artist use on their concerts, because when I want to play the guitar, I forget all the songs I can play and always end up playing the same 10 John Mayer songs hehe.
It is implemented with a backend in Spring (with Java 21), a frontend in Angular, and uses PostgreSQL as the database. 
The Docker containers are used to host both services (backend and frontend) and are managed through Docker Compose, with a volume to persist the data I use.
The Application is designed to be used only by me, to solve my own problem, if you find it useful you can run it through docker, all the data you need is on docker-compose.yml.

## Features
- Song-Playlist CRUD.
- SPOTIFY API Consume, where you can search for songs and add to the playlist you created.
- Error handling.
- Spring MVC.
- RESTfull API.
- Minimalist UI desing.
