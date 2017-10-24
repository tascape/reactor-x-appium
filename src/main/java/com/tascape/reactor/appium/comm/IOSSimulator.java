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

import com.tascape.reactor.exception.EntityCommunicationException;
import com.tascape.reactor.libx.DefaultExecutor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author linsong wang
 */
public class IOSSimulator {
    private static final Logger LOG = LoggerFactory.getLogger(IOSSimulator.class);

    private static final JSONObject SIMULATORS = getAllSimlators();

    public static String getUdid(String iOSVersion, String deviceName) {
        JSONArray devices = SIMULATORS.optJSONObject("devices").optJSONArray(iOSVersion);
        if (devices == null) {
            throw new EntityCommunicationException("cannot find simulator with version " + iOSVersion);
        }
        for (int i = 0, j = devices.length(); i < j; i++) {
            JSONObject device = devices.getJSONObject(i);
            if (deviceName.equals(device.getString("name"))) {
                return device.getString("udid");
            }
        }
        throw new EntityCommunicationException("cannot find simulator with name " + deviceName);
    }

    private static JSONObject getAllSimlators() {
        JSONObject json = new JSONObject();
        CommandLine cmdLine = new CommandLine("xcrun");
        cmdLine.addArgument("simctl");
        cmdLine.addArgument("list");
        cmdLine.addArgument("-j");
        cmdLine.addArgument("devices");
        LOG.debug("{}", cmdLine.toString());
        Executor executor = new DefaultExecutor();
        List<String> output = new ArrayList<>();
        executor.setStreamHandler(new ESH(output));
        try {
            if (executor.execute(cmdLine) == 0) {
                json = new JSONObject(StringUtils.join(output, "\n"));
            }
        } catch (IOException ex) {
            throw new EntityCommunicationException(ex);
        }
        LOG.debug(json.toString(2));
        return json;
    }

    private static class ESH implements ExecuteStreamHandler {
        private static final String PATTERN = ".+? KB/s \\(.+? bytes in .+?s\\)";

        private final List<String> list;

        ESH() {
            this.list = null;
        }

        ESH(List<String> list) {
            this.list = list;
        }

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
                if (line.matches(PATTERN)) {
                    continue;
                } else {
                    LOG.warn(line);
                }
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
                if (list != null) {
                    list.add(line);
                }
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
}
