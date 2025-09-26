# Dockerfile for React Frontend (Vite)
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
COPY .env ./
COPY .env.production ./
COPY .env.development ./
RUN npm install
COPY . .
# RUN npm run build -- --mode production
ARG VITE_MODE=production
RUN npm run build -- --mode $VITE_MODE
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

# Dockerfile (frontend)
# FROM node:20-alpine AS build
# WORKDIR /app
# COPY package*.json ./
# COPY .env ./
# RUN npm install
# COPY . .
# RUN npm run build

# FROM nginx:alpine
# COPY --from=build /app/build /usr/share/nginx/html