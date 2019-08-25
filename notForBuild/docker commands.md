### Build image command should be performed from web/target directory
docker build -t restapi -f ../../notForBuild/Dockerfile .

### Run docker image
docker run -d -p 8080:8080 restapi