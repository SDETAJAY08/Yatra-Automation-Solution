package com.yatra.automation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YatraAutomationScript {

	public static void main(String[] args) throws InterruptedException {
		
		
		
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--disable-notifications");
		
		//Step1: Launch the browser 
		//WebDriver is Interface of Parent type and wd is ref variable and ChromeDriver is a Child class
		WebDriver wd = new ChromeDriver(chromeOptions); 
		
		WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(20)); //to syncronize and handle the flasky scripts
		
		//Step2: load the page for us
		wd.get("https://www.yatra.com");
		
		//Step3: Maximize the browser window 
		wd.manage().window().maximize(); //this is called method chaining 
		
		//close login pop-up if it comes
		By popUpLocator = By.xpath("//*[contains(@class, 'style_popup')][1]");
		closePopUp(wait, popUpLocator);
		
		//Step4: Click on departure date 
		//what is datatype of locator -> By class and  
		//Why dont you create an object of By class?-> 
		//its an abstract class and we dont need to create object of abstract class also xpath is a static method hence directly been accessed using By.xpath
		//hence xpath -> static method and returns By kind of data
		//By -> abstract class 
		clickOnDepartureDate(wait);
		
		
		WebElement currentMonthWebElement = selectTheMonthFromCalandar(wait,0);
		WebElement nextMonthWebElement = selectTheMonthFromCalandar(wait,1);
		
		Thread.sleep(2000);
		String lowestPriceForCurrentMonth = getMeLowestPrice(currentMonthWebElement);
		
		String lowestPriceForNexttMonth = getMeLowestPrice(nextMonthWebElement);
		
		System.out.println(lowestPriceForCurrentMonth);
		
		System.out.println(lowestPriceForNexttMonth);
		
		compareTwoMonthsPrices(lowestPriceForCurrentMonth , lowestPriceForNexttMonth);
	}

	public static void clickOnDepartureDate(WebDriverWait wait) {
		By departureDateButtonLocator = By.xpath("//div[@aria-label=\"Departure Date inputbox\" and @role=\"button\"]");
		WebElement depatureDateButton = wait.until(ExpectedConditions.elementToBeClickable(departureDateButtonLocator));
		depatureDateButton.click();
	}

	public static void closePopUp(WebDriverWait wait, By popUpLocator) {
		try {
		WebElement popUpElement = wait.until(ExpectedConditions.visibilityOfElementLocated(popUpLocator));
		
		WebElement crossButton = popUpElement.findElement(By.xpath(".//img[@alt=\"cross\"]"));
		
		crossButton.click();
		}
		catch(TimeoutException e)
		{
			System.out.println("Popup not shown");
			
		}
	}

	public static String getMeLowestPrice(WebElement monthWebElement) {
		By priceLocator =By.xpath(".//span[contains(@class,\"custom-day-content \")]"); //dot is important for chaining
		List<WebElement> augPriceList = monthWebElement.findElements(priceLocator); //this is called chaining of webelement where with help of one webelement we will find another webelement
		
		//traverse the list and find lowest price
		int lowestPrice= Integer.MAX_VALUE;
		WebElement priceElement = null; //default value of non primitive data type is null
		for(WebElement price:augPriceList )
		{
			//System.out.println(price.getText());
			
			//tell the lowest price
			String priceString = price.getText();
			if(priceString.length() > 0) {
			priceString = priceString.replace("₹","").replace(",","");
			
		//convert this string to integer using wrapper class
			int priceInt = Integer.parseInt(priceString);
			if(priceInt < lowestPrice)
			{
				lowestPrice =priceInt;
				priceElement = price;
			}
		}}
		
	
		WebElement dateElement = priceElement.findElement(By.xpath(".//../..")); //findGrandparent of that price to get date
		
		
		String result = dateElement.getAttribute("aria-label")+"--Price is Rs"+lowestPrice;
		return result;
	}
		 
		 public static WebElement selectTheMonthFromCalandar(WebDriverWait wait ,int index)
		 {
			 
			 By calendarMonthLocator = By.xpath("//*[@class=\"react-datepicker__month-container\"]");
				List<WebElement> calendarMonthsList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(calendarMonthLocator));
				System.out.println(calendarMonthsList.size());
				WebElement monthCalendarWebElement = calendarMonthsList.get(index); 
				return monthCalendarWebElement;
		 }
		 
		 public static void compareTwoMonthsPrices(String currentMonthPrice , String nextMonthPrice)
		 {
			 int currentMonthRSIndex = currentMonthPrice.indexOf("is");
			 int nextMonthRSIndex = nextMonthPrice.indexOf("is");
			 System.out.println(currentMonthRSIndex);
			 System.out.println(nextMonthRSIndex);
			 String currentPrice = currentMonthPrice.substring(currentMonthRSIndex + 5 );
			 
			 
			 String nextPrice = nextMonthPrice.substring(nextMonthRSIndex + 5);
			 
			 
			 int current = Integer.parseInt(currentPrice);
			 int next = Integer.parseInt(nextPrice);
			 
			 if(current < next )
			 {
				 System.out.println("Lowest price for two months is" +current);
			 }
			 
			 else if(current == next)
			 {
				 System.out.println("Price same for both months");
			 }
			 else
			 {
				 System.out.println("Lowest price for two months is" +next);
			 }
			 
		 }
		 
		
		
		
		
		
		

	}


