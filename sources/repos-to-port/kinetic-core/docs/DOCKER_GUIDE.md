# Docker Usage Guide

This guide explains how to run the Salesforce Toolkit in an isolated Docker environment, which avoids the need to install Python or dependencies on your local machine.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed on your machine.
- [Docker Compose](https://docs.docker.com/compose/install/) (usually included with Docker Desktop).

## Quick Start

### 1. Build the Image
Build the Docker image containing the toolkit and all dependencies:

```bash
docker-compose build
```

### 2. Run Tests
Execute the unit tests inside the container to verify everything is working:

```bash
docker-compose run tests
```
*Expected Output:* You should see `pytest` output showing passing tests (green).

### 3. Usage via CLI
You can run any `sf-toolkit` CLI command using `docker-compose run toolkit [command]`.

**Display Help:**
```bash
docker-compose run toolkit --help
```

**Test Authentication:**
```bash
# Ensure your .env file is configured first!
docker-compose run toolkit auth --method jwt
```

## Configuration

The Docker setup uses your local `.env` file. 

1. Create a `.env` file in the project root (see `config/.env.example`).
2. Docker Compose automatically loads this file.

### Certificates
If you are using JWT authentication with a certificate file (e.g., `server.key`):

1. Place the key file inside the project directory (e.g., inside a `certs/` folder).
2. In your `.env` file, use the path relative to the container, which maps to `/app`.

**Example:**
If your local structure is:
```
project/
  certs/
    server.key
  .env
```

Your `.env` should look like this:
```bash
SF_PRIVATE_KEY_PATH=/app/certs/server.key
```
*(Note: `/app` is where the project folder is mounted inside the container)*

## Troubleshooting

**Permission Issues (Linux):**
If you encounter permission errors with file mounts, you may need to run docker-compose with `sudo`.

**"File not found" for keys:**
Ensure that your `SF_PRIVATE_KEY_PATH` in `.env` points to a path *inside the container* (starting with `/app/`), not your local host path.
