# Before creating this image, run
#   ./gradlew backend:distTar
#
# At runtime, the file /etc/application.conf should be present, and contain the Cassandra configuration.
# For example, it could be added via Docker Volumes.
# Configuration documentation: https://docs.datastax.com/en/developer/java-driver/4.4/manual/core/configuration/

FROM archlinux:latest AS builder

RUN pacman -Syuu --noconfirm tar

WORKDIR /opt/clovis
COPY build/distributions/*.tar .
RUN tar -xvf backend*.tar
RUN rm backend*.tar

FROM openjdk:17-alpine

COPY --from=builder /opt/clovis/backend* .
ENTRYPOINT [ "backend" ]
