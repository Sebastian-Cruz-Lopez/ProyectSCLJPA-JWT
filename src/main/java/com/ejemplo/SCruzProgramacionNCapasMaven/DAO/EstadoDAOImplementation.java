package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Estado;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EstadoDAOImplementation implements IEstadoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetEstadosByPais(int idPais) {
        Result result = new Result();

        try {
            jdbcTemplate.execute("{CALL GetEstadosByPaisSP(?,?)}", (CallableStatementCallback<Boolean>) callableStatemen -> {
                
                callableStatemen.setInt(1, idPais);
                callableStatemen.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                
                callableStatemen.execute();
                
                ResultSet resultSet = (ResultSet) callableStatemen.getObject(2);
                
                result.objects = new ArrayList<>();
                
                while (resultSet.next()) {                    
                    Estado estado = new Estado();
                    
                    estado.setIdEstado(resultSet.getInt("idEstado"));
                    estado.setNombre(resultSet.getString("Nombre"));
                    
                    result.objects.add(estado);
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

}
