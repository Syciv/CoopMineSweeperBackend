# CoopSudokuBackend
# Лабораторная работа №4 по курсу "Технологии разработки распределенных приложений"
__________

Приложение предназначено для совместного решения головоломки судоку с возможностью создания собственной комнаты для приглашения других игроков

### Постановка задачи
•	Приложение должно обеспечивать параллельную работу нескольких клиентов и серверов. Дополнительное требование: возможность запуска нескольких серверов на одном компьютере.
•	Клиентские приложения должны автоматически находить серверы для обслуживания и выполнения заданных функций.
•	Серверы системы могут выполнять различные функции.
•	При разрыве сеанса приложения должны автоматически восстанавливать свою работоспособность.
•	Приложения должны поддерживать возможность взаимодействия в различных режимах.
•	Для организации взаимодействия нужно использовать различные средства коммуникации (именованные каналы, мейлслоты, сокеты, очереди сообщений, удалённый вызов процедур, WCF-сервисы), сравнив их возможности.

### Введение в предметную область
Судоку – головоломка с числами. Игровое поле представляет собой квадрат размером 9×9, разделённый на меньшие квадраты со стороной в 3 клетки. Таким образом, всё игровое поле состоит из 81 клетки. В них уже в начале игры стоят некоторые числа (от 1 до 9), называемые подсказками. От игрока требуется заполнить свободные клетки цифрами от 1 до 9 так, чтобы в каждой строке, в каждом столбце и в каждом малом квадрате 3×3 каждая цифра встречалась бы только один раз.

### Архитектура системы.
Архитектура системы  включает в себя несколько серверов, множество клиентов, распределённых по комнатам, диспетчер и базу данных. Данный тип архитектуры выбран для обеспечения отказоустойчивости в случае отключения одного из серверов. 
Запустив клиент, пользователь может либо создать свою комнату и получить её идентификатор для подключения, либо подключиться к существующей комнате. При создании комнаты клиент обращается к сервису Consul, в котором сервера регистрируют себя при запуске, для получения списка доступных в данный момент серверов и выбирает случайный из них. Если пользователь желает присоединиться к существующей комнате, он указывает её идентификатор, после чего клиент по очереди обращается к доступным серверам за списком существующих комнат и подключается к тому, на котором создана выбранная комната. Далее клиенты получают головоломку и в поле судоку могут заполнять цифрами ячейки, при этом другие пользователи, подключённые к комнате, будут получать изменения, значения в соответствующих ячейках будут меняться.
[Клиентское приложение](https://github.com/Syciv/CoopSudokuClient)

### Описание серверного приложения
Серверов может быть неограниченное количество, они независимы и содержат независимо работающие комнаты. При запуске серверы регистрируют себя в сервисе Consul. Для проверки состояния сервера существуют HTTP -эндпоинты /ping и /check-my-health, которые периодически использует Consul.
На сервере реализованы процедуры создания комнаты, получения состояния комнаты, получения списка комнат, а также слушания сообщений от клиента об изменении полей головоломки и передачи этого сообщения другим пользователям соответствующей комнаты.
Серверы реализованы на языке Java с использованием фреймворка Spring. Каждый сервер способен одновременно принимать HTTP, SOAP и WebSocket запросы в нескольких потоках, многопоточность реализована на уровне фреймворка.

### База данных
База данных в системе используется для хранения возможных вариантов головоломки судоку. Для этого существует таблица с полями сложности, изначальной головоломки и решения головоломки, хранящимися в виде строки с подряд записанными значениями полей. Используется СУБД Postgres.
Миграции выполняются с помощью библиотеки liquibase, изменения задаются в resoures/db/changelog.sql