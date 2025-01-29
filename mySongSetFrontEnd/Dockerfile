# Usamos la imagen de node
FROM node:18

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos los archivos del frontend
COPY package*.json ./
RUN npm install
COPY . .

# Instalamos Angular CLI globalmente
RUN npm install -g @angular/cli

# Construimos la aplicación Angular
RUN npm run build --prod

# Exponemos el puerto 4200
EXPOSE 4200

# Iniciamos el servidor de Angular (asegúrate de que esté en el contenedor)
CMD ["ng", "serve", "--host", "0.0.0.0", "--port", "4200"]

