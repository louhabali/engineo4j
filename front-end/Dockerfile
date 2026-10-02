FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

# #####################################

FROM nginx:1.27-alpine
# COPY nginx.conf /etc/nginx/conf.d/default.conf
# COPY --from=build /app/dist/front-end/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist/front-end/browser /usr/share/nginx/html

COPY certs/neo4flix.pem /etc/nginx/certs/neo4flix.pem
COPY certs/neo4flix-key.pem /etc/nginx/certs/neo4flix-key.pem

EXPOSE 80
EXPOSE 443
# EXPOSE 80
