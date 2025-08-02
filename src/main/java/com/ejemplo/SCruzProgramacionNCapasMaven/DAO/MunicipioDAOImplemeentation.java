package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Municipio;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioDAOImplemeentation implements IMunicipioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetMunicipiosByEstado(int idEstado) {
        Result result = new Result();
        
        try {
            
            jdbcTemplate.execute("{CALL GetMunicipiosByEstadoSP(?,?)}", (CallableStatementCallback<Boolean>) callableStattement ->{
               
                callableStattement.setInt(1, idEstado);
                
                callableStattement.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                
                callableStattement.execute();
                
                ResultSet resultSet = (ResultSet) callableStattement.getObject(2);
                
                result.objects = new ArrayList<>();
                
                while (resultSet.next()) {                    
                    Municipio municipio = new Municipio();
                    
                    municipio.setIdMunicipio(resultSet.getInt("idMunicipio"));
                    municipio.setNombre(resultSet.getString("Nombre"));
                    
                    result.objects.add(municipio);
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
