\# Aplicación Web de Validación Pico y Placa



\## Descripción



Esta aplicación permite validar si un vehículo puede circular en una fecha y hora determinadas, de acuerdo con las reglas de restricción de circulación conocidas como "Pico y Placa".



La solución está compuesta por dos capas claramente separadas:



\- Frontend desarrollado en Angular.

\- Backend desarrollado en Java utilizando Spring Boot.



La comunicación entre ambas capas se realiza mediante servicios REST sobre HTTP. Toda la lógica de validación se encuentra implementada exclusivamente en la capa de backend.



---



\## Arquitectura de la Solución



La aplicación está estructurada en dos proyectos independientes:



\- picoplaca-backend: API REST en Spring Boot que contiene la lógica de negocio.

\- picoplaca-frontend: Aplicación web en Angular que consume el servicio REST.



Flujo de funcionamiento:



1\. El usuario ingresa la placa y la fecha con hora en el formulario web.

2\. El frontend envía la información al backend mediante una petición HTTP POST.

3\. El backend valida:

&nbsp;  - Que la fecha no sea anterior a la fecha y hora actual.

&nbsp;  - El día de la semana.

&nbsp;  - El horario restringido.

&nbsp;  - El último dígito de la placa.

4\. El backend responde con un resultado indicando si el vehículo puede o no circular.

5\. El frontend muestra el resultado al usuario.



---



\## Reglas de Validación Implementadas



Restricción por día (según último dígito de placa):



\- Lunes: 1 y 2

\- Martes: 3 y 4

\- Miércoles: 5 y 6

\- Jueves: 7 y 8

\- Viernes: 9 y 0

\- Sábado y Domingo: Sin restricción



Horarios restringidos:



\- 07:00 a 09:30

\- 16:00 a 19:30



Validaciones adicionales:



\- No se permite consultar fechas y horas anteriores al momento actual.

\- La lógica de validación se encuentra únicamente en el backend.



---



\## Requisitos Previos



Para ejecutar el proyecto localmente se requiere:



\- Java 17 o superior

\- Maven 3.9 o superior

\- Node.js 18 o superior

\- Angular CLI instalado globalmente



Verificación de versiones:



```

java -version

mvn -v

node -v

npm -v

ng version

```



---



\## Ejecución del Backend (Spring Boot)



1\. Ubicarse en la carpeta del backend:



```

cd picoplaca-backend

```



2\. Compilar y ejecutar:



```

mvn spring-boot:run

```



El servicio quedará disponible en:



```

http://localhost:8080

```



Endpoint principal:



```

POST http://localhost:8080/api/validate

```



Ejemplo de cuerpo de solicitud JSON:



```json

{

&nbsp; "plate": "ABC1231",

&nbsp; "dateTime": "2026-03-09T08:00"

}

```



---



\## Ejecución del Frontend (Angular)



1\. Ubicarse en la carpeta del frontend:



```

cd picoplaca-frontend

```



2\. Instalar dependencias:



```

npm install

```



3\. Ejecutar la aplicación:



```

ng serve

```



La aplicación quedará disponible en:



```

http://localhost:4200

```



El frontend se comunica con el backend en:



```

http://localhost:8080/api/validate

```



---



\## Consideraciones para Ambiente de Producción



La aplicación ha sido desarrollada considerando buenas prácticas de separación de responsabilidades:



\- Arquitectura en capas.

\- Lógica de negocio centralizada en el backend.

\- Comunicación REST desacoplada.

\- Validaciones críticas realizadas en el servidor.



Para un entorno productivo real se recomienda:



\- Implementar manejo global de excepciones con códigos HTTP adecuados.

\- Agregar validaciones adicionales de formato de placa.

\- Incorporar pruebas unitarias.

\- Configurar variables de entorno para URLs y puertos.

\- Implementar contenedorización con Docker.

\- Configurar CORS de manera restrictiva según dominio autorizado.



---



\## Estructura del Proyecto



```

picoplaca-backend

&nbsp; ├── controller

&nbsp; ├── service

&nbsp; └── model



picoplaca-frontend

&nbsp; ├── src

&nbsp; └── app

```



---



\## Autor



Jonathan Liquinchana



---



\## Notas Finales



La solución cumple con los requerimientos solicitados:



\- Aplicación web en Angular.

\- Backend en Java con Spring Boot.

\- Comunicación mediante servicios REST.

\- Validación de fecha no anterior a la actual.

\- Lógica de pico y placa implementada en backend.

\- Código estructurado y preparado para extensión futura.

