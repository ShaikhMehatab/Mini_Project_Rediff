package moneyrediff;

import static org.junit.Assert.*;

import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import jxl.Cell;
import jxl.Workbook;
import jxl.Sheet;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class LoginPage {
	
	private WebDriver driver; 
	private String baseUrl;
	private String UserId;
	private String UserPassword;
	
	private String[] data;

	@Before
	public void setUp() throws Exception {
		
		//Getting Details From LoginDetails.Text File
				Properties p= new Properties();
				p.load(new FileInputStream(".\\Configuration\\LoginDetails.txt"));
				baseUrl=p.getProperty("rAppURL");
				UserId=p.getProperty("UserId");
				UserPassword=p.getProperty("UserPassword");
				
				//Setting a path for accessing chrome
				System.setProperty("webdriver.chrome.driver","C:\\Mehatab Shaikh\\Workspace\\cd1\\chromedriver.exe"); 
				driver = new ChromeDriver();
				
				//visiting site
				//baseURL="https://money.rediff.com/index.html";	
				
				//for maximize window
				driver.manage().window().maximize();	
				
				//wait for next instruction gets execute
				driver.manage().timeouts().implicitlyWait(59, TimeUnit.SECONDS);	
	}

	@Test
	public void test() throws IOException, InterruptedException{
	
		//Calling baseURL to visit Money.Rediff site
		driver.get(baseUrl);
		
		//Clicking On Sign-In Option
		driver.findElement(By.xpath(" //*[@id=\'signin_info\']/a[1] ")).click();
		
		//Code For Sign-in
		driver.findElement(By.xpath("//*[@id=\'useremail\']")).sendKeys(UserId);
		driver.findElement(By.xpath("//*[@id=\'userpass\']")).sendKeys(UserPassword);
		driver.findElement(By.xpath("//*[@id=\'loginsubmit\']")).click();
		
		/*//Writing data Into Text File
		String Module_Name = "Module_Name-Login\n";
		String Test_Result= "Test_Result-Login Is Successful\n";
		String Comments= "Comments-User Successfully Logged In\n";
	
		writeText(Module_Name, Test_Result, Comments);*/
		
		/*String Module_Name = "Module_name-Creating Portfolio\n";
		String Test_Result= "Test_Result-Creating Portfolio Successful\n";
		String Comments= "Comments-User Successfully Created Portfolio\n";*/
			
		String Comments= "Comments-8. iBeta Stock Is Already Exist\n 9. Domestic Stock Is Already Exist\n 10. Preffered Stock Is Already Exist\n";
		appendText(Comments);
		
		String strFile = "./DataPool/Portfolio.xls";
		String[] Portfolio= Read_Rediff(1,"Portfolio",strFile);
		
		for(int iter=1; iter<Portfolio.length; iter++)
		{
					
		String strPortfolio = Portfolio[iter];
			
		//Creating Portfolio
		driver.findElement(By.xpath("//*[@id=\'headcontent\']/div[1]/ul/li[2]/a")).click();
		driver.findElement(By.xpath("//*[@id=\'createPortfolio\']/img")).click();		
		
		//1. Large Cap Stock
		driver.findElement(By.xpath("//*[@id=\'create\']")).sendKeys(strPortfolio);
		driver.findElement(By.xpath("//*[@id=\'createPortfolioButton\']")).click();
		
		if(driver.findElement(By.xpath("//*[@id=\'portfolioAddError\']/div/div")).isDisplayed())
		
			Thread.sleep(10000, 1);
			driver.findElement(By.xpath("//*[@id=\'portfolioAddClose\']")).click();
	
		}
	}
	
	/*
		//2. Mutual Funds
		driver.findElement(By.xpath("//*[@id=\'create\']")).sendKeys("Mutual Funds");
		driver.findElement(By.xpath("//*[@id=\'createPortfolioButton\']")).click();
		//3. Mid Cap Stocks
		driver.findElement(By.xpath("//*[@id=\'create\']")).sendKeys("Mid Cap Stocks");
		driver.findElement(By.xpath("//*[@id=\'createPortfolioButton\']")).click();
		//4. Hybrid Stock
		driver.findElement(By.xpath("//*[@id=\'create\']")).sendKeys("Hybrid Stock");
		driver.findElement(By.xpath("//*[@id=\'createPortfolioButton\']")).click();
		//5. Growth Stock
		driver.findElement(By.xpath("//*[@id=\'create\']")).sendKeys("Growth Stock");
		driver.findElement(By.xpath("//*[@id=\'createPortfolioButton\']")).click();		
		//6. Income Stock
		driver.findElement(By.xpath("//*[@id=\'create\']")).sendKeys("Income Stock");
		driver.findElement(By.xpath("//*[@id=\'createPortfolioButton\']")).click();
	*/
	
	@After
	public void tearDown() throws Exception {
	}


public static String[] Read_Rediff (int row, String column, String strFilePath)
{
				//Printing Statement to check on console
				System.out.println("Inside read Method");
				
				//Printing Statements On Console
				System.out.print("All Portfolio Is Created Successfully Which Is Given By User\n");
				System.out.print("All Data In Sheet Is Retrieved Successfully\n");
				System.out.print("Required Data Write In Text File Successfully\n");
				
				//Declaring variables
				Cell c= null;
				int reqCol=0;
				int reqRow = 0;
				WorkbookSettings ws = null;
				Workbook workbook = null;
				Sheet sheet = null;
				FileInputStream fs = null;

try
{
	
					fs = new FileInputStream(new File(strFilePath));
					ws = new WorkbookSettings();
					ws.setLocale(new Locale("en", "EN"));
					String[] data=null;
					
					// opening the work book and sheet for reading data
					workbook = Workbook.getWorkbook(fs, ws);
					sheet = workbook.getSheet(0);
					data=new String[sheet.getRows()];
					
					// Sanitise given data
					String col = column.trim();
					
					//loop for going through the given row
					for(int j=0; j<sheet.getColumns(); j++)
						
					{
						
					Cell cell = sheet.getCell(j,0);
					if((cell.getContents().trim()).equalsIgnoreCase(col))
						
					{
						//get Column
						reqCol= cell.getColumn();
						System.out.println("column No:"+reqCol);
					
							//Loop For getting total rows in sheets
							for (int i = 0; i < sheet.getRows(); i++)
					        {
					
					
							c = sheet.getCell(reqCol, reqRow);
							
							  data[i] = c.getContents();
							
							System.out.println(data[i]);
							fs.close();
							reqRow=reqRow+1;
					        }
							
					return data;	//returning the string[] data in the sheets
					}
					
					}
}

//Handling The Exceptions
catch(BiffException be)
{
					System.out.println("The given file should have .xls extension.");
}

catch(Exception e)
{
					e.printStackTrace();

}

					System.out.println("NO MATCH FOUND IN GIVEN FILE: PROBLEM IS COMING FROM DATA FILE");
					
					return null;
}
		
public static void writeText(String Module_Name, String Test_Result, String Comments ) throws IOException
{
					//Creating Text File To Store Test Case Result
					//File f = new File("ResultTestCase.txt");
					
					//Creating Object Of Writing Text File
					FileWriter fw = new FileWriter("ResultTestCase.txt", true);
					
					//Adding Data Into Text File
					fw.write(Module_Name + Test_Result + Comments);
					
					fw.close();
}
public static void appendText(String Comments ) throws IOException
{
					//Creating Text File To Store Test Case Result
					//File f = new File("ResultTestCase.txt");
					
					//Creating Object Of Writing Text File
					FileWriter fw = new FileWriter("ResultTestCase.txt", true);
					
					//Adding Data Into Text File
					fw.write( Comments);
					
					fw.close();
}
}

