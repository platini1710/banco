import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
  const [credenciales, setCredenciales] = useState({ rut: '', password: '' });
  const [error, setError] = useState(null);
  const [cargando, setCargando] = useState(false);
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setError(null); // Limpiamos el error si el usuario empieza a corregir

    // REGLA 1: Validación del RUT (Solo números y máximo 8)
    if (name === 'rut') {
      const soloNumeros = value.replace(/\D/g, ''); // Elimina cualquier letra o símbolo
      if (soloNumeros.length <= 8) {
        setCredenciales({ ...credenciales, [name]: soloNumeros });
      }
      return;
    }

    // REGLA 2: Validación de Password (Máximo 8 caracteres en tiempo real)
    if (name === 'password') {
      if (value.length <= 8) {
        setCredenciales({ ...credenciales, [name]: value });
      }
      return;
    }

    setCredenciales({ ...credenciales, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    // --- REGLAS DE NEGOCIO AL ENVIAR ---
    
    // RUT: Exactamente 8 números
    if (credenciales.rut.length !== 8) {
      setError('El RUT debe tener exactamente 8 dígitos numéricos.');
      return;
    }

    // Password: Mínimo 5 caracteres
    if (credenciales.password.length < 5) {
      setError('La contraseña debe tener un mínimo de 5 caracteres.');
      return;
    }

    // Si pasa las validaciones, ejecutamos el login
    setCargando(true);

    try {
      // Reemplaza localhost si lo despliegas en tu instancia de AWS
      const response = await fetch('http://localhost:8083/consulta/usuario/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          rut: credenciales.rut,
          password: credenciales.password
        })
      });
  
      if (response.ok) {

        const data = await response.json();
        console.log("token " + data.token);
        localStorage.setItem('token', data.token); // Guardamos la credencial
              localStorage.setItem('rut', credenciales.rut); // Guardamos la credencial
        navigate('/dashboard'); // Redirigimos al área protegida
      } else {
        setError('Las credenciales ingresadas no coinciden con nuestros registros.');
      }
    } catch (err) {
      setError('Sistema temporalmente no disponible. Intente más tarde.');
    } finally {
      setCargando(false);
    }
  };

  return (
    <div style={styles.wrapper}>
      {/* Barra superior delgada típica de bancos (JPMorgan Chase) */}
      <div style={styles.topBar}></div>

      <div style={styles.container}>
        {/* Logo minimalista */}
        <div style={styles.logoContainer}>
          <h1 style={styles.logoText}>
            CHASE <span style={styles.logoSymbol}>O</span>
          </h1>
        </div>

        <div style={styles.card}>
          <h2 style={styles.title}>Iniciar sesión</h2>

          {/* Caja de alertas */}
          {error && (
            <div style={styles.errorBox}>
              <span style={{ fontWeight: '700' }}>Importante:</span> {error}
            </div>
          )}

          <form onSubmit={handleSubmit} style={styles.form}>
            {/* Input del RUT */}
            <div style={styles.inputGroup}>
              <input
                type="text"
                id="rut"
                name="rut"
                value={credenciales.rut}
                onChange={handleChange}
                style={error && credenciales.rut.length !== 8 && credenciales.rut.length > 0 ? { ...styles.input, ...styles.inputError } : styles.input}
                required
                placeholder="RUT de usuario (8 números)"
                maxLength={8}
                autoComplete="off"
              />
            </div>

            {/* Input de la Contraseña */}
            <div style={styles.inputGroup}>
              <input
                type="password"
                id="password"
                name="password"
                value={credenciales.password}
                onChange={handleChange}
                style={error && credenciales.password.length < 5 && credenciales.password.length > 0 ? { ...styles.input, ...styles.inputError } : styles.input}
                required
                placeholder="Contraseña (5 a 8 caracteres)"
                maxLength={8}
              />
            </div>

            {/* Enlaces de ayuda */}
            <div style={styles.extraLinks}>
              <label style={styles.rememberMe}>
                <input type="checkbox" /> Recordarme
              </label>
              <a href="#" style={styles.forgot}>¿Olvidaste tu contraseña?</a>
            </div>

            {/* Botón de Acción */}
            <button 
              type="submit" 
              style={cargando ? styles.buttonDisabled : styles.button}
              disabled={cargando}
            >
              {cargando ? 'Verificando...' : 'Ingresar'}
            </button>
          </form>
        </div>

        <div style={styles.newAccount}>
          <p>¿No eres cliente? <a href="#" style={styles.blueLink}>Abre una cuenta</a></p>
        </div>
      </div>

      {/* Footer con tu firma */}
      <footer style={styles.footer}>
        <div style={styles.footerLinks}>
          <span>Contáctanos</span> | <span>Privacidad</span> | <span>Seguridad</span> | <span>Términos de uso</span>
        </div>
        <p style={styles.footerCopy}>
          &copy; 2026 J.P.Morgan Chase & Co. | <strong>Hecho por Augusto Espinoza</strong>
        </p>
      </footer>
    </div>
  );
};

// --- ESTILOS INSPIRADOS EN JPMORGAN CHASE ---
const styles = {
  wrapper: { 
    display: 'flex', 
    flexDirection: 'column', 
    minHeight: '100vh', 
    backgroundColor: '#FFFFFF', 
    fontFamily: '"Open Sans", Helvetica, Arial, sans-serif',
    color: '#333',
    margin: '-8px' // Limpia márgenes por defecto del navegador
  },
  topBar: {
    height: '4px',
    backgroundColor: '#00467F', // Azul Chase
  },
  container: { 
    flex: 1, 
    display: 'flex', 
    flexDirection: 'column', 
    justifyContent: 'center', 
    alignItems: 'center', 
    padding: '20px',
    background: 'linear-gradient(180deg, #F4F7F9 0%, #FFFFFF 100%)' 
  },
  logoContainer: { 
    marginBottom: '30px', 
  },
  logoText: { 
    fontSize: '28px', 
    fontWeight: '400', 
    color: '#00467F', 
    margin: 0, 
    letterSpacing: '2px',
    fontFamily: 'serif' 
  },
  logoSymbol: {
    fontWeight: '700',
    borderLeft: '2px solid #00467F',
    paddingLeft: '10px',
    marginLeft: '10px'
  },
  card: { 
    backgroundColor: '#FFFFFF', 
    padding: '40px', 
    border: '1px solid #D8D8D8', 
    borderRadius: '2px', 
    boxShadow: '0 4px 12px rgba(0,0,0,0.05)',
    width: '100%', 
    maxWidth: '400px' 
  },
  title: { 
    fontSize: '24px', 
    fontWeight: '300', 
    color: '#00467F', 
    marginBottom: '25px', 
    textAlign: 'left' 
  },
  errorBox: { 
    backgroundColor: '#FDF0F0', 
    color: '#D61A0C', 
    padding: '15px', 
    borderLeft: '4px solid #D61A0C',
    marginBottom: '20px', 
    fontSize: '13px',
  },
  form: { 
    display: 'flex', 
    flexDirection: 'column', 
    gap: '25px' 
  },
  inputGroup: { 
    display: 'flex', 
    flexDirection: 'column',
  },
  input: { 
    padding: '12px 0px', 
    border: 'none',
    borderBottom: '1px solid #999', 
    fontSize: '16px',
    outline: 'none',
    transition: 'border-color 0.3s',
  },
  inputError: {
    borderBottom: '2px solid #D61A0C' // Borde rojo si falla la validación
  },
  extraLinks: {
    display: 'flex',
    justifyContent: 'space-between',
    fontSize: '13px',
    color: '#555'
  },
  rememberMe: {
    display: 'flex',
    alignItems: 'center',
    gap: '5px',
    cursor: 'pointer'
  },
  forgot: {
    color: '#00467F',
    textDecoration: 'none',
    fontWeight: '600'
  },
  button: { 
    backgroundColor: '#00467F', 
    color: '#FFFFFF', 
    padding: '14px', 
    borderRadius: '4px', 
    border: 'none', 
    fontSize: '16px', 
    fontWeight: '600', 
    cursor: 'pointer', 
    marginTop: '10px',
    transition: 'background-color 0.2s',
  },
  buttonDisabled: { 
    backgroundColor: '#CCCCCC', 
    color: '#FFFFFF', 
    padding: '14px', 
    borderRadius: '4px', 
    border: 'none', 
    fontSize: '16px', 
    fontWeight: '600', 
    cursor: 'not-allowed', 
    marginTop: '10px'
  },
  newAccount: {
    marginTop: '30px',
    fontSize: '14px',
  },
  blueLink: {
    color: '#00467F',
    textDecoration: 'none',
    fontWeight: '700'
  },
  footer: { 
    textAlign: 'center', 
    padding: '40px 20px', 
    color: '#666', 
    fontSize: '12px',
    backgroundColor: '#F4F7F9',
    borderTop: '1px solid #E1E1E1'
  },
  footerLinks: {
    marginBottom: '10px',
    fontWeight: '600'
  },
  footerCopy: {
    margin: 0,
    opacity: 0.8
  }
};

export default LoginPage;