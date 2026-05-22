import React from 'react';

export default function Default({ cuentaActiva, cargando }) {
  
  // Formateador automático para Pesos Chilenos (CLP)
  const formatearDinero = (monto) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0
    }).format(monto);
  };

  return (
    <div style={mainContentStyle}>
      <div style={cardStyle}>
        <h3 style={sectionTitleStyle}>Resumen de Cuenta Corriente</h3>
        
        <div style={balanceGrid}>
          <div style={balanceCardPrimary}>
            <span style={balanceLabel}>Saldo Disponible</span>
            <span style={balanceValueMain}>
              {cargando ? 'Cargando...' : 
               cuentaActiva ? formatearDinero(cuentaActiva.saldoActual) : '$ 0'}
            </span>
          </div>
          
          <div style={balanceCardSecondary}>
            <span style={balanceLabel}>Saldo Contable</span>
            <span style={balanceValue}>
              {cargando ? '...' : 
               cuentaActiva ? formatearDinero(cuentaActiva.saldoActual) : '$ 0'}
            </span>
          </div>
          
          <div style={balanceCardSecondary}>
            <span style={balanceLabel}>Sobregiro Disponible</span>
            <span style={balanceValue}>{formatearDinero(0)}</span>
          </div>
          
          <div style={balanceCardSecondary}>
            <span style={balanceLabel}>Sobregiro Utilizado</span>
            <span style={balanceValue}>{formatearDinero(0)}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

// --- ESTILOS ---
const mainContentStyle = {
  padding: '40px',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'flex-start',
  width: '100%',
};

const cardStyle = {
  backgroundColor: '#ffffff',
  width: '100%',
  maxWidth: '900px',
  borderRadius: '12px',
  boxShadow: '0 8px 24px rgba(0, 0, 0, 0.08)',
  padding: '40px'
};

const sectionTitleStyle = {
  color: '#333333',
  fontSize: '22px',
  borderBottom: '2px solid #f0f0f0',
  paddingBottom: '15px',
  marginBottom: '30px',
  marginTop: 0
};

const balanceGrid = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
  gap: '20px',
};

const balanceCardPrimary = {
  backgroundColor: '#f8fafc',
  borderLeft: '4px solid #009038', 
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
  color: '#002464',
  fontWeight: '700'
};

const balanceValue = {
  fontSize: '20px',
  color: '#334155',
  fontWeight: '600'
};