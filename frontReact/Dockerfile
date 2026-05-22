# ==========================================
# Etapa 1: Construcción (Build Stage)
# ==========================================
# Usamos una imagen ligera de Node.js
#FROM node:18-alpine AS build
FROM node:22-alpine AS build
# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo los archivos de dependencias primero (para aprovechar la caché de Docker)
COPY package*.json ./

# Instalamos las dependencias
RUN npm install

# Copiamos el resto del código del proyecto
COPY . .

# Construimos la aplicación para producción (Vite creará la carpeta /dist)
RUN npm run build

# ==========================================
# Etapa 2: Producción (Production Stage)
# ==========================================
# Usamos Nginx para servir los archivos estáticos
FROM nginx:alpine

# Copiamos los archivos construidos de la Etapa 1 a la carpeta pública de Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Exponemos el puerto 80 del contenedor
EXPOSE 80

# Arrancamos Nginx
CMD ["nginx", "-g", "daemon off;"]