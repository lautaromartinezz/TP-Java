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

INSERT INTO puerta (id, numero, id_puesto) VALUES
(1, 'A-101', 1),
(2, 'A-102', 1),
(3, 'A-103', 1),
(4, 'B-201', 2),
(5, 'B-202', 2),
(6, 'C-301', 3);

INSERT INTO resultado (id, fecha, energia_generada, nombre_nino, id_monster, id_puerta) VALUES
(1, '2026-10-01 20:15:00', 1200, 'Juan Perez', 1, 1),
(2, '2026-10-01 21:00:00', 850, 'Ana Gomez', 2, 2),
(3, '2026-10-02 19:45:00', 1500, 'Luis Diaz', 1, 3),
(4, '2026-10-02 22:10:00', 640, 'Sofia Ruiz', 3, 4),
(5, '2026-10-03 20:30:00', 980, 'Mateo Lara', 4, 5),
(6, '2026-10-03 23:05:00', 1120, 'Valen Cruz', 1, 6),
(7, '2026-10-04 21:20:00', 430, 'Emma Soto', 2, 1),
(8, '2026-10-04 22:40:00', 760, 'Tomas Vega', 5, 2),
(9, '2026-10-05 20:05:00', 1310, 'Julia Rios', 1, 4),
(10, '2026-10-05 21:55:00', 590, 'Bruno Paz', 6, 5);
