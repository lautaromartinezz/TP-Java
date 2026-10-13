CREATE DATABASE IF NOT EXISTS javaDB;
USE javaDB;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS resultado;
DROP TABLE IF EXISTS puerta_asignada;
DROP TABLE IF EXISTS puesto_asignado;
DROP TABLE IF EXISTS puerta;
DROP TABLE IF EXISTS puesto;
DROP TABLE IF EXISTS monster;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE monster (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE puesto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE puerta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE puesto_asignado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_monster INT NOT NULL,
    id_puesto INT NOT NULL,
    fecha DATE NOT NULL,
    CONSTRAINT fk_puesto_asignado_monster FOREIGN KEY (id_monster) REFERENCES monster(id),
    CONSTRAINT fk_puesto_asignado_puesto FOREIGN KEY (id_puesto) REFERENCES puesto(id),
    CONSTRAINT uq_puesto_asignado_monster_fecha UNIQUE (id_monster, fecha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE puerta_asignada (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_puesto_asignado INT NOT NULL,
    id_puerta INT NOT NULL,
    CONSTRAINT fk_puerta_asignada_puesto_asignado FOREIGN KEY (id_puesto_asignado) REFERENCES puesto_asignado(id),
    CONSTRAINT fk_puerta_asignada_puerta FOREIGN KEY (id_puerta) REFERENCES puerta(id),
    CONSTRAINT uq_puerta_asignada UNIQUE (id_puesto_asignado, id_puerta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE resultado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_puerta_asignada INT NOT NULL,
    fecha DATETIME NOT NULL,
    energia_generada INT NOT NULL,
    nombre_nino VARCHAR(255) NOT NULL,
    CONSTRAINT fk_resultado_puerta_asignada FOREIGN KEY (id_puerta_asignada) REFERENCES puerta_asignada(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
