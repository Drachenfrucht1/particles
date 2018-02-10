particles
===

A gravitation simulator written in java

## Installing

You will need java 8 to compile this project.<br>
To compile the compile the programm run

```
mvn install
```

The executable .jar while will be /target/particles-[version].jar<br>
You can also watch the webview [here](https://drachenfrucht1.github.io/particles).

## Configuration

There are several options you can change.<br>
All of them are located in the Settings class.

----------
The mode specifies where and how the image should be rendered<br>
There are three modes:

 - web mode: creates a webserver to which you can connect with the html file<br> in the docs folder
 - realtime mode: renders the image in realtime into a javafx window
 - render mode: saves the rendered images one after the other into the output folder.<br> **Use this mode if you want to set the calculations per second above 120**

You can also set the width and height of the playground where the objects move.

**Thank you for your interest in the project.**<br>
Feel free to create a pull request or an issue if you have some improvements.




