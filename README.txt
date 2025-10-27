Instructions on how to run using docker compose:

navigate to root directory (directory where docker-compose.yml is)
if on windows and not on wsl, make sure docker desktop is open
docker compose build
docker compose up -d (or without -d if you want)



references and resources:
https://stackoverflow.com/questions/64507628/mongodb-docker-init-script -- used to populate databases init. python script used to generate dummy data. 
https://spring.io/projects/spring-cloud-gateway -- gateway filters guide
https://hub.docker.com/_/mongo -- mongodb docker documentation
https://spring.io/guides/gs/accessing-data-mongodb -- mongodb spring boot implementation
https://www.youtube.com/watch?v=-pv5pMBlMxs -- video tutorial on kafka + docker + spring boot project
https://restfulapi.net/resource-naming/ -- api naming conventions guide
https://medium.com/@dinesharney/implementing-the-saga-pattern-using-choreography-and-orchestration-53e66cbd520e --  Choreography-Based Saga pattern using kafka example
https://medium.com/@darshak.kachchhi/setting-up-a-kafka-cluster-using-docker-compose-a-step-by-step-guide-a1ee5972b122 -- kafka Kraft mode guide for docker compose