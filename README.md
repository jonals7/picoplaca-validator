# Aplicación Web de Validación Pico y Placa

## Descripción

Esta aplicación permite validar si un vehículo puede circular en una fecha y hora determinadas, de acuerdo con las reglas de restricción de circulación conocidas como **Pico y Placa**.

La solución está compuesta por dos capas claramente separadas:

- **Frontend** desarrollado en Angular.
- **Backend** desarrollado en Java utilizando Spring Boot.

La comunicación entre ambas capas se realiza mediante servicios REST sobre HTTP. Toda la lógica de validación se encuentra implementada exclusivamente en la capa de backend.

---

## Arquitectura de la Solución

La aplicación está estructurada en dos proyectos independientes:

- `picoplaca-backend`: API REST en Spring Boot que contiene la lógica de negocio.
- `picoplaca-frontend`: Aplicación web en Angular que consume el servicio REST.

**Flujo de funcionamiento:**

1. El usuario ingresa la placa y la fecha con hora en el formulario web.
2. El frontend envía la información al backend mediante una petición HTTP POST.
3. El backend valida:
   - Que la fecha no sea anterior a la fecha y hora actual.
   - El formato de la placa mediante expresión regular.
   - El día de la semana y el horario restringido.
   - El último dígito de la placa.
4. El backend responde con un resultado indicando si el vehículo puede o no circular, junto con la placa, la fecha evaluada y un mensaje explicativo.
5. El frontend muestra el resultado al usuario con retroalimentación visual (verde / rojo).

---

## Reglas de Validación Implementadas

**Restricción por día (según último dígito de placa):**

| Último dígito | Día restringido |
|---|---|
| 1 y 2 | Lunes |
| 3 y 4 | Martes |
| 5 y 6 | Miércoles |
| 7 y 8 | Jueves |
| 9 y 0 | Viernes |
| — | Sábado y Domingo: Sin restricción |

**Horarios restringidos:**

- 07:00 a 09:30
- 16:00 a 19:30

**Validaciones adicionales:**

- No se permite consultar fechas y horas anteriores al momento actual.
- El formato de placa debe ser: 3 letras seguidas de 3 o 4 dígitos (Ej: `ABC-123`, `ABC1234`).
- Toda la lógica de validación se encuentra únicamente en el backend.

---

## Requisitos Previos

Para ejecutar el proyecto localmente se requiere:

- Java 21
- Maven 3.9 o superior
- Node.js 18 o superior
- Angular CLI instalado globalmente

**Verificación de versiones:**

```bash
java -version
mvn -v
node -v
npm -v
ng version
```

---

## Estructura del Proyecto

```
picoplaca-backend/
├── src/
│   ├── main/java/com/pico/picoplaca/
│   │   ├── controller/
│   │   │   └── PicoPlacaController.java
│   │   ├── service/
│   │   │   └── PicoPlacaService.java
│   │   ├── model/
│   │   │   ├── ValidationRequest.java
│   │   │   └── ValidationResponse.java
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── InvalidPlateException.java
│   │   │   └── InvalidDateException.java
│   │   └── PicoplacaApplication.java
│   └── test/
│       └── PicoPlacaServiceTest.java
└── pom.xml

picoplaca-frontend/
└── src/app/
    ├── components/
    │   └── pico-placa/
    │       ├── pico-placa.component.ts
    │       ├── pico-placa.component.html
    │       └── pico-placa.component.css
    ├── services/
    │   ├── pico-placa.service.ts
    │   └── pico-placa.service.spec.ts
    ├── models/
    │   ├── validation-request.model.ts
    │   └── validation-response.model.ts
    ├── app.component.ts
    ├── app.component.html
    └── app.config.ts
```

---

## Ejecución del Backend (Spring Boot)

1. Ubicarse en la carpeta del backend:

```bash
cd picoplaca-backend
```

2. Compilar y ejecutar:

```bash
mvn spring-boot:run
```

El servicio quedará disponible en `http://localhost:8080`

**Endpoint principal:**

```
POST http://localhost:8080/api/validate
Content-Type: application/json
```

**Ejemplo de solicitud:**

```json
{
  "plate": "ABC-123",
  "dateTime": "2026-03-09T08:00"
}
```

**Ejemplo de respuesta exitosa:**

```json
{
  "plate": "ABC-123",
  "dateTime": "2026-03-09T08:00",
  "canCirculate": false,
  "message": "El vehículo NO puede circular en la fecha y hora indicadas (restricción Pico y Placa)."
}
```

**Ejemplo de respuesta de error (placa inválida):**

```json
{
  "timestamp": "2026-03-09T08:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Formato de placa inválido. Debe tener 3 letras seguidas de 3 o 4 dígitos (ej: ABC-123)"
}
```

---

## Ejecución del Frontend (Angular)

1. Ubicarse en la carpeta del frontend:

```bash
cd picoplaca-frontend
```

2. Instalar dependencias:

```bash
npm install
```

3. Ejecutar la aplicación:

```bash
ng serve
```

La aplicación quedará disponible en `http://localhost:4200`

---

## Pruebas

**Backend — pruebas unitarias:**

```bash
cd picoplaca-backend
mvn test
```

Cubre los siguientes escenarios:
- Fecha nula o pasada → lanza `InvalidDateException`
- Placa nula o con formato inválido → lanza `InvalidPlateException`
- Vehículo restringido por día y horario → `canCirculate: false`
- Vehículo fuera de horario de restricción → `canCirculate: true`
- Fin de semana sin restricción → `canCirculate: true`

**Frontend — pruebas del servicio:**

```bash
cd picoplaca-frontend
ng test
```

Cubre los siguientes escenarios:
- El servicio se crea correctamente
- POST al endpoint correcto con los datos del request
- Respuesta con `canCirculate: false` para vehículo restringido
- Respuesta con `canCirculate: true` para vehículo libre

---

## Consideraciones para Ambiente de Producción

- Configurar CORS de manera restrictiva según el dominio autorizado (actualmente `*`).
- Externalizar la URL del backend en el frontend mediante variables de entorno (`environment.ts`).
- Implementar contenedorización con Docker para facilitar el despliegue.
- Configurar HTTPS en ambas capas.
- Agregar rate limiting en el backend para evitar abuso del endpoint.

---

## Autor

Jonathan Liquinchana
