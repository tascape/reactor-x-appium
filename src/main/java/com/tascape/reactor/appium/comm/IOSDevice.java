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

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 */
public class IOSDevice extends Device<IOSDriver> {
    private static final Logger LOG = LoggerFactory.getLogger(IOSDevice.class);

    public static final String VERSION = "reactor.appium.ios.VERSION";

    public static final String DEVICE_NAME = "reactor.appium.ios.DEVICE_NAME";

    private final DesiredCapabilities capabilities = IOSDevice.initCapabilities();

    private IOSDriver<MobileElement> iOSDriver;

    /**
     * Gets an instance of simulator.
     *
     * @param iOSVersion, such as "iOS 11.0"
     * @param deviceName, such as "iPhone 6s"
     *
     * @return an iOS instance
     *
     * @throws Exception
     */
    public static IOSDevice newIOSSimulator(String iOSVersion, String deviceName) throws Exception {
        IOSDevice iOSDevice = new IOSDevice();
        iOSDevice.getCapabilities().setCapability("platformVersion", iOSVersion);
        iOSDevice.getCapabilities().setCapability("deviceName", "iPhone Simulator");
        String udid = IOSSimulatorControl.getUdid(iOSVersion, deviceName);
        iOSDevice.getCapabilities().setCapability("udid", udid);
        return iOSDevice;
    }

    @Override
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    @Override
    public void connect(String host, int port) throws Exception {
        String url = "http://" + host + ":" + port + "/wd/hub";
        iOSDriver = new IOSDriver(new URL(url), capabilities);
        driver = iOSDriver;
    }

    public static final DesiredCapabilities initCapabilities() {
        return new DesiredCapabilities() {
            {
                setCapability("platformName", "iOS");
                setCapability("deviceName", SYS_CONFIG.getProperty(DEVICE_NAME, "iPhone Simulator"));
                setCapability("automationName", "XCUITest");
                setCapability("autoAcceptAlerts", true);
            }
        };
    }

    public static void main(String[] args) throws Exception {
        IOSDevice iOSDevice = IOSDevice.newIOSSimulator("iOS 11.0", "iPhone 6s");

        IOSDriver<MobileElement> ios = iOSDevice.getAppiumDriver();
        ios.closeApp();
        ios.findElementByAccessibilityId("SIGN UP - IT'S FREE").click();

        Thread.sleep(20000000);
    }
}
