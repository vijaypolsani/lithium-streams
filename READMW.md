+++++++++++++++++++++++
Activity Streams Design:
+++++++++++++++++++++++

=============================
PRODUCER Layer:
1. Producers of the Events (LIA / LSW / Klout)
Event Logger has to be modified in LIA to add information regarding Message Content and Private Message data. The information is missing and would add value and then written to log files.
A program similar to Real-Time will be tailing the log files and grep the Event Logs specific data and then push them into a Kafka using client API. (Can we directly push using Client API or write an intermediate file and then a simple client program grabs them to push into Kafka?)

=============================
MESSAGING Bus:
2. Asynchronous Message Bus (Kafka)
Kafka is the messaging bus as it solves the high volume real time data streaming. Also Kafka supports the message archive until a specified time, which can be used for Batch mode of message delivery.

=============================
STREAM PROCESSING:
Transformation:
3. Message Transformation (Activity Streams 1.0 JSON) (Storm / Spark / Samza)
Real-Time:
4. Message Filtering / Message Querying
Batch:
5. Time Window based Message History

Transformation:
Convert the Logger Events to the common JSON Activity Streams format using the standard Jackson streaming API and provide the output.
Real-Time:
The real-time message processing involves Message Filtering, Removing duplicates, Sorting and handle other dynamic processing requirements. Streaming API like Apache Storm or Apache Spark can be used to solve the requirements.
Batch:
Batch processing can be done by querying Kafka store and then transforming and running any custom processing to deliver the output.

=============================
MIDDLEWARE:
6. Consumer Registration & Management (Event Bus / Dynamic Router)
7. Consumer Endpoint / Adaptor connectors (CometD / WebSockets)
8. Consumer Endpoint (Bulk Data Transfer)
•	The middleware layer provides the layer of dynamic consumer configurations. The dynamic nature is that new Consumers can be added or removed based on configurations performed on this layer without bringing the system down. Selection of the producers will also be part of the configuration. Architecture patterns like Control Bus and Dynamic Router of the integration patters could be used to achieve the result.
•	Exposing the Consumer Endpoints using the Connector API’s like CometD for long polling and web clients using WebScokets for streaming the data using Push model.
•	Expose Consumer Endpoints using REST Server API for performing registration of the consumer and consuming of the stream messages.
•	Batch based connectors are exposed using the client PULL model for bulk download of the streams data according to the configured time span.
•	Apache Streams project can be leverages for this purpose but will be decided based on the scale of the requirements. (Support of the project is non-existent and may be easier to build rather than tweaking the current.)

=============================
CONSUMER:
6. Consumer Client API (REST / SOAP / Java / JS)
Consumer applications using Java/JS/Rest libraries to display the streaming data to the channel for display and usage.

