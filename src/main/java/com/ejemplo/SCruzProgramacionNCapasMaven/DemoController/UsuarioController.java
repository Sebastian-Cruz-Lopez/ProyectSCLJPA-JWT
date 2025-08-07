package com.ejemplo.SCruzProgramacionNCapasMaven.DemoController;

import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.ColoniaDAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.DireccionDAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.EstadoDAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.GetJPADAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.MunicipioDAOImplemeentation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.PaisDAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.RollDAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.UsuarioDAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.UsuarioJPADAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Colonia;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Direccion;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Result;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.ResultValidaDatos;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Roll;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.Usuario;
import com.ejemplo.SCruzProgramacionNCapasMaven.ML.UsuarioDireccion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;

    @Autowired
    private PaisDAOImplementation paisDAOImplementation;

    @Autowired
    private EstadoDAOImplementation estadoDAOImplementation;

    @Autowired
    private RollDAOImplementation rollDAOImplementation;

    @Autowired
    private MunicipioDAOImplemeentation municipioDAOImplementation;

    @Autowired
    private ColoniaDAOImplementation coloniaDAOImplementation;

    @Autowired
    private DireccionDAOImplementation direccionDAOImplementation;

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private GetJPADAOImplementation getJPADAOImplementation;

    @GetMapping
    public String Index(Model model) {

        Result result = usuarioJPADAOImplementation.GetAll();

        if (result.correct) {
            model.addAttribute("usuarioDireccion", result.objects);
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("rolls", getJPADAOImplementation.GetRoll().objects);
        }

        return "UsuarioIndex";
    }

    @GetMapping("/login")
    public String Login() {
        return "UsuarioLogin";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "UsuarioAcceso";
    }

    @RequestMapping
    public String Index(@ModelAttribute Usuario usuario, Model model) {
        Result result = usuarioDAOImplementation.GetAllDinamic(usuario);
        model.addAttribute("usuario", new Usuario());

        model.addAttribute("usuarioDireccion", result.objects);
        model.addAttribute("rolls", getJPADAOImplementation.GetRoll().objects);

        return "UsuarioIndex";
    }

    @GetMapping("form/{idUsuario}") //AQUI SE PREPARA LA VISTA DEL FORMULARIO
    public String Accion(Model model, @PathVariable int idUsuario) {

        if (idUsuario < 1) {
            // Vista para agregar usuario nuevo
            model.addAttribute("paises", getJPADAOImplementation.GetPais().objects);
            model.addAttribute("rolls", getJPADAOImplementation.GetRoll().objects);
            model.addAttribute("usuarioDireccion", new UsuarioDireccion());
            return "UsuarioForm";
        } else {
            model.addAttribute("usuarioDireccion", usuarioJPADAOImplementation.GetByid(idUsuario).object);

            return "UsuarioDetail";
        }

    }

    @GetMapping("/formeditable")
    public String AccionEditable(@RequestParam int idUsuario, @RequestParam(required = false) Integer idDireccion, Model model) {

        if (idDireccion == null) { //EDITAR USUARIO

            UsuarioDireccion usuarioDireccion = new UsuarioDireccion();

            usuarioDireccion = (UsuarioDireccion) usuarioJPADAOImplementation.GetByid(idUsuario).object;
            usuarioDireccion.Direccion = new Direccion();
            usuarioDireccion.Direccion.setIdDireccion(-1);
            model.addAttribute("usuarioDireccion", usuarioDireccion);

            //model.addAttribute("usuarioDireccion", usuarioDireccion);
            model.addAttribute("rolls", getJPADAOImplementation.GetRoll().objects);

        } else if (idDireccion == 0) { //AGREGAR DIRECCION

            UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
            usuarioDireccion.Usuario = new Usuario();
            usuarioDireccion.Usuario.setIdUsuario(idUsuario);
            usuarioDireccion.Direccion = new Direccion();
            usuarioDireccion.Direccion.setIdDireccion(0);
            model.addAttribute("usuarioDireccion", usuarioDireccion);
            model.addAttribute("paises", getJPADAOImplementation.GetPais().objects);

        } else { //EDITAR DIRECCION
            UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
            usuarioDireccion.Direccion = new Direccion();
            usuarioDireccion.Direccion = (Direccion) usuarioJPADAOImplementation.GetDireccionByid(idDireccion).object;
            usuarioDireccion.Usuario = new Usuario();
            usuarioDireccion.Usuario.setIdUsuario(idUsuario);
            //usuarioDireccion.Direccion = direccion;
            model.addAttribute("usuarioDireccion", usuarioDireccion);
            //model.addAttribute("direccion", direccion);
            model.addAttribute("paises", getJPADAOImplementation.GetPais().objects);
            model.addAttribute("estados", getJPADAOImplementation.GetEstado(usuarioDireccion.Direccion.Colonia.Municipio.Estado.getIdEstado()).objects);
            model.addAttribute("municipios", getJPADAOImplementation.GetMunicipio(usuarioDireccion.Direccion.Colonia.Municipio.getIdMunicipio()).objects);
            model.addAttribute("colonias", getJPADAOImplementation.GetColonia(usuarioDireccion.Direccion.Colonia.getIdColonia()).objects);

        }
        return "UsuarioForm";
    }

    @PostMapping("form") //AQUI SE RECUPERA LOS DATOS DEL FORMULARIO
    public String Accion(@Valid @ModelAttribute UsuarioDireccion usuarioDireccion, BindingResult bindingResult, @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile, @RequestParam(value = "estatus", required = false, defaultValue = "0") Integer estatus,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarioDireccion", usuarioDireccion);
            return "UsuarioForm";
        }

        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                byte[] bytes = imagenFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                usuarioDireccion.Usuario.setImagen(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (usuarioDireccion.Usuario == null) {
            usuarioDireccion.Usuario = new Usuario();
        }
        usuarioDireccion.Usuario.setEstatus(estatus);

        Result result = new Result();
        if (usuarioDireccion.Usuario.getIdUsuario() == 0) { // agregar usuario
            result = usuarioJPADAOImplementation.Add(usuarioDireccion);
        } else {
            if (usuarioDireccion.Direccion.getIdDireccion() == 0) { //agregar direccion
                result = usuarioJPADAOImplementation.AddDireccion(usuarioDireccion);

            } else if (usuarioDireccion.Direccion.getIdDireccion() == -1) { //editar usuario
                result = usuarioJPADAOImplementation.UpdateUsario(usuarioDireccion);
            } else {
                result = usuarioJPADAOImplementation.UpdateDireccion(usuarioDireccion); //editar direccion
            }
        }

        if (result.correct) {
            return "redirect:/usuario"; //SE REDIRECCIONA A LA VISTA DONDE ESTA EL GETALL
        } else {
            return "/usuario";
        }

    }

    @GetMapping("cargamasiva")
    public String CargaMasiva() {
        return "cargamasiva";
    }

    @PostMapping("cargamasiva")
    public String cargaMasiva(@RequestParam MultipartFile archivo, Model model, HttpSession session) throws IOException {

        if (archivo != null && !archivo.isEmpty()) {
            String originalFilename = archivo.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();

            String root = System.getProperty("user.dir");
            String path = "src/main/resources/archivos";
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String absolutePath = root + "/" + path + "/" + fecha + originalFilename;

            // Guardar archivo en disco
            File destFile = new File(absolutePath);
            archivo.transferTo(destFile);

            List<UsuarioDireccion> usuariosDireccion = new ArrayList<>();

            if (fileExtension.equals("txt")) {
                usuariosDireccion = LecturaArchivoTXT(archivo);
            } else if (fileExtension.equals("xlsx")) {
                // Convertimos archivo guardado en MultipartFile para usar método
                MultipartFile multipartFile = convertFileToMultipartFile(destFile);
                usuariosDireccion = LecturaArchivoXLSX(multipartFile);
            } else {
                model.addAttribute("archivoCorrecto", false);
                model.addAttribute("errorMensaje", "Tipo de archivo no soportado");
                return "cargamasiva";
            }

            List<ResultValidaDatos> listaErrores = ValidarDatos(usuariosDireccion);

            if (listaErrores.isEmpty()) {
                session.setAttribute("path", absolutePath);
                model.addAttribute("archivoCorrecto", true);
                return "redirect:/usuario/cargamasiva/Procesar";
            } else {
                model.addAttribute("listaErrores", listaErrores);
                model.addAttribute("hayErrores", true);
                model.addAttribute("archivoCorrecto", false);
            }
        } else {
            model.addAttribute("archivoCorrecto", false);
            model.addAttribute("errorMensaje", "Archivo vacío o inválido");
        }
        return "cargamasiva";
    }

    @GetMapping("cargamasiva/Procesar")
    public String ProcesarCargaMasiva(HttpSession session, Model model) throws IOException {

        String ruta = (String) session.getAttribute("path");
        if (ruta != null && !ruta.isEmpty()) {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                session.removeAttribute("path");
                model.addAttribute("archivoCorrecto", false);
                model.addAttribute("errorMensaje", "Archivo no encontrado en el servidor");
                return "cargamasiva";
            }

            MultipartFile multipartFile = convertFileToMultipartFile(archivo);
            String fileExtension = archivo.getName().substring(archivo.getName().lastIndexOf('.') + 1).toLowerCase();

            List<UsuarioDireccion> usuariosDireccion;
            if (fileExtension.equals("txt")) {
                usuariosDireccion = LecturaArchivoTXT(multipartFile);
            } else if (fileExtension.equals("xlsx")) {
                usuariosDireccion = LecturaArchivoXLSX(multipartFile);
            } else {
                model.addAttribute("archivoCorrecto", false);
                model.addAttribute("errorMensaje", "Tipo de archivo no soportado");
                return "cargamasiva";
            }

            List<ResultValidaDatos> listaErrores = ValidarDatos(usuariosDireccion);

            if (listaErrores.isEmpty()) {
                for (UsuarioDireccion usuarioDireccion : usuariosDireccion) {
                    usuarioJPADAOImplementation.Add(usuarioDireccion);
                }
                session.removeAttribute("path");
                model.addAttribute("cargaExitosa", true);
                return "redirect:/usuario";
            } else {
                model.addAttribute("listaErrores", listaErrores);
                model.addAttribute("hayErrores", true);
                model.addAttribute("archivoCorrecto", false);
                session.removeAttribute("path");
                return "cargamasiva";
            }
        }
        return "redirect:/usuario/cargamasiva";
    }

    public List<UsuarioDireccion> LecturaArchivoXLSX(MultipartFile archivo) {
        List<UsuarioDireccion> listaUsuarios = new ArrayList<>();

        try (InputStream inputStream = archivo.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet primeraHoja = workbook.getSheetAt(0);
            Iterator<Row> filas = primeraHoja.iterator();

            boolean primeraFila = true;

            while (filas.hasNext()) {
                Row filaActual = filas.next();

                if (primeraFila) { // Salta encabezado
                    primeraFila = false;
                    continue;
                }

                UsuarioDireccion usuarioDireccion = new UsuarioDireccion();
                Usuario usuario = new Usuario();
                Direccion direccion = new Direccion();
                Roll roll = new Roll();

                // Usuario
                usuario.setNombre(getStringValue(filaActual.getCell(0)));
                usuario.setApellidoPaterno(getStringValue(filaActual.getCell(1)));
                usuario.setApellidoMaterno(getStringValue(filaActual.getCell(2)));

                if (filaActual.getCell(3) != null && DateUtil.isCellDateFormatted(filaActual.getCell(3))) {
                    usuario.setFechaNacimiento(filaActual.getCell(3).getDateCellValue());
                } else {
                    usuario.setFechaNacimiento(null);
                }

                usuario.setTelefono(getStringValue(filaActual.getCell(4)));
                usuario.setEmail(getStringValue(filaActual.getCell(5)));
                usuario.setUsername(getStringValue(filaActual.getCell(6)));
                usuario.setPassword(getStringValue(filaActual.getCell(7)));

                String sexo = getStringValue(filaActual.getCell(8));
                if (sexo != null && !sexo.isEmpty()) {
                    usuario.setSexo(sexo.charAt(0));
                }

                usuario.setCelular(getStringValue(filaActual.getCell(9)));
                usuario.setCURP(getStringValue(filaActual.getCell(10)));

                // Roll
                Cell rollCell = filaActual.getCell(11);
                if (rollCell != null && rollCell.getCellType() == CellType.NUMERIC) {
                    roll.setIdRoll((int) rollCell.getNumericCellValue());
                    usuario.setRoll(roll);
                }

                usuario.setImagen(getStringValue(filaActual.getCell(12)));

                // Estatus (agregado)
                Cell estatusCell = filaActual.getCell(13);
                if (estatusCell != null && estatusCell.getCellType() == CellType.NUMERIC) {
                    usuario.setEstatus((int) estatusCell.getNumericCellValue());
                } else {
                    usuario.setEstatus(0);
                }

                // Dirección
                direccion.setCalle(getStringValue(filaActual.getCell(14)));
                direccion.setNumeroInterior(getStringValue(filaActual.getCell(15)));
                direccion.setNumeroExterior(getStringValue(filaActual.getCell(16)));

                Cell idColoniaCell = filaActual.getCell(17);
                if (idColoniaCell != null && idColoniaCell.getCellType() == CellType.NUMERIC) {
                    Colonia colonia = new Colonia();
                    colonia.setIdColonia((int) idColoniaCell.getNumericCellValue());
                    direccion.setColonia(colonia);
                }

                usuarioDireccion.setUsuario(usuario);
                usuarioDireccion.setDireccion(direccion);

                listaUsuarios.add(usuarioDireccion);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listaUsuarios;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                    } else {
                        double num = cell.getNumericCellValue();
                        return (num % 1 == 0) ? String.valueOf((long) num) : String.valueOf(num);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.toString().trim();
                case BLANK:
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public List<UsuarioDireccion> LecturaArchivoTXT(MultipartFile archivo) {
        List<UsuarioDireccion> usuariosDireccion = new ArrayList<>();
        try (InputStream inputStream = archivo.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            bufferedReader.readLine(); // saltar encabezado
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                if (linea.startsWith("|")) {
                    linea = linea.substring(1);
                }
                String[] datos = linea.split("\\|");

                String[] completados = Arrays.copyOf(datos, 18);
                for (int i = 0; i < completados.length; i++) {
                    if (completados[i] == null) {
                        completados[i] = "";
                    }
                }

                UsuarioDireccion ud = new UsuarioDireccion();
                Usuario u = new Usuario();
                Direccion d = new Direccion();
                Colonia c = new Colonia();
                Roll roll = new Roll();

                u.setNombre(completados[0].trim());
                u.setApellidoPaterno(completados[1].trim());
                u.setApellidoMaterno(completados[2].trim());
                u.setFechaNacimiento(parseFechaSegura(completados[3]));
                u.setTelefono(completados[4].trim());
                u.setEmail(completados[5].trim());
                u.setUsername(completados[6].trim());
                u.setPassword(completados[7].trim());
                u.setSexo(!completados[8].isEmpty() ? completados[8].charAt(0) : 'X');
                u.setCelular(completados[9].trim());
                u.setCURP(completados[10].trim());

                try {
                    roll.setIdRoll(Integer.parseInt(completados[11].trim()));
                } catch (NumberFormatException e) {
                    roll.setIdRoll(0);
                }
                u.setRoll(roll);

                u.setImagen(completados[12].trim());
                try {
                    u.setEstatus(Integer.parseInt(completados[13].trim()));
                } catch (NumberFormatException e) {
                    u.setEstatus(-1);
                }

                d.setCalle(completados[14].trim());
                d.setNumeroInterior(completados[15].trim());
                d.setNumeroExterior(completados[16].trim());
                try {
                    c.setIdColonia(Integer.parseInt(completados[17].trim()));
                } catch (NumberFormatException e) {
                    c.setIdColonia(0);
                }
                d.setColonia(c);

                ud.setUsuario(u);
                ud.setDireccion(d);

                usuariosDireccion.add(ud);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return usuariosDireccion;
    }

    private Date parseFechaSegura(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        formato.setLenient(false);
        try {
            return formato.parse(fecha.trim());
        } catch (ParseException e) {
            return null;
        }
    }

    private List<ResultValidaDatos> ValidarDatos(List<UsuarioDireccion> usuarios) {
        List<ResultValidaDatos> listaErrores = new ArrayList<>();
        int fila = 1;

        if (usuarios == null) {
            listaErrores.add(new ResultValidaDatos(0, "Archivo", "No se pudo leer el archivo"));
            return listaErrores;
        }
        for (UsuarioDireccion ud : usuarios) {
            Usuario u = ud.getUsuario();
            Direccion d = ud.getDireccion();

            if (u.getNombre() == null || u.getNombre().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Nombre", "Campo obligatorio"));
            }

            if (u.getApellidoPaterno() == null || u.getApellidoPaterno().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Apellido Paterno", "Campo obligatorio"));
            }

            if (u.getApellidoMaterno() == null || u.getApellidoMaterno().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Apellido Materno", "Campo obligatorio"));
            }

            if (u.getFechaNacimiento() == null) {
                listaErrores.add(new ResultValidaDatos(fila, "Fecha Nacimiento", "Campo obligatorio o formato inválido"));
            }

            if (!isValidPhoneNumber(u.getTelefono())) {
                listaErrores.add(new ResultValidaDatos(fila, "Teléfono", "Debe contener 10 dígitos numéricos"));
            }

            if (u.getEmail() == null || u.getEmail().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Email", "Campo obligatorio"));
            } else if (!isValidEmail(u.getEmail())) {
                listaErrores.add(new ResultValidaDatos(fila, "Email", "Formato inválido"));
            }

            if (u.getUsername() == null || u.getUsername().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Username", "Campo obligatorio"));
            }

            if (u.getPassword() == null || u.getPassword().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Password", "Campo obligatorio"));
            } else if (u.getPassword().length() < 6) {
                listaErrores.add(new ResultValidaDatos(fila, "Password", "Debe tener al menos 6 caracteres"));
            }

            if (u.getSexo() != 'H' && u.getSexo() != 'M') {
                listaErrores.add(new ResultValidaDatos(fila, "Sexo", "Debe ser 'H' o 'M'"));
            }

            if (!isValidPhoneNumber(u.getCelular())) {
                listaErrores.add(new ResultValidaDatos(fila, "Celular", "Debe contener 10 dígitos numéricos"));
            }

            if (u.getCURP() == null || u.getCURP().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "CURP", "Campo obligatorio"));
            } else if (!isValidCURP(u.getCURP())) {
                listaErrores.add(new ResultValidaDatos(fila, "CURP", "Formato inválido"));
            }

            if (u.getImagen() == null || u.getImagen().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Imagen", "Campo obligatorio"));
            }

            if (u.getEstatus() != 1 && u.getEstatus() != 0) {
                listaErrores.add(new ResultValidaDatos(fila, "Estatus", "Debe ser 1 o 0"));
            }

            if (d.getCalle() == null || d.getCalle().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Calle", "Campo obligatorio"));
            }

            if (d.getNumeroInterior() == null || d.getNumeroInterior().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Número Interior", "Campo obligatorio"));
            }

            if (d.getNumeroExterior() == null || d.getNumeroExterior().isEmpty()) {
                listaErrores.add(new ResultValidaDatos(fila, "Número Exterior", "Campo obligatorio"));
            }

            if (d.getColonia() == null || d.getColonia().getIdColonia() < 1) {
                listaErrores.add(new ResultValidaDatos(fila, "Colonia", "ID de colonia inválido"));
            }

            fila++;
        }
        return listaErrores;
    }

    public static MultipartFile convertFileToMultipartFile(File file) throws IOException {
        return convertFileToMultipartFile(file, "file");
    }

    public static MultipartFile convertFileToMultipartFile(File file, String paramName) throws IOException {
        String contentType = Files.probeContentType(file.toPath());
        try (FileInputStream input = new FileInputStream(file)) {
            return new MockMultipartFile(
                    paramName,
                    file.getName(),
                    contentType,
                    input
            );
        }
    }

    public boolean isValidPhoneNumber(String phone) {
        if (phone == null) {
            return false;
        }
        return phone.matches("\\d{10}");
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    public boolean isValidCURP(String curp) {
        if (curp == null) {
            return false;
        }
        String regex = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}[A-Z\\d]{2,3}$";
        return curp.toUpperCase().matches(regex);
    }

    @GetMapping("/GetEstadosByPais/{idPais}")
    @ResponseBody
    public Result GetEstadosByPais(@PathVariable("idPais") int idPais) {

        return getJPADAOImplementation.GetEstado(idPais);

    }

    @GetMapping("/GetMunicipiosByEstado/{idEstado}")
    @ResponseBody
    public Result GetMunicipiosByEstado(@PathVariable("idEstado") int idEstado) {

        return getJPADAOImplementation.GetMunicipio(idEstado);
    }

    @GetMapping("/GetColoniasByMunicipio/{idMunicipio}")
    @ResponseBody
    public Result GetColoniasByMunicipio(@PathVariable("idMunicipio") int idMunicipio) {

        return getJPADAOImplementation.GetColonia(idMunicipio);
    }

    @GetMapping("/delete/{idUsuario}")
    public String DeleteUsuario(@PathVariable int idUsuario, RedirectAttributes redirectAttrs) {
        Result result = usuarioJPADAOImplementation.DeleteUsuario(idUsuario); // eliminación física

        if (result.correct) {
            redirectAttrs.addFlashAttribute("mensaje", "Usuario eliminado exitosamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar el usuario.");
        }

        return "redirect:/usuario";
    }

    @GetMapping("/deletedireccion")
    public String DeleteDireccion(@RequestParam int idDireccion, @RequestParam int idUsuario, RedirectAttributes redirectAttrs) {
        Result result = usuarioJPADAOImplementation.DeleteDireccion(idDireccion); // eliminación física

        if (result.correct) {
            redirectAttrs.addFlashAttribute("mensaje", "Dirección eliminada correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar la dirección.");
        }

        return "redirect:/usuario/form/" + idUsuario;
    }

    @PostMapping("/Activo")
    @ResponseBody
    public Result ActivoUsuario(@RequestParam int IdUsuario, @RequestParam int Estatus) {
        return usuarioJPADAOImplementation.UpdateActivo(IdUsuario, Estatus);
    }

    @GetMapping("/GetDireccionByCP/{cp}")
    @ResponseBody
    public Result GetDireccionByCP(@PathVariable String cp) {
        return getJPADAOImplementation.GetDireccionByCP(cp);
    }

    @GetMapping("/usuario/check-session")
    @ResponseBody
    public ResponseEntity<String> checkSession(HttpSession session) {
        if (session == null || session.getAttribute("SPRING_SECURITY_CONTEXT") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesión caducada");
        }
        return ResponseEntity.ok("Sesión activa");
    }

}
