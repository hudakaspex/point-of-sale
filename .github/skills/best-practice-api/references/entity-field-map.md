# Entity Field Reference for DTO Mapping

## Product
```
Entity:   com.app.point_of_sale.Models.Product.Product
Repo:     ProductRepository (needs to be created)
Fields:
  id            UUID            → response.id
  name          String          → request/response.name (@NotBlank)
  description   String          → request/response.description (@Size max 1000)
  discontinued  Boolean         → request/response.discontinued
  createdAt     Instant         → response.createdAt (read-only)
  updatedtAt    Instant         → response.updatedAt (read-only, note typo in entity)
  category      Category (M:1)  → request.categoryId (UUID), response.categoryId + response.categoryName
```

## Category
```
Entity:   com.app.point_of_sale.Models.Category.Category
Repo:     CategoryRepository (needs to be created)
Fields:
  id            UUID            → response.id
  name          String          → request/response.name (@NotBlank)
  description   String          → request/response.description
  parent        Category (M:1)  → request.parentId (UUID, optional), response.parentId + response.parentName
  categories    List<Category>  → child categories (usually excluded from response)
```

## Customer
```
Entity:   com.app.point_of_sale.Models.Customer.Customer (extends Person)
Repo:     CustomerRepository (needs to be created)
Fields (inherited from Person):
  id            UUID            → response.id
  firstName     String          → request/response.firstName
  lastName      String          → request/response.lastName
  birthDate     LocalDate       → request/response.birthDate
  phone         String          → request/response.phone (unique)
  email         String          → request/response.email (@Email, unique)
  user          User (1:1)      → request.userId (UUID, optional), response.userId
```

## Employee
```
Entity:   com.app.point_of_sale.Models.Employee.Employee (extends Person)
Repo:     EmployeeRepository (needs to be created)
Fields:   Same as Customer (inherits from Person)
```

## Variant
```
Entity:   com.app.point_of_sale.Models.Variant.Variant
Repo:     VariantRepository (needs to be created)
Fields:
  id            UUID            → response.id
  name          String          → request/response.name (@NotBlank)
  sku           String          → request/response.sku
  discontinued  Boolean         → request/response.discontinued
  price         BigDecimal      → request/response.price (@PositiveOrZero)
  createdAt     Instant         → response.createdAt (read-only)
  updatedAt     Instant         → response.updatedAt (read-only)
  product       Product (M:1)   → request.productId (UUID), response.productId + response.productName
```

## User
```
Entity:   com.app.point_of_sale.Models.User.User
Repo:     UserRepository (exists)
Fields:
  id            UUID            → response.id
  fullname      String          → request/response.fullname
  email         String          → request/response.email (@Email, unique)
  password      String          → request only (never expose in response)
  createdAt     Instant         → response.createdAt (read-only)
  updatedAt     Instant         → response.updatedAt (read-only)
  roles         List<Role>      → response as list of role names/IDs
```

## Role
```
Entity:   com.app.point_of_sale.Models.User.Role.Role
Repo:     RoleRepository (needs to be created)
Fields:
  id            UUID            → response.id
  name          String          → request/response.name (@NotBlank)
  description   String          → request/response.description
  isActive      Boolean         → request/response.isActive
  createdAt     Instant         → response.createdAt (read-only)
  permissions   List<Permission> → request as List<UUID> permissionIds, response as nested DTOs or IDs
```

## Permission
```
Entity:   com.app.point_of_sale.Models.User.Permission.Permission
Repo:     PermissionRepository (needs to be created)
Fields:
  id            UUID            → response.id
  name          String          → request/response.name (@NotBlank)
  description   String          → request/response.description
  isActive      Boolean         → request/response.isActive (@NotNull)
  createdAt     Instant         → response.createdAt (read-only)
```
