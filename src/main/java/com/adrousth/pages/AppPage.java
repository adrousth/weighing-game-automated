package com.adrousth.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppPage {
    private WebDriver driver;
    private WebDriverWait wdw;

    @FindBy(id = "weigh")
    private WebElement weighButton;

    @FindBy(xpath = "//button[contains(text(), \"Reset\")]")
    private WebElement resetButton;

    @FindBy(xpath = "//div[contains(@class, \"result\")]/button")
    private WebElement result;

    @FindBy(xpath = "//div[contains(@class, \"game-info\")]/ol/li")
    private List<WebElement> weighingList;

    @FindBy(xpath = "//div[contains(@class, \"coins\")]")
    private WebElement answerButtons;

    @FindBy(xpath = "//input[contains(@data-side,\"left\")]")
    private List<WebElement> leftBowl;

    @FindBy(xpath = "//input[contains(@data-side,\"right\")]")
    private List<WebElement> rightBowl;

    public AppPage(WebDriver driver) {
        this.driver = driver;
        wdw = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }


    /**
     *  Clicks the weigh button which checks if the two bowls are the same or different.
     */
    public void clickWeighButton() {
        weighButton.click();
    }

    /**
     * Clicks the reset button which empties the bowls and resets the results.
     */
    public void clickResetButton() {
        resetButton.click();
    }

    /**
     * Gets the results after clicking the weigh button.
     * @return String containing less than (<), greater than (>) or equal (=)
     */
    public String getMeasurementResults() {
        wdw.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(result, "?")));
        return result.getText();
    }

    /**
     * Gets the list of all weighing results attempted.
     * @return List of Strings containing the contents of the bowls and their results (>,< or =) from previous weighings.
     */
    public List<String> getWeighingList() {
        List<String> list = new ArrayList<>();
        for (WebElement item :
                weighingList) {
            list.add(item.getText());
        }
        return list;
    }

    /**
     * Clicks one of the buttons indicating your answer.
     * @param answer The answer to be clicked.
     */
    public void clickAnswer(String answer) {
        answerButtons.findElement(By.xpath("//button[contains(text(), \"" + answer + "\")]")).click();
    }

    /**
     * Fills a single grid of the left bowl with an input.
     * @param grid The grid number to be filled: 0-8.
     * @param input The input to be put into the grid.
     */
    public void fillLeftBowlGrid(int grid, String input) {
        leftBowl.get(grid).sendKeys(input);
    }
    /**
     * Fills a single grid of the right bowl with an input.
     * @param grid The grid number to be filled: 0-8.
     * @param input The input to be put into the grid.
     */
    public void fillRightBowlGrid(int grid, String input) {
        rightBowl.get(grid).sendKeys(input);
    }


    /**
     * Fills the left bowl with the given values. Only uses up to the first 8.
     * @param fillings the inputs to be put into the bowl.
     */
    public void fillBowlLeft(String[] fillings) {
        for (int i = 0; i < fillings.length && i < 8; i++) {
            leftBowl.get(i).sendKeys(fillings[i]);
        }
    }

    /**
     * Fills the right bowl with the given values. Only uses up to the first 8.
     * @param fillings the inputs to be put into the bowl.
     */
    public void fillBowlRight(String[] fillings) {
        for (int i = 0; i < fillings.length && i < 8; i++) {
            rightBowl.get(i).sendKeys(fillings[i]);
        }
    }

    /**
     * Checks to see if there is an alert present and returns its message.
     * @return String containing the message in the alert, null if there is no alert.
     */
    public String checkAlert() {
        try {
            wdw.until(ExpectedConditions.alertIsPresent());
            return driver.switchTo().alert().getText();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

}
