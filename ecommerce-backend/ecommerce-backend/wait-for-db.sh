<<<<<<< HEAD
# #!/bin/bash

# echo "Waiting for MySQL to be ready..."

# until nc -z localhost 3306; do
#   echo "MySQL not ready yet. Retrying in 2s..."
#   sleep 2
# done

# echo "MySQL is up. Running tests..."
# ./mvnw test \
#   -Dspring.datasource.url=jdbc:mysql://localhost:3306/cartdb_docker \
#   -Dspring.datasource.username=root \
#   -Dspring.datasource.password=testpassword
=======
#!/bin/bash

echo "Waiting for MySQL to be ready..."

until nc -z localhost 3306; do
  echo "MySQL not ready yet. Retrying in 2s..."
  sleep 2
done

echo "MySQL is up. Running tests..."
./mvnw test \
  -Dspring.datasource.url=jdbc:mysql://localhost:3306/cartdb_docker \
  -Dspring.datasource.username=root \
  -Dspring.datasource.password=testpassword
>>>>>>> b66da306785e86bce1b5959bac7f8d2c847bc2da

