# Тест-кейс № AUTH_002
Название теста: 'Successful authorization entry'
## Описание
### Используемые разрешения:
* Доступ к камере: **ДА**
* Доступ к микрофону: **ДА**
### Тема приложения:
* **Светлая**
## Шаги действий
#### Шаг 0. Открываем экран авторизации
* Подожди пока элемент [AuthScreen](https://example.com) появится
#### Шаг 1. Вводим пользовательские данные и жмякаем 'Войти'
* Введи текст 'Username228' в [LoginField](https://example.com)
* Введи текст 'secretPassword1234' в [PasswordField](https://example.com)
* Кликни по [LoginButton](https://example.com)
#### Шаг 2. Попадаем на главный экран. Проверяем наличие фич
* Подожди пока элемент [MainScreen](https://example.com) появится
* Проверь элемент [LinearGeneration](https://example.com). Он должен быть видимым
* Проверь элемент [BicubicGeneration](https://example.com). Он должен быть видимым
* Проверь элемент [NeighborGeneration](https://example.com). Он должен быть видимым
