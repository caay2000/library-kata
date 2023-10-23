# library-kata

A Project Skeleton to build Kotlin apps with Gradle and Ktor

## Library kata

- Un usuario se puede registrar para gestionar sus reservas online
    - El usuario necesita dni, nombre, apellidos, fecha de nacimiento, email y numero de telefono para registrarse
    - No se puede repetir ni dni, email ni numero de telefono

- Listar todos los libros de la biblioteca
    - Un usuario deberia poder visualizar todos los libros de la biblioteca, con informacion sobre que libros estan disponibles y cuales no

- Añadir libros a la biblioteca
    - Un administrador podra añadir libros nuevos a la biblioteca
    - Un libro debe tener informacion sobre su ISBN, titulo, autor, numero de paginas, editorial y año de publicacion
    - La biblioteca puede tener varias copias del mismo libro

- Retirar un libro
    - Un usuario deberia poder retirar un libro si esta disponible (no prestado a otros usuarios)
    - Un usuario solo puede tener prestados un maximo de 5 libros a la vez

- Devolver un libro
    - Un usuario ha de poder devolver un libro prestado.


## API


### POST /account
  Request:

    {
      "data": {
        "type": "account",
        "attributes": {
          "identityNumber": "L30186716",
          "name": "curly.coated.retriever",
          "surname": "joyous lavender.blush",
          "birthdate": "2023-10-23",
          "email": "defiant.dark.salmon@mongrel.com",
          "phonePrefix": "+961",
          "phoneNumber": "661457963"
        }
      }
    }

  Response:

    {
      "data": {
        "id": "11561c28-106b-411c-b02b-d338f5b8f8ae",
        "type": "account",
        "attributes": {
          "identityNumber": "M39709834",
          "name": "green.bee.eater",
          "surname": "elegant dark.cyan",
          "birthdate": "2023-10-23",
          "email": "elated.dark.magenta@cheetah.io",
          "phonePrefix": "+983",
          "phoneNumber": "620710568",
          "registerDate": "2023-10-23T22:33:47.964967781"
        }
      }
    }


```http request
GET /account/{id}
```

```http request
POST /account
```
Request:
```http request


```
POST /account

GET /book

GET /book?isbn

GET /book/{id}

POST /book

GET /loan/{loanId}

POST /loan

POST /loan/{bookId}

```

```http request

```