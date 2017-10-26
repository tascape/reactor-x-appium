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
import com.tascape.reactor.exception.EntityCommunicationException;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Appium server instance.
 * http://www.automationtestinghub.com/3-ways-to-start-appium-server-from-java/
 *
 * @author linsong wang
 */
public class AppiumServer extends EntityCommunication {
    private static final Logger LOG = LoggerFactory.getLogger(AppiumServer.class);

    private AppiumDriverLocalService appiumDriverLocalService;

    private int port = 4723;

    @Override
    public void connect() throws Exception {
        while (true) {
            try {
                this.port = 4723 + RandomUtils.nextInt(0, 10000);
                AppiumServiceBuilder builder = new AppiumServiceBuilder();
                builder.usingPort(port);
                builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
                builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
                appiumDriverLocalService = builder.build();
                break;
            } catch (Throwable t) {
                LOG.warn(t.getLocalizedMessage());
                this.delay(RandomUtils.nextLong(1, 100));
            }
        }
        if (this.appiumDriverLocalService == null) {
            throw new EntityCommunicationException("cannot start appium server");
        }
        appiumDriverLocalService.start();
    }

    @Override
    public void disconnect() throws Exception {
        if (this.appiumDriverLocalService != null) {
            appiumDriverLocalService.stop();
        }
    }

    public int getPort() {
        return port;
    }

    private static class ESH implements ExecuteStreamHandler {
        @Override
        public void setProcessInputStream(OutputStream out) throws IOException {
            LOG.trace("setProcessInputStream");
        }

        @Override
        public void setProcessErrorStream(InputStream in) throws IOException {
            BufferedReader bis = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = bis.readLine();
                if (line == null) {
                    break;
                }
                LOG.error(line);
            }
        }

        @Override
        public void setProcessOutputStream(InputStream in) throws IOException {
            BufferedReader bis = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = bis.readLine();
                if (line == null) {
                    break;
                }
                LOG.trace(line);
            }
        }

        @Override
        public void start() throws IOException {
            LOG.trace("start");
        }

        @Override
        public void stop() {
            LOG.trace("stop");
        }
    }

    public static void main(String[] args) throws Exception {
        AppiumServer server = new AppiumServer();
        server.connect();
        LOG.info("{}", server.getPort());
        Thread.sleep(30000);
        server.disconnect();
    }
}
