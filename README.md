# FalconFitUser Application - Informe de Desarrollo

## Introducción
La aplicación **FalconFitUser** ha sido desarrollada para gestionar ejercicios y superseries personalizables por los usuarios. 
Este README contiene el desarrollo realizado, los desafíos encontrados, las soluciones aplicadas y las decisiones de diseño.

---

## Desarrollo Realizado

### Funcionalidades Implementadas
1. **Gestión de Ejercicios y Superseries:**
   - CRUD completo para ejercicios y superseries.
   - Vinculación de superseries con ejercicios relacionados.
   - Visualización en tiempo real de los cambios realizados en Strapi, sincronizados con la app.

2. **Personalización de Configuraciones:**
   - Tema oscuro/claro.
   - Cambio de idioma entre español e inglés.
   - Persistencia de configuraciones utilizando DataStore.

3. **Navegación:**
   - Flujo de usuario claro con fragmentos.
   - Pantallas diseñadas con material design.

4. **Persistencia y Comunicación con la API:**
   - Integración de Retrofit para comunicación con la API de Strapi.
   - Uso de Hilt para inyección de dependencias.
     
5. **Avances del segundo trimestre:**
   - Implementar AuthenticationInterceptor para añadir a los post en la cabecera el bearer token
   - Rellenar campos automaticamente al pulsar actualizar
   - Implementación de ubicaciones con la Api de Google Maps
   - Uso de camara para tomar fotos
   - Work Manager para enviar notificaciones cada hora.
   - Base de datos local en Room.

---

## Desafíos Encontrados

### Vinculación de Ejercicios a Superseries
Uno de los mayores complicaciones fue mostrar los ejercicios en el formulario de creación de superseries. 
Además, al realizar actualizaciones, no se reflejaban automaticamente los cambios, generando confusión de si se habia actualizado o no.

**Solución:** Se implementaron flujos observables (`StateFlow`) en el ViewModel, garantizando que los datos se actualizaran de forma reactiva cada vez que el usuario realizara un cambio.

### Persistencia de Configuraciones
Al implementar DataStore para la persistencia de las preferencias de usuario, surgieron problemas relacionados con la inicialización de múltiples DataStores.

**Solución:** Se modificó la arquitectura para que los DataStores fueran objetos singleton.

### Cambio de Idioma
El cambio de idioma representó un reto para mantener la consistencia de los textos en toda la aplicación, especialmente al intentar persistir la configuración entre sesiones.

**Solución:** Se utilizó `AppCompatDelegate.setApplicationLocales` junto con un `DataStore` adicional para almacenar las preferencias de idioma, asegurando que se aplicaran de forma global al reiniciar la aplicación.

---

## Decisiones de Diseño

### Uso de Material Design
Todas las pantallas siguen los principios de **Material Design** para garantizar una interfaz moderna, consistente y accesible. 
Se priorizaron elementos como botones, listas y switches estilizados para mejorar la experiencia del usuario.

## Render
https://falconfitrenderv2.onrender.com/ 
