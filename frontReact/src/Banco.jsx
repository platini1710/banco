import React, { useState } from 'react';

export default function DashboardBci() {
  // Estado local para manejar si el combo de cuentas está visible o no
  const [mostrarCuentas, setMostrarCuentas] = useState(false);
  // Estado para la cuenta seleccionada en el combo
  const [cuentaSeleccionada, setCuentaSeleccionada] = useState("111222333");

  return (
    <div style={dashboardContainer}>
      
      {/* Menú Lateral (Sidebar) */}
      <div style={sidebarStyle}>
        <div style={brandContainer}>
          <h2 style={brandStyle}>Mi Banco</h2>
        </div>
        
        <ul style={menuListStyle}>
          {/* Opción Interactiva: Mi Cuenta */}
          <li style={menuItemStyle}>
            <div 
              onClick={() => setMostrarCuentas(!mostrarCuentas)} 
              style={menuItemInteractive}
            >
              Mi Cuenta {mostrarCuentas ? '▲' : '▼'}
            </div>
            
            {/* Renderizado Condicional del Combo de Cuentas */}
            {mostrarCuentas && (
              <div style={comboContainer}>
                <label style={labelStyle}>Seleccione su cuenta:</label>
                <select 
                  style={selectStyle}
                  value={cuentaSeleccionada}
                  onChange={(e) => setCuentaSeleccionada(e.target.value)}
                >
                  <option value="111222333">Cta. Cte. 111-222-333</option>
                  <option value="444555666">Cta. Cte. 444-555-666</option>
                </select>
              </div>
            )}
          </li>
          
          <li style={menuItemStyle}><a href="#sobregiros" style={menuLinkStyle}>Línea de sobre Giros</a></li>
          <li style={menuItemStyle}><a href="#datos" style={menuLinkStyle}>Mis datos Personales</a></li>
          <li style={menuItemStyle}><a href="#bloqueo" style={menuLinkStyle}>Bloqueo temporal de tarjeta</a></li>
        </ul>
      </div>

      {/* Contenido Principal (Centro) */}
      <div style={mainContentStyle}>
        <div style={cardStyle}>
          <h3 style={sectionTitleStyle}>Resumen de Cuenta Corriente</h3>
          
          {/* Grilla de Saldos */}
          <div style={balanceGrid}>
            <div style={balanceCardPrimary}>
              <span style={balanceLabel}>Saldo Disponible</span>
              <span style={balanceValueMain}>$ 1.064.762</span>
            </div>
            
            <div style={balanceCardSecondary}>
              <span style={balanceLabel}>Saldo Contable</span>
              <span style={balanceValue}>$ 1.064.762</span>
            </div>
            
            <div style={balanceCardSecondary}>
              <span style={balanceLabel}>Sobregiro Disponible</span>
              <span style={balanceValue}>$ 0</span>
            </div>
            
            <div style={balanceCardSecondary}>
              <span style={balanceLabel}>Sobregiro Utilizado</span>
              <span style={balanceValue}>$ 0</span>
            </div>
          </div>

          {/* Enlaces de Movimientos y Cartolas */}
          <div style={linksContainer}>
            <a href="#movimientos" style={actionLinkStyle}>Últimos Movimientos &rarr;</a>
            <a href="#cartola" style={actionLinkStyle}>Cartola Actual &rarr;</a>
          </div>
        </div>
      </div>
      
    </div>
  );
}

// --- ESTILOS EN LÍNEA ---

const dashboardContainer = {
  display: 'flex',
  minHeight: '100vh',
  backgroundColor: '#f4f6f8', // Fondo ligeramente gris/azulado para resaltar las tarjetas
  fontFamily: "'Segoe UI', Roboto, Helvetica, Arial, sans-serif"
};

const sidebarStyle = {
  width: '280px',
  backgroundColor: '#002464', // Azul oscuro clásico bancario
  color: '#ffffff',
  boxShadow: '2px 0 8px rgba(0,0,0,0.1)',
  display: 'flex',
  flexDirection: 'column'
};

const brandContainer = {
  padding: '30px 20px',
  borderBottom: '1px solid rgba(255,255,255,0.1)'
};

const brandStyle = {
  margin: 0,
  fontSize: '24px',
  fontWeight: '700',
  letterSpacing: '1px'
};

const menuListStyle = {
  listStyleType: 'none',
  padding: '20px 0',
  margin: 0
};

const menuItemStyle = {
  padding: '0 20px',
  marginBottom: '15px'
};

const menuItemInteractive = {
  padding: '12px 15px',
  backgroundColor: 'rgba(255,255,255,0.05)',
  borderRadius: '6px',
  cursor: 'pointer',
  fontWeight: '600',
  fontSize: '15px',
  display: 'flex',
  justifyContent: 'space-between',
  transition: 'background-color 0.2s'
};

const menuLinkStyle = {
  color: '#cbd5e1',
  textDecoration: 'none',
  fontSize: '15px',
  display: 'block',
  padding: '10px 15px',
  transition: 'color 0.2s'
};

const comboContainer = {
  marginTop: '10px',
  padding: '15px',
  backgroundColor: 'rgba(0,0,0,0.2)',
  borderRadius: '6px'
};

const labelStyle = {
  display: 'block',
  fontSize: '12px',
  color: '#cbd5e1',
  marginBottom: '8px'
};

const selectStyle = {
  width: '100%',
  padding: '10px',
  borderRadius: '4px',
  border: 'none',
  outline: 'none',
  backgroundColor: '#ffffff',
  color: '#333333',
  fontSize: '14px'
};

const mainContentStyle = {
  flex: 1,
  padding: '40px',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center'
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
  marginBottom: '40px'
};

const balanceCardPrimary = {
  backgroundColor: '#f8fafc',
  borderLeft: '4px solid #009038', // Verde bancario para saldo positivo
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

const linksContainer = {
  display: 'flex',
  gap: '20px',
  borderTop: '1px solid #f0f0f0',
  paddingTop: '20px'
};

const actionLinkStyle = {
  color: '#002464',
  textDecoration: 'none',
  fontWeight: '600',
  fontSize: '15px',
  padding: '10px 15px',
  backgroundColor: '#f0f4f8',
  borderRadius: '6px',
  transition: 'background-color 0.2s ease'
};
