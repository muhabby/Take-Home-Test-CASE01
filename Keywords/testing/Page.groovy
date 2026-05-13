package testing

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ConditionType.EQUALS
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.openqa.selenium.Keys

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable
import testing.Locator


public class Page {
	TestObject findbyxpath(String xpath) {
		TestObject obj = new TestObject()
		obj.addProperty("xpath", ConditionType.EQUALS, xpath)
		return obj
	}
	
	public void inputForm(Map<String, String> data, boolean isPositive = true) {
		// Input Name
		if(data["First Name"]) {			
			WebUI.setText(findbyxpath(Locator.inputFieldText("Name")), data["First Name"])
		}
		if(data["Last Name"]) {			
			WebUI.setText(findbyxpath(Locator.inputFieldText("Name", "2")), data["Last Name"])
		}
		
		// Input Email
		if(data["Email"]) {			
			WebUI.setText(findbyxpath(Locator.inputFieldText("Email")), data["Email"])
		}
		
		// Select Gender
		if(data["Gender"]) {			
			WebUI.click(findbyxpath(Locator.inputCheckboxAndRadio("Gender", data["Gender"])))
		}
		
		// Input Mobile Number
		if(data["Mobile Number"]) {			
			WebUI.setText(findbyxpath(Locator.inputFieldText("Mobile(10 Digits)")), data["Mobile Number"])
		}
		
		// Input Date of Birth
		if(data["Date of Birth"]) {
			String dateOfBirth = data["Date of Birth"]
			WebUI.executeJavaScript("""
			    var input = document.getElementById('dateOfBirthInput');
			    var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;
			    nativeInputValueSetter.call(input, '${dateOfBirth}');
			    input.dispatchEvent(new Event('input', { bubbles: true }));
			    input.dispatchEvent(new Event('change', { bubbles: true }));
			""", null)
		}
		
		// Input Subjects
		if(data["Subjects"]) {
			String[] subjects = data["Subjects"].split(",")
			subjects.each { value ->
				WebUI.sendKeys(findbyxpath(Locator.inputFieldText("Subjects")), value.trim())
				WebUI.sendKeys(findbyxpath(Locator.inputFieldText("Subjects")), Keys.chord(Keys.ENTER))
			}
		}
		
		// Hobbies
		if(data["Hobbies"]) {
			String[] hobbies = data["Hobbies"].split(",")
			hobbies.each { value ->
				WebUI.click(findbyxpath(Locator.inputCheckboxAndRadio("Hobbies", value.trim())))
			}
		}
		
		// Upload Picture
		if(data["Picture"]) {
			String projectDir = RunConfiguration.getProjectDir()
			String filePath = projectDir + "/Data Files/" + data["Picture"]
			WebUI.uploadFile(findbyxpath(Locator.inputFile("Picture")), filePath)
		}
		
		// Input Current Address
		if(data["Current Address"]) {			
			WebUI.setText(findbyxpath(Locator.inputFieldTextarea("Current Address")), data["Current Address"])
		}
		
		// Input State and City
		if(data["State"]) {
			WebUI.sendKeys(findbyxpath(Locator.inputFieldText("State and City")), data["State"])
			WebUI.sendKeys(findbyxpath(Locator.inputFieldText("State and City")), Keys.chord(Keys.ENTER))
		}
		
		if(data["City"]) {			
			WebUI.sendKeys(findbyxpath(Locator.inputFieldText("State and City", "2")), data["City"])
			WebUI.sendKeys(findbyxpath(Locator.inputFieldText("State and City", "2")), Keys.chord(Keys.ENTER))
		}
		
		WebUI.click(findbyxpath(Locator.btnSubmit))
		
		if(data["TC Type"]) {
			if(data["TC Type"].equalsIgnoreCase("Positive")) {
				isPositive = true
			} else {
				isPositive = false
			}
		}
		
		// Verify
		if(isPositive) {			
			if(WebUI.waitForElementPresent(findbyxpath(Locator.txtSuccessSubmitForm), 5, FailureHandling.STOP_ON_FAILURE)) {
				println "Success"
			} else {
				println "Fail"
			}
		} else {
			if(WebUI.waitForElementNotPresent(findbyxpath(Locator.txtSuccessSubmitForm), 5, FailureHandling.STOP_ON_FAILURE)) {
				println "Success"
			} else {
				println "Fail"
			}
		}
	}
}
