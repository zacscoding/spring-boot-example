# Docker test

- <a href="build">Spring project build to docker </a>
- <a href="api">Sample of docker client</a>

<div id="build"></div>  

## Build to docker

### Maven 

> install dockerfile:build  

### Image to tar

> docker save springboot-docker > springboot-docker.tar  

### Image load  

> docker load < springboot-docker.tar

### First running  

> docker run -p 8080:8080 imageid

<div id="api"></div>

# Use docker client

## Enable docker remote api

- Create conf override config file  

> $ vi /etc/systemd/system/docker.service.d/startup_options.conf  

```$xslt
[Service]
ExecStart=
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2376 -H unix:///var/run/docker.sock
```   

> $ systemctl daemon-reload  

> $ systemctl restart docker






