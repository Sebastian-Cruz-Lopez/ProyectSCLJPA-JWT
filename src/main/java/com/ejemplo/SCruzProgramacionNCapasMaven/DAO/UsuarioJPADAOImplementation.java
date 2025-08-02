package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Colonia;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Direccion;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Roll;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Usuario;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.UsuarioDireccion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioJPADAOImplementation implements IUsuarioJPADAO {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public Result Add(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            Usuario usuarioJPA = new Usuario();

            usuarioJPA.setNombre(usuarioDireccion.Usuario.getNombre());
            usuarioJPA.setApellidoPaterno(usuarioDireccion.Usuario.getApellidoPaterno());
            usuarioJPA.setApellidoMaterno(usuarioDireccion.Usuario.getApellidoMaterno());
            usuarioJPA.setFechaNacimiento(usuarioDireccion.Usuario.getFechaNacimiento());
            usuarioJPA.setTelefono(usuarioDireccion.Usuario.getTelefono());
            usuarioJPA.setEmail(usuarioDireccion.Usuario.getEmail());
            usuarioJPA.setUsername(usuarioDireccion.Usuario.getUsername());
            usuarioJPA.setPassword(usuarioDireccion.Usuario.getPassword());
            usuarioJPA.setSexo(usuarioDireccion.Usuario.getSexo());
            usuarioJPA.setCelular(usuarioDireccion.Usuario.getCelular());
            usuarioJPA.setCURP(usuarioDireccion.Usuario.getCURP());

            usuarioJPA.Roll = new Roll();
            usuarioJPA.Roll.setIdRoll(usuarioDireccion.Usuario.Roll.getIdRoll());
            usuarioJPA.setImagen(usuarioDireccion.Usuario.getImagen());
            usuarioJPA.setEstatus(usuarioDireccion.Usuario.getEstatus());

            entityManager.persist(usuarioJPA);
            entityManager.flush();
            Direccion direccion = new Direccion();
            direccion.setCalle(usuarioDireccion.Direccion.getCalle());
            direccion.setNumeroInterior(usuarioDireccion.Direccion.getNumeroInterior());
            direccion.setNumeroExterior(usuarioDireccion.Direccion.getNumeroExterior());
            direccion.Colonia = new Colonia();
            direccion.Colonia.setIdColonia(usuarioDireccion.Direccion.Colonia.getIdColonia());
            direccion.setUsuario(usuarioJPA);
            entityManager.persist(direccion);

            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;

    }

    @Transactional
    @Override
    public Result DeleteUsuario(int idUsuario) {
        Result result = new Result();

        try {
            Usuario usuario = entityManager.find(Usuario.class, idUsuario);

            if (usuario != null) {
                // eliminar direcciones manualmente 
                Query query = entityManager.createQuery(
                        "DELETE FROM Direccion d WHERE d.usuario.idUsuario = :idUsuario"
                );
                query.setParameter("idUsuario", idUsuario);
                query.executeUpdate();

                // eliminar el usuario
                entityManager.remove(usuario);

                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con el ID: " + idUsuario;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result DeleteDireccion(int idDireccion) {
        Result result = new Result();

        try {
            Direccion direccion = entityManager.find(Direccion.class, idDireccion);

            if (direccion != null) {
                entityManager.remove(direccion);
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Dirección no encontrada con el ID: " + idDireccion;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result UpdateUsario(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {

            Usuario usuarioJPA = entityManager.find(Usuario.class, usuarioDireccion.Usuario.getIdUsuario());
            if (usuarioJPA != null) {
                usuarioJPA.setUsername(usuarioDireccion.Usuario.getUsername());
                usuarioJPA.setNombre(usuarioDireccion.Usuario.getNombre());
                usuarioJPA.setApellidoPaterno(usuarioDireccion.Usuario.getApellidoPaterno());
                usuarioJPA.setApellidoMaterno(usuarioDireccion.Usuario.getApellidoMaterno());
                usuarioJPA.setTelefono(usuarioDireccion.Usuario.getTelefono());
                usuarioJPA.setEmail(usuarioDireccion.Usuario.getEmail());
                usuarioJPA.setPassword(usuarioDireccion.Usuario.getPassword());
                usuarioJPA.setSexo(usuarioDireccion.Usuario.getSexo());
                usuarioJPA.setCelular(usuarioDireccion.Usuario.getCelular());
                usuarioJPA.setCURP(usuarioDireccion.Usuario.getCURP());
                usuarioJPA.Roll = new Roll();
                usuarioJPA.Roll.setIdRoll(usuarioDireccion.Usuario.Roll.getIdRoll());
                usuarioJPA.setImagen(usuarioDireccion.Usuario.getImagen());
                usuarioJPA.setEstatus(usuarioDireccion.Usuario.getEstatus());

                entityManager.merge(usuarioJPA);

                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado";
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result UpdateDireccion(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            Direccion direccionJPA = entityManager.find(Direccion.class, usuarioDireccion.Direccion.getIdDireccion());

            if (direccionJPA != null) {
                direccionJPA.setCalle(usuarioDireccion.Direccion.getCalle());
                direccionJPA.setNumeroInterior(usuarioDireccion.Direccion.getNumeroInterior());
                direccionJPA.setNumeroExterior(usuarioDireccion.Direccion.getNumeroExterior());

                direccionJPA.Colonia = new Colonia();
                direccionJPA.Colonia.setIdColonia(usuarioDireccion.Direccion.Colonia.getIdColonia());
                direccionJPA.Colonia.setCodigoPostal(usuarioDireccion.Direccion.Colonia.getCodigoPostal());

                entityManager.merge(direccionJPA);

                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Dirección no encontrada con el ID: " + usuarioDireccion.Direccion.getIdDireccion();
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetAll() {
        Result result = new Result();
        result.objects = new ArrayList<>();

        try {

            TypedQuery<Usuario> usuariosQuery = entityManager.createQuery("FROM Usuario ORDER BY idUsuario ASC", Usuario.class);

            List<Usuario> usuarios = usuariosQuery.getResultList();

            for (Usuario usuarioJPA : usuarios) {

                UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
                usuarioDireccion.Usuario = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Usuario();
                usuarioDireccion.Usuario.setIdUsuario(usuarioJPA.getIdUsuario());
                usuarioDireccion.Usuario.setNombre(usuarioJPA.getNombre());
                usuarioDireccion.Usuario.setApellidoPaterno(usuarioJPA.getApellidoPaterno());
                usuarioDireccion.Usuario.setApellidoMaterno(usuarioJPA.getApellidoMaterno());
                usuarioDireccion.Usuario.setFechaNacimiento(usuarioJPA.getFechaNacimiento());
                usuarioDireccion.Usuario.setTelefono(usuarioJPA.getTelefono());
                usuarioDireccion.Usuario.setEmail(usuarioJPA.getEmail());
                usuarioDireccion.Usuario.setUsername(usuarioJPA.getUsername());
                usuarioDireccion.Usuario.setPassword(usuarioJPA.getPassword());
                usuarioDireccion.Usuario.setSexo(usuarioJPA.getSexo());
                usuarioDireccion.Usuario.setCelular(usuarioJPA.getCelular());
                usuarioDireccion.Usuario.setCURP(usuarioJPA.getCURP());

                usuarioDireccion.Usuario.Roll = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Roll();
                usuarioDireccion.Usuario.Roll.setIdRoll(usuarioJPA.Roll.getIdRoll());
                usuarioDireccion.Usuario.Roll.setRoll(usuarioJPA.Roll.getRoll());
                usuarioDireccion.Usuario.setImagen(usuarioJPA.getImagen());
                usuarioDireccion.Usuario.setEstatus(usuarioJPA.getEstatus());

                TypedQuery<Direccion> direccionesQuery = entityManager.createQuery("FROM Direccion WHERE usuario.idUsuario = :idUsuario", Direccion.class);
                direccionesQuery.setParameter("idUsuario", usuarioJPA.getIdUsuario());
                List<Direccion> direccionesJPA = direccionesQuery.getResultList();

                if (direccionesJPA.size() != 0) {
                    usuarioDireccion.Direcciones = new ArrayList<>();

                    for (Direccion direccionJPA : direccionesJPA) {

                        com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion direccion = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion();
                        direccion.setCalle(direccionJPA.getCalle());
                        direccion.setNumeroInterior(direccionJPA.getNumeroInterior());
                        direccion.setNumeroExterior(direccionJPA.getNumeroExterior());

                        Colonia colonia = new Colonia();
                        colonia.setIdColonia(direccionJPA.getColonia().getIdColonia());

                        usuarioDireccion.Direcciones.add(direccion);

                    }

                }

                result.objects.add(usuarioDireccion);

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
    public Result GetByid(int idUsuario) {
        Result result = new Result();

        try {
            Usuario usuarioJPA = entityManager.find(Usuario.class, idUsuario);

            if (usuarioJPA == null) {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con el ID: " + idUsuario;
                return result;
            }

            usuarioJPA.getDirecciones().size();

            UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
            usuarioDireccion.Usuario = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Usuario();

            usuarioDireccion.Usuario.setIdUsuario(usuarioJPA.getIdUsuario());
            usuarioDireccion.Usuario.setNombre(usuarioJPA.getNombre());
            usuarioDireccion.Usuario.setApellidoPaterno(usuarioJPA.getApellidoPaterno());
            usuarioDireccion.Usuario.setApellidoMaterno(usuarioJPA.getApellidoMaterno());
            usuarioDireccion.Usuario.setFechaNacimiento(usuarioJPA.getFechaNacimiento());
            usuarioDireccion.Usuario.setTelefono(usuarioJPA.getTelefono());
            usuarioDireccion.Usuario.setEmail(usuarioJPA.getEmail());
            usuarioDireccion.Usuario.setUsername(usuarioJPA.getUsername());
            usuarioDireccion.Usuario.setPassword(usuarioJPA.getPassword());
            usuarioDireccion.Usuario.setSexo(usuarioJPA.getSexo());
            usuarioDireccion.Usuario.setCelular(usuarioJPA.getCelular());
            usuarioDireccion.Usuario.setCURP(usuarioJPA.getCURP());
            usuarioDireccion.Usuario.setImagen(usuarioJPA.getImagen());
            usuarioDireccion.Usuario.setEstatus(usuarioJPA.getEstatus());

            usuarioDireccion.Usuario.Roll = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Roll();
            usuarioDireccion.Usuario.Roll.setIdRoll(usuarioJPA.getRoll().getIdRoll());
            usuarioDireccion.Usuario.Roll.setRoll(usuarioJPA.getRoll().getRoll());

            usuarioDireccion.Direcciones = new ArrayList<>();

            for (com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Direccion direccionJPA : usuarioJPA.getDirecciones()) {
                com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion direccion = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion();

                direccion.setIdDireccion(direccionJPA.getIdDireccion());
                direccion.setCalle(direccionJPA.getCalle());
                direccion.setNumeroInterior(direccionJPA.getNumeroInterior());
                direccion.setNumeroExterior(direccionJPA.getNumeroExterior());

                direccion.Colonia = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Colonia();
                direccion.Colonia.setIdColonia(direccionJPA.Colonia.getIdColonia());
                direccion.Colonia.setNombre(direccionJPA.Colonia.getNombre());
                direccion.Colonia.setCodigoPostal(direccionJPA.Colonia.getCodigoPostal());

                direccion.Colonia.Municipio = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Municipio();
                direccion.Colonia.Municipio.setIdMunicipio(direccionJPA.Colonia.Municipio.getIdMunicipio());
                direccion.Colonia.Municipio.setNombre(direccionJPA.Colonia.Municipio.getNombre());

                direccion.Colonia.Municipio.Estado = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Estado();
                direccion.Colonia.Municipio.Estado.setIdEstado(direccionJPA.Colonia.Municipio.Estado.getIdEstado());
                direccion.Colonia.Municipio.Estado.setNombre(direccionJPA.Colonia.Municipio.Estado.getNombre());

                direccion.Colonia.Municipio.Estado.Pais = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Pais();
                direccion.Colonia.Municipio.Estado.Pais.setIdPais(direccionJPA.Colonia.Municipio.Estado.Pais.getIdPais());
                direccion.Colonia.Municipio.Estado.Pais.setNombre(direccionJPA.Colonia.Municipio.Estado.Pais.getNombre());

                usuarioDireccion.Direcciones.add(direccion);
            }

            result.correct = true;
            result.object = usuarioDireccion;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result AddDireccion(UsuarioDireccion usuarioDireccion) {
        Result result = new Result();

        try {
            Direccion direccionJPA = new Direccion();

            direccionJPA.setCalle(usuarioDireccion.Direccion.getCalle());
            direccionJPA.setNumeroInterior(usuarioDireccion.Direccion.getNumeroInterior());
            direccionJPA.setNumeroExterior(usuarioDireccion.Direccion.getNumeroExterior());

            Colonia coloniaJPA = new Colonia();
            coloniaJPA.setIdColonia(usuarioDireccion.Direccion.Colonia.getIdColonia());
            direccionJPA.setColonia(coloniaJPA);

            Usuario usuarioJPA = new Usuario();
            usuarioJPA.setIdUsuario(usuarioDireccion.Usuario.getIdUsuario());
            direccionJPA.setUsuario(usuarioJPA);

            entityManager.persist(direccionJPA);
            entityManager.flush();

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetDireccionByid(int idDireccion) {
        Result result = new Result();

        try {
            Direccion direccionEntity = entityManager.createQuery(
                    "SELECT d FROM Direccion d "
                    + "JOIN FETCH d.Colonia c "
                    + "JOIN FETCH c.Municipio m "
                    + "JOIN FETCH m.Estado e "
                    + "JOIN FETCH e.Pais p "
                    + "WHERE d.idDireccion = :idDireccion", Direccion.class)
                    .setParameter("idDireccion", idDireccion)
                    .getSingleResult();

            com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion direccionML = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion();

            direccionML.setIdDireccion(direccionEntity.getIdDireccion());
            direccionML.setCalle(direccionEntity.getCalle());
            direccionML.setNumeroInterior(direccionEntity.getNumeroInterior());
            direccionML.setNumeroExterior(direccionEntity.getNumeroExterior());

            direccionML.Colonia = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Colonia();
            direccionML.Colonia.setIdColonia(direccionEntity.Colonia.getIdColonia());
            direccionML.Colonia.setNombre(direccionEntity.Colonia.getNombre());
            direccionML.Colonia.setCodigoPostal(direccionEntity.Colonia.getCodigoPostal());

            direccionML.Colonia.Municipio = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Municipio();
            direccionML.Colonia.Municipio.setIdMunicipio(direccionEntity.Colonia.Municipio.getIdMunicipio());
            direccionML.Colonia.Municipio.setNombre(direccionEntity.Colonia.Municipio.getNombre());

            direccionML.Colonia.Municipio.Estado = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Estado();
            direccionML.Colonia.Municipio.Estado.setIdEstado(direccionEntity.Colonia.Municipio.Estado.getIdEstado());
            direccionML.Colonia.Municipio.Estado.setNombre(direccionEntity.Colonia.Municipio.Estado.getNombre());

            direccionML.Colonia.Municipio.Estado.Pais = new com.ejemplo.SCruzProgramacionNCapasMaven.ML.Pais();
            direccionML.Colonia.Municipio.Estado.Pais.setIdPais(direccionEntity.Colonia.Municipio.Estado.Pais.getIdPais());
            direccionML.Colonia.Municipio.Estado.Pais.setNombre(direccionEntity.Colonia.Municipio.Estado.Pais.getNombre());

            result.object = direccionML;

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result UpdateActivo(int IdUsuario, int Estatus) {
        Result result = new Result();

        try {
            Usuario usuarioJPA = entityManager.find(Usuario.class, IdUsuario);

            if (usuarioJPA != null) {
                usuarioJPA.setEstatus(Estatus);
                entityManager.merge(usuarioJPA);
                entityManager.flush();

                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con ID: " + IdUsuario;
            }
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
