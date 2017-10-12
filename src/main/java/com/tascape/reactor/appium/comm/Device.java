/*
 * Copyright (c) 2017 - present Nebula Bay.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tascape.reactor.appium.comm;

import com.tascape.reactor.comm.EntityCommunication;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 * @param <T> device type
 */
public abstract class Device<T extends AppiumDriver> extends EntityCommunication {
    private static final Logger LOG = LoggerFactory.getLogger(Device.class);

    public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("HH.mm.ss.SSS");

    private AppiumDriver<MobileElement> driver;

    public static final DesiredCapabilities initIOSCapabilities() {
        return new DesiredCapabilities() {
            {
                setCapability("platformVersion", "11.0");
                setCapability("platformName", "iOS");
                setCapability("deviceName", "iPhone Simulator");
                setCapability("automationName", "XCUITest");
            }
        };
    }

    public static IOSDevice newIOSDevice() throws Exception {
        try {
            return new IOSDevice();
        } catch (Exception ex) {
            LOG.warn(ex.getMessage());
            Thread.sleep(1000);
            return new IOSDevice();
        }
    }

    public abstract DesiredCapabilities getCapabilities();

    public void setAppiumDriver(T driver) {
        this.driver = driver;
    }

    public T getAppiumDriver() {
        return (T) this.driver;
    }

    public WebDriverWait getWebDriverWait(int seconds) {
        return new WebDriverWait(driver, seconds);
    }

    @Override
    public void disconnect() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

    public void clickByAccessibilityId(String accessibilityId) {
        driver.findElementByAccessibilityId(accessibilityId).click();
    }

    /**
     * Takes a screen shot of current screen.
     *
     * @return image file
     *
     * @throws IOException if error
     */
    public File takeScreenShot() throws IOException {
        File ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File f = this.getLogPath().resolve("screenshot-" + LocalDateTime.now().format(DT_FORMATTER) + ".png").toFile();
        LOG.debug("Screenshot {}", f.getAbsolutePath());
        FileUtils.moveFile(ss, f);
        return f;
    }

    public static void main(String[] args) throws Exception {
        IOSDevice iOSDevice = Device.newIOSDevice();

        IOSDriver<MobileElement> ios = iOSDevice.getAppiumDriver();
        ios.closeApp();
        ios.findElementByAccessibilityId("SIGN UP - IT'S FREE").click();

        Thread.sleep(20000000);
    }
}
