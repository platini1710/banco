import React, { useState, useRef } from 'react';
import axios from 'axios';
import { useMutation } from '@tanstack/react-query';

// El estado inicial sigue teniendo SOLO los datos que la API necesita
const initialState = {
  id: "",
  name: "",
  password: "",
  email: "",
  created: "2026-05-01",
  lastLogin: "2026-05-01",
  token: "temp-token",
  isActive: "true",
  phone: {
    id: 1,
    number: "",
    cityCode: ""
  }
};

export default function RegistroUsuario() {
  const [formData, setFormData] = useState(initialState);
  const [confirmPassword, setConfirmPassword] = useState("");
  
  const idInputRef = useRef(null);

  const mutation = useMutation({
    mutationFn: (nuevoUsuario) => {
      console.log("VITE_API_GATEWAY_URL ::" + import.meta.env.VITE_API_GATEWAY_URL);
      return axios.post(`${import.meta.env.VITE_API_GATEWAY_URL}/registro/usuario/sign-up`, nuevoUsuario);
    },
    onSuccess: () => {
      alert("¡Bienvenido! Usuario registrado con éxito.");
      
      setFormData(initialState);
      setConfirmPassword("");
      
      if (idInputRef.current) {
        idInputRef.current.focus();
      }
    },
    onError: (error) => {
      const mensajeDetallado = error.response?.data?.message 
                             || error.response?.data?.error 
                             || JSON.stringify(error.response?.data) 
                             || error.message;

      alert(`Error en el registro: ${mensajeDetallado}`);
      console.log("Estructura completa del error:", error.response?.data);
    }
  });

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "id") {
      const soloNumeros = value.replace(/[^0-9]/g, '');
      setFormData({ ...formData, [name]: soloNumeros });
      return;
    }

    // MODIFICADO: Bloqueamos letras en los campos de teléfono
    if (name.startsWith("phone.")) {
      const field = name.split(".")[1];
      const soloNumeros = value.replace(/[^0-9]/g, '');
      
      setFormData({
        ...formData,
        phone: { ...formData.phone, [field]: soloNumeros }
      });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (formData.id.length < 4) {
      alert("El campo ID debe tener un mínimo de 4 dígitos.");
      return;
    }

    if (formData.name.length > 20) {
      alert("El Nombre Completo no puede superar los 20 caracteres.");
      return;
    }

    if (formData.email.length > 30) {
      alert("El Correo Electrónico no puede superar los 30 caracteres.");
      return;
    }

    if (formData.password.length < 5 || formData.password.length > 8) {
      alert("La Contraseña debe tener un mínimo de 5 y un máximo de 8 caracteres.");
      return;
    }

    const tieneLetra = /[a-zA-Z]/.test(formData.password);
    if (!tieneLetra) {
      alert("La Contraseña debe contener al menos una letra (mayúscula o minúscula).");
      return;
    }

    if (formData.password !== confirmPassword) {
      alert("Las contraseñas no coinciden. Por favor, verifica e intenta de nuevo.");
      return;
    }

    // NUEVO: Validación de Código de Ciudad/País (Exactamente 2)
    if (formData.phone.cityCode.length !== 2) {
      alert("El código de ciudad debe tener exactamente 2 dígitos.");
      return;
    }

    // NUEVO: Validación de Número de Teléfono (Mínimo 8, Máximo 10)
    if (formData.phone.number.length < 8 || formData.phone.number.length > 10) {
      alert("El número de teléfono debe tener entre 8 y 10 dígitos.");
      return;
    }

    mutation.mutate(formData);
  };

  const isProcessing = mutation.isLoading || mutation.isPending;

  return (
    <div style={pageContainer}>
      <div style={cardStyle}>
        
        <div style={headerStyle}>
          <h2 style={titleStyle}>Crea tu cuenta</h2>
          <p style={subtitleStyle}>Únete y descubre las mejores ofertas</p>
        </div>

        <form onSubmit={handleSubmit} style={formStyle}>
          
          <div style={inputGroup}>
            <label style={labelStyle}>ID de Usuario (Mín. 4 dígitos)</label>
            <input 
              ref={idInputRef}
              type="text" 
              name="id" 
              value={formData.id} 
              placeholder="Ej: 1234" 
              onChange={handleChange} 
              style={inputStyle} 
              minLength={4} 
              maxLength={10}
              required 
            />
          </div>

          <div style={inputGroup}>
            <label style={labelStyle}>Nombre Completo</label>
            <input 
              type="text" 
              name="name" 
              value={formData.name} 
              placeholder="Ej: Juan Pérez" 
              onChange={handleChange} 
              style={inputStyle} 
              maxLength={20}
              required 
            />
          </div>

          <div style={inputGroup}>
            <label style={labelStyle}>Correo Electrónico</label>
            <input 
              type="email" 
              name="email" 
              value={formData.email} 
              placeholder="tucorreo@email.com" 
              onChange={handleChange} 
              style={inputStyle} 
              maxLength={30}
              required 
            />
          </div>

          <div style={rowGroup}>
            <div style={{ ...inputGroup, flex: 1, marginRight: '10px' }}>
              <label style={labelStyle}>Contraseña</label>
              <input 
                type="password" 
                name="password" 
                value={formData.password} 
                placeholder="Mínimo 5 caracteres" 
                onChange={handleChange} 
                style={inputStyle} 
                minLength={5}
                maxLength={8}
                required 
              />
            </div>

            <div style={{ ...inputGroup, flex: 1 }}>
              <label style={labelStyle}>Confirmar Contraseña</label>
              <input 
                type="password" 
                value={confirmPassword} 
                placeholder="Repite la contraseña" 
                onChange={(e) => setConfirmPassword(e.target.value)} 
                style={inputStyle} 
                minLength={5}
                maxLength={8}
                required 
              />
            </div>
          </div>
          
          <h4 style={sectionTitleStyle}>Datos de Contacto</h4>
          
          <div style={rowGroup}>
            <div style={{ ...inputGroup, flex: 1, marginRight: '10px' }}>
              <label style={labelStyle}>Cód. País</label>
              {/* MODIFICADO: type="tel" permite que maxLength funcione */}
              <input 
                type="tel" 
                name="phone.cityCode" 
                value={formData.phone.cityCode} 
                placeholder="Ej: 56" 
                onChange={handleChange} 
                style={inputStyle} 
                minLength={2}
                maxLength={2}
                required 
              />
            </div>
            
            <div style={{ ...inputGroup, flex: 2 }}>
              <label style={labelStyle}>Número de Teléfono</label>
              {/* MODIFICADO: type="tel" permite que maxLength funcione */}
              <input 
                type="tel" 
                name="phone.number" 
                value={formData.phone.number} 
                placeholder="Ej: 912345678" 
                onChange={handleChange} 
                style={inputStyle} 
                minLength={8}
                maxLength={10}
                required 
              />
            </div>
          </div>

          <button 
            type="submit" 
            disabled={isProcessing} 
            style={isProcessing ? btnDisabledStyle : btnStyle}
          >
            {isProcessing ? 'Procesando...' : 'Registrarme'}
          </button>
        </form>
        
        {mutation.isError && (
          <div style={errorBanner}>
            <p style={{ margin: 0 }}>Ocurrió un error al intentar registrar tus datos.</p>
          </div>
        )}

        <div style={footerStyle}>
          <p style={footerTextStyle}>Hecho por Augusto Espinoza Neira</p>
        </div>
        
      </div>
    </div>
  );
}

// --- ESTILOS EN LÍNEA ---
const pageContainer = { display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', backgroundColor: '#f4f4f4', fontFamily: "'Segoe UI', Roboto, Helvetica, Arial, sans-serif", padding: '20px' };
const cardStyle = { backgroundColor: '#ffffff', width: '100%', maxWidth: '480px', borderRadius: '12px', boxShadow: '0 8px 24px rgba(0, 0, 0, 0.08)', overflow: 'hidden', display: 'flex', flexDirection: 'column' };
const headerStyle = { backgroundColor: '#ffffff', padding: '30px 30px 10px 30px', textAlign: 'center', borderBottom: '2px solid #f0f0f0' };
const titleStyle = { margin: '0 0 5px 0', color: '#333333', fontSize: '24px', fontWeight: '700' };
const subtitleStyle = { margin: '0', color: '#666666', fontSize: '14px' };
const formStyle = { padding: '25px 30px 20px 30px' };
const sectionTitleStyle = { color: '#444444', fontSize: '16px', borderBottom: '1px solid #eeeeee', paddingBottom: '8px', marginBottom: '15px', marginTop: '25px' };
const inputGroup = { marginBottom: '18px' };
const rowGroup = { display: 'flex', justifyContent: 'space-between' };
const labelStyle = { display: 'block', marginBottom: '6px', color: '#555555', fontSize: '13px', fontWeight: '600' };
const inputStyle = { display: 'block', width: '100%', padding: '12px 15px', fontSize: '14px', color: '#333', backgroundColor: '#f9f9f9', border: '1px solid #dddddd', borderRadius: '6px', boxSizing: 'border-box', transition: 'border-color 0.2s ease', outline: 'none' };
const btnStyle = { width: '100%', padding: '14px', backgroundColor: '#BED000', color: '#4A4A4A', fontSize: '16px', fontWeight: '700', border: 'none', borderRadius: '6px', cursor: 'pointer', marginTop: '15px', transition: 'background-color 0.2s ease', boxShadow: '0 4px 6px rgba(190, 208, 0, 0.3)' };
const btnDisabledStyle = { ...btnStyle, backgroundColor: '#e0e0e0', color: '#888888', cursor: 'not-allowed', boxShadow: 'none' };
const errorBanner = { backgroundColor: '#ffebee', color: '#c62828', padding: '12px 15px', margin: '0 30px 20px 30px', borderRadius: '6px', fontSize: '14px', textAlign: 'center', border: '1px solid #ef9a9a' };
const footerStyle = { backgroundColor: '#fafafa', padding: '15px', textAlign: 'center', borderTop: '1px solid #eeeeee', marginTop: 'auto' };
const footerTextStyle = { margin: 0, color: '#999999', fontSize: '12px', fontWeight: '500', letterSpacing: '0.5px' };