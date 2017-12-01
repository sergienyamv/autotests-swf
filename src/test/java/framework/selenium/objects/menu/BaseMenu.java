package framework.selenium.objects.menu;

public interface BaseMenu {
    void openMenu();

    MenuItem getMenuItemByText(String text);
}
