import React, { useState, useEffect } from 'react';

export default function Sidebar({ rutUsuario, onSeleccionarCuenta, onCambiarVista }) {
  const [mostrarCuentas, setMostrarCuentas] = useState(true);
  const [mostrarSobregiros, setMostrarSobregiros] = useState(false);
  const [cuentasBancarias, setCuentasBancarias] = useState([]);
  
  // Mantenemos el nombre correcto: cuentaSeleccionada
  const [cuentaSeleccionada, setCuentaSeleccionada] = useState("");
  const [cargando, setCargando] = useState(false);

  // CORRECCIÓN 1: Separamos la carga de datos del estado visual del menú
  useEffect(() => {
    const obtenerCuentas = async () => {
      setCargando(true);
      try {
        const token = localStorage.getItem('token');
        const rutLocal = rutUsuario || localStorage.getItem('rut');

        // Si no hay RUT, no intentamos hacer la petición
        if (!rutLocal) return;

        const response = await fetch(`http://localhost:8083/consulta/usuario/usuario/${rutLocal}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.ok) {
          const data = await response.json();
          setCuentasBancarias(data);

          if (data.length > 0) {
            const primeraCuenta = data[0];
            setCuentaSeleccionada(primeraCuenta.idCuenta);
            if (onSeleccionarCuenta) onSeleccionarCuenta(primeraCuenta);
          }
        } else {
          console.error("Error del servidor al traer las cuentas:", response.status);
        }
      } catch (error) {
        console.error("Error de conexión:", error);
      } finally {
        setCargando(false);
      }
    };

    // Solo ejecuta la carga si aún no hay cuentas cargadas
    if (cuentasBancarias.length === 0) {
      obtenerCuentas();
    }
    // Simplificamos las dependencias para evitar loops infinitos o bloqueos
  }, [rutUsuario]); 

  const handleCambioCuenta = (e) => {
    const idBuscado = parseInt(e.target.value);
    setCuentaSeleccionada(idBuscado);
    
    const cuentaEncontrada = cuentasBancarias.find(c => c.idCuenta === idBuscado);
    if (onSeleccionarCuenta && cuentaEncontrada) onSeleccionarCuenta(cuentaEncontrada);
  };

  // Función reutilizable para navegar validando que exista una cuenta
  const navegarConValidacion = (e, vistaDestino) => {
    e.preventDefault(); // CORRECCIÓN 2: Evita que la URL salte con los href

    // CORRECCIÓN 3: Validamos con el nombre correcto del estado

    if (!cuentaSeleccionada) {
      alert('Por favor, seleccione una cuenta primero en el menú "Mi Cuenta".');
      return;
    }
    
    if (onCambiarVista) onCambiarVista(vistaDestino);
  };

  return (
    <div style={sidebarStyle}>
      <div style={brandContainer}>
        <h2 style={brandStyle}>Mi Banco</h2>
      </div>

      <ul style={menuListStyle}>

        {/* --- OPCIÓN 1: MI CUENTA --- */}
        <li style={menuItemStyle}>
          <div
            onClick={() => {
              setMostrarCuentas(!mostrarCuentas);
              if (onCambiarVista) onCambiarVista('resumen');
            }}
            style={menuItemInteractive}
          >
            Mi Cuenta {mostrarCuentas ? '▲' : '▼'}
          </div>

          {mostrarCuentas && (
            <div style={subMenuContainer}>
              <label style={labelStyle}>Seleccione su cuenta:</label>
              <select
                style={selectStyle}
                value={cuentaSeleccionada || ""}
                onChange={handleCambioCuenta}
                disabled={cargando}
              >
                {cargando ? (
                  <option value="">Cargando...</option>
                ) : cuentasBancarias.length === 0 ? (
                  <option value="">Sin cuentas</option>
                ) : (
                  cuentasBancarias.map((cuenta) => (
                    <option key={cuenta.idCuenta} value={cuenta.idCuenta}>
                      Cta. {cuenta.numeroCuenta}
                    </option>
                  ))
                )}
              </select>

              <div style={innerLinksContainer}>
                <a 
                  href="#" 
                  style={innerLinkStyle} 
                  onClick={(e) => navegarConValidacion(e, 'movimientos')}
                >
                  Últimos Movimientos
                </a>
                <a 
                  href="#" 
                  style={innerLinkStyle} 
                  onClick={(e) => navegarConValidacion(e, 'cartola')}
                >
                  Cartola Actual
                </a>
              </div>
            </div>
          )}
        </li>

        {/* --- OPCIÓN 2: SOBREGIROS --- */}
        <li style={menuItemStyle}>
          <div
            onClick={() => setMostrarSobregiros(!mostrarSobregiros)}
            style={menuItemInteractive}
          >
            Línea de Sobregiros {mostrarSobregiros ? '▲' : '▼'}
          </div>

          {mostrarSobregiros && (
            <div style={subMenuContainer}>
              <div style={innerLinksContainer}>
                <a
                  href=""
                  style={innerLinkStyle}
                  onClick={(e) => navegarConValidacion(e, 'consulta-sobregiro')}
                >
                  🔍 Consulta Sobregiro
                </a>
                <a
                  href="#"
                  style={innerLinkStyle}
                  onClick={(e) => navegarConValidacion(e, 'solicitar-avance')}
                >
                  💳 Solicitar Avance
                </a>
                <a
                  href="#"
                  style={innerLinkStyle}
                  onClick={(e) => navegarConValidacion(e, 'pagar-sobregiro')}
                >
                  💰 Pagar Sobregiro
                </a>
              </div>
            </div>
          )}
        </li>

        {/* --- OTRAS OPCIONES --- */}
        <li style={menuItemStyle}>
          <a href="#" onClick={(e) => { e.preventDefault(); if(onCambiarVista) onCambiarVista('datos'); }} style={menuLinkStyle}>
            Mis datos Personales
          </a>
        </li>
        <li style={menuItemStyle}>
          <a href="#" onClick={(e) => { e.preventDefault(); if(onCambiarVista) onCambiarVista('bloqueo'); }} style={menuLinkStyle}>
            Bloqueo temporal de tarjeta
          </a>
        </li>
      </ul>
    </div>
  );
}

// --- ESTILOS (Mantenidos intactos de tu código original) ---
const sidebarStyle = {
  width: '100%',
  backgroundColor: '#002464',
  color: '#ffffff',
  boxShadow: '2px 0 8px rgba(0,0,0,0.1)',
  display: 'flex',
  flexDirection: 'column',
  height: '100%',
  minHeight: '100vh',
};

const brandContainer = { padding: '30px 20px', borderBottom: '1px solid rgba(255,255,255,0.1)' };
const brandStyle = { margin: 0, fontSize: '24px', fontWeight: '700' };
const menuListStyle = { listStyleType: 'none', padding: '20px 0', margin: 0 };
const menuItemStyle = { padding: '0 20px', marginBottom: '15px' };

const menuItemInteractive = {
  padding: '12px 15px',
  backgroundColor: 'rgba(255,255,255,0.05)',
  borderRadius: '6px',
  cursor: 'pointer',
  fontWeight: '600',
  fontSize: '15px',
  display: 'flex',
  justifyContent: 'space-between',
};

const menuLinkStyle = {
  color: '#cbd5e1',
  textDecoration: 'none',
  fontSize: '15px',
  display: 'block',
  padding: '10px 15px',
};

const subMenuContainer = {
  marginTop: '10px',
  padding: '15px',
  backgroundColor: 'rgba(0,0,0,0.2)',
  borderRadius: '6px',
  display: 'flex',
  flexDirection: 'column',
  gap: '15px'
};

const labelStyle = { display: 'block', fontSize: '12px', color: '#cbd5e1' };

const selectStyle = {
  width: '100%',
  padding: '8px',
  borderRadius: '4px',
  border: 'none',
  backgroundColor: '#ffffff',
  color: '#333333',
};

const innerLinksContainer = {
  display: 'flex',
  flexDirection: 'column',
  gap: '10px',
  borderTop: '1px solid rgba(255,255,255,0.1)',
  paddingTop: '10px'
};

const innerLinkStyle = {
  color: '#60a5fa',
  textDecoration: 'none',
  fontSize: '14px',
  cursor: 'pointer'
};