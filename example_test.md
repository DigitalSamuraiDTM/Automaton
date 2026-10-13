# Тест-кейс № AUTH_001
Название теста: 'Successful authorization'
## Описание
### Используемые разрешения:
* Доступ к камере: **ДА**
* Доступ к микрофону: **ДА**
### Тема приложения:
* **Светлая**
## Шаги действий
#### Шаг 0. Открываем экран авторизации
* Кликни по [loginButton](https://youtube.com)
* Подожди пока элемент [loginScreen](https://youtube.com) появится
#### Шаг 1. Вводим учетные данные
* Введи текст 'admin' в [loginField](https://youtube.com)
* Введи текст 'password' в [passwordField](https://youtube.com)
#### Шаг 2. Авторизуемся и попадаем на главный экран
* Кликни по [submitButton](https://youtube.com)
* Подожди пока элемент [homeTitle](https://youtube.com) появится
* Проверь элемент [homeTitle](https://youtube.com). Он должен быть видимым
