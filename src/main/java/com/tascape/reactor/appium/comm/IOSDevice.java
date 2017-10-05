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

    private IOSDriver<MobileElement> driver;

    private final DesiredCapabilities capabilities = Device.initIOSCapabilities();

    @Override
    public void connect() throws Exception {
        this.connect("http://127.0.0.1:4723/wd/hub");
    }

    public void connect(String url) throws Exception {
        driver = new IOSDriver(new URL(url), capabilities);
        super.setAppiumDriver(driver);
    }

    @Override
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }
}
