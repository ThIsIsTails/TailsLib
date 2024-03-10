# TailsLib

Библиотека специально для моих плагинов, но и для ваших тоже может подойти).

# Использование

Библиотека предоставляет возможность создавать разные предметы и блоки.

Вот пример создания самого простого предмета
```java
// MyItem.java
public class MyItem implements CustomItem {
    @Override
    public @NotNull CustomItemData getItemData() {
        // Создаём данные для предмета. Можно сразу вернуть CustomItemData если не планируете добавлять описание предмету.
        CustomItemData data = new CustomItemData("testitem", 
        "Простой тестовый предмет", Material.IRON_AXE);

        // Ставим описание предмету благодаря SimpleDescBuilder
        data.setLore(new SimpleDescBuilder().addDesc("Простой предмет."));

        // Возвращаем данные о предмете.
        return data;
    }
}

// MyPlugin.java
public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CustomItemManager manager = CustomItemManager.getManager();

        // Регистрируем наш предмет.
        manager.register(new MyItem());
    }
}
```