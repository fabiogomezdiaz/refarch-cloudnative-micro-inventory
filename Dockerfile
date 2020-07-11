# STAGE: Build
FROM gradle:5.4.1-jdk8-alpine as builder

# Create Working Directory
ENV BUILD_DIR=/home/gradle/app/
RUN mkdir $BUILD_DIR
WORKDIR $BUILD_DIR

# Download Dependencies
COPY build.gradle settings.gradle $BUILD_DIR
RUN gradle build -x test --continue

# Copy Code Over and Build jar
COPY src src
RUN gradle build -x test

# STAGE: Deploy
FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.7

# OPTIONAL: Install Extra Packages
#RUN apk --no-cache update \
# && apk add jq bash bc ca-certificates curl \
# && update-ca-certificates

# Create app directory, chgrp, and chmod
ENV APP_HOME=/home/jboss/app
RUN mkdir -p $APP_HOME/scripts
RUN chgrp -R 0 $APP_HOME && chmod -R g=u $APP_HOME
WORKDIR $APP_HOME

# Copy jar file over from builder stage
COPY --from=builder /home/gradle/app/build/libs/micro-inventory-0.0.1.jar $APP_HOME
RUN mv ./micro-inventory-0.0.1.jar app.jar

COPY startup.sh startup.sh
COPY scripts/max_heap.sh scripts/

USER 2000

EXPOSE 8080 8090
ENTRYPOINT ["./startup.sh"]