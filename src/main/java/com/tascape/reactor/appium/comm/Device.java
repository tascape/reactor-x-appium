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
import static com.tascape.reactor.AbstractCaseResource.SYS_CONFIG;

/**
 *
 * @author linsong wang
 * @param <T> device type
 */
public abstract class Device<T extends AppiumDriver> extends EntityCommunication {
    private static final Logger LOG = LoggerFactory.getLogger(Device.class);

    public static final String APPIUM_HOST = "reactor.comm.APPIUM_HOST";

    public static final String APPIUM_PORT = "reactor.comm.APPIUM_PORT";

    public static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("HH.mm.ss.SSS");

    protected AppiumDriver<MobileElement> driver;

    public abstract DesiredCapabilities getCapabilities();

    public abstract void connect(String host, int port) throws Exception;

    @Override
    public void connect() throws Exception {
        String host = SYS_CONFIG.getProperty(APPIUM_HOST, "127.0.0.1");
        int port = SYS_CONFIG.getIntProperty(APPIUM_PORT, 4723);
        this.connect(host, port);
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
}
