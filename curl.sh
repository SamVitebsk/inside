# регистрация нового пользователя

curl --location --request POST 'http://localhost:8088/registration' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "password": "test"
}'

# авторизация
curl --location --request POST 'http://localhost:8088/authorization' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "password": "test"
}'

# создание нового сообщения
curl --location --request POST 'http://localhost:8088/article' \
--header 'Authorization: Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjU5Njk4NjM2LCJleHAiOjE2NjE4NDYxMjB9.BMBtAeI1kC-qMjYzDHII7kiDcn5X5QA4qKmLj62JaDI' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "message": "new message"
}'

# получить n последних сообщений
curl --location --request POST 'http://localhost:8088/article' \
--header 'Authorization: Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjU5Njk4NjM2LCJleHAiOjE2NjE4NDYxMjB9.BMBtAeI1kC-qMjYzDHII7kiDcn5X5QA4qKmLj62JaDI' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "test",
    "message": "history 5"
}'
