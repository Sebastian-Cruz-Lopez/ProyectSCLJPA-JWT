package com.ejemplo.SCruzProgramacionNCapasMaven.DAO;

import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Usuario;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;

public interface IGetJPADAO {

    Result GetRoll();

    Result GetPais();

    Result GetEstado(int idPais);

    Result GetMunicipio(int idEstado);

    Result GetColonia(int idMunicipio);

    Result GetDireccionByCP(String codigoPostal);
}
