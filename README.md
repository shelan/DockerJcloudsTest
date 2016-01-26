## DockerJcloudsTest

This is a sample for running docker with Jclouds API

### To get the id of the image

``` docker images --no-trunc ```

### To stop all the docker images

```
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
```