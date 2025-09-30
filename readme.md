# Documentación de la API de Gestión de Inventario

---

Esta documentación describe las operaciones disponibles para gestionar **Productos**, **Tipos de Producto**, *
*Categorías**, **Proveedores**, **Compras**, **Ventas** y **Autenticación** en el sistema de inventario.

## Etiquetas Principales

| Etiqueta        | Descripción                                                                              |
|:----------------|:-----------------------------------------------------------------------------------------|
| **productType** | Operaciones relacionadas con la clasificación de productos (ej: smartphone, laptop).     |
| **products**    | Operaciones para gestionar los productos individuales en el inventario.                  |
| **category**    | Operaciones para administrar las agrupaciones de productos (ej: electronics, computing). |
| **suppliers**   | Operaciones para la gestión de proveedores.                                              |
| **purchases**   | Operaciones para el registro y consulta de compras.                                      |

---

## 1. ProductType (Tipos de Producto)

### Endpoints

| Ruta                              | Método   | Resumen                                | Descripción                                           |
|:----------------------------------|:---------|:---------------------------------------|:------------------------------------------------------|
| `/ProductType/add`                | `POST`   | Agregar Tipo de Producto               | Agrega un nuevo tipo de producto.                     |
| `/ProductType/get`                | `GET`    | Obtener Tipos de Producto              | Recupera la lista completa.                           |
| `/ProductType/get/id/{id}`        | `GET`    | Obtener Tipo de Producto por ID        | Recupera un tipo de producto usando su `{id}`.        |
| `/ProductType/get/name/{name}`    | `GET`    | Obtener Tipo de Producto por Nombre    | Recupera un tipo de producto usando su `{name}`.      |
| `/ProductType/update/id/{id}`     | `PUT`    | Actualizar Tipo de Producto por ID     | Actualiza un tipo de producto por su `{id}`.          |
| `/ProductType/update/name/{name}` | `PUT`    | Actualizar Tipo de Producto por Nombre | Actualiza un tipo de producto por su `{name}` actual. |
| `/ProductType/delete/id/{id}`     | `DELETE` | Eliminar Tipo de Producto por ID       | Elimina un tipo de producto usando su `{id}`.         |
| `/ProductType/delete/name/{name}` | `DELETE` | Eliminar Tipo de Producto por Nombre   | Elimina un tipo de producto usando su `{name}`.       |

### Ejemplos de valores para `name` (Enum)

`smartphone`, `laptop`, `tablet`, `gaming_console`, `audio`, `peripheral`, `monitor`, `gpu`, `cpu`, `networking`,
`drone`, `vr_headset`, `3d_printer`, `development_board`.

---

## 2. Products (Productos)

### Endpoints

| Ruta                                              | Método   | Resumen                          | Descripción                                                    |
|:--------------------------------------------------|:---------|:---------------------------------|:---------------------------------------------------------------|
| `/products/get`, `/products/all`                  | `GET`    | Obtener Todos los Productos      | Recupera la lista de todos los productos.                      |
| `/products/get/id/{id}`                           | `GET`    | Obtener Producto por ID          | Recupera un producto específico usando su `{id}`.              |
| `/products/get/name/{name}`                       | `GET`    | Obtener Producto por Nombre      | Recupera un producto específico usando su `{name}`.            |
| `/products/get/productTypeName/{productTypeName}` | `GET`    | Obtener Productos por Tipo       | Filtra productos por `{productTypeName}`.                      |
| `/products/getByCategory/{categoryId}`            | `GET`    | Obtener Productos por Categoría  | Filtra productos por `{categoryId}`.                           |
| `/products/add`                                   | `POST`   | Agregar Producto                 | Agrega un nuevo producto al inventario.                        |
| `/products/update/id/{id}`                        | `PUT`    | Reemplazar Producto por ID       | Actualiza **todos** los campos de un producto por su `{id}`.   |
| `/products/update/{name}`                         | `PUT`    | Reemplazar Producto por Nombre   | Actualiza **todos** los campos de un producto por su `{name}`. |
| `/products/update/name/{name}`                    | `PATCH`  | Actualización Parcial por Nombre | Actualiza **algunos** campos de un producto por su `{name}`.   |
| `/products/delete/{name}`                         | `DELETE` | Eliminar Producto por Nombre     | Elimina un producto. **Requiere token de autorización.**       |
| `/products/delete/id/{id}`                        | `DELETE` | Eliminar Producto por ID         | Elimina un producto.                                           |

### Esquema del Cuerpo de Solicitud (Agregar/Actualizar)

| Propiedad       | Tipo      | Requerido | Restricciones                                |
|:----------------|:----------|:----------|:---------------------------------------------|
| `name`          | `string`  | Sí        | Nombre del producto (de la lista permitida). |
| `description`   | `string`  | Sí        | Mínimo 1, Máximo 50 caracteres.              |
| `categoryId`    | `integer` | Sí        | Rango: 6 a 8.                                |
| `buyPrice`      | `number`  | Sí        | Precio de compra.                            |
| `salePrice`     | `number`  | Sí        | Precio de venta.                             |
| `stock`         | `integer` | Sí        | Rango: 1 a N.                                |
| `supplierId`    | `integer` | Sí        | Rango: 1 a N.                                |
| `productTypeId` | `integer` | Sí        | Rango: 1 a N.                                |

---

## 3. Category (Categorías)

### Endpoints

| Ruta                             | Método   | Resumen                         | Descripción                                |
|:---------------------------------|:---------|:--------------------------------|:-------------------------------------------|
| `/categories/add`                | `POST`   | Agregar Categoría               | Agrega una nueva categoría.                |
| `/categories/get`                | `GET`    | Obtener Categorías              | Recupera la lista completa.                |
| `/categories/get/name/{name}`    | `GET`    | Obtener Categoría por Nombre    | Recupera una categoría usando su `{name}`. |
| `/categories/get/id/{id}`        | `GET`    | Obtener Categoría por ID        | Recupera una categoría usando su `{id}`.   |
| `/categories/update/id/{id}`     | `PUT`    | Actualizar Categoría por ID     | Actualiza una categoría por su `{id}`.     |
| `/categories/update/name/{name}` | `PUT`    | Actualizar Categoría por Nombre | Actualiza una categoría por su `{name}`.   |
| `/categories/delete/id/{id}`     | `DELETE` | Eliminar Categoría por ID       | Elimina una categoría usando su `{id}`.    |
| `/categories/delete/name/{name}` | `DELETE` | Eliminar Categoría por Nombre   | Elimina una categoría usando su `{name}`.  |

### Ejemplos de valores para `name` (Enum)

`electronics`, `computing`, `telecommunications`, `audio_video`, `gaming`, `photography`, `smart_devices`, `components`,
`networking`, `drones`, `virtual_reality`, `3d_printing`, `creative_electronics`.

---

## 4. Suppliers (Proveedores)

### Endpoints

| Ruta                            | Método   | Resumen                         | Descripción                                          |
|:--------------------------------|:---------|:--------------------------------|:-----------------------------------------------------|
| `/suppliers/get`                | `GET`    | Obtener Proveedores             | Devuelve la lista de proveedores y sus contactos.    |
| `/suppliers/add`                | `POST`   | Agregar Proveedor               | Agrega un nuevo proveedor y su contacto.             |
| `/suppliers/get/id/{id}`        | `GET`    | Obtener Proveedor por ID        | Recupera un proveedor específico usando su `{id}`.   |
| `/suppliers/get/name/{name}`    | `GET`    | Obtener Proveedor por Nombre    | Recupera un proveedor específico usando su `{name}`. |
| `/suppliers/update/id/{id}`     | `PUT`    | Actualizar Proveedor por ID     | Actualiza un proveedor por su `{id}`.                |
| `/suppliers/update/name/{name}` | `PUT`    | Actualizar Proveedor por Nombre | Actualiza un proveedor por su `{name}`.              |
| `/suppliers/delete/id/{id}`     | `DELETE` | Eliminar Proveedor por ID       | Elimina un proveedor usando su `{id}`.               |
| `/suppliers/delete/name/{name}` | `DELETE` | Eliminar Proveedor por Nombre   | Elimina un proveedor usando su `{name}`.             |

### Esquema del Cuerpo de Solicitud (Agregar/Actualizar)

| Propiedad         | Tipo     | Requerido | Restricciones                                 |
|:------------------|:---------|:----------|:----------------------------------------------|
| `name`            | `string` | Sí        | Nombre del proveedor (de la lista permitida). |
| `contact`         | `object` | Sí        | Información de contacto.                      |
| `contact.address` | `string` | Sí        | Dirección.                                    |
| `contact.email`   | `string` | Sí        | Formato de correo electrónico.                |
| `contact.phone`   | `string` | Sí        | Formato: `(###) ###-####` o `###-###-####`.   |

---

## 5. Purchases (Compras)

### Endpoints

| Ruta                         | Método | Resumen          | Descripción                                |
|:-----------------------------|:-------|:-----------------|:-------------------------------------------|
| `/purchase/registerPurchase` | `POST` | Registrar Compra | Registra una nueva compra de productos.    |
| `/purchase/all`              | `GET`  | Obtener Compras  | Recupera el historial completo de compras. |

### Esquema del Cuerpo de Solicitud (`POST /purchase/registerPurchase`)

| Propiedad  | Tipo      | Requerido | Restricciones                                                                  |
|:-----------|:----------|:----------|:-------------------------------------------------------------------------------|
| `product`  | `object`  | Sí        | Detalles del producto comprado (incluye `name`, `stock`, `description`, etc.). |
| `supplier` | `object`  | Sí        | Detalles del proveedor (incluye `name` y `contact`).                           |
| `quantity` | `integer` | Sí        | Cantidad comprada (Rango: 1 a 99999).                                          |
| `amount`   | `number`  | Sí        | Monto total de la compra (Mín: 1.00, Máx: 99999.00).                           |

---

## 6. Sales (Ventas)

### Endpoints

| Ruta              | Método | Resumen         | Descripción                                          |
|:------------------|:-------|:----------------|:-----------------------------------------------------|
| `/sales/register` | `POST` | Registrar Venta | Registra una nueva venta con una lista de productos. |

### Esquema del Cuerpo de Solicitud (`POST /sales/register`)

| Propiedad                 | Tipo      | Requerido | Restricciones                                                              |
|:--------------------------|:----------|:----------|:---------------------------------------------------------------------------|
| `productsList`            | `array`   | Sí        | Lista de objetos de producto-cantidad.                                     |
| `productsList[].name`     | `string`  | Sí        | Nombre del producto.                                                       |
| `productsList[].quantity` | `integer` | Sí        | Cantidad vendida (Mínimo: 1).                                              |
| `discount`                | `number`  | Sí        | Descuento global aplicado (Rango: 0.01% a 100%, con máximo dos decimales). |

---

## 7. Auth (Autenticación)

### Endpoints

| Ruta             | Método | Resumen             | Descripción                                         |
|:-----------------|:-------|:--------------------|:----------------------------------------------------|
| `/auth/register` | `POST` | Registro de Usuario | Registra un nuevo usuario en el sistema.            |
| `/auth/login`    | `POST` | Inicio de Sesión    | Autentica un usuario y devuelve un token de sesión. |

### Esquema del Cuerpo de Solicitud (Registro de Usuario)

| Propiedad   | Tipo     | Requerido | Restricciones                               |
|:------------|:---------|:----------|:--------------------------------------------|
| `firstName` | `string` | Sí        | Mínimo 2 caracteres.                        |
| `lastName`  | `string` | Sí        | Mínimo 2 caracteres.                        |
| `email`     | `string` | Sí        | Formato de correo electrónico.              |
| `userName`  | `string` | Sí        | Mínimo 4 caracteres.                        |
| `password`  | `string` | Sí        | Mínimo 8 caracteres, formato de contraseña. |

### Esquema del Cuerpo de Solicitud (Inicio de Sesión)

| Propiedad  | Tipo     | Requerido |
|:-----------|:---------|:----------|
| `userName` | `string` | Sí        |
| `password` | `string` | Sí        |

---

## Seguridad

La **Autenticación** se realiza mediante un *Bearer Token* que se debe incluir en el encabezado de las solicitudes que
lo requieran (ej. `/products/delete/{name}`).

* **Encabezado**: `Authorization`
* **Formato**: `Bearer token_jwt`

## Ejemplos de Solicitudes y Respuestas por Endpoint

-----

## 1\. ProductType (Tipos de Producto)

### 1.1. Agregar Tipo de Producto

| Endpoint           | Método |
|:-------------------|:-------|
| `/ProductType/add` | `POST` |

**Request Body**

```json
{
  "name": "smartphone"
}
```

**Response (200 OK)**

```json
{
  "id": 52,
  "name": "smartphone"
}
```

-----

## 2\. Products (Productos)

### 2.1. Agregar Producto

| Endpoint        | Método |
|:----------------|:-------|
| `/products/add` | `POST` |

**Request Body**

```json
{
  "name": "Google Pixel 9 Pro",
  "description": "Smartphone Android Desbloqueado con Gemini",
  "categoryId": 31,
  "buyPrice": 23349,
  "salePrice": 24905,
  "stock": 200,
  "supplierId": 30,
  "productTypeId": 51
}
```

**Response (200 OK)**

```json
{
  "id": 134,
  "name": "Google Pixel 9 Pro",
  "description": "Smartphone Android Desbloqueado con Gemini",
  "categoryId": 31,
  "buyPrice": 23349,
  "salePrice": 24905,
  "stock": 200,
  "createdDate": "2025-09-30T17:25:35.4666887",
  "updatedDate": "2025-09-30T17:25:35.4666887",
  "supplierId": 30,
  "productTypeId": 51
}
```

### 2.2. Obtener todos los productos existentes

| Endpoint        | Método |
|:----------------|:-------|
| `/products/all` | `GET`  |

**Response (200 OK)**

```json
[
  {
    "id": 1,
    "name": "name",
    "description": "description",
    "category": {
      "id": 1,
      "name": "category_name"
    },
    "buyPrice": 100.00,
    "salePrice": 100.00,
    "stock": 1,
    "createdDate": "2025-09-23T22:52:24.619062",
    "updatedDate": "2025-09-29T22:15:39.926697",
    "supplier": {
      "id": 1,
      "name": "Supplier_name",
      "contact": {
        "id": 1,
        "phone": "###-###-###",
        "email": "contact_email",
        "address": "contact_adress"
      }
    },
    "productType": {
      "id": 1,
      "name": "productType_Number"
    }
  },
  .
  .
  .
  N
]
```

-----

## 3\. Category (Categorías)

### 3.1. Agregar Categoría

| Endpoint          | Método |
|:------------------|:-------|
| `/categories/add` | `POST` |

**Request Body**

```json
{
  "name": "gaming"
}
```

**Response (200 OK)**

```json
{
  "id": 32,
  "name": "gaming"
}
```

-----

## 4\. Suppliers (Proveedores)

### 4.1. Agregar Proveedor

| Endpoint         | Método |
|:-----------------|:-------|
| `/suppliers/add` | `POST` |

**Request Body**

```json
{
  "name": "microsoft",
  "contact": {
    "address": "362 S 9th Street",
    "email": "Jedidiah.Schinner@hotmail.com",
    "phone": "(632) 893-3106"
  }
}
```

**Response (200 OK)**

```json
{
  "id": 37,
  "name": "microsoft",
  "contact": {
    "id": 42,
    "phone": "(632) 893-3106",
    "email": "Jedidiah.Schinner@hotmail.com",
    "address": "362 S 9th Street"
  }
}
```

-----

## 5\. Purchases (Compras)

### 5.1. Registrar Compra

| Endpoint                     | Método |
|:-----------------------------|:-------|
| `/purchase/registerPurchase` | `POST` |

**Request Body**

```json
{
  "product": {
    "name": "product_name"
  },
  "supplier": {
    "name": "Supplier_name",
    "contact": {}
  },
  "quantity": 100,
  "amount": 52300.71
}
```

**Response (200 OK)**

```json
{
  "id": 24,
  "product": {
    "id": 122,
    "name": "product_name",
    "description": "asd",
    "categoryId": 31,
    "buyPrice": 2.00,
    "salePrice": 2.00,
    "stock": 272,
    "createdDate": "2025-09-17T22:19:23.615555",
    "updatedDate": "2025-09-29T22:34:57.832603",
    "supplierId": 30,
    "productTypeId": 51
  },
  "supplier": {
    "id": 30,
    "name": "supplier_name",
    "contact": {
      "id": 35,
      "phone": "1234567890",
      "email": "correo@gmail.com",
      "address": "direccion"
    }
  },
  "quantity": 100,
  "amount": 52300.71,
  "date": "2025-09-30T19:38:11.7344974"
}
```

-----

## 6\. Sales (Ventas)

### 6.1. Registrar Venta

| Endpoint          | Método |
|:------------------|:-------|
| `/sales/register` | `POST` |

**Request Body**

    - discount puede omitirse si es que no hay alguno.
    - discount se aplica global sobre el total, no sobre cada uno de los productos.

```json
{
  "productsList": [
    {
      "name": "asd",
      "quantity": 5
    },
    {
      "name": "otro",
      "quantity": 5
    }
  ],
  "discount": 0.10
}
```

**Response (201 Created)**

```json
{
  "id": 45,
  "date": "2025-09-30T13:53:21.8054219",
  "amount": 13.5000,
  "saleDetailsResponseDto": [
    {
      "id": 79,
      "product": {
        "name": "product_name",
        "description": "product_description",
        "categoryId": 31,
        "buyPrice": 2.00,
        "salePrice": 2.00,
        "stock": 489,
        "createdDate": "2025-09-17T22:19:23.615555",
        "updatedDate": "2025-09-30T19:52:47.902619",
        "supplierId": 30,
        "productTypeId": 51,
        "quantity": null
      },
      "amount": 9.0000,
      "discount": 0.10,
      "quantity": 5
    },
    {
      "id": 80,
      "product": {
        "name": "other_product_name",
        "description": "other_product_description",
        "categoryId": 31,
        "buyPrice": 2.00,
        "salePrice": 1.00,
        "stock": 490,
        "createdDate": "2025-09-23T22:52:24.619062",
        "updatedDate": "2025-09-30T19:49:13.586814",
        "supplierId": 30,
        "productTypeId": 51,
        "quantity": null
      },
      "amount": 4.5000,
      "discount": 0.10,
      "quantity": 5
    }
  ]
}
```

-----

## 7\. Auth (Autenticación)

### 7.1. Registro de Usuario

| Endpoint         | Método |
|:-----------------|:-------|
| `/auth/register` | `POST` |

**Request Body**

```json
{
  "firstName": "first name",
  "lastName": "last name",
  "email": "email_example@example.com",
  "userName": "userName",
  "password": "passwordverysecure123_"
}
```

**Response (200 OK)**

```json
{
  "firstName": "first name",
  "lastName": "last name",
  "email": "email_example@example.com",
  "userName": "userName",
  "roles": []
}
```

### 7.2. Inicio de Sesión

| Endpoint      | Método |
|:--------------|:-------|
| `/auth/login` | `POST` |

**Request Body**

```json
{
  "userName": "userName",
  "password": "passwordverysecure123_"
}
```

**Response (200 OK)**

```json
{
  "token": "JWT_TOKEN"
}
```

---

## Seguridad

La **Autenticación** se realiza mediante un *Bearer Token* que se debe incluir en el encabezado de las solicitudes que
lo requieran (ej. `/products/delete/{name}`).

* **Encabezado**: `Authorization`
* **Formato**: `Bearer token_jwt`