/*
 * Copyright (c) 2017 - present Nebula Bay.
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

import com.tascape.reactor.driver.EntityDriver;
import com.tascape.reactor.appium.comm.Device;
import java.io.File;
import java.io.IOException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 * @param <T>
 */
@SuppressWarnings("ProtectedField")
public abstract class App<T extends Device> extends EntityDriver {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(App.class);

    public static final String APP_PATH = "reactor.appium.APP_PATH";

    protected T device;

    public abstract String getBundleId();

    public String getAppPath() {
        return SYS_CONFIG.getProperty(APP_PATH, "");
    }

    public abstract int getLaunchDelayMillis();

    protected String version;

    public void setAppiumDevice(T device) {
        this.device = device;
    }

    public void launch() throws Exception {
        device.connect();
        //device.getAppiumDriver().launchApp();
    }

    public File takeScreenShot() {
        try {
            return device.takeScreenShot();
        } catch (IOException ex) {
            LOG.warn(ex.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public void reset() throws Exception {
        if (device != null && device.getAppiumDriver() != null) {
            device.getAppiumDriver().resetApp();
        }
    }

    public void interactManually() throws Exception {
        interactManually(30);
    }

    /**
     * The method starts a GUI to let an user inspect element tree and take screenshot when the user is interacting
     * with the app-under-task manually. It is also possible to run UI Automation instruments JavaScript via this UI.
     * Please make sure to set timeout long enough for manual interaction.
     *
     * @param timeoutMinutes timeout in minutes to fail the manual steps
     *
     * @throws Exception if case of error
     */
    public void interactManually(int timeoutMinutes) throws Exception {
    }
}
