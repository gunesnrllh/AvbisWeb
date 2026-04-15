package com.testinium.step;

import com.testinium.base.BaseTest;
import com.testinium.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BaseSteps extends BaseTest {

    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static String SAVED_ATTRIBUTE;


    public BaseSteps() {
        initMap(getFileList());
    }

    WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        WebElement webElement = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'})", webElement);
        return webElement;
    }

    List<WebElement> findElements(String key) {

        return driver.findElements(getElementInfoToBy(findElementInfoByKey(key)));
    }

    public By getElementInfoToBy(ElementInfo elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());

        }
        return by;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }

    private boolean isDisplayedBy(By by) {
        return driver.findElement(by).isDisplayed();
    }

    private String getPageSource() {
        return driver.switchTo().alert().getText();
    }

    public static String getSavedAttribute() {
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength) {

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + chars[random.nextInt(chars.length)];
        }

        return stringRandom;
    }

    public WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    public String getElementText(String key) {
        return findElement(key).getText();
    }


    public String getElementAttributeValue(String key, String attribute) {
        return findElement(key).getAttribute(attribute);
    }


    public void javaScriptClicker(WebDriver driver, WebElement element) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", element);
    }

    public void javascriptclicker(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Step({"Wait <value> seconds", "<int> saniye bekle"})
    public void waitBySeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //logger.info("Waited " + seconds + " second");
    }




    @Step({"Wait <value> milliseconds", "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




    @Step({"Click to element <key>",
            "<key> elementine tikla"})
    public void clickElementMethod(String key) {
        if (!key.isEmpty()) {
            clickElement(findElement(key));
            waitBySeconds(2);
            logger.info(" clicked on element " + key);
        }
    }


    @Step("<key> elementin üstünde bekle")
    public void hover(String key) {
        hoverElement(findElement(key));
    }

    @Step({"Click to element <key> with focus", "<key> elementine focus ile tikla"})
    public void clickElementWithFocus(String key) {
        actions.moveToElement(findElement(key));
        actions.click();
        actions.build().perform();
        logger.info(key + " elementine focus ile tiklandı.");
    }

    @Step({"<key> elementini kontrol et", "Check the element <key>"})
    public void checkElement(String key) {


        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
        List<WebElement> elements = findElements(key);

        if (elements.size() == 0) {

            Assertions.fail("* '" + key + "' Elementi Sayfada Mevcut Degil.  * " +
                    "\n *" +
                    "\n********************FAIL*********************" +
                    "\n *" +
                    "\n '" + key + "' elementi Sayfada Mevcut Degil." + key + " Elementini bulunduran Sayfa Acilmadi veya Eksik Acildi" +
                    "\n *" +
                    "\n********************FAIL********************" +
                    "\n *");

        }
        if (elements.size() > 0) {
            logger.info(key + "  Elementi Bulundu. ");
        }
    }

    @Step({"Go to <url> address", "<url> adresine git"})
    public void goToUrl (String url) {
        driver.get(url);
        logger.info(" Going to " + url);
    }
    @Step("<cookieValue> keyli cookie ekle")
    public void cookieAdded(String cookieValue) {
        Cookie cookie = new Cookie.Builder("AutomationSecurityKey", cookieValue)
                .path("/")
                .domain("monsternotebook.com.tr")
                .isHttpOnly(true)
                .isSecure(true)
                .build();
        driver.manage().addCookie(cookie);
        System.out.println("AutomationSecurityKey eklendi: " + cookieValue);
        driver.navigate().refresh();
    }

    @Step({"Wait for element to load with id <id>", "Elementin yüklenmesini bekle <id>"})
    public void waitElementLoadWithCss(String id) {
        waitByMilliSeconds(500);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(By.cssSelector(id)).size() > 0) {
                logger.info(id + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element: '" + id + "' doesn't exist.");
    }

    @Step({"Check if element <key> exists else print message <message>", "Element <key> var mı kontrol et yoksa hata mesajı ver <message>"})
    public void getElementWithKeyIfExistsWithMessage(String key, String message) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                logger.info(key + " element found.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(message +
                "\n *" +
                "\n********************FAIL********************" +
                "\n *" +
                "\n " + message +
                "\n *" +
                "\n********************FAIL********************" +
                "\n *");
    }


    @Step({"Check if element <key> not exists", "Element yok mu kontrol et <key>"})
    public void checkElementNotExists(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);
        if (driver.findElements(by).size() == 0) {
            logger.info(key + " elementinin olmadığı kontrol edildi.");
            return;
        }
        logger.info(" !!! Sayfada görünmemesi gereken " + key + " elementi mevcut !!! ");
    }

    @Step({"Check if element <key> exists, click element v1", "<key> Elementi var mı kontrol et varsa tikla"})
    public void checkElementExistsAndClick(String key) {

        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                driver.findElement(by).click();
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element '" + key + "' not exist.");
    }

    @Step({"ıf element <key> exists click", "<key> elementi görünürse tikla"})
    public void ifExistClick(String key) {
        List<WebElement> elements = findElements(key);

        if (elements.size() == 0) {

            logger.info(key + " Elementi Bulunamadi. ");
        }
        if (elements.size() > 0) {
            logger.info(key + "  Elementi Bulundu. ");
            findElement(key).click();

        }
    }

    @Step({"Write value <text> to element <key>", "<text> textini <key> elemente yaz"})
    public void ssendKeys(String text, String key) {

        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
        List<WebElement> elements = findElements(key);

        if (elements.size() == 0) {

            logger.info(key + " The desired text could not be written to the element. Element does not exist or does not work. ");
            Assertions.fail(" * " + key + " The desired text could not be written to the element. Element does not exist or does not work. " + "\n *" + "\n********************FAIL********************" + "\n *" + "\n " + key + " elementine istenilen text yazilamadi. Element Yok veya Calismiyor. " + "\n *" + "\n********************FAIL********************" + "\n *");
        }
        if (elements.size() > 0) {
            findElement(key).clear();
            findElement(key).sendKeys(text);
            logger.info(text + " text " + key + "  text written to element.");
        }
    }
    @Step({"Check if current URL contains the value <expectedURL>", "Şuanki URL <url> değerini içeriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL) {
        waitBySeconds(2);
        String actualURL = driver.getCurrentUrl();

        if (actualURL != null && actualURL.contains(expectedURL)) {
            logger.info("Şu anki URL " + expectedURL + " değerini içeriyor.");
        } else {
            Assertions.fail("Su anki URL Beklenen URL Degerini Icermiyor" + "  Beklenen Deger : " + expectedURL + ", Su Anki:  " + actualURL);
        }

    }

    @Step({"Check if current URL contains the value <expectedURL> if not give error as a <message>", "Şuanki URL <url> değerini içeriyor mu kontrol et icermiyorsa hata olarak <message> yazdir"})
    public void checkURLContainst(String expectedURL, String message) {
        waitBySeconds(2);
        String actualURL = driver.getCurrentUrl();

        if (actualURL != null && actualURL.contains(expectedURL)) {
            logger.info("Şu anki URL " + expectedURL + " değerini içeriyor.");
        } else {
            Assertions.fail(message +
                    "\n *" +
                    "\n********************FAIL********************" +
                    "\n *" +
                    "\n " + message +
                    "\n *" +
                    "\n********************FAIL********************" +
                    "\n *");
        }
    }

    @Step({"Write value <text> to element <key> v2",
            "<text> textini <key> Alanina Yaz"})
    public void ssendKeys1(String text, String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(infoParam));
        wait.until(ExpectedConditions.elementToBeClickable(infoParam));
        if (!key.equals("")) {
            WebElement element = findElement(key);
            element.clear();
            element.sendKeys(text);
            logger.info(key + " elementine " + text + " texti yazıldı.");
        }
    }

    @Step({"Print element text by <key>", "<key> Elementinin text değerini yazdır"})
    public void printElementText(String key) {
        String messageBox = findElement(key).getText();
        logger.info(messageBox+" Adet urun listelendi");
    }

    @Step({"Refresh page", "Sayfayı yenile"})
    public void refreshPage() throws InterruptedException {
        driver.navigate().refresh();
        Thread.sleep(5000);

    }


    @Step({"Change page zoom to <value>%", "Sayfanın zoom değerini değiştir <value>%"})
    public void chromeZoomOut(String value) {
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }


    @Step({"Focus on frame with <key>", "Frame'e odaklan <key>"})
    public void chromeFocusFrameWithNumber(String key) {
        WebElement webElement = findElement(key);
        driver.switchTo().frame(webElement);
    }

    @Step({"Focus on default frame", "default frame gecis yap"})
    public void chromeFocusFrameDefault() {
        driver.switchTo().defaultContent();
    }

    @Step({"Accept Chrome alert popup", "Chrome uyarı popup'ını kabul et"})
    public void acceptChromeAlertPopup() {
        driver.switchTo().alert().accept();
    }


    //----------------------SONRADAN YAZILANLAR-----------------------------------\\


    // Key değeri alınan listeden rasgele element seçme amacıyla yazılmıştır. @Mehmetİnan

    public void randomPick(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        elements.get(index).click();
    }

    //Javascript driverın başlatılması
    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    //Javascript scriptlerinin çalışması için gerekli fonksiyon
    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }

    //Belirli bir locasyona sayfanın kaydırılması
    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }

    //Belirli bir elementin olduğu locasyona websayfasının kaydırılması
    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        WebElement webElement = driver.findElement(getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
        return webElement;
    }


    @Step({"<key> alanına kaydır" , "Swipe to <key> element"})
    public void scrollToElement(String key) {
        scrollToElementToBeVisible(key);
        logger.info(key + " elementinin olduğu alana kaydırıldı");

    }


    @Step({"<key> alanına js ile kaydır"})
    public void scrollToElementWithJs(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        WebElement element = driver.findElement(getElementInfoToBy(elementInfo));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }




    @Step({"<key> elementine javascript ile tikla", "Click <key> element with javascript"})
    public void clickToElementWithJavaScript(String key) {
        WebElement element = findElement(key);
        javascriptclicker(element);
        logger.info(key + " element clicked with javascript");
    }

    @Step("<key> alanını javascript ile temizle")
    public void clearWithJS(String key) {
        WebElement element = findElement(key);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value ='';", element);
    }


    @Step("<text> textini <key> elemente tek tek yaz")
    public void sendKeyOneByOne(String text, String key) throws InterruptedException {

        WebElement field = findElement(key);
        field.clear();
        if (!key.equals("")) {
            for (char ch : text.toCharArray())
                findElement(key).sendKeys(Character.toString(ch));
            Thread.sleep(10);
            logger.info(key + " elementine " + text + " texti karakterler tek tek girlilerek yazıldı.");
        }
    }

    @Step("<key> elementine <text> değerini js ile yaz")
    public void writeToKeyWithJavaScript(String key, String text) {
        WebElement element = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=arguments[1]", element, text);
        logger.info(key + " elementine " + text + " değeri js ile yazıldı.");
    }


    @Step({"Check if element <key> exists",
            "Element var mı kontrol et <key>"})
    public void getElementWithKeyIfExists(String key) {
        ElementInfo elementInfo = findElementInfoByKey(key);
        By by = getElementInfoToBy(elementInfo);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                logger.info(key + " element found.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail("Element: '" + key + "' doesn't exist.");
    }





    @Step("<element> elementine random mail yaz")
    public void randomMail(String element) {
        Random random = new Random();
        int randomNumber = 1000000 + random.nextInt(9000000);
        findElement(element).sendKeys("emre"+randomNumber+"@gmail.com");
    }

    @Step({"<key> dropdownindan <value> degerini sec"})
    public void selectFromDropdown(String key, String value) {
        WebElement dropdownElement = findElement(key);
        Select dropdown = new Select(dropdownElement);
        try {
            dropdown.selectByVisibleText(value);
            logger.info(key + " dropdownindan '" + value + "' secildi.");
        } catch (Exception e) {
            try {
                dropdown.selectByValue(value);
                logger.info(key + " dropdownindan value='" + value + "' secildi.");
            } catch (Exception ex) {
                logger.error(key + " dropdownindan '" + value + "' secilemedi: " + ex.getMessage());
                throw ex;
            }
        }
    }

    @Step({"<key> dropdownindan index <index> sec"})
    public void selectFromDropdownByIndex(String key, String index) {
        WebElement dropdownElement = findElement(key);
        Select dropdown = new Select(dropdownElement);
        int indexInt = Integer.parseInt(index);
        dropdown.selectByIndex(indexInt);
        logger.info(key + " dropdownindan index " + index + " secildi.");
    }


    @Step({"Wait for page to load", "Sayfanin yuklenmesini bekle"})
    public void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        logger.info("Sayfa yuklendi.");
    }

    @Step({"Wait for element <key> to be clickable", "<key> elementinin tiklanabilir olmasini bekle"})
    public void waitForElementToBeClickable(String key) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        By locator = getElementInfoToBy(findElementInfoByKey(key));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        logger.info(key + " elementi tiklanabilir hale geldi.");
    }

    @Step({"Wait for element <key> to be visible", "<key> elementinin gorunur olmasini bekle"})
    public void waitForElementToBeVisible(String key) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        By locator = getElementInfoToBy(findElementInfoByKey(key));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        logger.info(key + " elementi gorunur hale geldi.");
    }
}