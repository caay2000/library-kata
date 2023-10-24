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


## API Details

#### Account API

<details>
 <summary><code>GET <b>/account</b></code> <code>Retrieves all accounts</code></summary>

  ```http request
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

XXX
  ```
</details>
<details>
 <summary><code>GET <b>/account/{accountId}</b></code> <code>Retrieves account with  id {accountId}</code></summary>

  ```http request
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

{
  "data": {
    "id": "00000000-0000-0000-0000-000000000000",
    "type": "account"
    "attributes": {
      "birthdate": "1970-01-01",
      "email": "john.doe@email.example",
      "identityNumber": "B01234567",
      "name": "John",
      "phoneNumber": "+44",
      "phonePrefix": "+44",
      "registerDate": "2020-01-01T00:00:00Z",
      "surname": "Doe"
    }
  }
}
  ```
</details>
<details>
 <summary><code>POST <b>/account</b></code><code>Creates a new account</code></summary>

```http request
Content-Type: application/vnd.api+json

{
  "data": {
    "type": "account"
    "attributes": {
      "birthdate": "1970-01-01",
      "email": "john.doe@email.example",
      "identityNumber": "B01234567",
      "name": "John",
      "phoneNumber": "+600123456",
      "phonePrefix": "+44",
      "surname": "Doe"
    }
  }
}
```
```http request
HTTP/1.1 201 Created
Content-Type: application/vnd.api+json

{
  "data": {
    "id": "00000000-0000-0000-0000-000000000000",
    "type": "account"
    "attributes": {
      "birthdate": "1970-01-01",
      "email": "john.doe@email.example",
      "identityNumber": "B01234567",
      "name": "John",
      "phoneNumber": "+44",
      "phonePrefix": "+44",
      "registerDate": "2020-01-01T00:00:00Z",
      "surname": "Doe"
    }
  }
}
  ```
</details>


#### Book API

<details>
 <summary><code>GET <b>/book</b></code> <code>Retrieves all books</code></summary>

  ```http request
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

xxx
  ```
</details>
<details>
 <summary><code>GET <b>/book/{bookId}</b></code> <code>Retrieves book with id {bookId}</code></summary>

  ```http request
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

{
  "id": "00000000-0000-0000-0000-000000000000",
  "type": "book"
  "data": {
    "attributes": {
      "author": "John Doe",
      "isbn": "00000000-0000-0000-0000-000000000000",
      "pages": 90,
      "publisher": "John Doe Publishing Inc.",
      "title": "Life of John Doe"
    }
  }
}
  ```
</details>
<details>
 <summary><code>POST <b>/account</b></code><code>Creates a new book</code></summary>

```http request
Content-Type: application/vnd.api+json

{
  "data": {
    "type": "book"
    "attributes": {
      "author": "John Doe",
      "isbn": "00000000-0000-0000-0000-000000000000",
      "pages": 90,
      "publisher": "John Doe Publishing Inc.",
      "title": "Life of John Doe"
    }
  }
}
  ```
  ```http request
HTTP/1.1 201 Created
Content-Type: application/vnd.api+json

{
  "id": "00000000-0000-0000-0000-000000000000",
  "type": "book"
  "data": {
    "attributes": {
      "author": "John Doe",
      "isbn": "00000000-0000-0000-0000-000000000000",
      "pages": 90,
      "publisher": "John Doe Publishing Inc.",
      "title": "Life of John Doe"
    }
  }
}
  ```
</details>


#### Loan API

<details>
 <summary><code>GET <b>/loan</b></code> <code>Retrieves all loans</code></summary>

  ```http request
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

XXX
  ```
</details>
<details>
 <summary><code>GET <b>/loan/{loanId}</b></code> <code>Retrieves loan with id {loanId}</code></summary>

  ```http request
HTTP/1.1 200 OK
Content-Type: application/vnd.api+json

xxx
  ```
</details>
<details>
 <summary><code>POST <b>/loan</b></code><code>Creates a new loan</code></summary>

```http request
Content-Type: application/vnd.api+json

xxxx
```

```http request
HTTP/1.1 201 Created
Content-Type: application/vnd.api+json

xxxx
  ```
</details>
<details>
 <summary><code>DELETE <b>/loan/{bookId}</b></code><code>Finishes the loan for the book with id {bookId}</code></summary>

  ```http request
HTTP/1.1 202 Accepted
Content-Type: application/vnd.api+json

xxxx
  ```
</details>


