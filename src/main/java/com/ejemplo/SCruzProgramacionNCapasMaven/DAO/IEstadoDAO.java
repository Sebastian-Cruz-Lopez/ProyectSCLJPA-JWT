
package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;


public interface IEstadoDAO {
    
    Result GetEstadosByPais(int idPais);
    
}
