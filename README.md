# JSettings
Java implementation of the [Settings](https://dominicentek.github.io/SettingsFileFormatDocs/) file format
## Usage
### Constructing a new Settings instance
```java
import com.settingslib.Settings;

Settings newSettings = new Settings("Title");
Settings loadedSettings = Settings.get(new FileInputStream("settings.stgs"));
```
### Adding a checkbox
```java
Category root = settings.getRootCategory();
Checkbox checkbox = new Checkbox("Checkbox Title", /* id */ 1, /* locked */ false, /* checked */ false, /* defaultChecked */ false)
root.add(checkbox);
```
### More element types
```java
Slider slider = new Slider("Slider Title", /* id */ 2, /* locked */ false, /* min */ 1f, /* max */ 10f, /* step */ 0.5f, /* value */ 5f, /* defaultValue */ 5f);
DropdownBox dropdown = new Slider("Dropdown Title", /* id */ 3, /* locked */ false, /* selectedIndex */ 0, /* defaultSelectedIndex */ 0, /* options... */ "option 1", "option2", "option 3");
Button button = new Slider("Button Title", /* id */ 4, /* locked */ false);
Link link = new Slider("Link Title", /* id */ 5, /* locked */ false, /* destinationID */ 1);
Category category = new Category("Category Title", /* id */ 6, /* locked */ false);
```
### Handling button events
```java
Button.setHandler(id -> {
  System.out.println("Button with ID " + id " + clicked!");
});
button.click(); // Invoke the handler
```
### Restoring defaults
```java
Setting setting;
setting.restoreDefault();
```
