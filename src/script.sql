# source /home/david/NetBeansProjects/AgroGestiónAPP/src/documentacion/script.sql;
DROP DATABASE BAgroGestion;
CREATE DATABASE BAgroGestion;
USE BAgroGestion;

CREATE TABLE TFinca
(
    Nombre          VARCHAR(20)     PRIMARY KEY,
    Localizacion    VARCHAR(20)     NOT NULL,
    Descripcion     VARCHAR(100)    NOT NULL
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TParcela
(
    IdParcela       VARCHAR(20)     PRIMARY KEY,
    IdFinca         VARCHAR(20)     NOT NULL,
    Descripcion     VARCHAR(100)    NOT NULL,
    CONSTRAINT Parcela_Finca	FOREIGN KEY (IdFinca) REFERENCES TFinca(Nombre)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TTipo
(    
    Nombre          VARCHAR(20)     PRIMARY KEY,
    Descripcion     VARCHAR(300)    NOT NULL
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TVariedad
(
    Nombre          VARCHAR(20)     PRIMARY KEY,
    IdTipo          VARCHAR(20)     NOT NULL, 
    CONSTRAINT Variedad_Tipo	FOREIGN KEY (IdTipo) REFERENCES TTipo(Nombre)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TCultivar
( 
    Id              INT             PRIMARY KEY AUTO_INCREMENT,
    FechaInicio     DATE            NOT NULL,
    FechaFin        DATE            NOT NULL,
    IdVariedad      VARCHAR(20)     NOT NULL,
    IdParcela       VARCHAR(20)     NOT NULL,
    Unidades        INT             NOT NULL,
    CONSTRAINT Cultivo_Parcela	FOREIGN KEY (IdParcela)     REFERENCES TParcela(IdParcela),
    CONSTRAINT Cultivo_Variedad	FOREIGN KEY (IdVariedad)    REFERENCES TVariedad(Nombre)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TIngresoVenta
(   
    Id              INT             PRIMARY KEY AUTO_INCREMENT,
    Fecha           DATE            NOT NULL,
    NombreCliente   VARCHAR(20)     NOT NULL,
    PrecioUnidad    FLOAT           ,
    Cantidad        FLOAT           ,
    Total           FLOAT           NOT NULL,
    IdCultivar      INT             NOT NULL,
    Cobrado          ENUM('Cobrado','No cobrado') NOT NULL DEFAULT 'No cobrado',
    CONSTRAINT IngresoVenta_Cultivar	FOREIGN KEY (IdCultivar) REFERENCES TCultivar(Id)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TIngresoOtro
(
    Id              INT             PRIMARY KEY AUTO_INCREMENT,
    Fecha           DATE            NOT NULL,
    Procedencia     VARCHAR(20)     NOT NULL,
    Descripcion     VARCHAR(100)    NOT NULL,
    Total           FLOAT           NOT NULL,
    IdCultivar      INT             NOT NULL,
    Cobrado          ENUM('Cobrado','No cobrado') NOT NULL DEFAULT 'No cobrado',
    CONSTRAINT IngresoOtro_Cultivar	FOREIGN KEY (IdCultivar) REFERENCES TCultivar(Id)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TTrabajador
(
    DNI             VARCHAR(10)     PRIMARY KEY,       
    Nombre          VARCHAR(10)     NOT NULL,
    Tipo            ENUM('root','normal') NOT NULL DEFAULT 'normal',
    Contrasenya     VARCHAR(10)     NOT NULL
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;
INSERT INTO TTrabajador (DNI,Nombre,Tipo,Contrasenya) VALUES ("root","root","root","root");

CREATE TABLE TGastosManoObra
(
    Id              INT             PRIMARY KEY AUTO_INCREMENT,       
    FechaInicio     DATE            NOT NULL,
    FechaFin        DATE            NOT NULL,
    Categoria       ENUM('Fumigar','Labrar','Podar','Recolectar','Regar','Sembrar','Otros') NOT NULL DEFAULT 'Otros',
    DNI             VARCHAR(10)     NOT NULL,
    Tipo            ENUM('Horas','Dias') NOT NULL DEFAULT 'Dias',
    CantidadTipo    FLOAT           ,
    PrecioTipo      FLOAT           ,
    Total           FLOAT           NOT NULL,
    Descripcion     VARCHAR(300)    NOT NULL,
    IdCultivar      INT             NOT NULL,
    Pagado          ENUM('Pagado','No pagado') NOT NULL DEFAULT 'No pagado',
    CONSTRAINT GastosManoObra_Cultivar      FOREIGN KEY (IdCultivar) REFERENCES TCultivar(Id),
    CONSTRAINT GastosManoObra_Trabajador    FOREIGN KEY (DNI) REFERENCES TTrabajador(DNI)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TGastoProducto
(
    Id              INT             PRIMARY KEY AUTO_INCREMENT,       
    FechaFatura     DATE            NOT NULL,
    Proveedor       VARCHAR(20)     NOT NULL,
    Descripcion     VARCHAR(100)    NOT NULL,
    Cantidad        FLOAT           NOT NULL,
    Total           FLOAT           NOT NULL,
    IdCultivar      INT             NOT NULL,
    Pagado          ENUM('Pagado','No pagado') NOT NULL DEFAULT 'No pagado',
    CONSTRAINT GastoProducto_Cultivar	FOREIGN KEY (IdCultivar) REFERENCES TCultivar(Id)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;

CREATE TABLE TGastoOtro
(
    Id              INT             PRIMARY KEY AUTO_INCREMENT,       
    FechaFatura     DATE            NOT NULL,
    Descripcion     VARCHAR(100)    NOT NULL,
    Total           FLOAT           NOT NULL,
    IdCultivar      INT             NOT NULL,
    Pagado          ENUM('Pagado','No pagado') NOT NULL DEFAULT 'No pagado',
    CONSTRAINT GastoOtro_Cultivar	FOREIGN KEY (IdCultivar) REFERENCES TCultivar(Id)
)CHARACTER SET utf8 COLLATE utf8_spanish_ci;



