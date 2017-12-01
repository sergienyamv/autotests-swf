package vk.objects.containers;

import framework.selenium.objects.elements.Button;
import framework.selenium.objects.elements.Label;
import framework.selenium.objects.elements.TextBox;
import org.openqa.selenium.By;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DatePicker extends Button {
    private static String dayPickTemplate = "//td[@data-year='%s' and @data-month='%s' and .='%s']";
    private Label datepickerYear = new Label(By.className("ui-datepicker-year"), "Calendar year");
    private Label datepickerMonth = new Label(By.className("ui-datepicker-month"), "Calendar month");
    private Button nextMonth = new Button(By.className("ui-datepicker-next"), "Switch to next month");
    private Button prevMonth = new Button(By.className("ui-datepicker-prev"), "Switch to previous month");


    public DatePicker(By selector, String name) {
        super(selector, name);
    }

    private Button getDataPickButton() {
        return new Button(By.xpath(String.format("%s/img", getAbsoluteXPath())), "Datepicker icon");
    }

    private TextBox getDateLabel() {
        return new TextBox(By.xpath(String.format("%s/input", getAbsoluteXPath())), "Date label");
    }

    public void setDate(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        getDataPickButton().click();
        int viewYear = Integer.parseInt(datepickerYear.getText());
        int viewMonth = Month.valueOf(datepickerMonth.getText().toUpperCase()).getValue();
        int changeMonth = (month - viewMonth) + (year - viewYear) * 12;

        if (changeMonth > 0) {
            nextMonth.click(changeMonth);
        } else if (changeMonth < 0) {
            prevMonth.click(-changeMonth);
        }
        new Button(By.xpath(String.format(dayPickTemplate, year, month - 1, day)),String.format("%s/%s/%s", day, month, year)).click();
    }

    public void setDate(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        setDate(localDate);
    }

    public String getDate() {
        return getDateLabel().getAttributeOfFirstElement("value");
    }

    @Override
    public boolean isAttributeContains(String attr, String value) {
        return getDateLabel().isAttributeContains(attr, value);
    }
}
