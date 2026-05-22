// Contador.test.jsx
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Contador from './Contador';

// 1. Organizamos el bloque de pruebas
describe('Componente Contador', () => {
  
  it('debería aumentar el contador cuando el usuario hace clic', () => {
    
    // PASO 1: PREPARAR (Render)
    // "Dibujamos" el componente en la memoria virtual
    render(<Contador />);

    // Buscamos el botón como lo haría un usuario (buscando el texto inicial)
    const boton = screen.getByRole('button', { name: 'Tienes 0 clics' });
    
    // Afirmamos que el botón existe en la pantalla al inicio
    expect(boton).toBeDefined();

    // PASO 2: ACTUAR (Interacción)
    // Simulamos un clic físico del usuario sobre ese botón
    fireEvent.click(boton);

    // PASO 3: AFIRMAR (Expect)
    // Buscamos si la pantalla se actualizó con el nuevo texto
    const botonActualizado = screen.getByRole('button', { name: 'Tienes 1 clics' });
    
    // Verificamos que el nuevo texto es el que está en pantalla
    expect(botonActualizado).toBeDefined();
  });
});