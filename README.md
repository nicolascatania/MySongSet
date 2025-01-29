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
- Automatic calculation of playlist lenght (hh-mm-ss)

![General view](https://github.com/user-attachments/assets/cdd61d98-beb6-494e-b276-3c4ff6d5a6a5)


![Searching a song with Spotify API](https://github.com/user-attachments/assets/ed1c890c-8f78-4278-8902-18e93c09d0e0)


![Creating playlist](https://github.com/user-attachments/assets/cbd266f0-04ad-4afe-920c-5ae2a39943d5)
