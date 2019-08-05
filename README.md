# Simple Messenger
A simple messenger service using Apache Thrift and Java Swing.
The messenger supports:
a) P2P messaging
b) Multicast messaging
c) File Transfer across clients

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

1. java
2. javac
3. thrift

### Installing

IntelliJ was used as IDE support for building the project on Ubuntu18.04
However, command line can be used as well:
1. javac -cp .:libs/\* src/\* gen-java/fileTransfer/\* gen-java/simpleMessenger/\*
2. mkdir out; cd out; mkdir fileTransfer; mkdir simpleMessenger
3. mv src/\*.class out/
4. mv gen-java/fileTransfer/\*.class out/fileTransfer/
5. mv gen-java/simpleMessenger/\*.class out/simpleMessenger/

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags).

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
