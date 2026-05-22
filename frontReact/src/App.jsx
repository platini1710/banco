import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'; 
import LoginPage from './LoginPage'; 
import ProtectedRoute from './ProtectedRoute'; 
import DashboardLayout from './menuPrincipal/DashboardLayout';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* NUEVO: Si entran a la raíz pura "/", los mandamos al login */}
        <Route path="/" element={<Navigate to="/login" replace />} />

        {/* 1. Rutas Públicas */}
        <Route path="/login" element={<LoginPage />} />

        {/* 2. Rutas Protegidas */}
        <Route 
          path="/dashboard/*" 
          element={
            <ProtectedRoute>
              <DashboardLayout />
            </ProtectedRoute>
          } 
        />

        {/* 3. Comodín: Si escriben cualquier otra cosa, al login por seguridad */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
