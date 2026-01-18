# HealthConnect - Patient Service (Java Core)

This microservice acts as the central orchestration hub for the **HealthConnect** architecture. It is responsible for managing patient medical records, diagnostic data, and enforcing high-standard security protocols for sensitive file handling.

## 🛡️ Security Strategy: Double-Layer Encryption

The service implements a proactive security architecture to comply with international medical data protection standards:

1.  **Application-Level Encryption (Layer 1):** Before leaving the Java ecosystem, every file is encrypted using **AES-256-GCM**. This ensures that even if internal network traffic is intercepted, the data remains undecipherable.
2.  **PII Indexing (Data Masking):** Sensitive Personal Identifiable Information (Names, IDs) is processed via JPA `AttributeConverters`. This allows the database to store encrypted values while the application handles them as clear text transparently.
3.  **Type Integrity:** Strict implementation of native PostgreSQL UUIDs to prevent collisions and optimize indexing performance across microservices.

## 🚀 Technology Stack

* **Java 17** & **Spring Boot 3.4.x**
* **Spring Data JPA**: Advanced persistence orchestration with PostgreSQL.
* **Hibernate 6**: Handling of complex types and custom attribute converters.
* **Jakarta Persistence**: Management of `@ManyToOne` and `@OneToMany` relationships with Jackson recursion control.
* **Lombok**: For clean, maintainable, and boilerplate-free code.

## 🏗️ Microservices Architecture

The complete ecosystem consists of:
* **Patient Service (Java):** Business logic and application-level encryption (The "Brain").
* **Storage Service (Go):** Binary storage and second-layer encryption (The "Vault").
* **Audit Hub (Python):** (In Development) Access monitoring and anomaly detection.

## 🛠️ Configuration & Installation

### Prerequisites
* JDK 17
* PostgreSQL 14+
* Maven 3.8+

### Environment Configuration (application.yml)
```yaml
app:
  security:
    java-secret-key: ${JAVA_ENCRYPTION_KEY} # 32-byte key for AES
  storage:
    service-url: http://localhost:8082/api/v1/storage
    api-key: ${STORAGE_API_KEY}
    api-secret: ${STORAGE_API_SECRET}



## Instalation
git clone <your-repo-url>
cd healthconnect-patient-service
mvn clean install
mvn spring-boot:run