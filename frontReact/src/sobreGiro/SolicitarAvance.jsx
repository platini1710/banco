import React, { useState, useMemo } from 'react';
import axios from 'axios';
import { useQuery } from '@tanstack/react-query';

export default function SolicitarAvance({ cuentaActiva }) {
  const idCuenta = cuentaActiva?.idCuenta;

  // --- ESTADOS DEL FORMULARIO ---
  const [monto, setMonto] = useState('');
  const [cuotas, setCuotas] = useState('');
  const [errorValidacion, setErrorValidacion] = useState('');
  const [mostrarConfirmacion, setMostrarConfirmacion] = useState(false);

  // Opciones de cuotas solicitadas
  const opcionesCuotas = [2, 3, 6, 9, 12, 24, 36];

  // --- CONSULTA DE TASA DE INTERÉS ---
  const { data: datosSobregiro, isLoading, isError, error } = useQuery({
    queryKey: ['tasa-sobregiro', idCuenta],
    queryFn: async () => {
      const token = localStorage.getItem('token');
      const respuesta = await axios.get(`http://localhost:8083/sobregiro/cuenta/usuario/${idCuenta}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      return respuesta.data; 
    },
    enabled: !!idCuenta,
    retry: false
  });

  // Extraemos de forma segura la tasa (ejemplo: 2.15)
  const tasaInteres = datosSobregiro?.tasaInteres || 0;

  // --- CÁLCULO DE LA CUOTA (Amortización Francesa) ---
  // Usamos useMemo para que no recalcule a menos que cambie el monto, las cuotas o la tasa
  const valorCuota = useMemo(() => {
    if (!monto || !cuotas || tasaInteres === 0) return 0;
    
    const p = parseFloat(monto); // Capital prestado (Principal)
    const i = parseFloat(tasaInteres) / 100; // Tasa de interés en decimales
    const n = parseInt(cuotas); // Número de cuotas

    // Fórmula financiera: Cuota = P * [ i * (1 + i)^n ] / [ (1 + i)^n - 1 ]
    const cuotaCalculada = p * (i * Math.pow(1 + i, n)) / (Math.pow(1 + i, n) - 1);
    
    return Math.round(cuotaCalculada); // Redondeamos a entero para pesos chilenos
  }, [monto, cuotas, tasaInteres]);

  // --- MANEJADOR DEL CLIC PRINCIPAL ---
  const handlePreSolicitar = (e) => {
    e.preventDefault();
    setErrorValidacion('');
    setMostrarConfirmacion(false);

    if (!monto || parseFloat(monto) <= 0) {
      setErrorValidacion('Por favor, ingrese un monto válido para el avance.');
      return;
    }

    if (!cuotas) {
      setErrorValidacion('Debe seleccionar el número de cuotas para continuar.');
      return;
    }

    setMostrarConfirmacion(true);
  };

  if (!idCuenta) {
    return (
      <div style={mainContentStyle}>
        <div style={infoBoxStyle}>
          <p style={{ margin: 0, color: '#64748b', fontWeight: '500' }}>
            Por favor, seleccione una cuenta corriente en el menú izquierdo para solicitar un avance.
          </p>
        </div>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div style={mainContentStyle}>
        <p style={{ color: '#002464', fontWeight: '600' }}>Consultando tasas vigentes...</p>
      </div>
    );
  }

  if (isError) {
    return (
      <div style={mainContentStyle}>
        <div style={errorBannerStyle}>
          <strong>Error al cargar condiciones comerciales:</strong> {error.response?.data?.message || error.message}
        </div>
      </div>
    );
  }

  return (
    <div style={mainContentStyle}>
      <div style={cardStyle}>
        <h2 style={sectionTitleStyle}>Solicitar Avance en Cuenta Corriente</h2>
        <p style={subTitleStyle}>Cuenta Activa: <strong>{cuentaActiva?.numeroCuenta || idCuenta}</strong></p>

        <form onSubmit={handlePreSolicitar} style={formStyle}>
          {/* Fila 1: Monto del Avance */}
          <div style={inputGroup}>
            <label style={labelStyle}>Monto que necesita solicitar ($):</label>
            <input
              type="number"
              style={inputStyle}
              placeholder="Ej. 500000"
              value={monto}
              onChange={(e) => {
                setMonto(e.target.value);
                setMostrarConfirmacion(false); 
              }}
            />
          </div>

          {/* Fila 2: Selector de Cuotas */}
          <div style={inputGroup}>
            <label style={labelStyle}>Número de cuotas mensuales:</label>
            <select
              style={selectStyle}
              value={cuotas}
              onChange={(e) => {
                setCuotas(e.target.value);
                setMostrarConfirmacion(false); 
              }}
            >
              <option value="">-- Seleccione cantidad de meses --</option>
              {opcionesCuotas.map((num) => (
                <option key={num} value={num}>{num} cuotas</option>
              ))}
            </select>
          </div>

          {/* Fila 3: Muestra informativa de la tasa obtenida por API */}
          <div style={tasaInfoStyle}>
            <span>Tasa de interés aplicada:</span>
            <strong>{tasaInteres}% mensual</strong>
          </div>

          {/* Banner de Errores de Validación Local */}
          {errorValidacion && (
            <p style={{ color: '#991b1b', margin: '10px 0 0 0', fontSize: '14px', fontWeight: '600' }}>
              ⚠️ {errorValidacion}
            </p>
          )}

          <button type="submit" style={btnPrimaryStyle}>
            Evaluar Solicitud
          </button>
        </form>

        {/* --- RECUADRO DE CONFIRMACIÓN CON CÁLCULO --- */}
        {mostrarConfirmacion && (
          <div style={confirmBoxStyle}>
            <h3 style={{ marginTop: 0, color: '#002464', fontSize: '18px' }}>⚠️ Confirmación de Operación</h3>
            <p style={{ color: '#334155', fontSize: '15px', lineHeight: '1.6' }}>
              Usted está solicitando un avance de <strong>${parseFloat(monto).toLocaleString('es-CL')}</strong>.
              <br />
              Pagará <strong>{cuotas} cuotas</strong> fijas de <strong style={{ color: '#009038', fontSize: '18px' }}>${valorCuota.toLocaleString('es-CL')}</strong>.
            </p>
            <p style={{ fontSize: '12px', color: '#64748b', fontStyle: 'italic', marginTop: '10px' }}>
              * Tasa de interés mensual: {tasaInteres}%. Sujeto a evaluación final y comisiones de prepago.
            </p>
            <div style={btnActionsContainer}>
              <button 
                onClick={() => alert(`Simulación: POST al backend por $${monto} en ${cuotas} cuotas de $${valorCuota}`)} 
                style={btnConfirmStyle}
              >
                Sí, Solicitar Avance
              </button>
              <button 
                onClick={() => setMostrarConfirmacion(false)} 
                style={btnCancelStyle}
              >
                Cancelar
              </button>
            </div>
          </div>
        )}
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
  maxWidth: '650px',
  borderRadius: '12px',
  boxShadow: '0 8px 24px rgba(0, 0, 0, 0.04)',
  padding: '40px',
  border: '1px solid #f1f5f9'
};

const sectionTitleStyle = {
  color: '#1e293b',
  fontSize: '22px',
  fontWeight: '700',
  margin: 0
};

const subTitleStyle = {
  fontSize: '14px',
  color: '#64748b',
  marginTop: '5px',
  marginBottom: '25px'
};

const formStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '20px'
};

const inputGroup = {
  display: 'flex',
  flexDirection: 'column',
  gap: '6px'
};

const labelStyle = {
  fontSize: '14px',
  color: '#334155',
  fontWeight: '600'
};

const inputStyle = {
  padding: '10px 14px',
  borderRadius: '6px',
  border: '1px solid #cbd5e1',
  fontSize: '15px',
  outline: 'none',
  backgroundColor: '#f8fafc'
};

const selectStyle = {
  padding: '10px 14px',
  borderRadius: '6px',
  border: '1px solid #cbd5e1',
  fontSize: '15px',
  outline: 'none',
  backgroundColor: '#ffffff',
  cursor: 'pointer'
};

const tasaInfoStyle = {
  backgroundColor: '#f1f5f9',
  padding: '12px 16px',
  borderRadius: '6px',
  display: 'flex',
  justifyContent: 'space-between',
  fontSize: '14px',
  color: '#475569'
};

const btnPrimaryStyle = {
  backgroundColor: '#002464',
  color: '#ffffff',
  border: 'none',
  padding: '12px',
  borderRadius: '6px',
  fontSize: '15px',
  fontWeight: '600',
  cursor: 'pointer',
  marginTop: '10px',
  transition: 'background-color 0.2s'
};

const confirmBoxStyle = {
  marginTop: '30px',
  padding: '20px',
  backgroundColor: '#f0f4f8',
  borderRadius: '8px',
  borderLeft: '4px solid #009038',
  boxShadow: 'inset 0 2px 4px rgba(0,0,0,0.02)'
};

const btnActionsContainer = {
  display: 'flex',
  gap: '12px',
  marginTop: '15px'
};

const btnConfirmStyle = {
  backgroundColor: '#009038',
  color: '#ffffff',
  border: 'none',
  padding: '8px 16px',
  borderRadius: '4px',
  fontWeight: '600',
  cursor: 'pointer'
};

const btnCancelStyle = {
  backgroundColor: 'transparent',
  color: '#64748b',
  border: '1px solid #cbd5e1',
  padding: '8px 16px',
  borderRadius: '4px',
  fontWeight: '600',
  cursor: 'pointer'
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
  width: '100%'
};