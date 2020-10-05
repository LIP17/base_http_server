# base image: https://github.com/docker-library/openjdk/blob/52cb2ce3bda94b1c0229f11bfa176035ad41c9b8/11/jdk/slim-buster/Dockerfile
FROM openjdk:11.0.8-jdk-slim@sha256:a9bb2cfc86df7331450e54d758258a3c36bce034b694aec9a43c6b627f5b1c0c

# Install Gradle
COPY gradle /home/app/gradle
COPY gradlew /home/app
WORKDIR /home/app
RUN ./gradlew --no-daemon

# Build App
COPY . /home/app
COPY entrypoint.sh /home/entrypoint.sh
RUN ./gradlew clean installDist test ktlintCheck --no-daemon && \
    mv build/install/* /home/ && \
    rm -r build

WORKDIR /home

EXPOSE 8080
ENTRYPOINT ["/home/entrypoint.sh", "/home/base_http_server/bin/base_http_server"]
