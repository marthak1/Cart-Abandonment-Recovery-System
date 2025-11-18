# Dockerfile for React Frontend (Vite)
# Stage 1: Build (Debian-based)
FROM node:20-slim AS build

WORKDIR /app

# Copy package files
COPY package.json package-lock.json ./

# Clean install
RUN npm ci

# Copy source
COPY . .

# Build args
ARG VITE_MODE=production
ARG VITE_APP_API_URL=http://localhost:8080

ENV VITE_APP_API_URL=$VITE_APP_API_URL

# Build
RUN npm run build

# Stage 2: Production (Alpine for smaller size)
FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

RUN echo "healthy" > /usr/share/nginx/html/health

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:80/health || exit 1

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

