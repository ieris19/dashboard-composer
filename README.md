Dashboard Composer
========================= 
![Current Version][version-badge]
![License][license]

Dashboard composer is a Java application that allows you to create simple
dashboards and homepages. With these, you can create landing pages for your
browser, or create simple dashboards for self-hosted applications!
The possibilities are endless.

-------------------------

## Technologies

![Java][java-shield]
![HTML5][html5-shield]
![CSS3][css-shield]
![JavaScript][js-shield]

This application is built using Java, and the dashboard is built using HTML,
CSS, and JavaScript. The application uses JavaFX for the GUI, and the dashboard
is built using
[Smoky-Hill-Dash][inspiration-link] as a base. That means the project produces
simple static files that can be linked to with a browser ("file://" url) or
hosted on any web server.

-------------------------

## Features

Dashboard Composer is based on a simple dashboard template,
[Smoky-Hill-Dash][inspiration-link], and has been modified to be more complete
and easily customizable. It includes the following features:

**Customizable Dashboards** The obvious main feature of this application is the
ability to create and customize dashboards. You can add and remove links, a
search bar, and customize the look of the dashboard to your liking.

If you know HTML and CSS, and you just want a simple dashboard, you can easily
modify the original template or the output of this application for fine-tuned
control.

-------------------------

## Setup

The application currently doesn't offer a compiled binary, so you will have to
compile it yourself. To do so, you will need to have the JDK21 or later
installed and Maven. Then, you can run `mvn package` to compile the application
and from there you can run the jar file.

In the future, I plan to offer a jar and exe packaging for easier access.

To use the dashboard, you can open the `index.html` file in your browser.  
This file can be designated as the homepage in your browser, by changing the
browser settings to open the file on startup.  
The file can also be hosted on a web server and accessed from anywhere!
It's a simple folder with static assets, so it can be deployed in many ways!

-------------------------

## License

> You can check out the full license [here][license-text]

This project is licensed under the terms of the **Mozilla Public License Version
2.0** license.

[version-badge]: https://img.shields.io/badge/version-1.1.0-green.svg?style=for-the-badge

[license]: https://img.shields.io/github/license/ieris19/dashboard-composer?style=for-the-badge

[license-text]: https://www.mozilla.org/en-US/MPL/2.0/


[inspiration-link]: https://github.com/SteamWo1f/Smoky-Hill-Dash

[java-shield]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white

[html5-shield]: https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white

[css-shield]: https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white

[js-shield]: https://img.shields.io/badge/javascript-%23F7DF1E.svg?style=for-the-badge&logo=javascript&logoColor=black