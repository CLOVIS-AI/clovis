FROM alpine:latest

RUN apk add --no-cache mariadb-client mariadb-connector-c \
	openjdk11-jdk
