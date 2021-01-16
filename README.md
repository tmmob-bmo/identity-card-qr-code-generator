# Member Information QR Code Generator

This application reads the active users from the database and generates a QR code with the information of identity
number and registration number to put on the identity card which is given by Chamber of Computer Engineers organization.
Currently, reading all the active members and generates QR codes for them every time.

## Prerequisites

* JDK 8+
* Maven 3.3.x+
* A free 8081 port
* Docker (callable without sudo)
* Postgresql 9+

## Configuration

In order to run the application on local environment or in a server, below settings must be defined in the environment
variables.

```shell
BMO_QRCODE_EXPORT_PATH=<qr-code-image-export-path>;
BMO_QRCODE_DATASOURCE_PLATFORM=postgres;
BMO_QRCODE_DATASOURCE_DRIVER=org.postgresql.Driver;
BMO_QRCODE_DATASOURCE_URL=<jdbc-url-for-database>;
BMO_QRCODE_DATASOURCE_USERNAME=<database-username>;
BMO_QRCODE_DATASOURCE_PASSWORD=<database-password>
```

## How to run application on local environment

## Build and Deploy

## Development Process

## Contribution

## Issues

You can create or track the issues [here](https://github.com/TMMOB-BMO/identity-card-qr-code-generator/issues). Also, you can vote
for the issues that you want to be tackled soon.

## Hint

You can use any of these keywords to close the issue via commit message:

> close, closes, closed, fixes, fixed

e.g. closes #1, issue 1 will be closed automatically once the commit merged into default branch. In our case the default
branch is `main`.

## Copyright and license

TBD
