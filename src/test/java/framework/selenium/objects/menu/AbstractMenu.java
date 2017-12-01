package framework.selenium.objects.menu;

import framework.Logger;
import framework.selenium.objects.elements.Button;
import framework.selenium.objects.BasePage;
import org.openqa.selenium.By;

public abstract class AbstractMenu implements BaseMenu {
    private Button btnOpenMenu;
    private String templateMenuItem;

    public AbstractMenu(Button btnOpenMenu, String templateMenuItem) {
        this.templateMenuItem = templateMenuItem;
        this.btnOpenMenu = btnOpenMenu;
    }

    public Button getBtnOpenMenu() {
        return btnOpenMenu;
    }

    @Override
    public MenuItem getMenuItemByText(String text) {
        By locator = By.xpath(String.format(templateMenuItem, text));
        return new MenuItem(locator, text);
    }

    public void clickMenuItemByText(String text) {
        BasePage.waitForLoadScripts();
        openMenu();
        try {
            if (!getMenuItemByText(text).isDisplayedAndEnabled()) {
                openMenu();
            }
            getMenuItemByText(text).click();
        } catch (Exception e) {
            Logger.getInstance().error(String.format("error when i click by menu item: %s", e.getMessage()));
        }
    }

    @Override
    public void openMenu() {
        try {
            btnOpenMenu.waitForIsDisplayed();
            btnOpenMenu.click();
        } catch (Exception e) {
            Logger.getInstance().error(String.format("error after click: %s", e.getMessage()));
        }
    }

    public boolean isMenuItemDisplayed(String text) {
        if (!getMenuItemByText(text).isDisplayedAndEnabled()) {
            openMenu();
        }
        return getMenuItemByText(text).isDisplayedAndEnabled();
    }
}
