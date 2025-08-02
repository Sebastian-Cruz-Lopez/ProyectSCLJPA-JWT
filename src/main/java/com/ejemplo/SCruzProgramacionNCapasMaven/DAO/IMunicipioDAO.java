
package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;


public interface IMunicipioDAO {
    
    Result GetMunicipiosByEstado(int idEstado);
    
}
