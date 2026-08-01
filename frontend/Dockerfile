# ---- Build Stage ----
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# ---- Run Stage (Node-only, no Nginx) ----
FROM node:18-alpine
WORKDIR /app
RUN npm install -g serve
COPY --from=build /app/dist/lifeos-frontend /app/dist
EXPOSE 4000
CMD ["serve", "-s", "dist", "-l", "4000"]
