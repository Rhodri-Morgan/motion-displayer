# Motion Displayer Application

## About

This project is an implementation of block matching algorithms used in motion estimation. This application allows users to visualise the inter-frame movement/changes by drawing motion vectors within each macro block of a given video.

https://user-images.githubusercontent.com/52254823/150772308-401b890d-cb78-406c-a14e-d34b91db53b8.mp4

![bear_out.jpg](documentation_sources/bear_out.jpg)

_Example frame above. See [`bear_out.mp4`](documentation_sources/bear_out.mp4) as full example of output._

## Java and Dependencies

### Java Version

This project is using `java version 11.0.10`.

### Gradle Version

This project is using `Gradle 5.0`.

### Dependencies

- This project requires `OpenCV 4.5.3` for Java. Instructions for setup with Gradle and IDEA can be found [here](https://www.pisciottablog.com/2021/03/02/use-opencv-in-a-gradle-project-using-intellij-idea-community-edition-for-java-in-windows-10/).

- This project requires `JavaFX 12.0.2`. Instructions for setup with Gradle and IDEA can be found [here](https://openjfx.io/openjfx-docs/#gradle).

## Building Jar and Exe

### Jar

In IntelliJ IDEA `File -> Project Structure -> Artifacts -> Add JAR from modules with dependencies` and follow the images below.

**Ensure `opencv_java453.dll` is added manually.**

![Jar Instructions 1](documentation_sources/jar_instructions_1.png)

![Jar Instructions 2](documentation_sources/jar_instructions_2.png)

Then proceed to `Build -> Build Artifacts`. Output should be located at `out/artifacts/motion_displayer_main_jar`.

### Exe

Utilising Launch4J follow [this video](https://www.youtube.com/watch?v=h68WlAn_Vfg) on how to generate EXE. Use `jdk-11.0.10` instead of JRE as suggested in video.

There is an existing config file `launcher4j_config`.

## Running Executable

**The executable can only be run on Windows 10 64 bit.** You can [download the executable here](https://cdn.rhodrimorgan.com/Motion_Displayer.zip) otherwise [clone the repository including git lfs files](JRE_DOWNLOAD.md) and run `Motion Displayer.exe`. Do not delete any files.
