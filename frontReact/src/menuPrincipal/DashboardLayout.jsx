import React, { useState } from 'react';
import Sidebar from './Sidebar';
import Default from './Default';

// Aquí importaremos los nuevos componentes que vayas creando.
import ConsultaSobregiro from '../sobreGiro/ConsultaSobregiro'; 
import SolicitarAvance from '../sobreGiro/SolicitarAvance';
import PagarSobregiro from '../sobreGiro/PagarSobregiro';

export default function DashboardLayout() {
  const [cuentaActiva, setCuentaActiva] = useState(null);
  const [vistaActual, setVistaActual] = useState('resumen'); 

  // --- EL ENRUTADOR ---
  const renderizarVista = () => {
    switch (vistaActual) {
      case 'resumen':
        return <Default cuentaActiva={cuentaActiva} cargando={!cuentaActiva} />;
      
      case 'consulta-sobregiro':
        // CORRECCIÓN: Le pasamos específicamente el idCuenta, usando '?' por seguridad
        // en caso de que cuentaActiva aún sea null.

        return <ConsultaSobregiro idCuenta={cuentaActiva?.idCuenta} />;
        
      case 'solicitar-avance':
        // Si estos componentes también necesitan solo el ID, harías lo mismo.
        // Si necesitan todo el objeto de la cuenta, lo dejas como lo tenías.
        return <SolicitarAvance cuentaActiva={cuentaActiva} />;
        
      case 'pagar-sobregiro':
        return <PagarSobregiro cuentaActiva={cuentaActiva} />;
        
      default:
        return <Default cuentaActiva={cuentaActiva} cargando={!cuentaActiva} />;
    }
  };

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: '#f1f5f9' }}>
      <div style={{ width: '280px', flexShrink: 0 }}>
        <Sidebar 
          onSeleccionarCuenta={(cuenta) => setCuentaActiva(cuenta)} 
          onCambiarVista={(nuevaVista) => setVistaActual(nuevaVista)} 
        />
      </div>
      
      <div style={{ flexGrow: 1 }}>
        {renderizarVista()}
      </div>
    </div>
  );
}