/*
 * Copyright (c) 2015 - present Nebula Bay.
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

import com.tascape.reactor.ios.driver.LibIMobileDevice;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;

/**
 *
 * @author linsong wang
 */
public class IOSDevice extends Device<IOSDriver> {

    private IOSDriver<MobileElement> driver = null;

    private LibIMobileDevice libIMobileDevice = null;

    @Override
    public IOSDriver getAppiumDriver() {
        return driver;
    }

    @Override
    public void start(String name, int launchTries, int launchDelayMillis) {
    }

    @Override
    public void reset() {
    }

    public LibIMobileDevice getLibIMobileDevice() {
        return libIMobileDevice;
    }
}
