import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
// 1. Importamos las herramientas de Query
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from './App.jsx';

// 2. Creamos el cliente que administrará nuestras peticiones
const queryClient = new QueryClient();

createRoot(document.getElementById('root')).render(
  <StrictMode>
    {/* 3. Envolvemos nuestra App para darle los superpoderes de TanStack Query */}
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </StrictMode>,
);