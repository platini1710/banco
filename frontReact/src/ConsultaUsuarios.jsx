import React, { useState } from 'react';
import axios from 'axios';
import { useQuery } from '@tanstack/react-query';

export default function ConsultaUsuarios() {
  const [searchId, setSearchId] = useState('');
  const [queryId, setQueryId] = useState(null);

  const { data, isLoading, isError, error } = useQuery({
    queryKey: ['consulta-usuario', queryId],
    queryFn: async () => {
      const gatewayUrl = import.meta.env.VITE_API_GATEWAY_URL;
      console.log("la url es " + `${gatewayUrl}/consulta/usuarios/login/${queryId}`);
      const respuesta = await axios.get(`${gatewayUrl}/consulta/usuarios/login/${queryId}`);
      return respuesta.data;
    },
    enabled: !!queryId, 
    retry: false 
  });

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchId.trim() !== '') {
      setQueryId(searchId); 
    }
  };

  const usuario = data;

  return (
    <div style={pageContainer}>
      <div style={cardStyle}>
        
        {/* Cabecera idéntica al registro */}
        <div style={headerStyle}>
          <h2 style={titleStyle}>Consulta de Usuario</h2>
          <p style={subtitleStyle}>Busca rápidamente los datos ingresando el ID</p>
        </div>

        <div style={formStyle}>
          {/* Buscador */}
          <form onSubmit={handleSearch} style={{ display: 'flex', gap: '10px', marginBottom: '25px' }}>
            <div style={{ flex: 1 }}>
              <input 
                type="number" 
                placeholder="Ingrese el ID (Ej: 1234)" 
                value={searchId}
                onChange={(e) => setSearchId(e.target.value)}
                style={inputStyle}
                required
              />
            </div>
            <button 
              type="submit" 
              disabled={isLoading} 
              style={isLoading ? searchBtnDisabledStyle : searchBtnStyle}
            >
              {isLoading ? 'Buscando...' : 'Consultar'}
            </button>
          </form>

          {/* Manejo de Errores */}
          {isError && (
            <div style={errorBanner}>
              <p style={{ margin: 0 }}>
                No se encontró el usuario.<br/>
                <small>{error.response?.data?.message || error.message}</small>
              </p>
            </div>
          )}

          {/* Resultados de la búsqueda filtrados */}
          {usuario && (
            <div>
              <h4 style={sectionTitleStyle}>Datos Personales</h4>

              <div style={inputGroup}>
                <label style={labelStyle}>Nombre Completo</label>
                <input type="text" value={usuario.name || ''} readOnly style={readOnlyInputStyle} />
              </div>

              <div style={inputGroup}>
                <label style={labelStyle}>Correo Electrónico</label>
                <input type="email" value={usuario.email || ''} readOnly style={readOnlyInputStyle} />
              </div>

              <h4 style={sectionTitleStyle}>Datos de Contacto</h4>
              
              <div style={rowGroup}>
                <div style={{ ...inputGroup, flex: 1, marginRight: '10px' }}>
                  <label style={labelStyle}>Cód. País</label>
                  <input 
                    type="text" 
                    value={usuario.phone?.cityCode || usuario.cityCode || ''} 
                    readOnly 
                    style={readOnlyInputStyle} 
                  />
                </div>

                <div style={{ ...inputGroup, flex: 2 }}>
                  <label style={labelStyle}>Número de Teléfono</label>
                  <input 
                    type="text" 
                    value={usuario.phone?.number || usuario.number || ''} 
                    readOnly 
                    style={readOnlyInputStyle} 
                  />
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Firma del autor */}
        <div style={footerStyle}>
          <p style={footerTextStyle}>Hecho por Augusto Espinoza Neira</p>
        </div>

      </div>
    </div>
  );
}

// --- ESTILOS EN LÍNEA (Idénticos a RegistroUsuario) ---

const pageContainer = { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', backgroundColor: '#f4f4f4', fontFamily: "'Segoe UI', Roboto, Helvetica, Arial, sans-serif", padding: '20px' };
const cardStyle = { backgroundColor: '#ffffff', width: '100%', maxWidth: '480px', borderRadius: '12px', boxShadow: '0 8px 24px rgba(0, 0, 0, 0.08)', overflow: 'hidden', display: 'flex', flexDirection: 'column' };
const headerStyle = { backgroundColor: '#ffffff', padding: '30px 30px 10px 30px', textAlign: 'center', borderBottom: '2px solid #f0f0f0' };
const titleStyle = { margin: '0 0 5px 0', color: '#333333', fontSize: '24px', fontWeight: '700' };
const subtitleStyle = { margin: '0', color: '#666666', fontSize: '14px' };
const formStyle = { padding: '25px 30px 20px 30px' };
const sectionTitleStyle = { color: '#444444', fontSize: '16px', borderBottom: '1px solid #eeeeee', paddingBottom: '8px', marginBottom: '15px', marginTop: '10px' };
const inputGroup = { marginBottom: '18px' };
const rowGroup = { display: 'flex', justifyContent: 'space-between' };
const labelStyle = { display: 'block', marginBottom: '6px', color: '#555555', fontSize: '13px', fontWeight: '600' };

const inputStyle = { display: 'block', width: '100%', padding: '12px 15px', fontSize: '14px', color: '#333', backgroundColor: '#f9f9f9', border: '1px solid #dddddd', borderRadius: '6px', boxSizing: 'border-box', transition: 'border-color 0.2s ease', outline: 'none' };

// Estilo especial para los inputs de solo lectura (para que parezcan resultados y no campos editables)
const readOnlyInputStyle = { ...inputStyle, backgroundColor: '#f0f4f8', color: '#555', borderColor: '#e1e8ed', cursor: 'default' };

// Botón adaptado para ir al lado del buscador
const searchBtnStyle = { padding: '0 20px', backgroundColor: '#BED000', color: '#4A4A4A', fontSize: '15px', fontWeight: '700', border: 'none', borderRadius: '6px', cursor: 'pointer', transition: 'background-color 0.2s ease', boxShadow: '0 4px 6px rgba(190, 208, 0, 0.3)', whiteSpace: 'nowrap' };
const searchBtnDisabledStyle = { ...searchBtnStyle, backgroundColor: '#e0e0e0', color: '#888888', cursor: 'not-allowed', boxShadow: 'none' };

const errorBanner = { backgroundColor: '#ffebee', color: '#c62828', padding: '12px 15px', marginBottom: '20px', borderRadius: '6px', fontSize: '14px', textAlign: 'center', border: '1px solid #ef9a9a' };
const footerStyle = { backgroundColor: '#fafafa', padding: '15px', textAlign: 'center', borderTop: '1px solid #eeeeee', marginTop: 'auto' };
const footerTextStyle = { margin: 0, color: '#999999', fontSize: '12px', fontWeight: '500', letterSpacing: '0.5px' };