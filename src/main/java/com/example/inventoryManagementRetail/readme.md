# Inventory Management Retail Module

This module provides functionality for managing inventory, sales, purchases, suppliers, product types, and categories for a retail business.

## Functionality

*   **Product Management:** Add, update, delete, and retrieve products.
*   **Category Management:** Add, update, delete, and retrieve product categories.
*   **Product Type Management:** Add, update, delete, and retrieve product types.
*   **Supplier Management:** Add, update, delete, and retrieve suppliers.
*   **Purchase Management:** Register purchases of products from suppliers.
*   **Sale Management:** Register sales of products to customers.

## Usage Examples

### Registering a Sale (Service)

```java
@Service
public class ExampleService {

    private final SaleService saleService;

    public ExampleService(SaleService saleService) {
        this.saleService = saleService;
    }

    public void registerNewSale() {
        SaleDetailsRequestDto saleDetailsRequestDto = SaleDetailsRequestDto.builder()
                .productsList(List.of(ProductPatchRequestDto.builder().name("Product Name").build()))
                .amount(new BigDecimal("100.00"))
                .discount(new BigDecimal("0.10"))
                .quantity(1L)
                .build();

        SaleResponseDto saleResponseDto = saleService.registerSale(saleDetailsRequestDto);
        System.out.println("Sale registered with ID: " + saleResponseDto.getId());
    }
}
```

### Registering a Sale (REST API)

To register a sale, send a POST request to `/sales/register` with the following JSON payload:

```json
{
  "productsList": [
    {
      "name": "Product Name"
    }
  ],
  "amount": 100.00,
  "discount": 0.10,
  "quantity": 1
}
```

Example using `curl`:

```bash
curl -X POST \
  http://localhost:8080/sales/register \
  -H 'Content-Type: application/json' \
  -d '{
    "productsList": [
      {
        "name": "Product Name"
      }
    ],
    "amount": 100.00,
    "discount": 0.10,
    "quantity": 1
  }'
```

### Adding a Product (REST API)

To add a product, send a POST request to `/products/add` with the following JSON payload:

```json
{
  "name": "New Product",
  "description": "Product Description",
  "categoryId": 1,
  "buyPrice": 50.00,
  "salePrice": 100.00,
  "stock": 100,
  "supplierId": 1,
  "productTypeId": 1
}
```

Example using `curl`:

```bash
curl -X POST \
  http://localhost:8080/products/add \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "New Product",
  "description": "Product Description",
  "categoryId": 1,
  "buyPrice": 50.00,
  "salePrice": 100.00,
  "stock": 100,
  "supplierId": 1,
  "productTypeId": 1
}'
```

## Configuration Details

### CORS Configuration

The module includes a `CorsConfig` class to configure Cross-Origin Resource Sharing (CORS).

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

This configuration allows requests from `http://localhost:5173`.

## Dependencies

To use this module, you'll need the following dependencies in your `pom.xml`:

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```

---

🌍 This README is available in multiple languages:  
🔗 [readme.maxpfeffer.de](https://readme.maxpfeffer.de/readme/e66a148c009b3848f409ff9b1c00c6fea3a634f85f960aa6da63683ca119a6f197f2141503ccd65c7d74fc179f4ee7ca20d070c04fffb19700e3100616867c6d)