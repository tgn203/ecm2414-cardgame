FROM debian:bookworm-slim

# Prevent interactive installs
ENV DEBIAN_FRONTEND=noninteractive

# Updates and Java dev tools
RUN apt-get update \
    && apt-get upgrade \
    && apt-get install -y --no-install-recommends \
        openjdk-17-jdk \
        bash \
        git \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Java environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /workspace
