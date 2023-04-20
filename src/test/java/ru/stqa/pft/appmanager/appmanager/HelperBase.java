package ru.stqa.pft.appmanager.appmanager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.List;

public class HelperBase {

  protected ApplicationManager app;
  protected WebDriver wd;

  public HelperBase(ApplicationManager app) {

    this.app = app;
    this.wd = app.getDriver();
  }

  protected void click(By locator) {

    wd.findElement(locator).click();
  }

  protected void type(By locator, String text) {
    wd.findElement(locator).click();
    if (text != null) {
      String existingText = wd.findElement(locator).getAttribute("value");
      if (! text.equals(existingText)) {
        wd.findElement(locator).clear();
        wd.findElement(locator).sendKeys(text);
      }
    }
  }

  protected void attach(By locator, File file) {
    if (file != null) {
      wd.findElement(locator).sendKeys(file.getAbsolutePath());
    }
  }


  protected void select(By locator, String text) {
    if (text != null) {
      WebElement element = wd.findElement(locator);
      List<WebElement> elements = element.findElements(By.tagName("option"));
      for (WebElement el : elements) {
        if (el.getText().equals(text)) {
          wd.findElement(locator).click();
          new Select(wd.findElement(locator)).selectByVisibleText(text);
          break;
        }
      }
    }
  }

  public boolean isElementPresent(By locator) {
    try {
      wd.findElement(locator);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  public boolean isAlertPresent() {
    try {
      wd.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }
}
