FROM debian:bookworm-slim

# Prevent interactive installs
ENV DEBIAN_FRONTEND=noninteractive

# Updates and Java dev tools
RUN apt-get update \
    && apt-get upgrade \
    && apt-get install -y --no-install-recommends \
        curl \
        zip \
        unzip \
        bash \
        sudo \
        git \
        ca-certificates \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Unprivileged user
RUN useradd -m -s /bin/bash vscode \
    && echo "vscode ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# Install SDKMAN
ENV SDKMAN_DIR=/usr/local/sdkman
RUN curl -s "https://get.sdkman.io?ci=true" | bash && \
    chmod a+x "$SDKMAN_DIR/bin/sdkman-init.sh"

# Ensure other shells can access SDKMAN
ENV PATH="${SDKMAN_DIR}/bin:$PATH"
SHELL ["/bin/bash", "-c"]

# Install Java and Gradle with SDKMAN
RUN source "$SDKMAN_DIR/bin/sdkman-init.sh" && \
    sdk install java 25.0.1-tem && \
    sdk install gradle

# Switch to unprivileged user
USER vscode
WORKDIR /workspace

# Ensure SDKMAN is sourced for vscode user
RUN echo 'source /usr/local/sdkman/bin/sdkman-init.sh' >> ~/.bashrc
