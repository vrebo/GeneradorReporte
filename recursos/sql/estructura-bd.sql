--CREACION DE ENUMERADOS USADOS EN LAS TABLAS-----------------------------------------------------------------

CREATE TYPE copia_formato as ENUM(
	'BLURAY',
	'DVD'
);

CREATE TYPE copia_estado as ENUM(
	'VENDIDA',
	'EN-STOCK',
	'DAÑADA'
);

CREATE TYPE empleado_puesto as ENUM(
	'Gerente',
	'Vendedor'
);

CREATE TYPE empleado_estado as ENUM(
	'Laborando',
	'Incapacitado',
	'Liquidado'
);

CREATE TYPE pelicula_clasificacion AS ENUM (
	'A', 'B', 'B15', 'C', 'D'
);

--CREACION DE LA ESTRUCTURA DE LA BD-----------------------------------------------------------------

CREATE TABLE "cliente" (

"cliente_id" text NOT NULL,

"cliente_nombre" text NOT NULL,

"cliente_appater" text NOT NULL,

"cliente_apmater" text NOT NULL,

"cliente_fecharegistro" date NOT NULL,

"cliente_fechanacimiento" date NOT NULL,

"cliente_imagen" bytea NOT NULL,

"cliente_imagen_nombre" text NOT NULL,

PRIMARY KEY ("cliente_id") 

);



CREATE TABLE "copia_pelicula" (

"copia_id" serial8 NOT NULL,

"copia_fmto" copia_formato NOT NULL,

"copia_fechaadquisicion" date NOT NULL,

"copia_precio" DOUBLE PRECISION NOT NULL,

"copia_edo" copia_estado NOT NULL,

"pelicula_id" serial8 NOT NULL,

PRIMARY KEY ("copia_id") 

);



CREATE TABLE "detalle_venta" (

"detallevta_id" serial8 NOT NULL,

"venta_id" serial8 NOT NULL,

"copia_id" serial8 NOT NULL,

"detallevta_comentario" text NULL,

PRIMARY KEY ("detallevta_id") 

);



CREATE TABLE "empleado" (

"empleado_id" text NOT NULL,

"empleado_nombre" text NOT NULL,

"empleado_appater" text NOT NULL,

"empleado_apmater" text NOT NULL,

"empleado_horaentrada" time NOT NULL,

"empleado_horasalida" time NOT NULL,

"empleado_fechanacimiento" date NOT NULL,

"empleado_fecharegistro" date NOT NULL,

"empleado_edo" empleado_estado NOT NULL,

"empleado_puesto" empleado_puesto NOT NULL,

"empleado_sueldo" DOUBLE PRECISION NOT NULL,

"empleado_imagen" bytea NOT NULL,

"empleado_imagen_nombre" text NOT NULL,

PRIMARY KEY ("empleado_id") 

);



CREATE TABLE "pelicula" (

"pelicula_id" serial8 NOT NULL,

"pelicula_titulo" text NOT NULL,

"pelicula_anioestreno" INTERVAL year NOT NULL,

"pelicula_director" text NOT NULL,

"pelicula_estelares" text NOT NULL,

"pelicula_duracion" INTERVAL minute NOT NULL,

"pelicula_clasif" pelicula_clasificacion NOT NULL,

"genero_id" serial8 NOT NULL,

"pelicula_portada" bytea NOT NULL,

"pelicula_portada_nombre" text NOT NULL,

PRIMARY KEY ("pelicula_id") 

);



CREATE TABLE "venta" (

"venta_id" serial8 NOT NULL,

"venta_fecha" date NOT NULL,

"venta_neto" DOUBLE PRECISION NOT NULL,

"empleado_id" text NOT NULL,

"cliente_id" text NOT NULL,

PRIMARY KEY ("venta_id") 

);



CREATE TABLE "genero" (

"genero_id" serial8 NOT NULL,

"genero_nombre" text NOT NULL,

"genero_descripcion" text NULL,

PRIMARY KEY ("genero_id") 

);



CREATE TABLE "usuario" (

"usuario_id" serial8 NOT NULL,

"usename" text NOT NULL,

"privilegios" bigint NOT NULL,

PRIMARY KEY ("usuario_id") 

);





ALTER TABLE "detalle_venta" ADD CONSTRAINT "detallevta-pelicula" FOREIGN KEY ("copia_id") REFERENCES "copia_pelicula" ("copia_id");

ALTER TABLE "copia_pelicula" ADD CONSTRAINT "pelicula-copia" FOREIGN KEY ("pelicula_id") REFERENCES "pelicula" ("pelicula_id");

ALTER TABLE "detalle_venta" ADD CONSTRAINT "detallevta-venta" FOREIGN KEY ("venta_id") REFERENCES "venta" ("venta_id");

ALTER TABLE "venta" ADD CONSTRAINT "venta-cliente" FOREIGN KEY ("cliente_id") REFERENCES "cliente" ("cliente_id");

ALTER TABLE "venta" ADD CONSTRAINT "venta-empleado" FOREIGN KEY ("empleado_id") REFERENCES "empleado" ("empleado_id");

ALTER TABLE "pelicula" ADD CONSTRAINT "genero-pelicula" FOREIGN KEY ("genero_id") REFERENCES "genero" ("genero_id");

-- tablas log

CREATE TABLE cliente_log
(
    cliente_log_operation text NOT NULL,
    cliente_log_stamp timestamp without time zone NOT NULL,  
    cliente_log_userid text NOT NULL,
    cliente_log_cliente_id text NOT NULL,
    cliente_log_cliente_nombre text NOT NULL,  
    cliente_log_cliente_appater text NOT NULL,
    cliente_log_cliente_apmater text NOT NULL,
    cliente_log_cliente_fecharegistro date NOT NULL,
    cliente_log_cliente_fechanacimiento date NOT NULL,
    cliente_log_cliente_imagen bytea NOT NULL,
    cliente_log_cliente_imagen_nombre text NOT NULL
)WITH (
  OIDS=FALSE
);

ALTER TABLE cliente_log
  OWNER TO postgres;

CREATE TABLE copia_pelicula_log
(
    copia_pelicula_log_operation text NOT NULL,
    copia_pelicula_log_stamp timestamp without time zone NOT NULL,
    coipa_pelicula_log_userid text NOT NULL,
    coipa_pelicula_log_copia_id bigserial NOT NULL,
    coipa_pelicula_log_copia_fmto copia_formato NOT NULL,
    coipa_pelicula_log_copia_fechaadquisicion date NOT NULL,
    coipa_pelicula_log_copia_precio double precision NOT NULL,
    coipa_pelicula_log_copia_edo copia_estado NOT NULL,
    coipa_pelicula_log_pelicula_id bigserial NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE copia_pelicula_log
  OWNER TO postgres;


CREATE TABLE detalle_venta_log
(
  detalle_venta_log_operation text NOT NULL,
  detalle_venta_log_stamp timestamp without time zone NOT NULL,
  detalle_venta_log_userid text NOT NULL,
  detalle_venta_log_detallevta_id bigserial NOT NULL,
  detalle_venta_log_venta_id bigserial NOT NULL,
  detalle_venta_log_copia_id bigserial NOT NULL,
  detalle_venta_log_detallevta_comentario text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE detalle_venta_log
  OWNER TO postgres;


CREATE TABLE empleado_log
(
    empleado_log_operation text NOT NULL,
    empleado_log_stamp timestamp without time zone NOT NULL,
    empleado_log_userid text NOT NULL,
    empleado_log_empleado_id text NOT NULL,
    empleado_log_empleado_nombre text NOT NULL,
    empleado_log_empleado_appater text NOT NULL,
    empleado_log_empleado_apmater text NOT NULL,
    empleado_log_empleado_horaentrada time without time zone NOT NULL,
    empleado_log_empleado_horasalida time without time zone NOT NULL,
    empleado_log_empleado_fechanacimiento date NOT NULL,
    empleado_log_empleado_fecharegistro date NOT NULL,
    empleado_log_empleado_edo empleado_estado NOT NULL,
    empleado_log_empleado_puesto empleado_puesto NOT NULL,
    empleado_log_empleado_sueldo double precision NOT NULL,
    empleado_log_empleado_imagen bytea NOT NULL,
    empleado_log_empleado_imagen_nombre text NOT NULL
)
WITH (
  OIDS=FALSE
);

ALTER TABLE empleado_log
  OWNER TO postgres;


CREATE TABLE genero_log
(
  genero_log_operation text NOT NULL,
  genero_log_stamp timestamp without time zone NOT NULL,
  genero_log_userid text NOT NULL,
  genero_log_genero_id bigserial NOT NULL,
  genero_log_genero_nombre text NOT NULL,
  genero_log_genero_descripcion text
)WITH (
  OIDS=FALSE
);
ALTER TABLE genero_log
  OWNER TO postgres;


CREATE TABLE pelicula_log
(
  pelicula_log_operation text NOT NULL,
  pelicula_log_stamp timestamp without time zone NOT NULL,
  pelicula_log_userid text NOT NULL,
  pelicula_log_pelicula_id bigserial NOT NULL,
  pelicula_log_pelicula_titulo text NOT NULL,
  pelicula_log_pelicula_anioestreno interval year NOT NULL,
  pelicula_log_pelicula_director text NOT NULL,
  pelicula_log_pelicula_estelares text NOT NULL,
  pelicula_log_pelicula_duracion interval minute NOT NULL,
  pelicula_log_pelicula_clasif pelicula_clasificacion NOT NULL,
  pelicula_log_genero_id bigserial NOT NULL,
  pelicula_log_pelicula_portada bytea NOT NULL,
    pelicula_log_pelicula_portada_nombre text NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pelicula_log
  OWNER TO postgres;


CREATE TABLE usuario_log
(
  usuario_log_operation text NOT NULL,
  usuario_log_stamp timestamp without time zone NOT NULL,
  usuario_log_userid text NOT NULL,
  usuario_log_usuario_id bigserial NOT NULL,
  usuario_log_usename text NOT NULL,
  usuario_log_privilegios bigint NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usuario_log
  OWNER TO postgres;


CREATE TABLE venta_log
(
  venta_log_operation text NOT NULL,
  venta_log_stamp timestamp without time zone NOT NULL,
  venta_log_userid text NOT NULL,
  venta_log_venta_id bigserial NOT NULL,
  venta_log_venta_fecha date NOT NULL,
  venta_log_venta_neto double precision NOT NULL,
  venta_log_empleado_id text NOT NULL,
  venta_log_cliente_id text NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE venta_log
  OWNER TO postgres;
