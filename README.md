# SmartDrive driver for Android Things

This driver supports [SmartDrive](http://www.mindsensors.com/rpi/76-smartdrive-high-current-motor-controller) Driver for the High Current Motor Controller

NOTE: these drivers are not production-ready. They are offered as sample implementations of Android Things user space drivers for common peripherals as part of the Developer Preview release. There is no guarantee of correctness, completeness or robustness.

## How to use the driver

## Gradle dependency

To use the ```SmartDrive``` driver, simply add the line below to your project's build.gradle, where <version> matches the last version of the driver available on [jcenter](https://bintray.com/beta/#/neuberfran/SmartDrive/com.neuberfran.androidthings.driver.SmartDrive?tab=overview).

```
dependencies {
    compile 'com.neuberfran.androidthings.driver-SnartDrive:<version>'
}
```

## Sample usage

```js
import com.neuberfran.androidthings.driver.SmartDrive;

// Access the environmental driver:

SmartDrive SmartDrive;

try {
    SmartDrive = new SmartDrive(i2cBusName);
    // Configure driver settings according to your use case
    SmartDrive.setTemperatureOversampling(SmartDrive.OVERSAMPLING_1X);
    // Ensure the driver is powered and not sleeping before trying to read from it
    SmartDrive.setMode(SmartDrive.MODE_NORMAL);
} catch (IOException e) {
    // couldn't configure the device...
}

// Read the current temperature:

try {
    float temperature = SmartDrive.readTemperature();
} catch (IOException e) {
    // error reading temperature
}

// Close the environmental sensor when finished:
```

## Schematic

![alt text](https://github.com/neuberfran/SmartDrive/blob/master/SmartDrive.png)

## License
Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
