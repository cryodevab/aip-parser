# OFMX AIP Parser

This program is slowly becoming a tool to parse various AIP pages to the [OFMX](https://github.com/openflightmaps/ofmx) format.

## Where to get it

The OFM obstacle translator can be downloaded from [here](https://github.com/cryodevab/aip-parser/raw/master/downloads/ofmx_parser.jar). It currently supports Sweden (ES), Greece (LG) and Czechia (LK).

## How to run it

Run it like this:

`java -jar ofmx_parser.jar [ES|LG|LK] <input_file> <output_file>`

It should work in any operating system as long as you have the latest Java JRE. However, it has only been tested in Windows and Linux.

### Input files

* The input file for Sweden can be fetched from the [Swedish AROWeb](https://aro.lfv.se/Editorial/View/IAIP?folderId=21), download the latest _"Flyghinder..."_ CSV file.
* The input file for Greece can be fetched from [EAD Basic](https://www.ead.eurocontrol.int/cms-eadbasic/opencms/en/login/ead-basic/) (AIP ENR 5.4). Example file can be found [here](https://github.com/cryodevab/aip-parser/raw/master/aip-files/LG_ENR_5_4_en_2019-02-28.pdf).
* The input file for Czechia requires communication with the Czech authorities in order to acquire it. The sample data which this program was developed on can be found [here](https://raw.githubusercontent.com/cryodevab/aip-parser/master/aip-files/obstacles_20190328_UTF8.csv).

### Output files

The output file follows the OFM [Obstacle CSV](https://github.com/openflightmaps/ofmx/wiki/Obstacle-CSV) specification.

