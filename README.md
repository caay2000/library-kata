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

```http request
GET /account/{id}
```

```http request
- POST /account
- Content-Type: `application/vnd.api+json`
- Request Body:
{
    "data": {
        "type": "account",
        "attributes": {
            "identityNumber": "Y86351836",
            "name": "pool.frog",
            "surname": "cute dodger.blue",
            "birthdate": "2023-10-21",
            "email": "ashamed.cornflower.blue@earwig.io",
            "phonePrefix": "+679",
            "phoneNumber": "651384567"
        }
    }
}
```

GET /book

GET /book?isbn

GET /book/{id}

POST /book

GET /loan/{loanId}

POST /loan

POST /loan/{bookId}

```