# First stage, build the custom JRE
FROM eclipse-temurin:21-jdk-alpine AS jre-builder

# Set a working directory inside the container
WORKDIR /app

# Install necessary tools for Maven and jdeps
RUN apk update && \
    apk add --no-cache tar gzip curl git binutils  # Added git, curl, binutils (important for jdeps)

# --- Install Maven ---
ENV MAVEN_VERSION 3.9.10
ENV MAVEN_HOME /usr/lib/mvn

RUN mkdir -p /usr/share/maven /usr/share/maven/ref && \
    curl -fsSL https://apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV PATH $MAVEN_HOME/bin:$PATH

# Copy your application source code to the build directory
COPY . /app

# Build the application
RUN mvn clean package -DskipTests

RUN jar xvf target/onestudy-0.0.1-SNAPSHOT.jar

# Analyze module dependencies for jlink
# --multi-release 21 for Java 21
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 21  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    target/onestudy-0.0.1-SNAPSHOT.jar > modules.txt

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules $(cat modules.txt) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-21

# Second stage, Use the custom JRE and build the app image
FROM alpine:3.19

# Set JAVA_HOME and update PATH for the custom JRE
ENV JAVA_HOME=/opt/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy the optimized JRE from the jre-builder stage
COPY --from=jre-builder /optimized-jdk-21 $JAVA_HOME

# Add app user
ARG APPLICATION_USER=spring

# Create a user to run the application, don't run as root
# Using --disabled-password and --home /app for better practice
RUN addgroup --system $APPLICATION_USER &&  \
    adduser --system $APPLICATION_USER --ingroup $APPLICATION_USER --no-create-home --home /app

# Create the application directory and set permissions
RUN mkdir /app && chown -R $APPLICATION_USER:$APPLICATION_USER /app

# Copy the built JAR to the app directory
# Corrected JAR name to onestudy-0.0.1-SNAPSHOT.jar
COPY --chown=$APPLICATION_USER:$APPLICATION_USER target/onestudy-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

USER $APPLICATION_USER

EXPOSE 8080

# Use exec form for ENTRYPOINT for better signal handling
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]