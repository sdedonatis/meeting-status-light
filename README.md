# status-light
A status light indicator for Jabber, Teams or others meeting apps, so everyone knows if you are in a meeting or busy without asking you.

In theory it can work with any meeting app that shows a color changing icon because the java agent running in the PC only looks for changes in color in that pixel (configurable).

##Schematic
	PC (agent) --> usb-cable -->RP2040 --> Neopixels

Animated GIF

##BOM
| Name                 | Quantity | Descriptions          |
| -------------------- | :------: | ----------------------|
| RP2040 Zero          |    1     |                       |
| WS2812B strip        |    1     |3 or 4 leds            |
| wires                |    3     |                       |
| 3x10mm screw         |    2     |                       |
| USB A to USB C cable |    1     |                       |
| Printed bottom       |    1     |Opaque filament        |
| Printed top          |    1     |Opaque filament        |
| Printed indicator    |    1     |white or clear filament|

##Building instructions
How to build the hardware

##instalation
Upload the firmware to the indicator
Compile java agent

#Running
Run the client in java
