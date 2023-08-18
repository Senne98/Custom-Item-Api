# Custom-Item-Api
An API for making custom blocks and items for 1.20+

## How to use?

### Custom items
You can add a custom item by creating a ```new CustomItem(Material, NamespacedKey)```.
This will create the custom item if there isn't a custom item with this NamespacedKey.
You can change the texture,tags,...
If you want to place a custom block using this item you can add this with CustomItem#setCustomBlock(NamespacedKey)

example:
````java

NamespacedKey itemKey = new NamespacedKey(plugin, "Test");
CustomItem customItem = new CustomItem(Material.FURNACE, itemKey);
customItem.setCustomBlock(blockKey);
customItem.setName(Component.text("Beautiful Furnace"));
``````

### Custom blocks
You can add a custom item by creating a ```new CustomBlock(Material, NamespacedKey)```.
This will create the custom item if there isn't a custom item with this NamespacedKey.
You can change the texture,tags,...
The block uses an item texture, NOT a block texture. This means that if you set custom model data (cmd) you can change the texture.
If you want to drop a custom item after breaking this block you made with CustomBlock#setDropItem(NamespacedKey)

example:
````java
NamespacedKey blockKey = new NamespacedKey(plugin, "Test");
CustomBlock customBlock = new CustomBlock(Material.FURNACE, blockKey);
customBlock.setRotation(CustomBlock.Rotation.AROUND_Y);
``````

### Save
Items and Blocks you create get saved when you stop the server, if you want to be sure they get saved you can use Save#save().

### Features / Bugs
If you find bugs or want a feature to be added, ask in the [discord](https://discord.gg/DBabnRZAhC) or create an issue on [github](https://github.com/Senne98/Custom-Item-Api/issues).

