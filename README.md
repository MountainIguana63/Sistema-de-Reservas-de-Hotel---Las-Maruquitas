# Sistema-de-Reservas-de-Hotel---Las-Maruquitas

El siguiente programa es un proyecto para la materia Programación Orientada a Objetos. Desarrollado por los estudiantes de Ingenieria en Software y Negocios Digitales de la Escuela Superior de Economía y Negocios (ESEN):

* Sara Abigail Arévalo Esperanza 20255938
* Nathaly Elizabeth Avilés Monzón 20255750
* Natalia Jimena Díaz Beltrán 20255867
* Jessica Karina González Clímaco 20255765
* Víctor Alejandro Solano Ramírez 20255966

Este programa es una solución de consola diseñada para automatizar las reservas de un hotel. Básicamente, el sistema orquesta todo el backend de un hotel real: valida quién inicia sesión (rol de usuario y permisos acordes), permite crear o cancelar reservas, y gestiona la disponibilidad de las habitaciones aplicando herencia y polimorfismo. Además, garantiza que ninguna información se pierda entre sesiones al guardar todo automáticamente en formato JSON.

## IMPORTANTE, ANTES DE CORRER EL PROGRAMA

Este proyecto utiliza la librería Gson (gson-2.13.2.jar), desarrollada por Google, para gestionar la persistencia de datos. Su función es serializar los objetos en memoria (Usuarios, Reservas, Habitaciones) y almacenarlos en archivos .json, así como deserializarlos al iniciar el sistema.

El archivo binario ya se encuentra incluido en el repositorio dentro del directorio lib/, por lo que no requiere descargas adicionales. A continuación, se detalla el proceso para enlazar la librería al classpath según el entorno de trabajo:

Para que el proyecto compile correctamente y reconozca la librería, se debe enlazar al entorno de desarrollo. Los siguientes pasos aplican por igual tanto para sistemas Windows como para macOS:

1. Abrir el proyecto: Inicia IntelliJ IDEA y abra la carpeta raíz del proyecto (Sistema-de-Reservas-de-Hotel---Las-Maruquitas).
2. localice el archivo: En el panel lateral izquierdo (Project tool window), busque y despliege la carpeta llamada lib.
3. Seleccionar la librería: Dentro de la carpeta, ubique el archivo gson-2.13.2.jar.
4. Agregar como biblioteca: Haga clic derecho sobre el archivo .jar. En el menú contextual que se despliega, busque y seleccione la opción "Add as Library...".
5. Confirmar la acción: Aparecerá un cuadro de diálogo para confirmar a qué módulo se agregará la librería. Deje las opciones por defecto y presiona OK.

Notará que la configuración fue exitosa porque el icono del archivo gson-2.13.2.jar cambiará visualmente (ahora le permitirá desplegarlo para ver las clases internas) y todas las líneas de código que marcaban error en rojo relacionadas con la persistencia de datos desaparecerán.

## ¿Cómo funciona el programa?

El programa tiene tres tipos de usuarios: Super Admin, Admin y Recepcionista. Por defecto viene creado un usuario Super Admin con usuario "admin" y contraseña "poo2026".

### Super Admin
Este rol tiene acceso absoluto a todas las configuraciones del sistema, diseñado para el gerente general o dueño del hotel.

* Gestión de Usuarios: Permite crear nuevas cuentas para Administradores o Recepcionistas, y eliminar usuarios existentes (el sistema cuenta con un candado de seguridad que impide borrar al último Super Administrador).
* Configuración de Habitaciones: Acceso al CRUD completo para registrar nuevas habitaciones (Estándar o Suites), darlas de baja del sistema o modificar los precios por noche.
* Auditoría Global: Capacidad para visualizar todo el historial de huéspedes y el estado general de todas las reservas del hotel.

### Admin
* Control de Inventario: Permite actualizar el estado de las habitaciones, modificar precios y visualizar el catálogo completo de habitaciones disponibles u ocupadas.
* Monitor de Reservas: Acceso de lectura y gestión sobre las reservas activas para solucionar problemas o hacer ajustes operativos.

### Recepcionista
* Catálogo de Disponibilidad: Muestra un listado en tiempo real de las habitaciones que están libres para ser asignadas.
* Registro de Huéspedes: Permite ingresar los datos de nuevos clientes. (Incluye validación lógica para requerir el DUI de un responsable si el huésped es menor de edad).
* Creación y Cancelación de Reservas: Permite vincular a un huésped con una habitación disponible, establecer fechas de inicio/fin y generar el cálculo del total a pagar por la estadía.