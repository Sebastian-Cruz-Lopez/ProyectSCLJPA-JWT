
package com.ejemplo.SCruzProgramacionNCapasMaven.DemoController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demouser")
public class TablaController {
    
    @GetMapping("tablausuario/{usuario}")
    
    public String Usuario(Model model, @PathVariable String usuario){
        
        model.addAttribute("Tabla", usuario);
        return "Tabla";
    }
    
    //http://localhost:8080/demouser/tablausuario/Tabla
    
}
