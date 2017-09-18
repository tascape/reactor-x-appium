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
package com.tascape.reactor.appium.driver;

import com.tascape.reactor.appium.comm.AppiumIOSDevice;
import org.apache.commons.lang3.StringUtils;
import org.libimobiledevice.ios.driver.binding.exceptions.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 */
public class IOSApp extends App<AppiumIOSDevice> {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public void setDevice(AppiumIOSDevice device) {
        this.device = device;
    }

    @Override
    public String getVersion() {
        if (StringUtils.isBlank(version)) {
            try {
                version = device.getLibIMobileDevice().getAppVersion(getBundleId());
            } catch (SDKException ex) {
                LOG.warn(ex.getMessage());
                version = "";
            }
        }
        return version;
    }
    @Override
    public String getBundleId() {
        return "com.tascape.reactor";
    }

    @Override
    public int getLaunchDelayMillis() {
        return 10000;
    }

    @Override
    public String getName() {
        return "IOSApp";
    }

    @Override
    public void launch() throws Exception {
        device.getLibIMobileDevice().getDebugService().killApp(this.getBundleId());
        device.start(this.getName(), getLaunchTries(), getLaunchDelayMillis());
    }
}
