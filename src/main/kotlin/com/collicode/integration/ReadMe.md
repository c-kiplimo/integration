# Integration Library

The **Integration Library** provides pluggable configurations for integrating **Kafka**, **Redis**, and **Prometheus
metrics** into your Spring Boot applications.  
It is designed for modularity and can be selectively enabled/disabled via configuration properties.

---

## Features

- **Kafka Integration** → Provides a configurable Kafka producer service for publishing messages.
- **Redis Integration** → Adds a Redis provider for data storage and retrieval using Spring Data Redis.
- **Prometheus Metrics** → Enables Prometheus metrics export with common tags for application and environment.
- **Conditional Beans** → Each integration is enabled/disabled using properties, ensuring only required components are
  loaded.

