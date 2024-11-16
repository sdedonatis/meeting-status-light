# meeting status light
A status light indicator for Jabber, Teams or others meeting apps, so everyone knows if you are in a meeting or busy without asking you.

In theory it can work with any meeting app that shows a color changing icon because the java agent running in the PC only looks for changes in color in that pixel (configurable).

![Different status colors](https://raw.githubusercontent.com/sdedonatis/meeting-status-light/refs/heads/main/images/status%20examples.jpg)

## Schematic
	PC (agent) --> usb-cable -->RP2040 --> Neopixels

Animated GIF

## BOM
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

## Building instructions
How to build the hardware. 
* Print the 3 STL parts.
* Cut 4 leds from the led strip, along the line, or between the solder pads.
* Solder the 3 cables from the strip to the RP2040 (+5v to +5v, Din to pin 29, GND to GND)
* Insert the RP2040 inside "printed bottom" part, place "printed top" part above and screw.
* Glue the led strip to the inside of "printed top"
* Place "printed indicator" on top of the leds.

![Aseemble](https://raw.githubusercontent.com/sdedonatis/meeting-status-light/refs/heads/main/images/assemble.png)

## Instalation
* Upload the firmware (main.py) to the RP2040.
* Compile the java agent.AgentSystemTray

## Running
* Run java code (main class meetingstatus.AgentSystemTray)
* Clic the icon in the taskbar to configure the position of the pixel controlling the led color
