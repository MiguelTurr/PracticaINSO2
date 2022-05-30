-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.7.21-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE IF NOT EXISTS `veterinaria`; /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `veterinaria`;

-- 

CREATE TABLE IF NOT EXISTS `roles` (
  `IdRol` int(11) NOT NULL AUTO_INCREMENT,
  `TipoUsuario` varchar(20) NOT NULL,
  PRIMARY KEY (`IdRol`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DELETE FROM `roles`;
INSERT INTO `roles` (`IdRol`, `TipoUsuario`) VALUES
	(1, 'Administrador'),
	(2, 'Empleado'),
	(3, 'Cliente');

--

CREATE TABLE IF NOT EXISTS `usuarios` (
  `IdUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(100) NOT NULL,
  `Apellido1` varchar(100) NOT NULL,
  `Apellido2` varchar(100) DEFAULT NULL,
  `DNI` varchar(9) NOT NULL,
  `Telefono` varchar(20) NOT NULL,
  `FechaNacimiento` DATETIME NOT NULL,
  `Usuario` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `CreadoEn` DATETIME NOT NULL,
  `UltimaConexion` datetime DEFAULT CURRENT_TIMESTAMP,
  `IdRol` int(11) NOT NULL,
  PRIMARY KEY (`IdUsuario`),
  KEY `FK_Usuario_Rol` (`IdRol`),
  CONSTRAINT `FK_Usuario_Rol` FOREIGN KEY (`IdRol`) REFERENCES `roles` (`IdRol`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DELETE FROM `usuarios`;
INSERT INTO `usuarios` (`IdUsuario`, `Nombre`, `Apellido1`, `Apellido2`, `DNI`, `Telefono`, `FechaNacimiento`, `Usuario`, `Password`, `CreadoEn`, `IdRol`) VALUES
	(1, 'Juan', 'Lopez', 'Sánchez', '26807066Z', '736289332', '2000-02-10 00:00:00', 'JuLoSa', 'JuLoSa', NOW(), 1),
	(2, 'Angel', 'Sainz', 'Esteban', '84878797B', '651803219', '1990-02-10 00:00:00', 'AnSaEs', 'AnSaEs', NOW(), 2),
	(3, 'Benedicto', 'Rios', 'Santos', '39838241X', '659084226', '1980-02-10 00:00:00', 'BeRiSa', 'BeRiSa', NOW(), 3);

-- 

CREATE TABLE IF NOT EXISTS `animales` (
  `IdAnimal` int(11) NOT NULL AUTO_INCREMENT,
  `TipoAnimal` varchar(20) NOT NULL,
  PRIMARY KEY (`IdAnimal`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

DELETE FROM `animales`;
INSERT INTO `animales` (`IdAnimal`, `TipoAnimal`) VALUES
	(1, 'Perro'),
	(2, 'Gato'),
	(3, 'Conejo'),
    (4, 'Hámster'),
    (5, 'Loro'),
    (6, 'Periquito'),
    (7, 'Pez');

--

CREATE TABLE IF NOT EXISTS `mascotas` (
  `IdMascota` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) NOT NULL,
  `IdCliente` int(11) NOT NULL,
  `IdAnimal` int(11) NOT NULL,
  PRIMARY KEY (`IdMascota`),
  KEY `FK_Mascota_Cliente` (`IdCliente`),
  KEY `FK_Mascota_Animal` (`IdAnimal`),
  CONSTRAINT `FK_Mascota_Cliente` FOREIGN KEY (`IdCliente`) REFERENCES `usuarios` (`IdUsuario`),
  CONSTRAINT `FK_Mascota_Animal` FOREIGN KEY (`IdAnimal`) REFERENCES `animales` (`IdAnimal`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DELETE FROM `mascotas`;
INSERT INTO `mascotas` (`IdMascota`, `Nombre`, `IdCliente`, `IdAnimal`) VALUES
	(1, 'Yasuo', 3, 2),
	(2, 'Bola de nieve', 3, 2);

--

CREATE TABLE IF NOT EXISTS `consultas` (
  `IdConsulta` int(11) NOT NULL AUTO_INCREMENT,
  `FechaConsulta` DATETIME DEFAULT NOW(),
  `Descripcion` varchar(200) NOT NULL,
  `IdEmpleado` int(11) NOT NULL,
  `IdMascota` int(11) NOT NULL,
  PRIMARY KEY (`IdConsulta`),
  KEY `FK_Consulta_Empleado` (`IdEmpleado`),
  KEY `FK_Consulta_Mascota` (`IdMascota`),
  CONSTRAINT `FK_Consulta_Empleado` FOREIGN KEY (`IdEmpleado`) REFERENCES `usuarios` (`IdUsuario`),
  CONSTRAINT `FK_Consulta_Mascota` FOREIGN KEY (`IdMascota`) REFERENCES `mascotas` (`IdMascota`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--

CREATE TABLE IF NOT EXISTS `citas` (
  `IdCita` int(11) NOT NULL AUTO_INCREMENT,
  `FechaCita` DATETIME NOT NULL,
  `Descripcion` varchar(200) NOT NULL,
  `IdMascota` int(11) NOT NULL,
  PRIMARY KEY (`IdCita`),
  KEY `FK_Cita_Mascota` (`IdMascota`),
  CONSTRAINT `FK_Cita_Mascota` FOREIGN KEY (`IdMascota`) REFERENCES `mascotas` (`IdMascota`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DELETE FROM `citas`;
INSERT INTO `citas` (`IdCita`, `FechaCita`, `Descripcion`, `IdMascota`) VALUES
	(1, '2022-06-10 10:30:00', 'Revisión y análisis de sangre', 1);

--

CREATE TABLE IF NOT EXISTS `facturas` (
  `IdFactura` int(11) NOT NULL AUTO_INCREMENT,
  `IdCliente` int(11) NOT NULL,
  `FechaFactura` DATETIME DEFAULT NOW(),
  PRIMARY KEY (`IdFactura`),
  KEY `FK_Factura_Cliente` (`IdCliente`),
  CONSTRAINT `FK_Factura_Cliente` FOREIGN KEY (`IdCliente`) REFERENCES `usuarios` (`IdUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DELETE FROM `facturas`;
INSERT INTO `facturas` (`IdFactura`, `IdCliente`, `FechaFactura`) VALUES
    	(1, 3, NOW()),
	(2, 3, '2022-05-10 10:30:00');

--

CREATE TABLE IF NOT EXISTS `productos` (
  `IdProducto` int(11) NOT NULL AUTO_INCREMENT,
  `Producto` varchar(100) NOT NULL,
  `Precio` float NOT NULL,
  `Cantidad` int(11) NOT NULL,
  `Descuento` float default 0.0,
  PRIMARY KEY (`IdProducto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

DELETE FROM `productos`;
INSERT INTO `productos` (`IdProducto`, `Producto`, `Precio`, `Cantidad`, `Descuento`) VALUES
	
    	(1, 'Consulta', 30.0, -1, 0.0),
	(2, 'Collar de perro', 10.5, 20, 0.0),
    	(3, 'Juguete de perro', 2.9, 25, 0.0),
    	(4, 'Comida de pez', 1.5, 50, 0.0),
    	(5, 'Comida de gato', 3.5, 50, 0.0),
    	(6, 'Radiografia', 150.0, -1, 0.0);

--

CREATE TABLE IF NOT EXISTS `infofacturas` (
  `IdInfo` int(11) NOT NULL AUTO_INCREMENT,
  `IdFactura` int(11) NOT NULL,
  `IdProducto` int(11) NOT NULL,
  `Cantidad` int(11) NOT NULL,
  `Precio` float NOT NULL,
  PRIMARY KEY (`IdInfo`),
  KEY `FK_Info_Factura` (`IdFactura`),
  KEY `FK_Info_Producto` (`IdProducto`),
  CONSTRAINT `FK_Info_Factura` FOREIGN KEY (`IdFactura`) REFERENCES `facturas` (`IdFactura`),
  CONSTRAINT `FK_Info_Producto` FOREIGN KEY (`IdProducto`) REFERENCES `productos` (`IdProducto`)

) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DELETE FROM `infofacturas`;
INSERT INTO `infofacturas` (`IdInfo`, `IdFactura`, `IdProducto`, `Cantidad`, `Precio`) VALUES
    	(1, 1, 1, 1, 30.0),
    	(2, 1, 5, 5, 3.5),
    	(3, 1, 6, 1, 150.0),

	(4, 2, 1, 1, 30.0);


/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;