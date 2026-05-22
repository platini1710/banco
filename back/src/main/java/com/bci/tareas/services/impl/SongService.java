package com.bci.tareas.services.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Service
public class SongService {
    private final StringRedisTemplate redisTemplate;

    public SongService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void guardarCancion(String id, String artista, String titulo, String anio) {
        String key = "cancion:" + id;

        // Usamos opsForHash para tratar la clave como un objeto con campos
        redisTemplate.opsForHash().put(key, "artista", artista);
        redisTemplate.opsForHash().put(key, "titulo", titulo);
        redisTemplate.opsForHash().put(key, "anio", anio);
    }
    public Map<Object, Object> obtenerCancionCompleta(String id) {
        String key = "cancion:" + id;

        // entries(key) recupera todos los campos (artista, titulo, anio) de una sola vez [cite: 112, 154]
        return redisTemplate.opsForHash().entries(key);
    }

    // Dentro de SongController...


}