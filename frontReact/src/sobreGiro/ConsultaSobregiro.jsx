import React from 'react';
import axios from 'axios';
import { useQuery } from '@tanstack/react-query';

export default function ConsultaSobregiro({ idCuenta }) {

  // Consumo del servicio usando TanStack Query
  const { data: datosCuenta, isLoading, isError, error } = useQuery({
    queryKey: ['sobregiro-cuenta', idCuenta],
    queryFn: async () => {
      // 1. Obtenemos el token guardado tras el login
      const token = localStorage.getItem('token'); 
      
      console.log("Consultando idCuenta ::", idCuenta);

      // 2. Enviamos la petición con la cabecera de Autorización
      const respuesta = await axios.get(`http://localhost:8083/sobregiro/cuenta/usuario/${idCuenta}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      
      return respuesta.data;
    },
    // Regla de Oro: Solo ejecuta la API si hay un ID de cuenta seleccionado
    enabled: !!idCuenta,
    retry: false
  });

  // Vista inicial obligatoria si no se ha escogido cuenta en el combo
  if (!idCuenta) {  
    return (
      <div style={mainContentStyle}>
        <div style={infoBoxStyle}>
          <p style={{ margin: 0, color: '#64748b', fontWeight: '500' }}>
            Por favor, seleccione una cuenta corriente en el menú izquierdo para visualizar el resumen de sobregiro.
          </p>
        </div>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div style={mainContentStyle}>
        <p style={{ color: '#002464', fontWeight: '600' }}>Actualizando saldos financieros...</p>
      </div>
    );
  }

  if (isError) {
    return (
      <div style={mainContentStyle}>
        <div style={errorBannerStyle}>
          <strong>Error de consulta:</strong> {error.response?.data?.message || error.message}
        </div>
      </div>
    );
  }

  return (
    <div style={mainContentStyle}>
      <div style={cardStyle}>
        <h2 style={sectionTitleStyle}>Resumen de Cuenta Corriente: {idCuenta}</h2>
        
        {/* Grilla flexible para organizar las tarjetas de saldos */}
        <div style={balanceGrid}>
          
          {/* Tarjeta Principal: Saldo Disponible */}
          <div style={balanceCardPrimary}>
            <span style={balanceLabel}>Saldo Disponible</span>
            <span style={balanceValueMain}>
              ${datosCuenta?.saldoDisponible?.toLocaleString('es-CL') || '0'}
            </span>
          </div>

          {/* Tarjeta: Sobregiro Utilizado */}
          <div style={balanceCardSecondary}>
            <span style={balanceLabel}>Sobregiro Utilizado</span>
            <span style={balanceValue}>
              ${datosCuenta?.sobregiroUtilizado?.toLocaleString('es-CL') || '0'}
            </span>
          </div>

          {/* Tarjeta: Monto Aprobado */}
          <div style={balanceCardSecondary}>
            <span style={balanceLabel}>Monto Aprobado</span>
            <span style={balanceValue}>
              ${datosCuenta?.montoAprobado?.toLocaleString('es-CL') || '0'}
            </span>
          </div>

        </div>
      </div>
    </div>
  );
}

// --- ESTILOS CORPORATIVOS ---
const mainContentStyle = {
  padding: '40px',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'flex-start',
  width: '100%',
  boxSizing: 'border-box'
};

const cardStyle = {
  backgroundColor: '#ffffff',
  width: '100%',
  maxWidth: '900px',
  borderRadius: '12px',
  boxShadow: '0 8px 24px rgba(0, 0, 0, 0.04)',
  padding: '40px',
  border: '1px solid #f1f5f9'
};

const sectionTitleStyle = {
  color: '#1e293b',
  fontSize: '22px',
  fontWeight: '700',
  borderBottom: '2px solid #f1f5f9',
  paddingBottom: '15px',
  marginBottom: '30px',
  marginTop: 0
};

const balanceGrid = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
  gap: '20px'
};

const balanceCardPrimary = {
  backgroundColor: '#f8fafc',
  borderLeft: '4px solid #009038', // Verde Bci Corporativo
  padding: '20px',
  borderRadius: '8px',
  display: 'flex',
  flexDirection: 'column'
};

const balanceCardSecondary = {
  backgroundColor: '#f8fafc',
  borderLeft: '4px solid #cbd5e1',
  padding: '20px',
  borderRadius: '8px',
  display: 'flex',
  flexDirection: 'column'
};

const balanceLabel = {
  fontSize: '14px',
  color: '#64748b',
  marginBottom: '8px',
  fontWeight: '500'
};

const balanceValueMain = {
  fontSize: '28px',
  color: '#002464', // Azul oscuro bancario
  fontWeight: '700'
};

const balanceValue = {
  fontSize: '22px',
  color: '#334155',
  fontWeight: '600'
};

const infoBoxStyle = {
  backgroundColor: '#f0f4f8',
  borderLeft: '4px solid #002464',
  padding: '20px',
  borderRadius: '8px',
  maxWidth: '600px',
  textAlign: 'center'
};

const errorBannerStyle = {
  backgroundColor: '#fef2f2',
  color: '#991b1b',
  padding: '15px 20px',
  borderRadius: '8px',
  border: '1px solid #fee2e2',
  width: '100%',
  maxWidth: '900px'
};