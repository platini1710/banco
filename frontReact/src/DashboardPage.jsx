import React from 'react';
// 1. QUITAMOS el BrowserRouter de aquí, solo dejamos Routes, Route y Link
import { Routes, Route, Link } from 'react-router-dom';
import RegistroUsuario from './RegistroUsuario';
import ConsultaUsuarios from './ConsultaUsuarios';

// Importamos TanStack Query
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

// Creamos el cliente
const queryClient = new QueryClient();

function DashboardPage() {
  return (
    // Mantenemos el Provider para las mutaciones, pero SIN el BrowserRouter
    <QueryClientProvider client={queryClient}>
      
        {/* Contenedor principal de toda la pantalla */}
        <div style={styles.appContainer}>
          
          {/* Barra de Navegación Estilo Falabella */}
          <nav style={styles.navbar}>
            <div style={styles.logoContainer}>
              <Link to="/" style={styles.logo}>MiTienda</Link>
            </div>
            <div style={styles.navLinks}>
              <Link to="/" style={styles.link}>Inicio</Link>
              <Link to="/registroUsuario" style={styles.link}>Registro</Link>
              <Link to="/consultaUsuarios" style={styles.link}>Consulta</Link>
            </div>
          </nav>

          {/* Contenedor central con tarjeta flotante */}
          <div style={styles.contentWrapper}>
            <div style={styles.contentCard}>
              
              {/* Aquí adentro ocurren los cambios de pantalla dinámicos */}
              <Routes>
                <Route path="/" element={<Inicio />} />
                <Route path="/registroUsuario" element={<RegistroUsuario />} />
                <Route path="/consultaUsuarios" element={<ConsultaUsuarios />} />
              </Routes>
              
            </div>
          </div>

          {/* Footer personalizado */}
          <footer style={styles.footer}>
            <p style={styles.footerText}>
              Desarrollado con 💚 | <strong>Hecho por Augusto Espinoza</strong>
            </p>
          </footer>

        </div>
        
    </QueryClientProvider>
  );
}

// Componente de bienvenida mejorado
function Inicio() {
  return (
    <div style={styles.inicioContainer}>
      <h2 style={styles.title}>Bienvenido al Sistema</h2>
      <p style={styles.subtitle}>
        Gestiona tus usuarios de forma rápida y segura. Usa el menú superior para comenzar a navegar.
      </p>
    </div>
  );
}

// ==========================================
// ESTILOS MODERNOS (CSS-in-JS)
// ==========================================
const styles = {
  appContainer: {
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    background: 'linear-gradient(135deg, #f6f8fd 0%, #f1f5f9 100%)',
    fontFamily: "'Segoe UI', Roboto, Helvetica, Arial, sans-serif",
    margin: '-8px'
  },
  navbar: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#CDE702', 
    padding: '15px 50px',
    boxShadow: '0 4px 12px rgba(0,0,0,0.08)',
  },
  logoContainer: {
    display: 'flex',
    alignItems: 'center',
  },
  logo: {
    fontSize: '24px',
    fontWeight: '900',
    color: '#333',
    textDecoration: 'none',
    letterSpacing: '-0.5px'
  },
  navLinks: {
    display: 'flex',
    gap: '25px',
  },
  link: {
    textDecoration: 'none',
    color: '#333',
    fontWeight: '600',
    fontSize: '15px',
    padding: '8px 12px',
    borderRadius: '4px',
  },
  contentWrapper: {
    flex: 1, 
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'flex-start',
    padding: '40px 20px',
  },
  contentCard: {
    backgroundColor: '#ffffff',
    width: '100%',
    maxWidth: '900px',
    minHeight: '400px',
    padding: '40px',
    borderRadius: '16px', 
    boxShadow: '0 10px 40px rgba(0,0,0,0.06)', 
  },
  inicioContainer: {
    textAlign: 'center',
    paddingTop: '50px',
  },
  title: {
    fontSize: '32px',
    color: '#222',
    marginBottom: '15px',
  },
  subtitle: {
    fontSize: '18px',
    color: '#666',
    lineHeight: '1.5',
  },
  footer: {
    backgroundColor: '#222',
    color: '#fff',
    padding: '20px',
    textAlign: 'center',
    marginTop: 'auto', 
  },
  footerText: {
    margin: 0,
    fontSize: '14px',
    letterSpacing: '0.5px'
  }
};

export default DashboardPage;