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
package com.tascape.reactor.appium.suite;

import com.tascape.reactor.exception.EntityCommunicationException;
import com.tascape.reactor.appium.comm.IOSDevice;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This suite supports plug-n-play for multiple DEVICES.
 *
 * @author linsong wang
 */
public interface IOSSuite {

    BlockingQueue<IOSDevice> SIMULATORS = null;

    default IOSDevice getAvailableSimulator() throws InterruptedException {
        IOSDevice device = SIMULATORS.poll(1, TimeUnit.SECONDS);
        if (device == null) {
            throw new EntityCommunicationException("Cannot find a device available");
        }
        return device;
    }

    default int getNumberOfSimulators() {
        return SIMULATORS.size();
    }
}
