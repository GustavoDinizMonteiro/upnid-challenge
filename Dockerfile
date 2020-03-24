 
FROM openjdk:13-jdk-alpine

# Install Maven
RUN \
  apk add maven && \
  rm -rf /var/lib/apt/lists/*

# Set environment variables.
ENV HOME /root
# Define working directory.
WORKDIR /root

# Cloning and install dependencies
RUN \
  apk add git && \
  git clone https://github.com/GustavoDinizMonteiro/upnid-challenge.git && \
  (cd upnid-challenge && mvn -DskipTests=true install)

# Define working directory.
WORKDIR /root/upnid-challenge