# adhan-peek
Simple program to show remaining minutes of the next prayer time and close after a few seconds.

This is meant to be invoked by shortcuts (e.g. [hotkey](https://docs.fedoraproject.org/en-US/quick-docs/proc_setting-key-shortcut/) or [hotcorner](https://extensions.gnome.org/extension/4167/custom-hot-corners-extended/)).

This program uses the [Adhan](https://github.com/batoulapps/Adhan) repository to calculate prayer times. For more information, please check out the repository.

## Usage
You need to give 4 parameters to the jar file:
- Latitude of your location
- Longitude of your location
- Calculation method
- Configuration file

### Location
Latitude and longitude must be decimal number, like `-62.2146 3.1415`.

### Calculation Method
You can choose from available methods:
- MuslimWorldLeague
- Egyptian
- UmmAlQura
- Dubai
- Qatar
- Kuwait
- MoonsightingCommittee
- Singapore
- NorthAmerica
- Turkey

Or you can define your own method by giving a path to a file. Example file can ben found `configurations/custom_method.txt`.

### Configuration File
You must provide a configuration file. Example file can ben found `configurations/configurations.txt`.

You can configure
- Size of the window
- Color of the window
- Position of the window
- Size of the text
- Color of the text
- Timeout
- Maximum negative time (For example program shows -5 if the prayer time passed 5 minutes)

## Example
```bash
java -jar adhan-peek-1.0.jar 12.345 6.789 MuslimWorldLeague conf.txt
```

## Demo
Here is a demo with custom hot corner
![Demo](https://github.com/cahidenes/visuals/blob/main/adhan-peek.gif?raw=true)