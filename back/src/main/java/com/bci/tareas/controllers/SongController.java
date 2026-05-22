package com.bci.tareas.controllers;

import com.bci.tareas.services.impl.SongService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/canciones") // La ruta base para este controlador
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    // Método para obtener la canción por su ID
    @GetMapping("/{id}")
    public Map<Object, Object> obtenerCancion(@PathVariable String id) {
        // Llamamos al servicio que recupera los datos en microsegundos 🏎️
        return songService.obtenerCancionCompleta(id);
    }
    @PostMapping("/guardar")
    public String guardar(@RequestParam String id,
                          @RequestParam String artista,
                          @RequestParam String titulo,
                          @RequestParam String anio) {
        // Llamamos al servicio para guardar los datos en un Hash 👤 en microsegundos [cite: 160, 164]
        songService.guardarCancion(id, artista, titulo, anio);
        return "¡Canción guardada con éxito!";
    }
}