package com.adrousth;

import com.adrousth.pages.AppPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

public class WeighingGame {

    private WebDriver driver;

    private WebDriverWait wdw;

    private  AppPage appPage;

    public WeighingGame() {
        driver = new ChromeDriver();
        wdw = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Starts the weighing game with the optimal solution in two "weighings".
     *
     * The game consists of a scale and 9 objects which all weigh the same except for 1 which is lighter than the others.
     * The goal is to find the object that is not the same (lightest).
     *
     * Divides the 9 objects into 3 groups (group one, two and three) with 3 objects each.
     * Measure the first two groups against each other.
     * If one group is lighter than the other, then the lighter object must be in that group.
     * If they are the same weight then the lighter object must be in the third group.
     * Once you know the group that has the lighter object you can repeat the same steps as above using the group to
     * Create three new groups each with one number from the group found to be lightest.
     * If the two numbers weighed are not equal then it is the one that is lighter.
     * If they are equal then it is the third number.
     */
    public void startOptimal() {

        String[] groupOne = {"0","1","2"};
        String[] groupTwo = {"3","4","5"};
        String[] groupThree = {"6","7","8"};
        appPage = new AppPage(driver);

        try {
            driver.get("http://sdetchallenge.fetch.com/");

            String results = firstWeighing(groupOne, groupTwo);

            String answer = "";
            if (results.equals("<")) {
                System.out.println("lightest item is in first group: " + Arrays.toString(groupOne));
                answer = secondWeighing(groupOne);
            } else if (results.equals(">")) {
                System.out.println("lightest item is in second group: " + Arrays.toString(groupTwo));
                answer = secondWeighing(groupTwo);
            } else if (results.equals("=")) {
                System.out.println("lightest item is in third group: " + Arrays.toString(groupThree));
                answer = secondWeighing(groupThree);
            } else {
                System.out.println("error, invalid character " + results);
            }


            System.out.println("The answer should be: " + answer);
            appPage.clickAnswer(answer);

            String alertMessage = appPage.checkAlert();
            System.out.println("final results: " + alertMessage);


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    /**
     * First weighing for the optimal solution.
     * @param leftGroup the group of inputs to be put on the left.
     * @param rightGroup the group of inputs to be put on the right.
     * @return String containing the results (>,< or =) from the weighing.
     */
    private String firstWeighing(String[] leftGroup, String[] rightGroup) {
        appPage.fillBowlLeft(leftGroup);
        appPage.fillBowlRight(rightGroup);

        appPage.clickWeighButton();

        return appPage.getMeasurementResults();
    }

    /**
     * Second weighing for the optimal solution.
     * @param group the group containing the lightest object as found by the first weighing
     * @return String containing the final answer for the game.
     */
    private String secondWeighing(String[] group) {
        appPage.clickResetButton();

        appPage.fillLeftBowlGrid(0, group[0]);
        appPage.fillRightBowlGrid(0, group[1]);

        appPage.clickWeighButton();

        String results = appPage.getMeasurementResults();
        if (results.equals("<")) {
            return group[0];
        } else if (results.equals(">")) {
            return group[1];
        } else if (results.equals("=")) {
            return group[2];
        } else {
            System.out.println("error, invalid character " + results);
            return "";
        }

    }

}

