FROM python:3.11-slim

WORKDIR /app

# Install build dependencies
# cryptography needs these to build headers
RUN apt-get update && apt-get install -y \
    build-essential \
    libssl-dev \
    libffi-dev \
    && rm -rf /var/lib/apt/lists/*

# Install python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Install test dependencies
RUN pip install --no-cache-dir pytest pytest-cov

# Copy source code and install in editable mode
COPY . .
RUN pip install -e .

# Default entrypoint is the CLI
ENTRYPOINT ["sf-toolkit"]
CMD ["--help"]
