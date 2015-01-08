HomeArchive
===========
This is a small java-spring web application developed mainly for learning purposes. It performs CRUD operations with any kind of digital content using MongoDB and GridFS format. Searching DB is implemented via Elasticsearch.

The frontend is build with AngularJS framework.
For the file upload [angular-file-upload](https://github.com/nervgh/angular-file-upload) module has been used.

A very nice example of [AngularJS-REST-Spring Security](https://github.com/philipsorst/angular-rest-springsecurity) was used to write the user login module

Build
-----
To build the project, [maven] (http://maven.apache.org/download.cgi) and [yeoman-bower-grunt](http://yeoman.io/codelab/setup.html)  must be installed.
The application is build by running:

$ __mvn install__

from the project root.

Deployment
----------

Deployment requires installation and proper configuration of Elasticseacrh, MongoDB and TomCat (or any other web container). For this, a vagrant script is provided, to deploy the application inside of the VirtualBox, where the configuration will be guaranteed.
This requires installation of [VirtualBox] (https://www.virtualbox.org/wiki/Downloads) and [Vagrant] (https://www.vagrantup.com/downloads.html).

Deploy the application by running:

$ __vagrant up__

from the project root.

The deployment may take awhile depending on the machine. At least 8Gb RAM memory is required.
Access the application from a browser at:

__localhost:8080/homearchive__

You can login with user name/paasword: __admin/admin__ or __user/user__


