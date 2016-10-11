Tiff Library for Java
==========

Library for importing and exporting TIFF Files.

Check conformance with:
- Baseline 6.0
- Tiff/EP
- Tiff/IT

Licensing
---------
The library is licensed under:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

CI Status
---------
- [![Build Status](https://travis-ci.org/EasyinnovaSL/Tiff-Library-4J.svg?branch=master)](https://travis-ci.org/EasyinnovaSL/Tiff-Library-4J "Tiff-Library-4J Travis-CI master branch build") Travis-CI: `master`

- [![Build Status](https://travis-ci.org/EasyinnovaSL/Tiff-Library-4J.svg?branch=develop)](https://travis-ci.org/EasyinnovaSL/Tiff-Library-4J "Tiff-Library-4J Travis-CI develop build") Travis-CI: `develop`

Getting the TiffLibrary
------------------------
####Download release version
You can download the latest release version from the [maven repository](http://mvnrepository.com/artifact/com.easyinnova/tifflibrary4java).

Building the TiffLibrary from Source
----------------------------------------
###Pre-requisites
If you want to build the code from source you'll require:

 * Java 8, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
 * [Maven v3+](https://maven.apache.org/)

####Downloading the latest release source
You can use [Git](https://git-scm.com/) to download the source code.
```
git clone https://github.com/EasyinnovaSL/Tiff-Library-4J.git
```
or download the latest release from [GitHub] (https://github.com/EasyinnovaSL/Tiff-Library-4J/releases).

####Use Maven to compile the source
Move to the downloaded project directory and call Maven install:

    cd TiffLibrary4J
    mvn clean install

The build JAR will be generated under the directory target.