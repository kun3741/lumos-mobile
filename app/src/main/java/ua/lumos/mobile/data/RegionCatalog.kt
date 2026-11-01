package ua.lumos.mobile.data

val regions = listOf(
    Region(
        id = "if",
        name = "Івано-Франківщина",
        image_url = "https://lumos.foo/api/region-images/if.svg",
        availability = RegionAvailability.CONNECTED,
        provider = "Прикарпаттяобленерго",
        supported_queues = "1.1, 1.2, 2.1, 2.2, 3.1, 3.2",
        update_frequency = "Після кожної зміни офіційного графіка",
        description = "Графіки планових відключень для населених пунктів Івано-Франківської області. Люмос збирає оприлюднені дані та показує їх окремо для кожної черги.",
        notes = "Фактичний стан електромережі може відрізнятися через аварійні відключення. Перед важливими подіями перевіряйте останнє повідомлення оператора системи розподілу."
    ),
    Region(
        id = "lviv",
        name = "Львівщина",
        image_url = "https://lumos.foo/api/region-images/lviv.svg",
        availability = RegionAvailability.CONNECTED,
        provider = "Львівобленерго",
        supported_queues = "1.1, 1.2, 2.1, 2.2, 3.1, 3.2",
        update_frequency = "Після кожної зміни офіційного графіка",
        description = "Графіки погодинних відключень для Львова та Львівської області. Дані згруповані за чергами, щоб потрібний розклад було легко знайти.",
        notes = "Опублікований графік описує планові обмеження. Екстрені та аварійні відключення можуть не потрапити до розкладу одразу."
    ),
    Region(
        id = "volyn",
        name = "Волинь",
        image_url = "https://lumos.foo/api/region-images/volyn.svg",
        availability = RegionAvailability.CONNECTED,
        provider = "Волиньобленерго",
        supported_queues = "1.1, 1.2, 2.1, 2.2, 3.1, 3.2",
        update_frequency = "Після публікації нового зображення графіка",
        description = "Графіки відключень для Волинської області. Люмос розпізнає часові проміжки з офіційного графіка та перетворює їх на зручний список.",
        notes = "Якщо оператор замінив зображення графіка, застосунку може знадобитися короткий час для оновлення розпізнаних проміжків."
    ),
    Region(
        id = "ternopil",
        name = "Тернопільщина",
        image_url = "https://lumos.foo/api/region-images/ternopil.svg",
        availability = RegionAvailability.PLANNED,
        provider = "Тернопільобленерго",
        supported_queues = "Буде визначено після підключення",
        update_frequency = "Підключення готується",
        description = "Регіон додано до плану розвитку Люмос. Після підключення тут з’являться офіційні графіки та вибір черги.",
        notes = "Картка позначена як «У розробці», тому зараз вона не містить чинного розкладу відключень."
    ),
    Region(
        id = "rivne",
        name = "Рівненщина",
        image_url = "https://lumos.foo/api/region-images/rivne.svg",
        availability = RegionAvailability.PLANNED,
        provider = "Рівнеобленерго",
        supported_queues = "Буде визначено після підключення",
        update_frequency = "Підключення готується",
        description = "Майбутній розділ із графіками для Рівного та Рівненської області. Структура даних уже готова до підключення джерела.",
        notes = "Поки регіон перебуває в розробці, інформація на цьому екрані має ознайомчий характер."
    ),
    Region(
        id = "kyiv",
        name = "Київщина",
        image_url = "https://lumos.foo/api/region-images/kyiv.svg",
        availability = RegionAvailability.PLANNED,
        provider = "Оператор Київської області",
        supported_queues = "Буде визначено після підключення",
        update_frequency = "Підключення готується",
        description = "Запланований каталог графіків для Київської області. Після інтеграції користувач зможе вибрати свою групу та переглянути детальний розклад.",
        notes = "Дані ще не підключені. Статус регіону зміниться, коли сервер Люмос почне отримувати офіційний графік."
    )
)
