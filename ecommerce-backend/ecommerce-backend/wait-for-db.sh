#!/bin/bash

echo "Waiting for MySQL to be ready..."

# Wait until MySQL responds on port 3306
until nc -z db 3306; do
  echo "MySQL not ready yet. Retrying in 2s..."
  sleep 2
done

echo "✅ MySQL is up. Running tests..."
./mvnw test -Dspring.profiles.active=test
