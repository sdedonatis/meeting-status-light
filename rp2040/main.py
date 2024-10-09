import machine
from neopixel import NeoPixel
import time

# Number of leds
NUM_LEDS = 4
# Pin with the LED
LED_PIN = 29

#Instantiates a Neopixel in pin 29 with 4 leds
led = NeoPixel(machine.Pin (LED_PIN), NUM_LEDS)

#Initial color (gray)
for led_num in range(NUM_LEDS):
    led[led_num]= (40, 40, 40)
    led.write ()

led.write ()
print("Input comma seperated RGB colors, ex. 0,0,255")

while True:
    #reads a new line from serial
    #line = uart.readline().decode('utf-8').strip()
        
    line = input("RBG?")
    # Splits a line in 3 comma separated values
    values = line.split(',')
        
    if len(values) == 3:
        try:
            # Convert string to numbers
            value1 = int(values[0])
            value2 = int(values[1])
            value3 = int(values[2])
        
            # Print values for debugging
            #print("Color 1:", value1)
            #print("Color 2:", value2)
            #print("Color 3:", value3)
             
            #Changes leds colors
            for led_num in range(NUM_LEDS):
                led[led_num] = (value1, value2, value3)
            led.write ()
            
        except ValueError:
            print("Error: Couldn't convert value to numbers.")
    else:
        print("Error: Line doesn't contains 3 values.")

    # Waits before reading again
    time.sleep(0.1)
