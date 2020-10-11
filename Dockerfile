FROM lip17/base-jvm11:0.1@sha256:9aaef3f4a9871347ae0cc04d8b2d04ecdb525b5c2e9eef13d9eb1e7c32afde1c

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
RUN rm -r app

EXPOSE 8080
ENTRYPOINT ["/home/entrypoint.sh", "/home/base_http_server/bin/base_http_server"]
