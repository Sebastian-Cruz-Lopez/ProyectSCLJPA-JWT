package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Colonia;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Estado;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Municipio;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Pais;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Roll;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Usuario;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.UsuarioDireccion;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository //---ESSTA CLASE SE ENCARGA DEL MANEJO DE LA BD
public class UsuarioDAOImplementation implements IUsuarioDAO {

    @Autowired //---ESTA ES LA CLASE DE LA CONEXION DE LA BD
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetAll() {

        Result result = new Result();

        try {
            int procesoCorrecto = jdbcTemplate.execute("{CALL UsuarioGetAllSP(?)}", (CallableStatementCallback<Integer>) callableStatement -> {

                int idUsuarioPrevio = 0;

                callableStatement.registerOutParameter(1, Types.REF_CURSOR);

                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                result.objects = new ArrayList<>();

                while (resultSet.next()) {

                    idUsuarioPrevio = resultSet.getInt("IdUsuario");

                    if (!result.objects.isEmpty() && idUsuarioPrevio == ((UsuarioDireccion) (result.objects.get(result.objects.size() - 1))).Usuario.getIdUsuario()) {

                        //SE AGREGAN SOLO LAS DIRECCIONES//
                        Direccion direccion = new Direccion();
                        direccion.setIdDireccion(resultSet.getInt("idDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();
                        direccion.Colonia.setIdColonia(resultSet.getInt("idColonia"));

                        ((UsuarioDireccion) (result.objects.get(result.objects.size() - 1))).Direcciones.add(direccion);

                    } else {
                        //SE AGREGAN LOS USUARIOS Y SUS DIRECCIONES//
                        UsuarioDireccion usuarioDireccion = new UsuarioDireccion();

                        usuarioDireccion.Usuario = new Usuario();
                        usuarioDireccion.Usuario.setIdUsuario(resultSet.getInt("idUsuario"));
                        usuarioDireccion.Usuario.setNombre(resultSet.getString("Nombre"));
                        usuarioDireccion.Usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                        usuarioDireccion.Usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));

                        usuarioDireccion.Usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuarioDireccion.Usuario.setTelefono(resultSet.getString("Telefono"));
                        usuarioDireccion.Usuario.setEmail(resultSet.getString("Email"));
                        usuarioDireccion.Usuario.setUsername(resultSet.getString("UserName"));
                        usuarioDireccion.Usuario.setPassword(resultSet.getString("Password"));
                        usuarioDireccion.Usuario.setSexo(resultSet.getString("Sexo").charAt(0));
                        usuarioDireccion.Usuario.setCelular(resultSet.getString("Celular"));
                        usuarioDireccion.Usuario.setCURP(resultSet.getString("CURP"));

                        usuarioDireccion.Usuario.setImagen(resultSet.getString("Imagen"));
                        usuarioDireccion.Usuario.setEstatus(resultSet.getInt("Estatus"));

                        usuarioDireccion.Usuario.Roll = new Roll();
                        usuarioDireccion.Usuario.Roll.setIdRoll(resultSet.getInt("idRoll"));
                        usuarioDireccion.Usuario.Roll.setRoll(resultSet.getString("Roll"));

                        usuarioDireccion.Direcciones = new ArrayList<>();

                        Direccion direccion = new Direccion();
                        direccion.setIdDireccion(resultSet.getInt("idDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();
                        direccion.Colonia.setIdColonia(resultSet.getInt("idColonia"));

                        usuarioDireccion.Direcciones.add(direccion);

                        result.objects.add(usuarioDireccion);

                    }

                }
                return 1;
            });

            if (procesoCorrecto == 1) {

                result.correct = true;

            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result Add(UsuarioDireccion usuarioDireccion) {

        Result result = new Result();

        try {
            jdbcTemplate.execute("{CALL UsuarioAddSP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", (CallableStatementCallback<Integer>) callableStatement -> {

                callableStatement.setString(1, usuarioDireccion.Usuario.getNombre());
                callableStatement.setString(2, usuarioDireccion.Usuario.getApellidoPaterno());
                callableStatement.setString(3, usuarioDireccion.Usuario.getApellidoMaterno());
                callableStatement.setDate(4, new java.sql.Date(usuarioDireccion.Usuario.getFechaNacimiento().getTime()));
                callableStatement.setString(5, usuarioDireccion.Usuario.getTelefono());
                callableStatement.setString(6, usuarioDireccion.Usuario.getEmail());
                callableStatement.setString(7, usuarioDireccion.Usuario.getUsername());
                callableStatement.setString(8, usuarioDireccion.Usuario.getPassword());
                callableStatement.setString(9, String.valueOf(usuarioDireccion.Usuario.getSexo()));
                callableStatement.setString(10, usuarioDireccion.Usuario.getCelular());
                callableStatement.setString(11, usuarioDireccion.Usuario.getCURP());
                usuarioDireccion.Usuario.Roll = new Roll();
                callableStatement.setInt(12, usuarioDireccion.Usuario.getRoll().getIdRoll());
                callableStatement.setString(13, usuarioDireccion.Usuario.getImagen());
                callableStatement.setInt(14, usuarioDireccion.Usuario.getEstatus());
                usuarioDireccion.Direccion = new Direccion();
                callableStatement.setString(15, usuarioDireccion.Direccion.getCalle());
                callableStatement.setString(16, usuarioDireccion.Direccion.getNumeroInterior());
                callableStatement.setString(17, usuarioDireccion.Direccion.getNumeroExterior());
                usuarioDireccion.Direccion.Colonia = new Colonia();
                callableStatement.setInt(18, usuarioDireccion.Direccion.Colonia.getIdColonia());

                int rowAffected = callableStatement.executeUpdate();

                result.correct = rowAffected == 1;

                return 1;
            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;

        }

        return result;
    }

    @Override
    public Result Add(List<UsuarioDireccion> usuariosDireccion) {
        Result result = new Result();

        try {
            for (UsuarioDireccion usuarioDireccion : usuariosDireccion) {
                this.Add(usuarioDireccion);
            }

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result UsuarioGetByid(int idUsuario) {
        Result result = new Result();

        try {
            result.correct = jdbcTemplate.execute("{CALL UsuarioGetDetalle(?,?,?)}", (CallableStatementCallback<Boolean>) callableStatement -> {

                callableStatement.setInt(1, idUsuario);
                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                callableStatement.registerOutParameter(3, java.sql.Types.REF_CURSOR);

                callableStatement.execute();

                ResultSet resultSetUsuario = (ResultSet) callableStatement.getObject(2);
                ResultSet resultSetDirecciones = (ResultSet) callableStatement.getObject(3);

                UsuarioDireccion usuarioDireccion = new UsuarioDireccion();

                if (resultSetUsuario.next()) {

                    usuarioDireccion.Usuario = new Usuario();

                    usuarioDireccion.Usuario.setIdUsuario(resultSetUsuario.getInt("idUsuario"));
                    usuarioDireccion.Usuario.setNombre(resultSetUsuario.getString("Nombre"));
                    usuarioDireccion.Usuario.setApellidoPaterno(resultSetUsuario.getString("ApellidoPaterno"));
                    usuarioDireccion.Usuario.setApellidoMaterno(resultSetUsuario.getString("ApellidoMaterno"));
                    usuarioDireccion.Usuario.setFechaNacimiento(resultSetUsuario.getDate("FechaNacimiento"));
                    usuarioDireccion.Usuario.setTelefono(resultSetUsuario.getString("Telefono"));
                    usuarioDireccion.Usuario.setEmail(resultSetUsuario.getString("Email"));
                    usuarioDireccion.Usuario.setUsername(resultSetUsuario.getString("Username"));
                    usuarioDireccion.Usuario.setPassword(resultSetUsuario.getString("Password"));
                    usuarioDireccion.Usuario.setSexo(resultSetUsuario.getString("Sexo").charAt(0));
                    usuarioDireccion.Usuario.setCelular(resultSetUsuario.getString("Celular"));
                    usuarioDireccion.Usuario.setCURP(resultSetUsuario.getString("CURP"));

                    usuarioDireccion.Usuario.Roll = new Roll();
                    usuarioDireccion.Usuario.Roll.setIdRoll(resultSetUsuario.getInt("idRoll"));
                    usuarioDireccion.Usuario.Roll.setRoll(resultSetUsuario.getString("Roll"));

                }

                usuarioDireccion.Direcciones = new ArrayList<>();

                while (resultSetDirecciones.next()) {

                    Direccion direccion = new Direccion();
                    direccion.setIdDireccion(resultSetDirecciones.getInt("idDireccion"));
                    direccion.setCalle(resultSetDirecciones.getNString("Calle"));
                    direccion.setNumeroInterior(resultSetDirecciones.getNString("NumeroInterior"));
                    direccion.setNumeroExterior(resultSetDirecciones.getNString("NumeroExterior"));

                    direccion.Colonia = new Colonia();
                    direccion.Colonia.setIdColonia(resultSetDirecciones.getInt("idColonia"));
                    direccion.Colonia.setNombre(resultSetDirecciones.getString("NombreColonia"));
                    direccion.Colonia.setCodigoPostal(resultSetDirecciones.getString("CodigoPostal"));

                    direccion.Colonia.Municipio = new Municipio();
                    direccion.Colonia.Municipio.setIdMunicipio(resultSetDirecciones.getInt("idMunicipio"));
                    direccion.Colonia.Municipio.setNombre(resultSetDirecciones.getString("NombreMunicipio"));

                    direccion.Colonia.Municipio.Estado = new Estado();
                    direccion.Colonia.Municipio.Estado.setIdEstado(resultSetDirecciones.getInt("idEstado"));
                    direccion.Colonia.Municipio.Estado.setNombre(resultSetDirecciones.getString("NombreEstado"));

                    direccion.Colonia.Municipio.Estado.Pais = new Pais();
                    direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSetDirecciones.getInt("idPais"));
                    direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSetDirecciones.getString("NombrePais"));

                    usuarioDireccion.Direcciones.add(direccion);

                }

                result.object = usuarioDireccion;

                return true;
            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetAllDinamic(Usuario usuario) {
        Result result = new Result();

        try {

            result.correct = jdbcTemplate.execute("{CALL GetAllDinamic(?,?,?,?,?,?)}", (CallableStatementCallback<Boolean>) callableStatement -> {

                callableStatement.setString(1, usuario.getNombre());
                callableStatement.setString(2, usuario.getApellidoPaterno());
                callableStatement.setString(3, usuario.getApellidoMaterno());
                callableStatement.setInt(4, usuario.getRoll().getIdRoll());
                callableStatement.setInt(5, usuario.getEstatus());
                callableStatement.registerOutParameter(6, java.sql.Types.REF_CURSOR);

                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(6);

                result.objects = new ArrayList<>();

                while (resultSet.next()) {

                    int idUsuario = resultSet.getInt("IdUsuario");

                    if (!result.objects.isEmpty() && idUsuario == ((UsuarioDireccion) (result.objects.get(result.objects.size() - 1))).Usuario.getIdUsuario()) {

                        //SE AGREGAN SOLO LAS DIRECCIONES//
                        Direccion direccion = new Direccion();
                        direccion.setIdDireccion(resultSet.getInt("idDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();
                        direccion.Colonia.setIdColonia(resultSet.getInt("idColonia"));

                        ((UsuarioDireccion) (result.objects.get(result.objects.size() - 1))).Direcciones.add(direccion);

                    } else {
                        //SE AGREGAN LOS USUARIOS Y SUS DIRECCIONES//
                        UsuarioDireccion usuarioDireccion = new UsuarioDireccion();

                        usuarioDireccion.Usuario = new Usuario();
                        usuarioDireccion.Usuario.setIdUsuario(resultSet.getInt("idUsuario"));
                        usuarioDireccion.Usuario.setNombre(resultSet.getString("Nombre"));
                        usuarioDireccion.Usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                        usuarioDireccion.Usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));

                        usuarioDireccion.Usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuarioDireccion.Usuario.setTelefono(resultSet.getString("Telefono"));
                        usuarioDireccion.Usuario.setEmail(resultSet.getString("Email"));
                        usuarioDireccion.Usuario.setUsername(resultSet.getString("UserName"));
                        usuarioDireccion.Usuario.setPassword(resultSet.getString("Password"));
                        usuarioDireccion.Usuario.setSexo(resultSet.getString("Sexo").charAt(0));
                        usuarioDireccion.Usuario.setCelular(resultSet.getString("Celular"));
                        usuarioDireccion.Usuario.setCURP(resultSet.getString("CURP"));

                        usuarioDireccion.Usuario.setImagen(resultSet.getString("Imagen"));
                        usuarioDireccion.Usuario.setEstatus(resultSet.getInt("Estatus"));

                        usuarioDireccion.Usuario.Roll = new Roll();
                        usuarioDireccion.Usuario.Roll.setIdRoll(resultSet.getInt("idRoll"));
                        usuarioDireccion.Usuario.Roll.setRoll(resultSet.getString("Roll"));

                        usuarioDireccion.Direcciones = new ArrayList<>();

                        Direccion direccion = new Direccion();
                        direccion.setIdDireccion(resultSet.getInt("idDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        direccion.Colonia = new Colonia();
                        direccion.Colonia.setIdColonia(resultSet.getInt("idColonia"));

                        usuarioDireccion.Direcciones.add(direccion);

                        result.objects.add(usuarioDireccion);

                    }

                }

                return true;

            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result UpdateUsuario(Usuario usuario) {
        Result result = new Result();

        try {
            jdbcTemplate.execute("{CALL UsuarioUpdateSP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", (CallableStatementCallback<Object>) callableStatement -> {

                callableStatement.setInt(1, usuario.getIdUsuario());
                callableStatement.setString(2, usuario.getNombre());
                callableStatement.setString(3, usuario.getApellidoPaterno());
                callableStatement.setString(4, usuario.getApellidoMaterno());
                callableStatement.setDate(5, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
                callableStatement.setString(6, usuario.getTelefono());
                callableStatement.setString(7, usuario.getEmail());
                callableStatement.setString(8, usuario.getUsername());
                callableStatement.setString(9, usuario.getPassword());
                callableStatement.setString(10, String.valueOf(usuario.getSexo()));
                callableStatement.setString(11, usuario.getCelular());
                callableStatement.setString(12, usuario.getCURP());
                callableStatement.setInt(13, usuario.Roll.getIdRoll());
                callableStatement.setString(14, usuario.getImagen());
                callableStatement.setInt(15, usuario.getEstatus());

                int rowAffected = callableStatement.executeUpdate();
                result.correct = true;

                return null;
            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result UpdateDireccion(Direccion direccion) {
        Result result = new Result();

        try {
            jdbcTemplate.execute("{CALL DireccionUpdateSP(?,?,?,?,?)}", (CallableStatementCallback<Object>) callableStatement -> {

                callableStatement.setInt(1, direccion.getIdDireccion());
                callableStatement.setString(2, direccion.getCalle());
                callableStatement.setString(3, direccion.getNumeroInterior());
                callableStatement.setString(4, direccion.getNumeroExterior());
                callableStatement.setInt(5, direccion.Colonia.getIdColonia());

                int rowAffected = callableStatement.executeUpdate();
                result.correct = rowAffected == 1;

                return null;
            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result DeleteUsuario(int idUsuario) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL UsuarioDeleteTotalSP(?)}", (CallableStatementCallback<Object>) cs -> {
                cs.setInt(1, idUsuario);
                cs.execute();
                result.correct = true;
                return null;
            });
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result DeleteDireccion(int idDireccion) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL DireccionDeleteSP(?)}", (CallableStatementCallback<Object>) cs -> {
                cs.setInt(1, idDireccion);
                cs.execute();
                result.correct = true;
                return null;
            });
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result UpdateActivo(int idUsuario, int Estatus) {
        Result result = new Result();

        try {
            int procesoCorrecto = jdbcTemplate.execute("{CALL UsuarioUpdateActivo(?,?)}", (CallableStatementCallback<Integer>) callableStatment -> {

                callableStatment.setInt(1, idUsuario);
                callableStatment.setInt(2, Estatus);

                callableStatment.executeUpdate();

                return 1;
            });

            if (procesoCorrecto == 1) {
                result.correct = true;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result AddDireccion(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            jdbcTemplate.execute("{CALL DireccionAddSP(?,?,?,?,?)}", (CallableStatementCallback<Integer>) callableStatement -> {

                callableStatement.setString(1, usuarioDireccion.Direccion.getCalle());
                callableStatement.setString(2, usuarioDireccion.Direccion.getNumeroInterior());
                callableStatement.setString(3, usuarioDireccion.Direccion.getNumeroExterior());
                callableStatement.setInt(4, usuarioDireccion.Direccion.Colonia.getIdColonia());
                callableStatement.setInt(5, usuarioDireccion.Usuario.getIdUsuario());

                int rowAffected = callableStatement.executeUpdate();
                result.correct = rowAffected == 1;

                return 1;
            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

}
