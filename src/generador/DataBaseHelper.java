/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Adrián
 */
public class DataBaseHelper {

    private Connection connection;
    private Statement statement;

    public DataBaseHelper() {
        connection = null;
        statement = null;
    }

    public boolean crearConexion() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/SILO", "postgres", "1");
            statement = connection.createStatement();
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String select(String consultaSQL, boolean e) throws SQLException {
        String r = "";
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(consultaSQL);
        while (rs.next()) {
            if (e) {
                r += rs.getString(1);
            } else {
                r += rs.getString(1) + "#";
            }
        }
        return r;
    }
    
    public String selectDatos(String consultaSQL) {
        String r = "";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(consultaSQL);
            while(rs.next()) {
                r += rs.getString(1) + "#";
                r += rs.getString(2) + "#";
                r += rs.getString(3);
            }
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return r;
    }

    public String[] seleccionarDatosCopiasPeliculas(String estado) {
        ArrayList<String> array = new ArrayList<>();
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT copia_id, pelicula_titulo, copia_fmto, copia_fechaadquisicion, copia_precio\n"
                    + "FROM copia_pelicula, pelicula\n"
                    + "where copia_pelicula.pelicula_id = pelicula.pelicula_id\n"
                    + "and copia_edo = '" + estado + "'\n"
                    + "ORDER BY copia_id;");

            while (rs.next()) {
                String copiaPelicula = "";
                copiaPelicula += rs.getString("copia_id") + "&";
                copiaPelicula += rs.getString("pelicula_titulo") + "&";
                copiaPelicula += rs.getString("copia_fmto") + "&";
                copiaPelicula += rs.getString("copia_fechaadquisicion") + "&";
                copiaPelicula += rs.getString("copia_precio");
                array.add(copiaPelicula);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return array.toArray(new String[0]);
    }

    public String[] seleccionarDatosEmpleadoMensualEstado(String estado) {
        ArrayList<String> array = new ArrayList<>();
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT empleado_id, "
                    + "coalesce(empleado_nombre||' '||empleado_appater||' '"
                    + "||empleado_apmater), \n"
                    + "       coalesce('Entrada: "
                    + "'||extract(hour from empleado_horaentrada)||':'"
                    + "||extract(minute from empleado_horaentrada) ||'\n'||"
                    + "'Salida'||empleado_horasalida),\n"
                    + "       empleado_fecharegistro, empleado_sueldo\n"
                    + "  FROM empleado\n"
                    + "  WHERE empleado_edo = '" + estado + "';");
            while (rs.next()) {
                String datosEmpleado = "";
                datosEmpleado += rs.getString(1) + "&";
                datosEmpleado += rs.getString(2) + "&";
                datosEmpleado += rs.getString(3) + "&";
                datosEmpleado += rs.getString(4) + "&";
                datosEmpleado += rs.getString(5);
                array.add(datosEmpleado);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return array.toArray(new String[0]);
    }

    public ResultSet seleccionarDatosPeliculasGeneralInventario() {
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT distinct(pelicula.pelicula_id), "
                    + "pelicula_titulo, extract(year from pelicula_anioestreno),\n"
                    + "       pelicula_estelares, pelicula_director, pelicula_clasif, "
                    + "genero_nombre, pelicula_duracion, "
                    + "(select count(copia_pelicula.pelicula_id) from copia_pelicula "
                    + "where copia_pelicula.pelicula_id = pelicula.pelicula_id "
                    + "and copia_pelicula.copia_edo = 'EN-STOCK'), pelicula_portada\n"
                    + "  FROM pelicula, copia_pelicula, genero\n"
                    + "  WHERE genero.genero_id = pelicula.genero_id;");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public ResultSet seleccionarDatosPeliculasGeneralInventario(String anio) {
        ResultSet rs = null;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT distinct(pelicula.pelicula_id), "
                    + "pelicula_titulo, extract(year from pelicula_anioestreno),\n"
                    + "       pelicula_estelares, pelicula_director, pelicula_clasif, "
                    + "genero_nombre, pelicula_duracion, (select "
                    + "count(copia_pelicula.pelicula_id) from copia_pelicula "
                    + "where copia_pelicula.pelicula_id = pelicula.pelicula_id "
                    + "and copia_pelicula.copia_edo = 'EN-STOCK'), pelicula_portada\n"
                    + "  FROM pelicula, copia_pelicula, genero\n"
                    + "  WHERE genero.genero_id = pelicula.genero_id"
                    + "  AND extract (year from pelicula.pelicula_anioestreno) = " + anio + ";");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public ResultSet seleccionarDatosPeliculasGeneralInventarioClasificaciones(String clasif) {
        ResultSet rs = null;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT distinct(pelicula.pelicula_id), "
                    + "pelicula_titulo, extract(year from pelicula_anioestreno), "
                    + "pelicula_estelares, pelicula_director, pelicula_clasif, "
                    + "genero_nombre, pelicula_duracion, (select count(copia_pelicula.pelicula_id) "
                    + "from copia_pelicula where copia_pelicula.pelicula_id = pelicula.pelicula_id "
                    + "and copia_pelicula.copia_edo = 'EN-STOCK'), pelicula_portada\n"
                    + "FROM pelicula, copia_pelicula, genero\n"
                    + "WHERE genero.genero_id = pelicula.genero_id\n"
                    + "AND pelicula.pelicula_clasif = '" + clasif + "';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public ResultSet seleccionarDatosPeliculasGeneralInventarioGenero(String genero) {
        ResultSet rs = null;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT distinct(pelicula.pelicula_id), "
                    + "pelicula_titulo, extract(year from pelicula_anioestreno), "
                    + "pelicula_estelares, pelicula_director, pelicula_clasif, "
                    + "genero_nombre, pelicula_duracion, (select "
                    + "count(copia_pelicula.pelicula_id) from copia_pelicula "
                    + "where copia_pelicula.pelicula_id = pelicula.pelicula_id "
                    + "and copia_pelicula.copia_edo = 'EN-STOCK'), pelicula_portada\n"
                    + "FROM pelicula, copia_pelicula, genero\n"
                    + "WHERE genero.genero_id = pelicula.genero_id\n"
                    + "AND genero.genero_nombre = '" + genero + "'\n"
                    + "AND copia_pelicula.copia_edo = 'EN-STOCK';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public ResultSet seleccionarDatosRegistroMensualClientes(String fechaInicio, String fechaFin) {
        ResultSet rs = null;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT cliente_id, "
                    + "coalesce (cliente_nombre || ' ' || cliente_appater "
                    + "|| ' ' || cliente_apmater), extract (year from age(now(),"
                    + "cliente_fechanacimiento)), to_char(cliente_fecharegistro,"
                    + " 'dd/mm/yyyy'), cliente_imagen\n"
                    + "FROM cliente\n"
                    + "WHERE cliente_fecharegistro >= '" + fechaInicio + "'\n"
                    + "AND cliente_fecharegistro <= '" + fechaFin + "';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public ResultSet seleccionarDatosRegistroMensualEmpleados(String fechaInicio, String fechaFin) {
        ResultSet rs = null;
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT empleado_id, "
                    + "coalesce (empleado_nombre || ' ' || empleado_appater || "
                    + "' ' || empleado_apmater), extract (year from (age(now(), "
                    + "empleado_fechanacimiento))),\n"
                    + " to_char(empleado_fecharegistro, 'dd/mm/yyyy'), "
                    + "coalesce ((coalesce(extract (hour from empleado_horaentrada) "
                    + "|| ':'\n"
                    + "  || extract (minute from empleado_horaentrada))) || "
                    + "' - ' || (coalesce(extract (hour from empleado_horasalida)) "
                    + "|| ':' || extract(minute from empleado_horasalida))), "
                    + "empleado_sueldo, empleado_imagen\n"
                    + "FROM empleado\n"
                    + "WHERE empleado_fecharegistro >= '" + fechaInicio + "'\n"
                    + "AND empleado_fecharegistro <= '" + fechaFin + "';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    public String[] seleccionarDatosVentasMensual(String fechaInicio, String fechaFin) {
        ArrayList<String> array = new ArrayList<>();
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT venta.venta_id, "
                    + "to_char(venta.venta_fecha, 'dd/mm/yyyy'), coalesce "
                    + "(empleado.empleado_id || ' ' || empleado.empleado_nombre "
                    + "|| ' ' || empleado.empleado_appater),\n"
                    + "coalesce (cliente.cliente_id || ' ' || \n"
                    + "cliente.cliente_nombre || ' ' || cliente.cliente_appater || \n"
                    + "' ' || cliente.cliente_apmater), venta.venta_neto\n"
                    + "FROM venta, empleado, cliente\n"
                    + "WHERE venta.empleado_id = empleado.empleado_id\n"
                    + "AND venta.cliente_id = cliente.cliente_id\n"
                    + "AND venta.venta_fecha >= '" + fechaInicio + "'\n"
                    + "AND venta.venta_fecha <= '" + fechaFin + "' ORDER BY venta.venta_id;");

            while (rs.next()) {
                String datosVenta = "";
                datosVenta += rs.getString(1) + "&";
                datosVenta += rs.getString(2) + "&";
                datosVenta += rs.getString(3) + "&";
                datosVenta += rs.getString(4) + "&";
                datosVenta += rs.getString(5);
                array.add(datosVenta);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return array.toArray(new String[0]);
    }

    public String seleccionarDatosVentasCantidadVentas(String fechaInicio, String fechaFin) {
        String result = "";
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery("select count(venta.venta_id), "
                    + "coalesce(empleado.empleado_id||' '||empleado.empleado_nombre||"
                    + "' '||empleado.empleado_appater||' '||empleado.empleado_apmater), "
                    + "sum(venta.venta_neto)\n"
                    + "from venta, empleado\n"
                    + "where venta.empleado_id = empleado.empleado_id\n"
                    + "and venta.venta_fecha >= '" + fechaInicio + "'\n"
                    + "and venta.venta_fecha <= '" + fechaFin + "'\n"
                    + "group by empleado.empleado_id\n"
                    + "order by count(venta.venta_id) desc;");
            rs.next();
            result += rs.getString(1) + "&";
            result += rs.getString(2) + "&";
            result += rs.getString(3);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "puto1");
        }
        return result;
    }

    public String seleccionarDatosVentasCantidadNeta(String fechaInicio, String fechaFin) {
        String result = "";
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery("select sum(venta.venta_neto), "
                    + "coalesce(empleado.empleado_id||' '||empleado.empleado_nombre||"
                    + "' '||empleado.empleado_appater||' '||empleado.empleado_apmater), "
                    + "count(venta.venta_id)\n"
                    + "from venta, empleado\n"
                    + "where venta.empleado_id = empleado.empleado_id\n"
                    + "and venta.venta_fecha >= '" + fechaInicio + "'\n"
                    + "and venta.venta_fecha <= '" + fechaFin + "'\n"
                    + "group by empleado.empleado_id\n"
                    + "order by sum(venta.venta_neto) desc;");
            rs.next();
            result += rs.getString(1) + "&";
            result += rs.getString(2) + "&";
            result += rs.getString(3);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "puto2");
        }
        return result;
    }

    public String[] seleccionarDatosEmpleadoVentas(String fecha1, String fecha2) {
        ArrayList<String> array = new ArrayList();
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("");
            while(rs.next()) {
                String datos = "";
                datos += rs.getString(1) + "&";
                datos += rs.getString(2) + "&";
                datos += rs.getString(3) + "&";
                datos += rs.getString(4);
                array.add(datos);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return array.toArray(new String[0]);
    }

    public void cerrarConexion() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

}
