# Places Directory Service

This service provides a directory of places with their details, including name, description, address, and opening hours. It supports grouping opening hours by days of the week.

## Features

- Add, update, and delete places
- Retrieve place details
- Group opening hours by days of the week

## Technologies

- Java
- Spring Boot
- Gradle

## Getting Started

## Prerequisites

- JDK 21 or higher
- Gradle

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/tony-fernandez/places-directory-service.git
   ```
2. Navigate to the project directory:
   ```sh
   cd places-directory-service
   ```
3. Build the project:
   ```sh
   ./gradlew build
   ```

## Running the Application

To run the application, use the following command:
```sh
./gradlew bootRun
```

## Running Tests

## Accessing OpenAPI Documentation

[Open API Specification](docs/openapi.yaml)

## Accessing Swagger UI
The API documentation is available using Swagger UI. To access the Swagger UI, navigate to:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

To execute the tests, use the following command:
```sh
./gradlew test
```


## API Endpoints

### POST /places
This endpoint creates a new place with an address as well as a list of opening times.  The API ensures uniqueness of the place name and opening times. 

If the place name already exists, the API returns a `409 Conflict` status code with an error message. If the opening times overlap with an existing place, the API returns a `409 Conflict` status code with an error message.
Additionally, the API ensures that no duplicate opening times are provided for the same day of the week. If duplicate opening times are provided, the API returns a `409 Conflict` status code with an error message.

On successful execution, the API returns a `201 Created` status code.
#### Request Body
```json
{
   "address": {
      "street": "Bahnhofstrasse 1",
      "city": "Zurich",
      "postcode": "8000",
      "country": "Switzerland"
   },
   "description": "A family-friendly restaurant serving international cuisine",
   "name": "Delicious Restaurant",
   "openingHours": [
      {
         "dayOfWeek": "MONDAY",
         "closed": true,
         "openingTime": null,
         "closingTime": null
      },
      {
         "dayOfWeek": "TUESDAY",
         "closed": false,
         "openingTime": "11:30",
         "closingTime": "15:00"
      },
      {
         "dayOfWeek": "TUESDAY",
         "closed": false,
         "openingTime": "18:00",
         "closingTime": "00:00"
      },
      {
         "dayOfWeek": "WEDNESDAY",
         "closed": false,
         "openingTime": "11:30",
         "closingTime": "15:00"
      },
      {
         "dayOfWeek": "WEDNESDAY",
         "closed": false,
         "openingTime": "18:00",
         "closingTime": "00:00"
      },
      {
         "dayOfWeek": "THURSDAY",
         "closed": false,
         "openingTime": "11:30",
         "closingTime": "15:00"
      },
      {
         "dayOfWeek": "THURSDAY",
         "closed": false,
         "openingTime": "18:00",
         "closingTime": "00:00"
      },
      {
         "dayOfWeek": "FRIDAY",
         "closed": false,
         "openingTime": "11:30",
         "closingTime": "15:00"
      },
      {
         "dayOfWeek": "FRIDAY",
         "closed": false,
         "openingTime": "18:00",
         "closingTime": "00:00"
      },
      {
         "dayOfWeek": "SATURDAY",
         "closed": false,
         "openingTime": "18:00",
         "closingTime": "00:00"
      },
      {
         "dayOfWeek": "SUNDAY",
         "closed": false,
         "openingTime": "11:30",
         "closingTime": "15:00"
      }
   ]
}
```
#### Response
After successfully creating a new place, the API returns the place details, address, opening hours as well as a representation of the opening hours grouped by days, for example:

```
Opening Hours
Monday: Closed
Tuesday - Friday: 11:30 - 15:00
                  18:00 - 00:00
Saturday:         18:00 - 00:00
Sunday:           11:30 - 15:00                  
```

```json
{
  "id": "5b55cbca-f4d0-4917-9b73-47fb1a37e7bd",
  "name": "Delicious Restaurant",
  "description": "A family-friendly restaurant serving international cuisine",
  "address": {
    "id": "79d60d4f-95f4-454e-990a-dbb75ce05186",
    "street": "Bahnhofstrasse 1",
    "city": "Zurich",
    "postcode": "8000",
    "country": "Switzerland"
  },
  "openingHours": [
    {
      "id": "efa4132a-28ae-4fd7-b4f4-b6c828b256e2",
      "dayOfWeek": "FRIDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "85150d7d-bcab-4778-b027-9feedc8ea68b",
      "dayOfWeek": "THURSDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "35fa7e03-9473-4c69-8c4b-257d0ab5f69f",
      "dayOfWeek": "SUNDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "638a7537-93b9-40d0-951e-8d4e92ec1906",
      "dayOfWeek": "MONDAY",
      "openingTime": null,
      "closingTime": null,
      "closed": true
    },
    {
      "id": "f3d1d8b7-0705-4cf7-a4c2-725ed77bab9f",
      "dayOfWeek": "FRIDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "9485a7e0-1f3f-45b6-b840-c8569798c229",
      "dayOfWeek": "TUESDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "e68d7c3b-450f-463a-b6cc-65100709b1e4",
      "dayOfWeek": "TUESDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "c256ab5e-46a2-412b-9ff0-bfcb76781fa2",
      "dayOfWeek": "WEDNESDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "443983ea-1b2d-4aca-949f-8782f23710bc",
      "dayOfWeek": "WEDNESDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "a2765791-2bb8-4a53-b433-73378eee85ee",
      "dayOfWeek": "THURSDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "ee18d2a1-e1f9-4118-abed-6b0384867827",
      "dayOfWeek": "SATURDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    }
  ],
  "groupedOpeningHours": [
    {
      "daysOfWeek": "Monday",
      "openingHours": [
        "Closed"
      ]
    },
    {
      "daysOfWeek": "Tuesday - Friday",
      "openingHours": [
        "11:30 - 15:00",
        "18:00 - 00:00"
      ]
    },
    {
      "daysOfWeek": "Saturday",
      "openingHours": [
        "18:00 - 00:00"
      ]
    },
    {
      "daysOfWeek": "Sunday",
      "openingHours": [
        "11:30 - 15:00"
      ]
    }
  ]
}
```

### GET /places
This endpoint retrieves all places in the directory. The API returns a list of places with their details, address, opening hours as well as a representation of the opening hours grouped by days.
### GET /places/{placeId}
This endpoint retrieves a place by ID. The API returns the place details, address, opening hours as well as a representation of the opening hours grouped by days.  If the place does not exist, the API returns a `404 Not Found` status code with an error message.

On successful execution, the API returns a `200 Ok` status code.
#### Response

```json
{
  "id": "5b55cbca-f4d0-4917-9b73-47fb1a37e7bd",
  "name": "Delicious Restaurant",
  "description": "A family-friendly restaurant serving international cuisine",
  "address": {
    "id": "79d60d4f-95f4-454e-990a-dbb75ce05186",
    "street": "Bahnhofstrasse 1",
    "city": "Zurich",
    "postcode": "8000",
    "country": "Switzerland"
  },
  "openingHours": [
    {
      "id": "efa4132a-28ae-4fd7-b4f4-b6c828b256e2",
      "dayOfWeek": "FRIDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "85150d7d-bcab-4778-b027-9feedc8ea68b",
      "dayOfWeek": "THURSDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "35fa7e03-9473-4c69-8c4b-257d0ab5f69f",
      "dayOfWeek": "SUNDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "638a7537-93b9-40d0-951e-8d4e92ec1906",
      "dayOfWeek": "MONDAY",
      "openingTime": null,
      "closingTime": null,
      "closed": true
    },
    {
      "id": "f3d1d8b7-0705-4cf7-a4c2-725ed77bab9f",
      "dayOfWeek": "FRIDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "9485a7e0-1f3f-45b6-b840-c8569798c229",
      "dayOfWeek": "TUESDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "e68d7c3b-450f-463a-b6cc-65100709b1e4",
      "dayOfWeek": "TUESDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "c256ab5e-46a2-412b-9ff0-bfcb76781fa2",
      "dayOfWeek": "WEDNESDAY",
      "openingTime": "11:30",
      "closingTime": "15:00",
      "closed": false
    },
    {
      "id": "443983ea-1b2d-4aca-949f-8782f23710bc",
      "dayOfWeek": "WEDNESDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "a2765791-2bb8-4a53-b433-73378eee85ee",
      "dayOfWeek": "THURSDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    },
    {
      "id": "ee18d2a1-e1f9-4118-abed-6b0384867827",
      "dayOfWeek": "SATURDAY",
      "openingTime": "18:00",
      "closingTime": "00:00",
      "closed": false
    }
  ],
  "groupedOpeningHours": [
    {
      "daysOfWeek": "Monday",
      "openingHours": [
        "Closed"
      ]
    },
    {
      "daysOfWeek": "Tuesday - Friday",
      "openingHours": [
        "11:30 - 15:00",
        "18:00 - 00:00"
      ]
    },
    {
      "daysOfWeek": "Saturday",
      "openingHours": [
        "18:00 - 00:00"
      ]
    },
    {
      "daysOfWeek": "Sunday",
      "openingHours": [
        "11:30 - 15:00"
      ]
    }
  ]
}
```

### DELETE /places/{placeId}
Deletes a place by ID. If the place does not exist, the API returns a `404 Not Found` status code with an error message.

On successful execution, the API returns a `200 Ok` status code.
### PUT /places/{placeId}/addresses/{addressId}
By providing the place ID, address ID and address payload, this endpoint updates the address of a place. If the place or address does not exist, the API returns a `404 Not Found` status code with an error message.

On successful execution, the API returns a `200 Ok` status code.
#### Request Body
```json
{
   "street": "Bahnhofstrasse 120",
   "city": "Zurich",
   "postcode": "8000",
   "country": "Switzerland"
}
```
#### Response
The response returns the updated address details.
```json
{
   "id": "79d60d4f-95f4-454e-990a-dbb75ce05186",
   "street": "Bahnhofstrasse 120",
   "city": "Zurich",
   "postcode": "8000",
   "country": "Switzerland"
}
```

### POST /places/{placeId}/opening-hours
This endpoint adds a new opening time to a place. On successful execution, the API returns a `201 Created` status code.  If the place does not exist, the API returns a `404 Not Found` status code with an error message. The API ensures that no duplicate opening times are provided for the same day of the week. If duplicate opening times are provided, the API returns a `409 Conflict` status code with an error message.
#### Request Body
```json
{
   "dayOfWeek": "SUNDAY",
   "openingTime": "16:30",
   "closingTime": "00:00",
   "closed": false
}
```
#### Response
```json
{
   "id": "cc83cfc1-ba01-49bf-85db-c1ea1863ad72",
   "dayOfWeek": "SUNDAY",
   "openingTime": "16:30",
   "closingTime": "00:00",
   "closed": false
}
```
### PUT /places/{placeId}/opening-hours/{openingHourId}
The endpoint updates an opening time by ID. If the place or opening time does not exist, the API returns a `404 Not Found` status code with an error message.
On successful execution, the API returns a `200 Ok` status code.
```json
{
   "dayOfWeek": "SUNDAY",
   "openingTime": "16:30",
   "closingTime": "23:00",
   "closed": false
}
```
#### Response
```json
{
   "id": "cc83cfc1-ba01-49bf-85db-c1ea1863ad72",
   "dayOfWeek": "SUNDAY",
   "openingTime": "16:30",
   "closingTime": "23:00",
   "closed": false
}
```
### DELETE /places/{placeId}/opening-hours/{openingHourId}
The endpoint deletes an opening time by ID. If the place or opening time does not exist, the API returns a `404 Not Found` status code with an error message.

On successful execution, the API returns a `200 Ok` status code.

## Exceptions

| Name                           | Description                                                                                                                                                                      |
|--------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| AddressNotFoundException       | Thrown when attempting to update a non existent address.                                                                                                                         |
| DuplicateOpeningHoursException | Thrown when attempting to create a new place with duplicate addresses, or when attempting to create a new opening hours or update an existing entry that will cause a duplicate. |
| ExistingPlaceException         | Thrown when attempting to create a place with a name already stored.                                                                                                             |
| OpeningHoursNotFoundException  | Thrown when attempting to update a non existent opening hours entry.                                                                                                             |
| PlaceNotFoundException         | Thrown when attempting to retrieve a non place entry.                                                                                                                            |
