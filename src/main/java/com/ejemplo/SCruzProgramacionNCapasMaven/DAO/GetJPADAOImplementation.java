package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Colonia;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Direccion;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Estado;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Municipio;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Pais;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Roll;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Usuario;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.DireccionCP;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.UsuarioDireccion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GetJPADAOImplementation implements IGetJPADAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetRoll() {
        Result result = new Result();

        try {
            List<Roll> rolls = entityManager.createQuery("FROM Roll", Roll.class).getResultList();

            result.objects = new ArrayList<>(rolls);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetPais() {
        Result result = new Result();

        try {
            List<Pais> paises = entityManager.createQuery("FROM Pais", Pais.class).getResultList();
            result.objects = new ArrayList<>(paises);
            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetEstado(int idPais) {
        Result result = new Result();

        try {
            List<Estado> estados = entityManager.createQuery(
                    "FROM Estado e WHERE e.Pais.idPais = :idPais", Estado.class)
                    .setParameter("idPais", idPais)
                    .getResultList();

            result.objects = new ArrayList<>(estados);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetMunicipio(int idEstado) {
        Result result = new Result();

        try {

            List<Municipio> municipios = entityManager.createQuery(
                    "FROM Municipio m WHERE m.Estado.idEstado = :idEstado", Municipio.class)
                    .setParameter("idEstado", idEstado)
                    .getResultList();

            result.objects = new ArrayList<>(municipios);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetColonia(int idMunicipio) {
        Result result = new Result();

        try {

            List<Colonia> colonias = entityManager.createQuery(
                    "FROM Colonia c WHERE c.Municipio.idMunicipio = :idMunicipio", Colonia.class)
                    .setParameter("idMunicipio", idMunicipio)
                    .getResultList();

            result.objects = new ArrayList<>(colonias);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result GetDireccionByCP(String codigoPostal) {
        Result result = new Result();

        try {
            List<Object[]> datos = entityManager.createQuery(
                    "SELECT p.idPais, p.nombrePais, "
                    + "e.idEstado, e.nombreEstado, "
                    + "m.idMunicipio, m.nombreMunicipio, "
                    + "c.idColonia, c.nombreColonia, c.codigoPostal "
                    + "FROM Colonia c " 
                    + "JOIN c.Municipio m "
                    + "JOIN m.Estado e "
                    + "JOIN e.Pais p "
                    + "WHERE c.codigoPostal = :codigoPostal", Object[].class)
                    .setParameter("codigoPostal", codigoPostal)
                    .getResultList();

            List<DireccionCP> direccionesCP = new ArrayList<>();

            for (Object[] row : datos) {
                DireccionCP direccionCP = new DireccionCP();
                direccionCP.setIdPais(((Number) row[0]).intValue());
                direccionCP.setNombrePais((String) row[1]);
                direccionCP.setIdEstado(((Number) row[2]).intValue());
                direccionCP.setNombreEstado((String) row[3]);
                direccionCP.setIdMunicipio(((Number) row[4]).intValue());
                direccionCP.setNombreMunicipio((String) row[5]);
                direccionCP.setIdColonia(((Number) row[6]).intValue());
                direccionCP.setNombreColonia((String) row[7]);
                direccionCP.setCodigoPostal((String) row[8]);

                direccionesCP.add(direccionCP);
            }

            if (!direccionesCP.isEmpty()) {
                result.correct = true;
                result.objects = new ArrayList<>(direccionesCP);
            } else {
                result.correct = false;
                result.errorMessage = "No se encontró información para el código postal: " + codigoPostal;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;

    }
}
