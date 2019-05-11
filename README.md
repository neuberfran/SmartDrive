# SmartDrive driver for Android Things

This driver supports [SmartDrive](http://www.mindsensors.com/rpi/76-smartdrive-high-current-motor-controller) Driver for the High Current Motor Controller

NOTE: these drivers are not production-ready. They are offered as sample implementations of Android Things user space drivers for common peripherals as part of the Developer Preview release. There is no guarantee of correctness, completeness or robustness.

## How to use the driver

## Gradle dependency

To use the ```SmartDrive``` driver, simply add the line below to your project's ```
build.gradle```, where ```<version>``` matches the last version of the driver available on [jcenter](https://bintray.com/beta/#/neuberfran/SmartDrive/driver-SmartDrive/1.0.2?tab=overview).

```
dependencies {

    implementation 'com.neuberfran.androidthings.driver-SnartDrive:<version>'

}
```

## Sample usage

```js
import com.neuberfran.androidthings.driver.SmartDrive;

// Access the environmental driver:

SmartDrive mSmartDrive;

try {

mSmartDrive = new SmartDrive(i2cBusName);
    
} catch (IOException e) {
    // couldn't configure the device...
}

// Reset encoder values:

try {
   mSmartDrive.command(CMD_R);
} catch (IOException e) {
    // error reading temperature
}

// Run Motor 01 in Direction_Forward, speed = 100, duration 9 seconds:

try {
   mSmartDrive.SmartDrive_Run_Seconds(SmartDrive_Motor_1, SmartDrive_Direction_Forward, 100, 9, SmartDrive_Completion_Wait_For,SmartDrive_Next_Action_Brake);
} catch (IOException e) {
    // error reading temperature
}


// Condition to run unlimited:

  while (!buttongpio.value) {
      mSmartDrive.SmartDrive_Run_Unlimited(SmartDrive_Motor_1, SmartDrive_Direction_Forward, 100);
 }

// Close the SmartDrive driver:

try {
    mSmartDrive.close();
} catch (IOException e) {
    // error closing sensor
}
```




## Schematic


![alt text](https://github.com/neuberfran/SmartDrive/blob/master/imgSmartDrive.png)


## License
Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
