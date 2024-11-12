# Java Client for DataTracks

The Java client for DataTracks allows clients to connect to a DataTracks server and perform network-based operations such as sending and receiving data. Built using Java's NIO (New I/O) API, the client can communicate with the server using efficient, non-blocking TCP or UDP connections, depending on the selected configuration.

## Features
- **TCP/UDP Support**: The client supports both TCP and UDP protocols for communication, allowing flexibility in network communication.
- **Protocol Support**: The client supports the custom data protocol used by the DataTracks system, enabling smooth data exchange with the server.
- **Asynchronous I/O**: Built with Java NIO for scalable and non-blocking network communication.
- **Authentication**: The client supports various authentication methods to ensure secure communication between the client and the server.
- **Data Serialization**: Data is serialized and deserialized using Protocol Buffers, ensuring efficient and compact communication.

## Requirements
- **Java 11+**: The client requires at least Java 11.
- **Network Access**: The client must have network access to the DataTracks server to function properly.
- **Dependencies**: The client relies on Protocol Buffers for data serialization.

## Usage
To run the Java client, simply execute the following command:
```bash
java -jar datatracks-client.jar --host <server-host> --port <server-port>