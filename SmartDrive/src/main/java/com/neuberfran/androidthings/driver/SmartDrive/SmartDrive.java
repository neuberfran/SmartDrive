/*
 * Copyright 2016 Google Inc.
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

package com.neuberfran.androidthings.driver.SmartDrive;

import android.util.Log;

import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Driver for the SmartDrive High Current Motor Controller
 *
 * http://www.mindsensors.com/rpi/76-smartdrive-high-current-motor-controller
 *
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class SmartDrive implements AutoCloseable {

    private static final String TAG = SmartDrive.class.getSimpleName ();

    /*
     * Default I2C address for the i2c driver.
     *
     */

    public static final int DEFAULT_I2C_ADDRESS = 0x1b;

    /*
     * Alternative I2C address for the i2c driver.
     */
    public static final int ALTERNATIVE_I2C_ADDRESS = 0x36;


    public static final String I2C_PIN_NAME = "I2C1";

    //  Motor selection related constants
    public static final int SmartDrive_Motor_1 = 0x01;
    public static final int SmartDrive_Motor_2 = 0x02;
    public static final int SmartDrive_Motor_Both = 0x03;

    // Motor action constants
    // stop and let the motor coast.
    public static final int SmartDrive_Next_Action_Float = 0x00;

    // apply brakes, and resist change to tachometer
    public static final int SmartDrive_Next_Action_Brake = 0x01;

    // apply brakes, and restore externally forced change to tachometer
    public static final int SmartDrive_Next_Action_BrakeHold = 0x02;

    // Direction related constants
    public static final int SmartDrive_Direction_Forward = 0x01;
    public static final int SmartDrive_Direction_Reverse = 0x00;

    // Next action (upon completion of current action)
    public static final int SmartDrive_Completion_Wait_For = 0x01;
    public static final int SmartDrive_Completion_Dont_Wait = 0x00;

    // Commonly used speed constants, these are just convenience constants,
    // You can use any value between 0 and 100.
    public static final int SmartDrive_Speed_Full = 90;
    public static final int SmartDrive_Speed_Medium = 60;
    public static final int SmartDrive_Speed_Slow = 25;

    public static final int SmartDrive_CONTROL_SPEED = 0x01;
    public static final int SmartDrive_CONTROL_BRK = 0x10;
    public static final int SmartDrive_CONTROL_TIME = 0x40;
    public static final int SmartDrive_CONTROL_ON = 0x20;
    public static final int SmartDrive_CONTROL_GO = 0x80;

    public static final int SmartDrive_COMMAND = 0x41;

    public static final int SmartDrive_SPEED_M1 = 0x46;
    public static final int SmartDrive_TIME_M1 = 0x47;

    public static final int SmartDrive_SPEED_M2 = 0x4E;
    public static final int SmartDrive_TIME_M2 = 0x4F;

    // Read registers.
    public static final int SmartDrive_POSITION_M1 = 0x52;
    public static final int SmartDrive_POSITION_M2 = 0x56;
    public static final int SmartDrive_STATUS_M1 = 0x5A;
    public static final int SmartDrive_STATUS_M2 = 0x5B;

    // Status data registers
    public static final int SmartDrive_CURRENT_M1 = 0x70;
    public static final int SmartDrive_CURRENT_M2 = 0x72;

    // Supported I2C commands
    public static final int R = 0x52;
    public static final int S = 0x53;
    public static final int a = 0x61;
    public static final int b = 0x62;
    public static final int c = 0x63;
    public static final int A = 0x41;
    public static final int B = 0x42;
    public static final int C = 0x43;

    public I2cDevice mDevice;

    /*
     * Create a new SmartDrive driver connected on the given bus.
     *
     * @param bus I2C bus the sensor is connected to.
     * @throws IOException
     */
    public SmartDrive(String bus) throws IOException {
        this ( bus, DEFAULT_I2C_ADDRESS );
    }

    /*
     * Create a new SmartDrive driver connected on the given bus and address.
     *
     * @param bus     I2C bus the sensor is connected to.
     * @param address I2C address of the sensor.
     * @throws IOException
     */
    public SmartDrive(String bus, int address) throws IOException {
        PeripheralManager pioService = PeripheralManager.getInstance ();
        I2cDevice device = pioService.openI2cDevice ( bus, address );

        mDevice = device;
        try {
            connect(device);
        } catch (IOException | RuntimeException e) {
            try {
                close();
            } catch (IOException | RuntimeException ignored) {
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        }
    }

    /*
     * Create a new SmartDrive driver connected to the given I2c device.
     *
     * @param device I2C device of the sensor.
     * @throws IOException
     *
    /*package*/  SmartDrive(I2cDevice device) throws IOException {

        connect ( device );

    }

    private void connect(I2cDevice device) throws IOException {

        mDevice = device;
      
    }

    /*
     *  Writes a specified command on the command register of the SmartDrive.
     *
     *  @param cmd The command you wish the SmartDrive to execute.
     */
    public void command(int cmd) throws IOException {

        mDevice.writeRegByte ( SmartDrive_COMMAND, (byte) cmd );

    }


    /*
     * Turns the specified motor(s) forever
     * @param motor_number Number of the motor(s) you wish to turn.
     * @param direction The direction you wish to turn the motor(s).
     * @param speed The speed at which you wish to turn the motor(s).
     */

    @SuppressWarnings("StatementWithEmptyBody")
    public void SmartDrive_Run_Unlimited(int motor_number, int direction, int speed) throws IOException {

        try {
            int ctrl = 0;
            ctrl |= SmartDrive_CONTROL_SPEED;
            ctrl |= SmartDrive_CONTROL_BRK;

            if (motor_number != SmartDrive_Motor_Both) ;
            ctrl |= SmartDrive_CONTROL_GO;
            if (direction == 0x01) {
               speed = speed;
            } else if (direction != 0x01) {
               speed = speed * -1;
            }
            if ((motor_number & 0x01) != 0) ;
            //byte array = [0X46, speed, 0, 0, ctrl];
            byte[] array = new byte[]{0x46, (byte) speed, 0, 0, (byte) ctrl};
            WriteArray ( array );
            if ((motor_number & 0x02) != 0) ;
            //array = [0x4E, speed, 0, 0, ctrl];
            byte[] array2 = new byte[]{0x4E, (byte) speed, 0, 0, (byte) ctrl};
            WriteArray ( array2 );
            if (motor_number == SmartDrive_Motor_Both) ;
            WriteByte ( SmartDrive_COMMAND, S );

        } catch (IOException e) {
            Log.w ( TAG, "Unable to access I2C device", e );
        }
    }

    /*
     * Stops the specified motor(s)
     * @param motor_number Number of the motor(s) you wish to turn.
     * @param next_action How you wish to stop the motor(s).
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void SmartDrive_Stop(int motor_number, int next_action) throws IOException {

        try {
            if (next_action == SmartDrive_Next_Action_Brake || next_action == SmartDrive_Next_Action_BrakeHold) {
                if (motor_number == SmartDrive_Motor_1) ;
                WriteByte ( SmartDrive_COMMAND, A );
                if (motor_number == SmartDrive_Motor_2) ;
                WriteByte ( SmartDrive_COMMAND, B );
                if (motor_number == SmartDrive_Motor_Both) ;
                WriteByte ( SmartDrive_COMMAND, C );
            } else {
                if (motor_number == SmartDrive_Motor_1) ;
                WriteByte ( SmartDrive_COMMAND, a );
                if (motor_number == SmartDrive_Motor_2) ;
                WriteByte ( SmartDrive_COMMAND, b );
                if (motor_number == SmartDrive_Motor_Both) ;
                WriteByte ( SmartDrive_COMMAND, c );
            }

        } catch (IOException e) {
            Log.w ( TAG, "Unable to access I2C device", e );
        }
    }

    /*
     * Turns the specified motor(s) for a given amount of seconds
     * @param motor_number Number of the motor(s) you wish to turn.
     * @param direction The direction you wish to turn the motor(s).
     * @param speed The speed at which you wish to turn the motor(s).
     * @param duration The time in seconds you wish to turn the motor(s).
     * @param wait_for_completion Tells the program when to handle the next line of code.
     * @param next_action How you wish to stop the motor(s).
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void SmartDrive_Run_Seconds(int motor_number, int direction, int speed, int duration, int wait_for_completion, int next_action) throws IOException {

        try {
            int ctrl = 0;
            ctrl |= SmartDrive_CONTROL_SPEED;
            ctrl |= SmartDrive_CONTROL_TIME;

            if (next_action == SmartDrive_Next_Action_Brake) ;
            ctrl |= SmartDrive_CONTROL_BRK;
            if (next_action == SmartDrive_Next_Action_BrakeHold) ;
            ctrl |= SmartDrive_CONTROL_BRK;
            ctrl |= SmartDrive_CONTROL_ON;
            if (motor_number != SmartDrive_Motor_Both) ;
            ctrl |= SmartDrive_CONTROL_GO;
            if (direction == 0x01) {
               speed = speed;
            } else if (direction != 0x01) {
               speed = speed * -1;
            }
            if ((motor_number & 0x01) != 0) ;
            byte[] array = new byte[]{0x46, (byte) speed, (byte) duration, 0, (byte) ctrl};
            //array = [0x46, speed, duration, 0, ctrl];
            WriteArray ( array );
            if ((motor_number & 0x02) != 0) ;
            byte[] array2 = new byte[]{0x4E, (byte) speed, (byte) duration, 0, (byte) ctrl};
            WriteArray ( array2 );
            if (motor_number == SmartDrive_Motor_Both) ;
            WriteByte ( SmartDrive_COMMAND, S );
            if (wait_for_completion == SmartDrive_Completion_Wait_For) ;
            Log.i ( TAG, "passei 121 passei 121 passei 121" );

            // this delay is required for the status byte to be available for reading.
            Thread.sleep ( 1000 );

            SmartDrive_WaitUntilTimeDone ( motor_number );

        } catch (IOException e) {
            Log.w ( TAG, "Unable to access I2C device", e );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    /*
     * Waits until the specified time for the motor(s) to run is completed
     * @param motor_number Number of the motor(s) to wait for.
     */
    public void SmartDrive_WaitUntilTimeDone(int motor_number) throws IOException, InterruptedException {

        while (!SmartDrive_IsTimeDone ( motor_number )) ;

    }

    /*
     * Checks to ensure the specified time for the motor(s) to run is completed.
     * @param motor_number Number of the motor(s) to check.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean SmartDrive_IsTimeDone(int motor_number) throws IOException, InterruptedException {

        byte result = mDevice.readRegByte ( SmartDrive_STATUS_M1 );
        byte result2 = mDevice.readRegByte ( SmartDrive_STATUS_M2 );

        if (motor_number == SmartDrive_Motor_1) {
            result = ReadByte ( SmartDrive_STATUS_M1 );

            //Data register empty (receivers))
            if ((result & 0x40) == 0) {

                return true;

            } else {

                return false;
            }

        } else if (motor_number == SmartDrive_Motor_2) {
            result = ReadByte ( SmartDrive_STATUS_M2 );
            // look for the time bit to be zero.
            if ((result & 0x40) == 0) {

                return true;

            } else {

                return false;
            }
        } else if (motor_number == SmartDrive_Motor_Both) {
            result = ReadByte ( SmartDrive_STATUS_M1 );
            result2 = ReadByte ( SmartDrive_STATUS_M2 );
            // look for both time bits to be zero
            if (((result & 0x40) == 0) & ((result2 & 0x40) == 0)) {

                return true;

            } else {

                return false;
            }

        } else

            return false;

    }

    // reading the register of I2C peripheral
    public byte ReadByte(int data) throws IOException {
        byte result = mDevice.readRegByte ( data );
        try {
            result = mDevice.readRegByte ( data );
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return result;
    }

    // writing the register of I2C peripheral
    public void WriteByte(int address, int data) throws IOException {

        try {

            mDevice.writeRegByte ( address, (byte) data );

        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    // writing the registers of I2C peripheral
    public void WriteArray(byte[] arr) throws IOException {

        try {

            mDevice.write ( arr, arr.length );

        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void close() throws Exception {

        mDevice.close ();

    }

}
