-- ПРОСТОЙ СЕРВИС ВЕТЕРИНАРНОЙ КЛИНИКИ ДЛЯ ОБРАБОКИ ПОЛЬЗОВАТЕЛЕЙ И ИХ ПИТОМЦЕВ --

На сервисе реализованны следующие операции:

-Регистрация пользователя и авториция

-Создание питомца

-Редактирование питомца

-Удаление питомца

-Получение питомца по его идентификатору

-Получение списка питомцев авторизованного пользователя


Запросы выполняются в формате JSON. Примеры запросов в папке resources/json-requests.

В приложении в качестве БД ипользуется Postgres. DDL файлы для инициализации БД
и внесения данных находятся в папке resources.