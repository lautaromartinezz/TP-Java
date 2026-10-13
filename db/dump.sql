USE javaDB;

INSERT INTO monster (id, nombre) VALUES
(1, 'Dragon'),
(2, 'Goblin'),
(3, 'Skeleton'),
(4, 'Orc'),
(5, 'Slime'),
(6, 'Vampire'),
(7, 'Werewolf'),
(8, 'Basilisk'),
(9, 'Phoenix'),
(10, 'Hydra');

INSERT INTO puesto (id, nombre, descripcion) VALUES
(1, 'Sustos de puertas', 'Puesto principal de recoleccion de energia'),
(2, 'Turno nocturno', 'Puesto para turnos de madrugada'),
(3, 'Rondin', 'Puesto de monitoreo y apoyo');

INSERT INTO puerta (id, numero) VALUES
(1, 'A-101'),
(2, 'A-102'),
(3, 'A-103'),
(4, 'B-201'),
(5, 'B-202'),
(6, 'C-301');

INSERT INTO puesto_asignado (id, id_monster, id_puesto, fecha) VALUES
(1, 1, 1, '2026-10-01'),
(2, 2, 2, '2026-10-01'),
(3, 1, 1, '2026-10-02'),
(4, 3, 1, '2026-10-02'),
(5, 4, 2, '2026-10-03'),
(6, 5, 3, '2026-10-03'),
(7, 6, 1, '2026-10-04');

INSERT INTO puerta_asignada (id, id_puesto_asignado, id_puerta) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 2, 4),
(5, 2, 5),
(6, 3, 1),
(7, 3, 3),
(8, 4, 2),
(9, 5, 4),
(10, 6, 6),
(11, 7, 1),
(12, 7, 2);

INSERT INTO resultado (id, id_puerta_asignada, fecha, energia_generada, nombre_nino) VALUES
(1, 1, '2026-10-01 20:15:00', 1200, 'Juan Perez'),
(2, 4, '2026-10-01 21:00:00', 850, 'Ana Gomez'),
(3, 6, '2026-10-02 19:45:00', 1500, 'Luis Diaz'),
(4, 7, '2026-10-02 22:10:00', 640, 'Sofia Ruiz'),
(5, 8, '2026-10-02 20:30:00', 980, 'Mateo Lara'),
(6, 9, '2026-10-03 23:05:00', 1120, 'Valen Cruz'),
(7, 10, '2026-10-03 21:20:00', 430, 'Emma Soto'),
(8, 11, '2026-10-04 22:40:00', 760, 'Tomas Vega'),
(9, 12, '2026-10-04 20:05:00', 1310, 'Julia Rios'),
(10, 2, '2026-10-01 22:55:00', 590, 'Bruno Paz');
