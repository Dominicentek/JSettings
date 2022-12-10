import com.settingslib.Settings;
import com.settingslib.elements.*;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Settings settings = new Settings("this is a test settings file");
        Category root = settings.getRootCategory();
        root.add(new Checkbox("test checkbox", 1, false, false, true));
        root.add(new Slider("test slider", 2, false, 0, 10, 0.1f, 5, 5));
        root.add(new DropdownBox("test dropdown", 3, false, 0, 1, "el 1", "el 2", "el 3"));
        root.add(new Button("test button", 4, false));
        root.add(new Link("test link", 5, false, 4));
        OutputStream out = new FileOutputStream("test.stgs");
        out.write(settings.save());
        out.close();
        System.out.println(Settings.get(new FileInputStream("test.stgs")));
    }
}
