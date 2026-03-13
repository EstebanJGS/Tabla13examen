package com.examen.tabla13.controllers; // Minúscula

import com.examen.tabla13.models.Hospital; // Minúscula
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/entidades")
public class HospitalController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //  Mostrar los registros
    @GetMapping
    public String listarHospitales(Model model) {
        String sql = "SELECT * FROM hospitales";
        List<Hospital> hospitales = jdbcTemplate.query(sql, new RowMapper<Hospital>() {
            @Override
            public Hospital mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Hospital(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getInt("capacidad")
                );
            }
        });
        model.addAttribute("hospitales", hospitales);
        return "lista"; 
    }

    // formulario
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("hospital", new Hospital());
        return "crear";
    }

    // guardado 
    @PostMapping("/guardar")
    public String guardarHospital(Hospital hospital) {
        String sql = "INSERT INTO hospitales (id, nombre, direccion, telefono, capacidad) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, hospital.getId(), hospital.getNombre(), hospital.getDireccion(), hospital.getTelefono(), hospital.getCapacidad());
        return "redirect:/entidades";
    }

    // 4. Mostrar formulario de edición con datos cargados
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") int id, Model model) {
        String sql = "SELECT * FROM hospitales WHERE id = ?";
        Hospital hospital = jdbcTemplate.queryForObject(sql, new RowMapper<Hospital>() {
            @Override
            public Hospital mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Hospital(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getInt("capacidad")
                );
            }
        }, id);
        model.addAttribute("hospital", hospital);
        return "editar";
    }

    // 5. Procesar la actualización en la BD
    @PostMapping("/actualizar")
    public String actualizarHospital(Hospital hospital) {
        String sql = "UPDATE hospitales SET nombre = ?, direccion = ?, telefono = ?, capacidad = ? WHERE id = ?";
        jdbcTemplate.update(sql, hospital.getNombre(), hospital.getDireccion(), hospital.getTelefono(), hospital.getCapacidad(), hospital.getId());
        return "redirect:/entidades";
    }

    // 6. Mostrar formulario de confirmación para eliminar
    @GetMapping("/eliminar/{id}")
    public String mostrarFormularioEliminar(@PathVariable("id") int id, Model model) {
        String sql = "SELECT * FROM hospitales WHERE id = ?";
        Hospital hospital = jdbcTemplate.queryForObject(sql, new RowMapper<Hospital>() {
            @Override
            public Hospital mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Hospital(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getInt("capacidad")
                );
            }
        }, id);
        model.addAttribute("hospital", hospital);
        return "eliminar";
    }

    // 7. Procesar el borrado en la BD
    @PostMapping("/borrar")
    public String borrarHospital(Hospital hospital) {
        String sql = "DELETE FROM hospitales WHERE id = ?";
        jdbcTemplate.update(sql, hospital.getId());
        return "redirect:/entidades";
    }
}