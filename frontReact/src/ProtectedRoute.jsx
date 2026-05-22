import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
  // 1. Buscamos el token en el almacenamiento del navegador
  const token = localStorage.getItem('token');

  // 2. Si no hay token, lo redirigimos automáticamente a la pantalla de login
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  // 3. Si hay token, renderizamos el componente hijo (que será tu Dashboard)
  return children;
};

export default ProtectedRoute;