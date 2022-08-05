При локальном запуске приложения можно указать <br /> 
url базы данных, имя пользоваеля и пароль, <br />
они содержатся в переменных окружения<br />
POSTGRES_URL<br />POSTGRES_USER<br />
POSTGRES_PASSWORD. <br />
По умолчанию все значения равны postgres<br /><br />
Для запуска проекта в контейнерах нужно из корня выполнить команду<br />
docker-compose up -d --build <br /><br />
или команды<br />
docker-compose build <br />
docker-compose up -d <br /><br />
Для остановки <br />
docker-compose down

По умолчанию приложение работает на порту 8088,<br />
можно его изменить в переменной окружения SERVER_PORT <br /><br />
Запросы в файле curl.sh<br /><br />

Образ приложения<br />
https://hub.docker.com/repository/docker/asamusev/public


