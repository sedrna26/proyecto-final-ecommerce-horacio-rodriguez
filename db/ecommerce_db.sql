-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-12-2025 a las 23:41:46
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ecommerce_db`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_estadisticas_ventas` (IN `p_fecha_inicio` DATE, IN `p_fecha_fin` DATE)   BEGIN
    SELECT 
        COUNT(DISTINCT p.id) AS total_pedidos,
        COUNT(DISTINCT p.usuario_id) AS total_clientes,
        SUM(p.total) AS ingresos_totales,
        AVG(p.total) AS ticket_promedio,
        SUM(lp.cantidad) AS productos_vendidos
    FROM pedidos p
    LEFT JOIN lineas_pedido lp ON p.id = lp.pedido_id
    WHERE p.fecha_pedido BETWEEN p_fecha_inicio AND p_fecha_fin
        AND p.estado != 'CANCELADO';
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_restaurar_stock_pedido` (IN `p_pedido_id` INT)   BEGIN
    UPDATE productos p
    INNER JOIN lineas_pedido lp ON p.id = lp.producto_id
    SET p.stock = p.stock + lp.cantidad
    WHERE lp.pedido_id = p_pedido_id;
    
    UPDATE pedidos 
    SET estado = 'CANCELADO' 
    WHERE id = p_pedido_id;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lineas_pedido`
--

CREATE TABLE `lineas_pedido` (
  `id` int(11) NOT NULL,
  `pedido_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` double NOT NULL
) ;

--
-- Volcado de datos para la tabla `lineas_pedido`
--

INSERT INTO `lineas_pedido` (`id`, `pedido_id`, `producto_id`, `cantidad`, `precio_unitario`) VALUES
(9, 4, 45, 2, 99.99),
(10, 5, 45, 1, 99.99),
(11, 6, 45, 5, 99.99),
(12, 7, 45, 5, 99.99),
(13, 8, 44, 1, 1299.99),
(14, 8, 45, 1, 99.99),
(15, 8, 46, 1, 89.99),
(16, 9, 46, 1, 89.99),
(17, 10, 45, 2, 99.99),
(18, 11, 45, 1, 99.99);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `fecha_pedido` datetime NOT NULL DEFAULT current_timestamp(),
  `estado` enum('PENDIENTE','EN_PROCESO','ENVIADO','ENTREGADO','CANCELADO') NOT NULL,
  `total` double NOT NULL DEFAULT 0,
  `observaciones` varchar(500) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`id`, `usuario_id`, `fecha_pedido`, `estado`, `total`, `observaciones`) VALUES
(4, 1, '2025-12-12 21:38:01', 'PENDIENTE', 199.98, NULL),
(5, 1, '2025-12-12 21:38:28', 'PENDIENTE', 99.99, NULL),
(6, 1, '2025-12-12 21:39:35', 'PENDIENTE', 499.95, NULL),
(7, 1, '2025-12-12 21:41:13', 'PENDIENTE', 499.95, NULL),
(8, 1, '2025-12-12 21:42:45', 'PENDIENTE', 1489.97, NULL),
(9, 1, '2025-12-12 22:38:21', 'PENDIENTE', 89.99, NULL),
(10, 1, '2025-12-12 22:38:50', 'PENDIENTE', 199.98, NULL),
(11, 2, '2025-12-12 22:39:54', 'PENDIENTE', 99.99, NULL);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `pedidos_detallados`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `pedidos_detallados` (
`pedido_id` int(11)
,`fecha_pedido` datetime
,`estado` enum('PENDIENTE','EN_PROCESO','ENVIADO','ENTREGADO','CANCELADO')
,`total` double
,`usuario_nombre` varchar(100)
,`usuario_apellido` varchar(100)
,`usuario_email` varchar(100)
,`cantidad_productos` bigint(21)
);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` int(11) NOT NULL,
  `tipo_producto` varchar(31) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `precio` double NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `url_imagen` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `volumen_litros` double DEFAULT NULL,
  `tipo_bebida` varchar(50) DEFAULT NULL,
  `tiene_gas` tinyint(1) DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `peso_kg` double DEFAULT NULL,
  `requiere_refrigeracion` tinyint(1) DEFAULT NULL,
  `tipo_comida` varchar(50) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `tipo_producto`, `nombre`, `descripcion`, `precio`, `categoria`, `stock`, `url_imagen`, `activo`, `volumen_litros`, `tipo_bebida`, `tiene_gas`, `fecha_vencimiento`, `peso_kg`, `requiere_refrigeracion`, `tipo_comida`) VALUES
(44, 'PRODUCTO', 'Laptop Dell XPS 15', 'Laptop de alto rendimiento con procesador Intel i7', 1299.99, 'Electrónica', 14, 'https://loremflickr.com/300/200/laptop,computer', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(45, 'PRODUCTO', 'Mouse Logitech MX Master 3', 'Mouse inalámbrico ergonómico', 99.99, 'Accesorios', 33, 'https://loremflickr.com/300/200/mouse,wireless', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(46, 'PRODUCTO', 'Teclado Mecánico Keychron K2', 'Teclado mecánico retroiluminado', 89.99, 'Accesorios', 28, 'https://loremflickr.com/300/200/keyboard,mechanical', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(47, 'PRODUCTO', 'Monitor Samsung 27\'\' 4K', 'Monitor 4K UHD con tecnología HDR', 349.99, 'Electrónica', 20, 'https://loremflickr.com/300/200/monitor,4k', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(48, 'BEBIDA', 'Coca Cola', 'Gaseosa sabor cola', 1.5, 'Bebidas', 100, 'https://loremflickr.com/300/200/coke,soda', 1, 2, 'Gaseosa', 1, NULL, NULL, NULL, NULL),
(49, 'BEBIDA', 'Agua Mineral', 'Agua mineral natural', 0.8, 'Bebidas', 150, 'https://loremflickr.com/300/200/water,bottle', 1, 1.5, 'Agua', 0, NULL, NULL, NULL, NULL),
(50, 'BEBIDA', 'Jugo de Naranja', 'Jugo natural de naranja', 2.5, 'Bebidas', 80, 'https://loremflickr.com/300/200/orange,juice', 1, 1, 'Jugo', 0, NULL, NULL, NULL, NULL),
(51, 'COMIDA', 'Pan Integral', 'Pan integral artesanal', 2.99, 'Alimentos', 40, 'https://loremflickr.com/300/200/bread,wholewheat', 1, NULL, NULL, NULL, '2025-12-17', 0.5, 0, 'Panadería'),
(52, 'COMIDA', 'Queso Gouda', 'Queso holandés maduro', 8.99, 'Alimentos', 25, 'https://loremflickr.com/300/200/cheese,gouda', 1, NULL, NULL, NULL, '2026-01-11', 0.3, 1, 'Lácteos'),
(53, 'COMIDA', 'Manzanas Rojas', 'Manzanas rojas frescas', 3.5, 'Alimentos', 60, 'https://loremflickr.com/300/200/apples,fruit', 1, NULL, NULL, NULL, '2025-12-22', 1, 1, 'Frutas');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `productos_mas_vendidos`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `productos_mas_vendidos` (
`id` int(11)
,`nombre` varchar(100)
,`categoria` varchar(50)
,`precio` double
,`total_vendido` decimal(32,0)
,`cantidad_pedidos` bigint(21)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `productos_stock_bajo`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `productos_stock_bajo` (
`id` int(11)
,`nombre` varchar(100)
,`categoria` varchar(50)
,`stock` int(11)
,`precio` double
);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `fecha_registro` datetime NOT NULL DEFAULT current_timestamp(),
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `apellido`, `email`, `telefono`, `direccion`, `fecha_registro`, `activo`, `password`, `rol`) VALUES
(1, 'Juan', 'Pérez', 'juan.perez@email.com', '555-1234', 'Calle Principal 123', '2025-12-11 16:05:49', 1, 'pass123', 'ADMIN'),
(2, 'María', 'González', 'maria.gonzalez@email.com', '555-5678', 'Avenida Central 456', '2025-12-11 16:05:49', 1, 'maria456', 'USUARIO'),
(3, 'Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '555-9012', 'Boulevard Norte 789', '2025-12-11 16:05:49', 1, 'carlos789', 'USUARIO'),
(4, 'Admin', 'Sistema', 'admin@ecommerce.com', '123-456-7890', 'Admin Office', '2025-12-12 19:30:41', 1, 'admin123', 'ADMIN');

-- --------------------------------------------------------

--
-- Estructura para la vista `pedidos_detallados`
--
DROP TABLE IF EXISTS `pedidos_detallados`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `pedidos_detallados`  AS SELECT `p`.`id` AS `pedido_id`, `p`.`fecha_pedido` AS `fecha_pedido`, `p`.`estado` AS `estado`, `p`.`total` AS `total`, `u`.`nombre` AS `usuario_nombre`, `u`.`apellido` AS `usuario_apellido`, `u`.`email` AS `usuario_email`, count(`lp`.`id`) AS `cantidad_productos` FROM ((`pedidos` `p` join `usuarios` `u` on(`p`.`usuario_id` = `u`.`id`)) left join `lineas_pedido` `lp` on(`p`.`id` = `lp`.`pedido_id`)) GROUP BY `p`.`id`, `p`.`fecha_pedido`, `p`.`estado`, `p`.`total`, `u`.`nombre`, `u`.`apellido`, `u`.`email` ;

-- --------------------------------------------------------

--
-- Estructura para la vista `productos_mas_vendidos`
--
DROP TABLE IF EXISTS `productos_mas_vendidos`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `productos_mas_vendidos`  AS SELECT `prod`.`id` AS `id`, `prod`.`nombre` AS `nombre`, `prod`.`categoria` AS `categoria`, `prod`.`precio` AS `precio`, sum(`lp`.`cantidad`) AS `total_vendido`, count(distinct `lp`.`pedido_id`) AS `cantidad_pedidos` FROM ((`productos` `prod` join `lineas_pedido` `lp` on(`prod`.`id` = `lp`.`producto_id`)) join `pedidos` `p` on(`lp`.`pedido_id` = `p`.`id`)) WHERE `p`.`estado` <> 'CANCELADO' GROUP BY `prod`.`id`, `prod`.`nombre`, `prod`.`categoria`, `prod`.`precio` ORDER BY sum(`lp`.`cantidad`) DESC ;

-- --------------------------------------------------------

--
-- Estructura para la vista `productos_stock_bajo`
--
DROP TABLE IF EXISTS `productos_stock_bajo`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `productos_stock_bajo`  AS SELECT `productos`.`id` AS `id`, `productos`.`nombre` AS `nombre`, `productos`.`categoria` AS `categoria`, `productos`.`stock` AS `stock`, `productos`.`precio` AS `precio` FROM `productos` WHERE `productos`.`stock` < 10 AND `productos`.`activo` = 1 ORDER BY `productos`.`stock` ASC ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `lineas_pedido`
--
ALTER TABLE `lineas_pedido`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_pedido` (`pedido_id`),
  ADD KEY `idx_producto` (`producto_id`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_usuario` (`usuario_id`),
  ADD KEY `idx_estado` (`estado`),
  ADD KEY `idx_fecha` (`fecha_pedido`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_categoria` (`categoria`),
  ADD KEY `idx_activo` (`activo`),
  ADD KEY `idx_tipo_producto` (`tipo_producto`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_activo` (`activo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `lineas_pedido`
--
ALTER TABLE `lineas_pedido`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `lineas_pedido`
--
ALTER TABLE `lineas_pedido`
  ADD CONSTRAINT `lineas_pedido_ibfk_1` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `lineas_pedido_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
