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
package com.tascape.reactor.appium.driver;

import com.tascape.reactor.appium.comm.IOSDevice;
import com.tascape.reactor.exception.EntityDriverException;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 */
public abstract class IOSApp extends App<IOSDevice> {
    private static final Logger LOG = LoggerFactory.getLogger(IOSApp.class);

    private final Map<Class<? extends IOSWindow>, IOSWindow> loadedWindows = new HashMap<>();

    public void setDevice(IOSDevice device) {
        this.device = device;
    }

    @Override
    public void launch() throws Exception {
        String app = this.getAppPath();
        if (StringUtils.isNotBlank(app)) {
            LOG.debug("launch app by package");
            device.getCapabilities().setCapability("app", app);
        } else {
            String bundleId = this.getBundleId();
            if (StringUtils.isNotBlank(bundleId)) {
                LOG.debug("launch app by bundle id");
                device.getCapabilities().setCapability("bundleId", bundleId);
            }
        }
        super.launch();
    }

    public <T extends IOSWindow> T open(Class<T> windowClass) {
        IOSWindow window = loadedWindows.get(windowClass);
        if (window == null) {
            try {
                window = windowClass.newInstance();
                PageFactory.initElements(new AppiumFieldDecorator(device.getAppiumDriver()), window);
                window.setApp(this);
                window.load();
                loadedWindows.put(windowClass, window);
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new EntityDriverException(ex.getLocalizedMessage());
            }
        }
        return windowClass.cast(window);
    }

    public <T extends IOSWindow> T open(By by, Class<T> windowClass) {
        MobileElement element = (MobileElement) device.getAppiumDriver().findElement(by);
        return this.open(element, windowClass);
    }

    public <T extends IOSWindow> T open(MobileElement element, Class<T> windowClass) {
        element.click();
        IOSWindow window = loadedWindows.get(windowClass);
        if (window == null) {
            try {
                window = windowClass.newInstance();
                PageFactory.initElements(new AppiumFieldDecorator(device.getAppiumDriver()), window);
                window.setApp(this);
                window.load();
                loadedWindows.put(windowClass, window);
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new EntityDriverException(ex.getLocalizedMessage());
            }
        }
        return windowClass.cast(window);
    }

    @Override
    public int getLaunchDelayMillis() {
        return 10000;
    }

    @Override
    public String getName() {
        return "IOSApp";
    }
}
